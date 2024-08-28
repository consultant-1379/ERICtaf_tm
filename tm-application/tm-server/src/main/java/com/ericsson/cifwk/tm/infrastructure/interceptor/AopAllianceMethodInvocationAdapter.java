package com.ericsson.cifwk.tm.infrastructure.interceptor;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * Adapts a Shiro {@link org.apache.shiro.aop.MethodInvocation MethodInvocation} to an AOPAlliance
 * {@link org.aopalliance.intercept.MethodInvocation}.
 */
public class AopAllianceMethodInvocationAdapter implements org.apache.shiro.aop.MethodInvocation {
    private final MethodInvocation mi;

    public AopAllianceMethodInvocationAdapter(MethodInvocation mi) {
        this.mi = mi;
    }

    @Override
    public Method getMethod() {
        return mi.getMethod();
    }

    @Override
    public Object[] getArguments() {
        return mi.getArguments();
    }

    @Override
    public String toString() {
        return "Method invocation [" + mi.getMethod() + "]";
    }

    @Override
    public Object proceed() throws Throwable {
        return mi.proceed();
    }

    @Override
    public Object getThis() {
        return mi.getThis();
    }
}
