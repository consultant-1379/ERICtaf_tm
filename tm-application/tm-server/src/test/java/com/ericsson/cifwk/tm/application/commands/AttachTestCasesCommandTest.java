package com.ericsson.cifwk.tm.application.commands;

import com.ericsson.cifwk.tm.application.requests.AttachTestCasesRequest;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersionRepository;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItem;
import com.ericsson.cifwk.tm.presentation.dto.SimpleTestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestInfoList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.util.Arrays;

import static com.ericsson.cifwk.tm.test.ResponseAsserts.assertStatus;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AttachTestCasesCommandTest {

    public static final long TEST_PLAN_ID = 2L;
    public static final long TEST_CASE_ID = 3L;
    public static final long TEST_CASE_VERSION_ID = 4L;

    @Mock
    private TestCampaignRepository testCampaignRepository;

    @Mock
    private TestCaseVersionRepository testCaseVersionRepository;

    @Mock
    private TestCaseRepository testCaseRepository;

    @Mock
    private TestCase testCase;

    @Spy
    private TestCampaign testCampaign;

    @InjectMocks
    private AttachTestCasesCommand command = new AttachTestCasesCommand();

    private TestInfoList testInfoList;

    private TestCaseVersion testCaseVersion;

    @Before
    public void setUp() {
        testInfoList = new TestInfoList();
        SimpleTestCaseInfo testCase2 = new SimpleTestCaseInfo();
        testCase2.setId(TEST_CASE_ID);
        testInfoList.setTestCases(Arrays.asList(testCase2));

        testCampaign.setId(TEST_PLAN_ID);
        when(testCampaignRepository.find(eq(TEST_PLAN_ID))).thenReturn(testCampaign);

        testCaseVersion = new TestCaseVersion();
        testCaseVersion.setId(TEST_CASE_VERSION_ID);

        when(testCase.getCurrentVersion()).thenReturn(testCaseVersion);

        when(testCaseRepository.find(any(Long[].class)))
                .thenReturn(new TestCase[]{testCase});
    }

    @Test
    public void testAttachTestCases() {
        testCampaign.setLocked(false);
        AttachTestCasesRequest request = new AttachTestCasesRequest(TEST_PLAN_ID, testInfoList);
        Response response = command.apply(request);

        assertStatus(response, Response.Status.NO_CONTENT);

        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                assertThat((TestCaseVersion) args[0], equalTo(testCaseVersion));
                return null;
            }
        }).when(testCampaign).addTestCampaignItem(any(TestCampaignItem.class));

        verify(testCampaign).addTestCampaignItem(any(TestCampaignItem.class));
    }

    @Test(expected=BadRequestException.class)
    public void testAttachToLockedTestPlan() {
        testCampaign.setLocked(true);
        AttachTestCasesRequest request = new AttachTestCasesRequest(TEST_PLAN_ID, testInfoList);
        command.apply(request);
    }

}
