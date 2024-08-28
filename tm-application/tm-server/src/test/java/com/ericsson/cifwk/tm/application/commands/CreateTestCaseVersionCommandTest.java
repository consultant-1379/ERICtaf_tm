/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.commands;

import com.ericsson.cifwk.tm.application.services.TestCaseService;
import com.ericsson.cifwk.tm.domain.helper.UserHelper;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestCaseMapper;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCaseView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

import static com.ericsson.cifwk.tm.test.ResponseAsserts.assertStatus;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CreateTestCaseVersionCommandTest {

    private final String TEST_CASE_ID = "TC-120";

    @Mock
    private TestCaseService testCaseService;

    @Mock
    private TestCaseRepository testCaseRepository;

    @Mock
    private TestCaseMapper testCaseMapper;

    @Mock
    private TestCaseInfo testCaseVersionMock;

    @Mock
    private TestCase testCaseMock;

    @Mock
    private UserHelper userHelper;

    private TestCaseVersion newVersion;

    @InjectMocks
    private CreateTestCaseVersionCommand command = new CreateTestCaseVersionCommand();


    @Before
    public void setUp() {
        newVersion = new TestCaseVersion();

        when(testCaseRepository.findByAnyId(TEST_CASE_ID)).thenReturn(testCaseMock);
        when(testCaseMock.addNewMinorVersion()).thenReturn(newVersion);
        when(userHelper.setUpdateUser(testCaseMock)).thenReturn("");

    }

    @Test
    public void testApply() {
        Response response = command.apply(TEST_CASE_ID);

        assertStatus(response, Response.Status.CREATED);

        verify(testCaseMapper, times(1)).mapEntity(testCaseMock, TestCaseInfo.class, TestCaseView.Detailed.class);
    }
}
