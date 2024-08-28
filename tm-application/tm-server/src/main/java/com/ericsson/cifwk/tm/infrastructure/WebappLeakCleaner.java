package com.ericsson.cifwk.tm.infrastructure;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import net.sf.ehcache.transaction.xa.processor.XARequestProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.logging.LogManager;

/**
 * @author vladislavs.korehovs@ericsson.com
 */
public class WebappLeakCleaner {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebappLeakCleaner.class);

    @PostConstruct
    public void init() throws ClassNotFoundException {
        // Prevent intermittent issues: NoClassDefError XARequestProcessor
        // when stoppoing GuiceFilter, during tomcat controlled undeploy
        // filter eventually calls CacheManager.shutdown() which throws NoClassDefError sometimes
        WebappLeakCleaner.class.getClassLoader().loadClass(
                XARequestProcessor.class.getName());
    }

    @PreDestroy
    public void cleanLeaks() {
        // in embedded mode we cannot unregister driver each time, it will cause problems with
        // com.mchange.v2.c3p0
        if (System.getProperty("embedded") != null) {
            return;
        }
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t instanceof ForkJoinWorkerThread) {
                t.setContextClassLoader(null);
                try {
                    t.stop();
                } catch (Exception e) {
                    LOGGER.warn("SEVERE problem cleaning up ForkJoinWorkerThread"
                            + e.getMessage());
                }
            }
            if (t.isAlive()) {
                AbandonedConnectionCleanupThread.uncheckedShutdown();
            }
        }
        manuallyDeregisterDriver();
        unregisterLoggers();


    }

    private void unregisterLoggers() {
        // Unregister Loggers
        Object logManager = LogManager.getLogManager();
        Class logManagerClass = logManager.getClass();
        if ("org.apache.juli.ClassLoaderLogManager".equals(logManagerClass.getName())) {
            try {
                Method method = logManagerClass.getDeclaredMethod("getClassLoaderInfo", ClassLoader.class);
                method.setAccessible(true);
                Object loggerInfo = method.invoke(logManager, WebappLeakCleaner.class.getClassLoader());
                Object rootNode = null;
                Object logger = null;
                Map children = null;
                if (loggerInfo != null) {
                    Field field = loggerInfo.getClass().getDeclaredField("loggers");
                    field.setAccessible(true);
                    Map loggers = (Map) field.get(loggerInfo);
                    loggers.clear();
                    field = loggerInfo.getClass().getDeclaredField("rootNode");
                    field.setAccessible(true);
                    rootNode = field.get(loggerInfo);
                }
                if (rootNode != null && "org.apache.juli.ClassLoaderLogManager$LogNode".equals(
                        rootNode.getClass().getName())) {
                    Field field = rootNode.getClass().getDeclaredField("logger");
                    field.setAccessible(true);
                    logger = field.get(rootNode);
                    field = rootNode.getClass().getDeclaredField("children");
                    field.setAccessible(true);
                    children = (Map) field.get(rootNode);
                }
                if (children != null) {
                    children.clear();
                }
                if (logger != null && "org.apache.juli.ClassLoaderLogManager$RootLogger".equals(
                        logger.getClass().getName())) {
                    Field handlersField = java.util.logging.Logger.class.getDeclaredField("handlers");
                    handlersField.setAccessible(true);
                    List handlers = (List) handlersField.get(logger);
                    closeHandlers(handlers);
                }
            } catch (NoSuchMethodException | InvocationTargetException |
                    IllegalAccessException | NoSuchFieldException e) {
                LOGGER.warn("SEVERE problem cleaning up loggers: " + e.getMessage());
            }
        }
    }

    private void manuallyDeregisterDriver() {
        // This manually deregisters loaded JDBC driver
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            if (driver.getClass().getClassLoader() == WebappLeakCleaner.class.getClassLoader()) {
                try {
                    DriverManager.deregisterDriver(driver);
                } catch (SQLException e) {
                    LOGGER.warn("SEVERE problem cleaning up MySQL Driver: "
                            + e.getMessage());
                }
            }
        }
    }

    private void closeHandlers(List handlers) {
        for (Object handler : new ArrayList(handlers)) {
            if (handler != null && handler instanceof SLF4JBridgeHandler) {
                handlers.remove(handler);
            }
        }
    }
}
