/*******************************************************************************
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/
package com.ericsson.cifwk.tm.application.security.impl;

import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.domain.model.users.UserRepository;
import com.ericsson.cifwk.tm.integration.ldap.LDAPConfiguration;
import com.ericsson.cifwk.tm.integration.ldap.UserDirectory;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.realm.Realm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceImplTest extends ShiroTestCase {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Realm realm;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Mock
    private AuthenticationToken token;

    @Mock
    private User user;

    @Mock
    private UserDirectory userDirectory;

    @Before
    public void setUp() {

        authenticationService = spy(authenticationService);
        doReturn(token).when(authenticationService).getToken("taf", "taf", false);
        when(realm.getName()).thenReturn("taf");
        when(subject.isAuthenticated()).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userSessionService.getCurrentSessionId()).thenReturn("test");
    }

    @Test
    public void authenticateNewUser() {
        assertNotNull(authenticationService.login("taf", "taf", false));
        verify(subject).login(token);
        verify(userSessionService).setCurrentUser(null);
    }

    @Test
    public void authenticateExistingUser() {
        when(userRepository.findByExternalId("taf")).thenReturn(user);

        assertNotNull(authenticationService.login("taf", "taf", false));
        verify(subject).login(token);
        verify(userSessionService).setCurrentUser(user);
    }

    @Test
    public void authenticateInvalidUser() {
        when(subject.isAuthenticated()).thenReturn(false);

        assertNull(authenticationService.login("taf", "wrong password", false));
        verify(subject).login(any(AuthenticationToken.class));
        verify(userSessionService, never()).setCurrentUser(user);
    }
}
