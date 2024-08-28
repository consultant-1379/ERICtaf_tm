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

import com.ericsson.cifwk.tm.application.security.AuthenticationService;
import com.ericsson.cifwk.tm.application.security.impl.AuthenticationServiceImpl;
import com.ericsson.cifwk.tm.infrastructure.annotations.GuiceModule;
import com.ericsson.cifwk.tm.integration.ldap.LDAPConfiguration;
import com.ericsson.cifwk.tm.integration.ldap.UserDirectory;
import com.ericsson.cifwk.tm.integration.ldap.impl.UserDirectoryImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.ldap.DefaultLdapContextFactory;
import org.apache.shiro.realm.ldap.LdapContextFactory;

import javax.inject.Singleton;

@GuiceModule(env = {"prod", "stage"})
public class AuthenticationModule extends AbstractModule {

    @Provides
    @Singleton
    LdapContextFactory provideLdapContextFactory(LDAPConfiguration configuration) {
        DefaultLdapContextFactory defaultFactory = new DefaultLdapContextFactory();
        defaultFactory.setPrincipalSuffix(null);
        defaultFactory.setSearchBase(configuration.getSearchBases().entrySet().iterator().next().getKey());
        defaultFactory.setUrl(configuration.getUrl());
        defaultFactory.setSystemUsername(configuration.getSystemUsername());
        defaultFactory.setSystemPassword(configuration.getSystemPassword());
        return defaultFactory;
    }

    @Override
    protected void configure() {
        bind(AuthenticationService.class).to(AuthenticationServiceImpl.class);
        bind(UserDirectory.class).to(UserDirectoryImpl.class);
        bind(Realm.class).to(TmsRealm.class).in(Scopes.SINGLETON);
        bind(RememberMeManager.class).to(LightWeightCookieManager.class);
    }

}
