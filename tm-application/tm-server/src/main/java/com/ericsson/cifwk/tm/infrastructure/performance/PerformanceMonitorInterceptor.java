/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.infrastructure.performance;

import com.ericsson.cifwk.tm.infrastructure.interceptor.Invocations;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.EtmPoint;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.inject.Inject;

public class PerformanceMonitorInterceptor implements MethodInterceptor {

    @Inject
    private EtmMonitor etmMonitor;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (!Invocations.isPublic(invocation)) {
            return invocation.proceed();
        }
        String marker = Invocations.toString(invocation);
        EtmPoint point = etmMonitor.createPoint(marker);
        try {
            return invocation.proceed();
        } finally {
            point.collect();
        }
    }

}
