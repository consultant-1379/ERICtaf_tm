package com.ericsson.cifwk.tm.application.commands;

import com.ericsson.cifwk.tm.application.security.impl.ShiroTestCase;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.domain.model.users.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CreateTestExecutionCommandTest extends ShiroTestCase {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateTestExecutionCommand command = new CreateTestExecutionCommand();

    @Test
    public void testGetUserByExternalId() {
        User sessionUser = mock(User.class);
        when(userRepository.findByExternalId(eq("foo"))).thenReturn(sessionUser);

        User byId = command.getUserByExternalId("foo");
        assertThat(byId, equalTo(sessionUser));

        when(userRepository.find(eq(1L))).thenReturn(sessionUser);
        User byEmptyString = command.getUserByExternalId("");
        assertThat(byEmptyString, equalTo(sessionUser));

        User byNull = command.getUserByExternalId(null);
        assertThat(byNull, equalTo(sessionUser));

        User byNonExistingId = command.getUserByExternalId("bar");
        assertThat(byNonExistingId, nullValue());
    }

}
