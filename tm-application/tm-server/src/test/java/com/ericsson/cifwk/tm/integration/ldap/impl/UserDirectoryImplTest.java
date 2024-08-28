/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */
package com.ericsson.cifwk.tm.integration.ldap.impl;

import com.ericsson.cifwk.tm.domain.model.users.UserRepository;
import com.ericsson.cifwk.tm.integration.ldap.LDAPSearchType;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javax.inject.Provider;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class UserDirectoryImplTest {

    private static final String USER_ID = "eeeeeee";
    private static final String USER_EMAIL = "that.guy@ericsson.com";

    @Mock
    Provider<UserRepository> userRepositoryProvider;

    @Spy
    @InjectMocks
    UserDirectoryImpl userDirectory = new UserDirectoryImpl();

    private UserInfo user;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(userRepositoryProvider.get()).thenReturn(mock(UserRepository.class));
        doReturn(user).when(userDirectory).findInLDAP(anyString(), any(LDAPSearchType.class));
        user = mock(UserInfo.class);
    }

    @Test
    public void testFindByName() throws Exception {
        doReturn(user).when(userDirectory).findInLDAP(eq(USER_ID), eq(LDAPSearchType.USER));
        UserInfo foundUser = userDirectory.find(USER_ID, LDAPSearchType.USER);
        assertThat(foundUser, equalTo(user));
    }

    @Test
    public void testFindByEmail() throws Exception {
        doReturn(user).when(userDirectory).findInLDAP(eq(USER_EMAIL), eq(LDAPSearchType.EMAIL));
        UserInfo foundUser = userDirectory.find(USER_EMAIL, LDAPSearchType.EMAIL);
        assertThat(foundUser, equalTo(user));
    }

    @Test
    public void testFindByNameEmail() throws Exception {
        doReturn(user).when(userDirectory).findInLDAP(eq(USER_ID), eq(LDAPSearchType.USER_AND_EMAIL));
        UserInfo foundUser = userDirectory.findByUsernameOrEmail(USER_ID);
        assertThat(foundUser, equalTo(user));

        doReturn(user).when(userDirectory).findInLDAP(eq(USER_EMAIL), eq(LDAPSearchType.USER_AND_EMAIL));
        foundUser = userDirectory.findByUsernameOrEmail(USER_EMAIL);
        assertThat(foundUser, equalTo(user));
    }

    @Test
    public void testFindNotExisting() throws Exception {
        String query = "definitelyNotExistingUser";
        doReturn(null).when(userDirectory).findInLDAP(eq(query), any(LDAPSearchType.class));
        UserInfo user = userDirectory.findByUsernameOrEmail(query);
        assertThat(user, nullValue());
    }
}
