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
import com.ericsson.cifwk.tm.application.security.impl.AuthenticationServiceMockImpl;
import com.ericsson.cifwk.tm.infrastructure.annotations.GuiceModule;
import com.ericsson.cifwk.tm.integration.ldap.UserDirectory;
import com.ericsson.cifwk.tm.integration.ldap.impl.UserDirectoryStub;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.realm.Realm;

@GuiceModule(env = {"dev", "test"}, priority = 0)
public class AuthenticationStubModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AuthenticationService.class).to(AuthenticationServiceMockImpl.class);
        bind(UserDirectory.class).to(UserDirectoryStub.class);
        bind(Realm.class).toProvider(RealmMockProvider.class).in(Scopes.SINGLETON);
        bind(RememberMeManager.class).to(LightWeightCookieManager.class);
    }

}
