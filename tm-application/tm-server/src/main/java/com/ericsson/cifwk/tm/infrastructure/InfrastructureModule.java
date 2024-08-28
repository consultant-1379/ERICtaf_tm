/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.infrastructure;

import com.ericsson.cifwk.tm.infrastructure.annotations.GuiceModule;
import com.ericsson.cifwk.tm.infrastructure.event.TrsEventService;
import com.ericsson.cifwk.tm.infrastructure.templating.TemplatingService;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.inject.Singleton;

@GuiceModule(priority = 2)
public class InfrastructureModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TemplatingService.class).asEagerSingleton();
        bind(WebappLeakCleaner.class).asEagerSingleton();
        bind(TrsEventService.class).asEagerSingleton();
    }

    @Provides
    @Singleton
    EventBus provideEventBus() {
        return new EventBus();
    }

}
