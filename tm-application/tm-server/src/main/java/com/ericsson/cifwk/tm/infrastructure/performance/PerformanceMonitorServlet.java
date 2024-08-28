package com.ericsson.cifwk.tm.infrastructure.performance;

import com.google.inject.Inject;
import etm.contrib.integration.web.HttpConsoleServlet;
import etm.core.monitor.EtmMonitor;

import javax.inject.Singleton;
import javax.servlet.ServletException;

@Singleton
public class PerformanceMonitorServlet extends HttpConsoleServlet {

    @Inject
    private EtmMonitor etmMonitor;

    @Override
    protected EtmMonitor getEtmMonitor() throws ServletException {
        return etmMonitor;
    }

}
