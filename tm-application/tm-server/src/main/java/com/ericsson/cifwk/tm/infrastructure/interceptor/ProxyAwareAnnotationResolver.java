package com.ericsson.cifwk.tm.infrastructure.interceptor;

import javassist.util.proxy.ProxyFactory;
import org.apache.shiro.aop.AnnotationResolver;
import org.apache.shiro.aop.MethodInvocation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ProxyAwareAnnotationResolver implements AnnotationResolver {

    @Override
    public Annotation getAnnotation(MethodInvocation mi, Class<? extends Annotation> clazz) {
        if (mi == null) {
            throw new IllegalArgumentException("method argument cannot be null");
        }
        Method m = mi.getMethod();
        if (m == null) {
            String msg = MethodInvocation.class.getName() +
                    " parameter incorrectly constructed.  getMethod() returned null";
            throw new IllegalArgumentException(msg);
        }
        Annotation annotation = m.getAnnotation(clazz);
        return annotation == null ? unproxy(mi.getThis().getClass()).getAnnotation(clazz) : annotation;
    }

    private Class unproxy(Class proxy) {
        Class possiblyProxy = proxy;
        while (possiblyProxy.getSuperclass() != null && ProxyFactory.isProxyClass(possiblyProxy)) {
            possiblyProxy = possiblyProxy.getSuperclass();
        }
        return possiblyProxy;
    }
}

