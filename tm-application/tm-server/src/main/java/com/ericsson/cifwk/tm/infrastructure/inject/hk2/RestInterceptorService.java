/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.infrastructure.inject.hk2;

import com.ericsson.cifwk.tm.infrastructure.interceptor.InterceptorMapping;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.inject.matcher.Matcher;
import org.aopalliance.intercept.ConstructorInterceptor;
import org.aopalliance.intercept.MethodInterceptor;
import org.glassfish.hk2.api.Filter;
import org.glassfish.hk2.api.InterceptionService;
import org.glassfish.hk2.utilities.BuilderHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class RestInterceptorService implements InterceptionService {

    private final InterceptorMapping mappingProvider = new InterceptorMapping();

    @Override
    public List<ConstructorInterceptor> getConstructorInterceptors(Constructor<?> arg) {
        return Lists.newArrayList();
    }

    @Override
    public Filter getDescriptorFilter() {
        return BuilderHelper.allFilter();
    }

    @Override
    public List<MethodInterceptor> getMethodInterceptors(Method method) {
        List<MethodInterceptor> allInterceptors = Lists.newArrayList();
        Multimap<Matcher, MethodInterceptor> interceptorMapping = mappingProvider.mapping();

        for (Matcher matcher : interceptorMapping.keySet()) {
            if (matcher.matches(method)) {
                Collection<MethodInterceptor> interceptors = interceptorMapping.get(matcher);
                allInterceptors.addAll(interceptors);
            }
        }
        return allInterceptors;
    }
}
