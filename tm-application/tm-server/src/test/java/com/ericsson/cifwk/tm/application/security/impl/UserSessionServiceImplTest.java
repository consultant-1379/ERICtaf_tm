package com.ericsson.cifwk.tm.application.security.impl;

import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.infrastructure.mapping.UserMapper;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserSessionServiceImplTest extends ShiroTestCase {

    @Mock
    UserMapper userMapper;

    @InjectMocks
    UserSessionServiceImpl userSessionService;


    @Test
    public void getCurrentUser() {
        User user = new User("taf");
        when(userMapper.mapEntity(eq(user), any(UserInfo.class))).thenReturn(userInfo);

        assertNull(userSessionService.getCurrentUser());
        assertFalse(userSessionService.hasCurrentUser());

        userSessionService.setCurrentUser(user);
        assertNotNull(userSessionService.getCurrentUser());
        assertTrue(userSessionService.hasCurrentUser());

        userSessionService.removeCurrentUser();
        assertNull(userSessionService.getCurrentUser());
        assertFalse(userSessionService.hasCurrentUser());
    }

}
