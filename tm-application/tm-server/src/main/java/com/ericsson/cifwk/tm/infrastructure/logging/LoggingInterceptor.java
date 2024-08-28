/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.infrastructure.logging;

import com.ericsson.cifwk.tm.infrastructure.interceptor.Invocations;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class LoggingInterceptor implements MethodInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String marker = Invocations.toString(invocation);
        LOGGER.info("Calling method {}", marker);
        try {
            return invocation.proceed();
        } finally {
            LOGGER.info("Exiting method {}", marker);
        }

    }

}
