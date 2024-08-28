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
import com.ericsson.cifwk.tm.infrastructure.scheduling.GuiceJobFactory;
import com.ericsson.cifwk.tm.infrastructure.scheduling.JobScheduler;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

@GuiceModule(env = {"stage", "prod"})
public class SchedulingModule extends AbstractModule {
    private static boolean loaded = false;

    public static boolean isLoaded() {
        return loaded;
    }

    @Override
    protected void configure() {
        bind(SchedulerFactory.class).to(StdSchedulerFactory.class).in(Scopes.SINGLETON);
        bind(GuiceJobFactory.class).in(Scopes.SINGLETON);
        bind(JobScheduler.class).asEagerSingleton();
        loaded = true;
    }
}
