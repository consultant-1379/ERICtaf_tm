package com.ericsson.cifwk.tm.application.commands;

import com.ericsson.cifwk.tm.application.Command;
import com.ericsson.cifwk.tm.application.services.TestCampaignService;
import com.ericsson.cifwk.tm.application.services.validation.ServiceValidationException;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestCampaignMapper;
import com.ericsson.cifwk.tm.presentation.dto.CopyTestCampaignsRequest;
import com.ericsson.cifwk.tm.presentation.responses.Responses;
import com.google.common.collect.Lists;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.util.List;

public class CopyTestCampaignsCommand implements Command<CopyTestCampaignsRequest> {

    @Inject
    private TestCampaignService testCampaignService;

    @Inject
    private TestCampaignMapper testCampaignMapper;

    @Override
    public Response apply(CopyTestCampaignsRequest request) {
        List<TestCampaign> entities = Lists.newArrayList();
        try {
            entities = testCampaignService.copyTestCampaigns(request);
        } catch (ServiceValidationException e) {
            throw new BadRequestException(Responses.badRequest(e.getMessage(), e.getDeveloperMessage()));
        }
        return Responses.created(testCampaignMapper.mapEntities(entities));
    }
}
