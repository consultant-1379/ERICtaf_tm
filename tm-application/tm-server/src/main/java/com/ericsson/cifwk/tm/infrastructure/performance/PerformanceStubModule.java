package com.ericsson.cifwk.tm.infrastructure.performance;


import com.ericsson.cifwk.tm.infrastructure.annotations.GuiceModule;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.NullMonitor;

import javax.inject.Singleton;

@GuiceModule(env = {"dev"})
public class PerformanceStubModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    EtmMonitor provideEtmMonitor() {
        return new NullMonitor();
    }

}
