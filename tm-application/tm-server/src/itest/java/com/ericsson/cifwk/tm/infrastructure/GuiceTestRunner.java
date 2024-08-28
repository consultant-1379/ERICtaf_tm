/*******************************************************************************
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.cifwk.tm.infrastructure;

import com.google.common.collect.Sets;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.netflix.governator.lifecycle.LifecycleManager;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;

public class GuiceTestRunner extends BlockJUnit4ClassRunner {

    private final Logger logger = LoggerFactory.getLogger(GuiceTestRunner.class);

    private final Iterable<Module> modules;
    private Injector injector;

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    public @interface GuiceModules {
        Class<?>[] value() default {};
    }

    public GuiceTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
        Set<Class<?>> moduleClasses = getModulesFor(klass);
        modules = Bootstrap.createApplicationModules(moduleClasses);
    }

    @Override
    public Object createTest() throws Exception {
        Object obj = super.createTest();
        injector = Bootstrap.injectorBuilder()
                .withModules(modules)
                .ignoringAllAutoBindClasses()
                .build()
                .createInjector();
        injector.injectMembers(obj);
        injector.getInstance(LifecycleManager.class).start();
        return obj;
    }

    @Override
    protected Statement withAfters(FrameworkMethod method, Object target, Statement statement) {
        Statement junitAfters = super.withAfters(method, target, statement);
        return new CustomStatement(junitAfters, new Runnable() {
            @Override
            public void run() {
                injector.getInstance(LifecycleManager.class).close();
                injector = null;
            }
        });
    }

    private Set<Class<?>> getModulesFor(Class<?> klass) throws InitializationError {
        GuiceModules annotation = klass.getAnnotation(GuiceModules.class);
        if (annotation != null) {
            return Sets.newHashSet(annotation.value());
        } else {
            logger.warn("Missing @GuiceModules annotation for class: {}", klass.getName());
            return Sets.newHashSet();
        }
    }
}
