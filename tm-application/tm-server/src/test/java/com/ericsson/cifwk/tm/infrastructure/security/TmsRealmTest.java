package com.ericsson.cifwk.tm.infrastructure.security;

import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.domain.model.users.UserRepository;
import com.ericsson.cifwk.tm.integration.ldap.LDAPConfiguration;
import com.ericsson.cifwk.tm.integration.ldap.LDAPSearchType;
import com.ericsson.cifwk.tm.integration.ldap.UserDirectory;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import com.google.inject.Provider;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.ldap.LdapContextFactory;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.naming.AuthenticationException;
import javax.naming.NamingException;
import java.util.HashSet;
import java.util.LinkedHashMap;

import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TmsRealmTest {

    @Mock
    LdapContextFactory contextFactory;
    @Mock
    UserRepository userRepository;
    @Mock
    UserDirectory userDirectory;
    TmsRealm tmsRealm;
    @Mock
    LDAPConfiguration config;

    @Before
    public void before() {
        LinkedHashMap<String, String> searchBasesAndPrincipals = new LinkedHashMap();
        searchBasesAndPrincipals.put("","CN=%s");
        when(config.getSearchBases()).thenReturn(searchBasesAndPrincipals);
        when(config.getRealmName()).thenReturn("test-realm");
        Provider<UserRepository> userRepositoryProvider = new Provider<UserRepository>() {
            public UserRepository get() {
                return userRepository;
            }
        };
        Provider<UserDirectory> userDirectoryProvide = new Provider<UserDirectory>() {
            public UserDirectory get() {
                return userDirectory;
            }
        };
        tmsRealm = new TmsRealm(config, contextFactory, userRepositoryProvider, userDirectoryProvide);
    }

    @Test
    public void testAuthenticationWithValidCredentials() throws NamingException {
        AuthenticationToken token = new UsernamePasswordToken("taf", "taf");
        AuthenticationInfo info = tmsRealm.queryForAuthenticationInfo(token, contextFactory);
        assertEquals("taf", new String((char[]) info.getCredentials()));
        assertEquals("taf", info.getPrincipals().getPrimaryPrincipal());
        assertEquals(1, info.getPrincipals().getRealmNames().size());
        assertEquals("test-realm", info.getPrincipals().getRealmNames().iterator().next());
    }

    @Test(expected = AuthenticationException.class)
    public void testAuthenticationWithInvalidCredentials() throws NamingException {
        when(contextFactory.getLdapContext("CN=taf", "taf")).thenThrow(AuthenticationException.class);
        AuthenticationToken token = new UsernamePasswordToken("taf", "taf");
        tmsRealm.queryForAuthenticationInfo(token, contextFactory);
    }

    @Test
    public void testAuthorizationUserDoesNotExistsInDB() throws NamingException {
        UserInfo userInfo = new UserInfo();
        userInfo.setName("John");
        userInfo.setSurname("Smith");
        userInfo.setEmail("john.smith@ericsson.net");
        HashSet<String> roles = new HashSet();
        roles.add("VIEWER");
        roles.add("ADMIN");
        roles.add("SUPERADMIN");
        userInfo.addRoles(roles);
        when(userDirectory.findInLDAP("taf", LDAPSearchType.USER)).thenReturn(userInfo);

        SimplePrincipalCollection principals = new SimplePrincipalCollection();
        principals.add("taf", "test-realm");
        AuthorizationInfo info = tmsRealm.queryForAuthorizationInfo(principals, contextFactory);
        assertThat(info.getRoles(), hasItems("ADMIN", "SUPERADMIN", "VIEWER"));
        assertEquals(null, info.getStringPermissions());
        assertEquals(null, info.getObjectPermissions());
        verify(userRepository).persist(new User("taf"));
    }

    @Test
    public void testAuthorizationUserExistsInDB() throws NamingException {
        UserInfo userInfo = new UserInfo();
        userInfo.setName("John");
        userInfo.setSurname("Smith");
        userInfo.setEmail("john.smith@ericsson.net");
        HashSet<String> roles = new HashSet();
        roles.add("VIEWER");
        roles.add("ADMIN");
        roles.add("SUPERADMIN");
        userInfo.addRoles(roles);
        User user = Mockito.mock(User.class);
        when(userDirectory.findInLDAP("taf", LDAPSearchType.USER)).thenReturn(userInfo);
        when(userRepository.findByExternalId("taf")).thenReturn(user);

        SimplePrincipalCollection principals = new SimplePrincipalCollection();
        principals.add("taf", "test-realm");
        AuthorizationInfo info = tmsRealm.queryForAuthorizationInfo(principals, contextFactory);
        assertThat(info.getRoles(), hasItems("ADMIN", "SUPERADMIN", "VIEWER"));
        assertEquals(null, info.getStringPermissions());
        assertEquals(null, info.getObjectPermissions());
        verify(userRepository, never()).persist(user);
        verify(user).setExternalAttributes("john.smith@ericsson.net", "John", "Smith");
    }
}
