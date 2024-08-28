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
import com.ericsson.cifwk.tm.infrastructure.interceptor.InterceptorMapping;
import com.google.common.collect.Multimap;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import org.aopalliance.intercept.MethodInterceptor;

import java.util.Collection;

@GuiceModule(priority = 6)
public class InterceptorModule extends AbstractModule {

    @Override
    protected void configure() {
        InterceptorMapping mappingProvider = new InterceptorMapping();

        Multimap<Matcher, MethodInterceptor> mapping =
                mappingProvider.mapping();

        for (Matcher matcher : mapping.keys()) {
            Collection<MethodInterceptor> interceptors = mapping.get(matcher);
            for (MethodInterceptor interceptor : interceptors) {
                this.requestInjection(interceptor);
                bindInterceptor(Matchers.any(), matcher, interceptor);
            }
        }
    }

}
