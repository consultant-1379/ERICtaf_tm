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

import com.ericsson.cifwk.tm.application.Command;
import com.ericsson.cifwk.tm.application.requests.ChangeLockStateRequest;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestCampaignMapper;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

public class ChangeLockStateCommand implements Command<ChangeLockStateRequest> {

    @Inject
    private TestCampaignRepository testCampaignRepository;

    @Inject
    private TestCampaignMapper testCampaignMapper;

    @Override
    public Response apply(ChangeLockStateRequest input) {
        long testPlanId = input.getTestPlanId();
        boolean shouldLock = input.shouldLock();

        TestCampaign testCampaign = testCampaignRepository.find(testPlanId);
        if (testCampaign == null) {
            throw new NotFoundException(Responses.notFound());
        }

        if (shouldLock) {
            testCampaign.lock();
        } else {
            testCampaign.unlock();
        }

        testCampaignRepository.save(testCampaign);
        TestCampaignInfo dto = testCampaignMapper.mapEntity(testCampaign, TestCampaignInfo.class);
        return Responses.ok(dto);
    }

}
