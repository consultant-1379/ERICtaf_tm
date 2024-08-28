package com.ericsson.cifwk.tm.application.commands;

import com.ericsson.cifwk.tm.application.requests.ChangeLockStateRequest;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestCampaignMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import static com.ericsson.cifwk.tm.test.ResponseAsserts.assertStatus;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChangeLockStateCommandTest {

    public static final long TEST_PLAN_ID = 2L;

    @Mock
    private TestCampaignRepository testCampaignRepository;

    @Mock
    private TestCampaignMapper testCampaignMapper;

    @Spy
    private TestCampaign entity = new TestCampaign();

    @InjectMocks
    private ChangeLockStateCommand command = new ChangeLockStateCommand();

    @Before
    public void setUp() {
        entity.setId(TEST_PLAN_ID);
        when(testCampaignRepository.find(eq(TEST_PLAN_ID))).thenReturn(entity);
    }

    @Test
    public void testLockTestPlan() {
        entity.setLocked(false);
        ChangeLockStateRequest request = new ChangeLockStateRequest(TEST_PLAN_ID, true);
        Response response = command.apply(request);

        assertStatus(response, Response.Status.OK);
        verify(entity).setLocked(eq(true));
        verify(testCampaignRepository).save(eq(entity));
    }

    @Test
    public void testUnlockTestPlan() {
        entity.setLocked(true);
        ChangeLockStateRequest request = new ChangeLockStateRequest(TEST_PLAN_ID, false);
        Response response = command.apply(request);

        assertStatus(response, Response.Status.OK);
        verify(entity).setLocked(eq(false));
        verify(testCampaignRepository).save(eq(entity));
    }

    @Test(expected=NotFoundException.class)
    public void testLockNonExisting() {
        ChangeLockStateRequest request = new ChangeLockStateRequest(10 + TEST_PLAN_ID, true);
        command.apply(request);
    }

}
