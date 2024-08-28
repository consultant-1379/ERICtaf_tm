package com.ericsson.cifwk.tm.infrastructure.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Adapts a Shiro {@link org.apache.shiro.aop.MethodInterceptor MethodInterceptor} to an AOPAlliance
 * {@link org.aopalliance.intercept.MethodInterceptor}.
 */
public class AopAllianceMethodInterceptorAdapter implements MethodInterceptor {
    private org.apache.shiro.aop.MethodInterceptor shiroInterceptor;

    public AopAllianceMethodInterceptorAdapter(org.apache.shiro.aop.MethodInterceptor shiroInterceptor) {
        this.shiroInterceptor = shiroInterceptor;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        return shiroInterceptor.invoke(new AopAllianceMethodInvocationAdapter(invocation));
    }

    @Override
    public String toString() {
        return "AopAlliance Adapter for " + shiroInterceptor.toString();
    }
}
