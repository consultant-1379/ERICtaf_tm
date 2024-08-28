/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.infrastructure.interceptor;

import com.ericsson.cifwk.tm.domain.annotations.Repository;
import com.ericsson.cifwk.tm.infrastructure.logging.LoggingInterceptor;
import com.ericsson.cifwk.tm.infrastructure.performance.PerformanceMonitorInterceptor;
import com.ericsson.cifwk.tm.integration.annotations.Integration;
import com.ericsson.cifwk.tm.presentation.annotations.Controller;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;
import org.aopalliance.intercept.MethodInterceptor;
import org.apache.shiro.authz.aop.AuthenticatedAnnotationMethodInterceptor;
import org.apache.shiro.authz.aop.GuestAnnotationMethodInterceptor;
import org.apache.shiro.authz.aop.PermissionAnnotationMethodInterceptor;
import org.apache.shiro.authz.aop.RoleAnnotationMethodInterceptor;
import org.apache.shiro.authz.aop.UserAnnotationMethodInterceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 *
 */
public class InterceptorMapping {
    private static Multimap<Matcher, MethodInterceptor> mapping = ArrayListMultimap.create();

    static {
        mapping.put(new MethodOrTypeAnnotationMatcher(Controller.class),
                new PerformanceMonitorInterceptor());
        mapping.put(new MethodOrTypeAnnotationMatcher(Integration.class),
                new PerformanceMonitorInterceptor());
        mapping.put(new MethodOrTypeAnnotationMatcher(Repository.class),
                new PerformanceMonitorInterceptor());
        mapping.put(new MethodOrTypeAnnotationMatcher(Controller.class),
                new LoggingInterceptor());

        ProxyAwareAnnotationResolver resolver = new ProxyAwareAnnotationResolver();

        RoleAnnotationMethodInterceptor roleAnnotationMethodInterceptor = new
                RoleAnnotationMethodInterceptor(resolver);
        PermissionAnnotationMethodInterceptor permissionAnnotationMethodInterceptor = new
                PermissionAnnotationMethodInterceptor(resolver);
        AuthenticatedAnnotationMethodInterceptor authenticatedAnnotationMethodInterceptor = new
                AuthenticatedAnnotationMethodInterceptor(resolver);
        UserAnnotationMethodInterceptor userAnnotationMethodInterceptor = new
                UserAnnotationMethodInterceptor(resolver);
        GuestAnnotationMethodInterceptor guestAnnotationMethodInterceptor = new
                GuestAnnotationMethodInterceptor(resolver);

        mapping.put(new MethodOrTypeAnnotationMatcher(
                        roleAnnotationMethodInterceptor.getHandler().getAnnotationClass()),
                new AopAllianceMethodInterceptorAdapter(roleAnnotationMethodInterceptor));
        mapping.put(new MethodOrTypeAnnotationMatcher(
                        permissionAnnotationMethodInterceptor.getHandler().getAnnotationClass()),
                new AopAllianceMethodInterceptorAdapter(permissionAnnotationMethodInterceptor));
        mapping.put(new MethodOrTypeAnnotationMatcher(
                        authenticatedAnnotationMethodInterceptor.getHandler().getAnnotationClass()),
                new AopAllianceMethodInterceptorAdapter(authenticatedAnnotationMethodInterceptor));
        mapping.put(new MethodOrTypeAnnotationMatcher(
                        userAnnotationMethodInterceptor.getHandler().getAnnotationClass()),
                new AopAllianceMethodInterceptorAdapter(userAnnotationMethodInterceptor));
        mapping.put(new MethodOrTypeAnnotationMatcher(
                        guestAnnotationMethodInterceptor.getHandler().getAnnotationClass()),
                new AopAllianceMethodInterceptorAdapter(guestAnnotationMethodInterceptor));
    }

    public Multimap<Matcher, MethodInterceptor> mapping() {
        return mapping;
    }

    private static class MethodOrTypeAnnotationMatcher extends AbstractMatcher<Method> {
        private final Class<? extends Annotation> annotation;

        public MethodOrTypeAnnotationMatcher(Class<? extends Annotation> annotationType) {
            this.annotation = annotationType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MethodOrTypeAnnotationMatcher that = (MethodOrTypeAnnotationMatcher) o;

            if (!annotation.equals(that.annotation)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return annotation.hashCode();
        }

        @Override
        public boolean matches(Method method) {
            return method.isAnnotationPresent(annotation)
                    || method.getDeclaringClass().isAnnotationPresent(annotation);
        }
    }
}
