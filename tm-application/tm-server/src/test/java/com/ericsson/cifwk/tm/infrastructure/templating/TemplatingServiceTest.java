/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.infrastructure.templating;

import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;

public class TemplatingServiceTest {
    @Mock
    EventBus eventBus;

    @InjectMocks
    TemplatingService templatingService = new TemplatingService();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        templatingService.configure();
    }

    @Test
    public void testGenerateFromTemplate() throws Exception {
        UserInfo userInfo = new UserInfo();
        String USER_ID = "mruser";
        String USER_EMAIL = "mr.user@ericsson.se";
        String USER_NAME = "John";
        String USER_SURNAME = "Doe";
        String URL = "http://ericsson.se";
        String TC1 = "TestCase1";
        String TC2 = "TestCase2";

        userInfo.setUserId(USER_ID);
        userInfo.setEmail(USER_EMAIL);
        userInfo.setName(USER_NAME);
        userInfo.setSurname(USER_SURNAME);

        HashMap<String, Object> variables = Maps.newHashMap();
        variables.put("testCases", Lists.newArrayList(TC1, TC2));
        variables.put("user", userInfo);
        variables.put("url", URL);

        String result = templatingService.generateFromTemplate(TemplatingService.NEW_ASSIGNMENT, variables);

        assertThat(result, allOf(
                containsString(USER_NAME),
                containsString(USER_SURNAME),
                containsString(URL),
                containsString(TC1),
                containsString(TC2)));
    }
}
