package com.ericsson.cifwk.tm.infrastructure.security;

import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.domain.model.users.UserRepository;
import com.ericsson.cifwk.tm.integration.ldap.LDAPConfiguration;
import com.ericsson.cifwk.tm.integration.ldap.LDAPSearchType;
import com.ericsson.cifwk.tm.integration.ldap.UserDirectory;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import com.google.common.base.Preconditions;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.realm.ldap.AbstractLdapRealm;
import org.apache.shiro.realm.ldap.LdapContextFactory;
import org.apache.shiro.subject.PrincipalCollection;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.naming.AuthenticationException;
import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Tms initially authorizes users in LDAP, takes groups form LDAP
 * and then users local database to get specific user roles.
 */
@Singleton
public class TmsRealm extends AbstractLdapRealm {

    private Provider<UserRepository> userRepository;
    private Provider<UserDirectory> userDirectory;
    private LinkedHashMap<String, String> templates;

    @Inject
    public TmsRealm(LDAPConfiguration config,
                    LdapContextFactory contextFactory,
                    Provider<UserRepository> userRepository,
                    Provider<UserDirectory> userDirectory) {
        super.setName(config.getRealmName());
        super.setUrl(config.getUrl());
        super.setSearchBase(config.getSearchBases().entrySet().iterator().next().getKey());
        super.setCacheManager(new MemoryConstrainedCacheManager());
        super.setSystemUsername(config.getSystemUsername());
        super.setSystemPassword(config.getSystemPassword());
        super.setLdapContextFactory(contextFactory);
        this.templates = config.getSearchBases();
        this.userDirectory = userDirectory;
        this.userRepository = userRepository;


    }

    @Override
    protected AuthenticationInfo queryForAuthenticationInfo(AuthenticationToken token,
                                                            final LdapContextFactory ldapContextFactory)
            throws NamingException {
        final UsernamePasswordToken upToken = (UsernamePasswordToken) token;

        // Binds using the username and password provided by the user.
        RetryIfFails retry = new RetryIfFails(ldapContextFactory) {
            @Override
            protected LdapContext provideContext() throws AuthenticationException {
                for (Map.Entry<String, String> template : templates.entrySet()) {
                    setSearchBase(template.getKey());
                    String principal = String.format(template.getValue(), upToken.getUsername());
                    try {
                        return ldapContextFactory.getLdapContext(
                                principal, String.valueOf(upToken.getPassword()));
                    } catch (NamingException e) {
                        //do nothing
                    }
                }
                throw new AuthenticationException();
            }
        };
        retry.runAll();
        return new SimpleAuthenticationInfo(upToken.getUsername(), upToken.getPassword(), getName());
    }

    @Override
    @Transactional
    protected AuthorizationInfo queryForAuthorizationInfo(PrincipalCollection principals,
                                                          LdapContextFactory ldapContextFactory)
            throws NamingException {
        final String username = (String) getAvailablePrincipal(principals);
        User user = userRepository.get().findByExternalId(username);
        if (user == null) {
            user = new User(username);
            userRepository.get().persist(user);
        }

        UserInfo userInfo = userDirectory.get().findInLDAP(username, LDAPSearchType.USER);
        Preconditions.checkNotNull(userInfo, String.format("Unable to find user %s in LDAP", username));
        user.setExternalAttributes(userInfo.getEmail(), userInfo.getName(), userInfo.getSurname());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(userInfo.getRoles());
        return info;
    }
}
