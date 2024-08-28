package com.ericsson.cifwk.tm.infrastructure.performance;

import etm.core.monitor.EtmMonitor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

public class PerformanceMonitor {

    @Inject
    private EtmMonitor etmMonitor;

    @PostConstruct
    public void start() {
        etmMonitor.start();
    }

    @PreDestroy
    public void stop() {
        etmMonitor.stop();
    }

}
