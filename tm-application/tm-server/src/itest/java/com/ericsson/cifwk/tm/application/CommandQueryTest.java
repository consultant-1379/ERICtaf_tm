package com.ericsson.cifwk.tm.application;

import com.ericsson.cifwk.tm.application.annotations.QuerySet;
import com.ericsson.cifwk.tm.application.annotations.Service;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.persist.Transactional;
import org.junit.Test;
import org.reflections.Reflections;

import javax.ws.rs.core.Response;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.fail;

public class CommandQueryTest {

    private static final String APPLICATION_PACKAGE = "com.ericsson.cifwk.tm.application";

    private Reflections reflections = new Reflections(APPLICATION_PACKAGE);
    private Set<Class<?>> querySetTypes = reflections.getTypesAnnotatedWith(QuerySet.class);
    private Set<Class<?>> serviceTypes = reflections.getTypesAnnotatedWith(Service.class);
    private StringBuilder violationLogBuilder = new StringBuilder();

    @Test
    public void verifyQuerySets() {
        verifyQueryReturnTypes();
        verifyTransactions();

        String violationLog = violationLogBuilder.toString();
        if (!violationLog.isEmpty()) {
            fail("Command-query contract violation!\n" + violationLog);
        }
    }

    private void verifyQueryReturnTypes() {
        Set<Method> violations = verifyPublicMethods(querySetTypes, new Predicate<Method>() {
            @Override
            public boolean apply(Method method) {
                return Response.class.equals(method.getReturnType())
                        || Object.class.equals(method.getDeclaringClass());
            }
        });
        logMethodViolations("Public methods in @QuerySet classes should return a JAX-RS Response", violations);
    }

    private void verifyTransactions() {
        Set<Class<?>> types = Sets.union(querySetTypes, serviceTypes);
        Set<Method> violations = verifyPublicMethods(types, new Predicate<Method>() {
            @Override
            public boolean apply(Method method) {
                return !method.isAnnotationPresent(Transactional.class);
            }
        });
        logMethodViolations("Query and service methods cannot be @Transactional (use commands)", violations);
    }

    private Set<Method> verifyPublicMethods(Set<Class<?>> types, final Predicate<Method> predicate) {
        return FluentIterable.from(types)
                .transformAndConcat(new Function<Class<?>, Iterable<Method>>() {
                    @Override
                    public Iterable<Method> apply(final Class<?> type) {
                        List<Method> violatingMethods = Lists.newArrayList();
                        for (Method method : type.getMethods()) {
                            if (!predicate.apply(method)) {
                                violatingMethods.add(method);
                            }
                        }
                        return violatingMethods;
                    }
                })
                .toSet();
    }

    private void logMethodViolations(String message, Set<Method> violations) {
        if (violations.size() > 0) {
            violationLogBuilder.append("\n").append(message).append(":\n");
            for (Method violation : violations) {
                violationLogBuilder.append("- ").append(violation).append("\n");
            }
        }
    }

}
