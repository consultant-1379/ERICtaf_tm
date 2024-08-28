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

import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class Invocations {

    private Invocations() {
    }

    public static String toString(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        Class<?> type = method.getDeclaringClass();
        return type.getSimpleName() + "::" + method.getName();
    }

    public static boolean isPublic(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        return Modifier.isPublic(method.getModifiers());
    }

}
