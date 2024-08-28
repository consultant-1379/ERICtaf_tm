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

import com.netflix.governator.configuration.ConfigurationProvider;
import com.netflix.governator.guice.BootstrapBinder;
import com.netflix.governator.guice.BootstrapModule;

class LifecycleBootstrapModule implements BootstrapModule {

    private final ConfigurationProvider configurationProvider;

    public LifecycleBootstrapModule(ConfigurationProvider configurationProvider) {
        this.configurationProvider = configurationProvider;
    }

    @Override
    public void configure(BootstrapBinder binder) {
        binder.bindConfigurationProvider().toInstance(configurationProvider);
    }

}
