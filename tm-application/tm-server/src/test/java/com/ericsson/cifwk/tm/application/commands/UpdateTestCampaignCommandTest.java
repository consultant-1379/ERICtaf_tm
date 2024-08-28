package com.ericsson.cifwk.tm.application.commands;

import com.ericsson.cifwk.tm.application.services.TestCampaignService;
import com.ericsson.cifwk.tm.application.services.validation.ServiceValidationException;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignRepository;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import static com.ericsson.cifwk.tm.test.ResponseAsserts.assertStatus;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UpdateTestCampaignCommandTest {

    @Mock
    private TestCampaignService testCampaignService;

    @Mock
    private TestCampaignRepository testCampaignRepository;

    @InjectMocks
    private UpdateTestCampaignCommand command = new UpdateTestCampaignCommand();

    private TestCampaignInfo input = new TestCampaignInfo();
    private TestCampaign entity = new TestCampaign();

    @Before
    public void setUp() {
        long id = 2L;
        input.setId(id);
        entity.setId(id);
        when(testCampaignRepository.find(eq(input.getId()))).thenReturn(entity);
    }

    @Test
    public void testApply() throws ServiceValidationException {
        Response response = command.apply(input);

        assertStatus(response, Response.Status.OK);
        verify(testCampaignService).populate(eq(entity), eq(input));
        verify(testCampaignRepository).save(eq(entity));
    }

    @Test(expected=BadRequestException.class)
    public void testUpdateLocked() {
        entity.setLocked(true);
        command.apply(input);
    }

    @Test(expected=NotFoundException.class)
    public void testNullDto() {
        TestCampaignInfo nonExisting = new TestCampaignInfo();
        nonExisting.setId(5L);
        command.apply(nonExisting);
    }

}
