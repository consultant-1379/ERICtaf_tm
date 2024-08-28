/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.infrastructure.inject.servlet;

import com.ericsson.cifwk.tm.infrastructure.Bootstrap;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.servlet.ServletContextEvent;

public class ApplicationListener extends GuiceServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        super.contextInitialized(servletContextEvent);
        Bootstrap.start();
    }

    @Override
    protected Injector getInjector() {
        return Bootstrap.getApplicationInjector();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        SLF4JBridgeHandler.uninstall();
        super.contextDestroyed(servletContextEvent);
        Bootstrap.shutdown();
    }
}
