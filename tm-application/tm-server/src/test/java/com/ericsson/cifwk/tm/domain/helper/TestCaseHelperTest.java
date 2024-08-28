package com.ericsson.cifwk.tm.domain.helper;

import com.ericsson.cifwk.tm.application.security.UserSessionService;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.domain.model.users.UserRepository;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by egergle on 20/01/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class TestCaseHelperTest extends TestCase {

    @Mock
    UserRepository userRepository;

    @Mock
    UserSessionService userSessionService;

    @InjectMocks
    UserHelper userHelper;

    UserInfo userInfo;
    User user;
    long id;
    TestCase testCase;

    @Before
    public void before() {
        id = 1;
        userInfo = new UserInfo();
        userInfo.setId(id);
        user = new User();
        user.setId(id);
        testCase = new TestCase();

        when(userRepository.find(id)).thenReturn(user);
    }

    @Test
    public void testSetUpdateUser() throws Exception {
        when(userSessionService.getCurrentUser()).thenReturn(userInfo);

        userHelper.setUpdateUser(testCase);
        assertThat(testCase.getUpdatedByUser().getId(), equalTo(id));
    }

    @Test
    public void testSetUpdateUserWhenNull() throws Exception {
        when(userSessionService.getCurrentUser()).thenReturn(null);

        userHelper.setUpdateUser(testCase);
        assertThat(testCase.getUpdatedByUser(), nullValue());
    }
}