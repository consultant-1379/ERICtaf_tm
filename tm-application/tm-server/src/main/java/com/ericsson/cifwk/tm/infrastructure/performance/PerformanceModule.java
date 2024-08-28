package com.ericsson.cifwk.tm.infrastructure.performance;

import com.ericsson.cifwk.tm.infrastructure.annotations.GuiceModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.NestedMonitor;

@GuiceModule(env = {"test", "stage", "prod"}, priority = 2)
public class PerformanceModule extends ServletModule {

    private static final String MONITORING_URI_PATTERN = "/performance*";

    @Override
    protected void configureServlets() {
        this.bind(PerformanceMonitor.class).asEagerSingleton();
        this.serve(MONITORING_URI_PATTERN).with(PerformanceMonitorServlet.class);
    }

    @Provides
    @Singleton
    EtmMonitor provideEtmMonitor() {
        return new NestedMonitor();
    }

}
