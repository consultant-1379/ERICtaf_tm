package com.ericsson.cifwk.tm.infrastructure.security;


import org.apache.shiro.realm.ldap.LdapContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.naming.ServiceUnavailableException;
import javax.naming.ldap.LdapContext;

public abstract class RetryIfFails<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RetryIfFails.class);
    private LdapContextFactory factory;

    public RetryIfFails(LdapContextFactory factory) {
        this.factory = factory;
    }

    protected LdapContext provideContext() throws Exception {
        return factory.getSystemLdapContext();
    }

    protected T run(LdapContext ctx) throws NamingException {
        return null;
    }

    public T runAll() throws NamingException {
        boolean retry = false;
        T r = null;
        try (LdapResource resource = new LdapResource(provideContext())) {
            r = run(resource.getContext());
        } catch (ServiceUnavailableException | CommunicationException e) {
            LOGGER.warn(
                    "Received LDAP communication error while executing ldap command (will retry):"
                            + e.getMessage());
            retry = true;
        } catch (NamingException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (retry) {
            try (LdapResource resource = new LdapResource(provideContext())) {
                r = run(resource.getContext());
            } catch (NamingException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return r;
    }
}
