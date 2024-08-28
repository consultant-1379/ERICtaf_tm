package com.ericsson.cifwk.tm.infrastructure;

import com.ericsson.cifwk.tm.infrastructure.scheduling.JobScheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class InitializationServlet extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(InitializationServlet.class);

    @Override
    public void init(ServletConfig config) throws ServletException {
        System.setProperty("jsse.enableSNIExtension", "false");
        super.init(config);
        if (SchedulingModule.isLoaded()) {
            JobScheduler jobScheduler = Bootstrap.getApplicationInjector().getInstance(JobScheduler.class);
            try {
                jobScheduler.start();
            } catch (SchedulerException e) {
                throw new ServletException(e);
            }
        } else {
            logger.warn("SchedulingModule is not loaded, quartz jobs will not run!");
            logger.warn("Please, make sure you have included SchedulingModule in your env.");
        }

    }
}
