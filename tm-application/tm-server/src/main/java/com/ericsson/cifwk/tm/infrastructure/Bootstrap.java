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
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;
import com.google.common.io.Resources;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.netflix.governator.configuration.CompositeConfigurationProvider;
import com.netflix.governator.configuration.ConfigurationProvider;
import com.netflix.governator.configuration.PropertiesConfigurationProvider;
import com.netflix.governator.configuration.SystemConfigurationProvider;
import com.netflix.governator.guice.LifecycleInjector;
import com.netflix.governator.guice.LifecycleInjectorBuilder;
import com.netflix.governator.lifecycle.LifecycleManager;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public final class Bootstrap {

    private static final String ROOT_PACKAGE = "com.ericsson.cifwk.tm";

    private static final Logger LOGGER = LoggerFactory.getLogger(Bootstrap.class);

    private static Injector injector;
    private static LifecycleManager lifecycleManager;

    private Bootstrap() {
    }

    @VisibleForTesting
    public static void setInjector(Injector injector) {
        Bootstrap.injector = injector;
    }

    public static synchronized void start() {
        if (lifecycleManager == null) {
            lifecycleManager = getApplicationInjector().getInstance(LifecycleManager.class);
            try {
                lifecycleManager.start();
            } catch (Exception e) {
                LOGGER.error("Failed to start lifecycle!", e);
                LOGGER.warn("Shutting down");
                shutdown();
            }
        }
    }

    public static synchronized void shutdown() {
        if (lifecycleManager != null) {
            lifecycleManager.close();
            lifecycleManager = null;
        }
        injector = null;
    }

    public static synchronized Injector getApplicationInjector() {
        if (injector == null) {
            injector = createInjector();
        }
        return injector;
    }

    private static Injector createInjector() {
        LOGGER.info("Configuring application injector");
        Reflections reflections = new Reflections(ROOT_PACKAGE);
        Set<Class<?>> moduleCandidates = reflections.getTypesAnnotatedWith(GuiceModule.class);
        Iterable<Module> modules = createApplicationModules(moduleCandidates);
        return injectorBuilder()
                .withModules(modules)
                .build()
                .createInjector();
    }

    static LifecycleInjectorBuilder injectorBuilder() {
        ConfigurationProvider configurationProvider = getConfigurationProvider();
        return LifecycleInjector.builder()
                .usingBasePackages(ROOT_PACKAGE)
                .withBootstrapModule(new LifecycleBootstrapModule(configurationProvider));
    }

    static Iterable<Module> createApplicationModules(Set<Class<?>> moduleClasses) {
        final String moduleType = new Environment().getModuleType();
        return FluentIterable.from(moduleClasses)
                .filter(new Predicate<Class<?>>() {
                    @Override
                    public boolean apply(Class<?> moduleClass) {
                        if (!Module.class.isAssignableFrom(moduleClass)) {
                            return false;
                        }
                        GuiceModule annotation = moduleClass.getAnnotation(GuiceModule.class);
                        if (annotation != null) {
                            HashSet<String> envSet = Sets.newHashSet(annotation.env());
                            return envSet.isEmpty() || envSet.contains(moduleType);
                        }
                        return false;
                    }
                })
                .transform(new Function<Class<?>, Module>() {
                    @Override
                    public Module apply(Class<?> moduleClass) {
                        try {
                            LOGGER.info("Creating injector module: {}", moduleClass.getName());
                            return (Module) moduleClass.newInstance();
                        } catch (InstantiationException | IllegalAccessException e) {
                            throw Throwables.propagate(e);
                        }
                    }
                })
                .toSortedList(new Comparator<Module>() {
                    @Override
                    public int compare(Module m1, Module m2) {
                        GuiceModule annotation1 = m1.getClass().getAnnotation(GuiceModule.class);
                        GuiceModule annotation2 = m2.getClass().getAnnotation(GuiceModule.class);
                        return Integer.compare(annotation1.priority(), annotation2.priority());
                    }
                });
    }

    private static ConfigurationProvider getConfigurationProvider() {
        Environment environment = new Environment();
        String moduleType = environment.getModuleType();
        LOGGER.info("Configuring environment: {}", moduleType);

        CompositeConfigurationProvider configurationProvider = new CompositeConfigurationProvider();
        configurationProvider.add(new PropertiesConfigurationProvider(environment.toProperties()));
        configurationProvider.add(new SystemConfigurationProvider());
        configureProperties(configurationProvider, "default");
        configureProperties(configurationProvider, moduleType);

        return configurationProvider;
    }

    private static void configureProperties(
            CompositeConfigurationProvider configurationProvider,
            String propertiesFile) {
        URL resource = Resources.getResource("environment/" + propertiesFile + ".properties");
        try {
            Reader reader = Resources.asCharSource(resource, Charsets.UTF_8).openStream();
            Properties properties = new Properties();
            properties.load(reader);
            configurationProvider.add(new PropertiesConfigurationProvider(properties));
        } catch (IOException e) {
            LOGGER.warn("Failed to load configuration: {}", propertiesFile);
        }
    }

}
