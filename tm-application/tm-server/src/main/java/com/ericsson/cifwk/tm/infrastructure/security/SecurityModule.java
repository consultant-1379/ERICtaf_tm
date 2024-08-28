/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.infrastructure.security;

import com.google.inject.Key;
import org.apache.shiro.guice.web.ShiroWebModule;
import org.apache.shiro.realm.Realm;

import javax.servlet.ServletContext;

public class SecurityModule extends ShiroWebModule {

    public static final Key<SecurityAuthenticationFilter> AUTHC_REST = Key.get(SecurityAuthenticationFilter.class);

    public SecurityModule(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    protected void configureShiroWeb() {
        bindRealm().to(Realm.class);

        addFilterChain("/**/api/login", ANON);
        addFilterChain("/**/api/version", ANON);
        addFilterChain("/**/api/**", AUTHC_REST);

        ShiroWebModule.guiceFilterModule();
    }

}
