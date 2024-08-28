package com.ericsson.cifwk.tm.infrastructure.security;

import org.apache.shiro.realm.ldap.LdapUtils;

import javax.naming.ldap.LdapContext;

/**
 * Allows Friendly Java 7 try-with-resources
 */
public class LdapResource implements AutoCloseable {
    private LdapContext context;

    public LdapResource(LdapContext context) {
        this.context = context;
    }

    @Override
    public void close() throws Exception {
        LdapUtils.closeContext(context);
    }

    public LdapContext getContext() {
        return context;
    }

}
