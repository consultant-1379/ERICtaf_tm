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
import com.ericsson.cifwk.tm.infrastructure.inject.servlet.ApplicationConfigurator;
import com.ericsson.cifwk.tm.infrastructure.inject.servlet.JerseyResourceConfig;
import com.ericsson.cifwk.tm.infrastructure.logging.LoggingFilter;
import com.ericsson.cifwk.tm.infrastructure.security.MdcFilter;
import com.ericsson.cifwk.tm.infrastructure.security.NoCacheFilter;
import com.ericsson.cifwk.tm.infrastructure.security.SecurityModule;
import com.ericsson.cifwk.tm.infrastructure.security.TmSessionListener;
import com.ericsson.cifwk.tm.presentation.annotations.Controller;
import com.ericsson.cifwk.tm.presentation.annotations.ControllerFeature;
import com.google.common.collect.Sets;
import com.google.inject.persist.PersistFilter;
import com.google.inject.servlet.ServletModule;
import org.apache.shiro.guice.web.GuiceShiroFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.reflections.Reflections;

import java.util.Set;

@GuiceModule(priority = 1)
public class ApplicationModule extends ServletModule {

    private static final String PRESENTATION_PACKAGE = "com.ericsson.cifwk.tm.presentation";

    private static final String GLOBAL_URI_PATTERN = "/*";
    private static final String API_URI_PATTERN = "/api*";

    @Override
    protected void configureServlets() {
        Reflections reflections = new Reflections(PRESENTATION_PACKAGE);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> controllerFeatures = reflections.getTypesAnnotatedWith(ControllerFeature.class);
        Sets.SetView<Class<?>> features = Sets.union(controllers, controllerFeatures);

        for (Class<?> feature : features) {
            bind(feature);
        }

        ApplicationConfigurator configurator = new ApplicationConfigurator();
        ResourceConfig resourceConfig = configurator
                .registerSharedFeatures(ResourceConfig.forApplicationClass(JerseyResourceConfig.class))
                .registerClasses(features);

        install(new SecurityModule(getServletContext()));

        ServletContainer jerseyServlet = new ServletContainer(resourceConfig);

        filter(GLOBAL_URI_PATTERN).through(GuiceShiroFilter.class);

        registerApiServletAndFilters(jerseyServlet);

        getServletContext().addListener(TmSessionListener.class);
    }

    private void registerApiServletAndFilters(ServletContainer jerseyServlet) {
        serve(API_URI_PATTERN).with(jerseyServlet);
        filter(API_URI_PATTERN).through(PersistFilter.class);
        filter(API_URI_PATTERN).through(MdcFilter.class);
        filter(API_URI_PATTERN).through(LoggingFilter.class);
        filter(API_URI_PATTERN).through(NoCacheFilter.class);
    }


}
