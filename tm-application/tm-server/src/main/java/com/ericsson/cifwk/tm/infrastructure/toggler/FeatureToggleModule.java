package com.ericsson.cifwk.tm.infrastructure.toggler;

import com.ericsson.cifwk.tm.infrastructure.annotations.GuiceModule;
import com.google.inject.servlet.ServletModule;
import org.togglz.console.TogglzConsoleServlet;
import org.togglz.core.manager.TogglzConfig;
import org.togglz.servlet.TogglzFilter;

@GuiceModule(priority = 2)
public class FeatureToggleModule extends ServletModule {

    private static final String TOGGLZ_URI_PATTERN = "/togglz*";

    @Override
    protected void configureServlets() {
        bind(TogglzConfig.class).to(TogglzConfiguration.class);

        serve(TOGGLZ_URI_PATTERN).with(new TogglzConsoleServlet());
        filter(TOGGLZ_URI_PATTERN).through(new TogglzFilter());
    }

}
