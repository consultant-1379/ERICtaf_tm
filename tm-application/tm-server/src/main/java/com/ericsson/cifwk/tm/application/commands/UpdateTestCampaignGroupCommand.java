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
import com.ericsson.cifwk.tm.application.services.UserProfileService;
import com.ericsson.cifwk.tm.domain.helper.UserHelper;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCampaignGroup;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCampaignGroupRepository;
import com.ericsson.cifwk.tm.domain.model.users.UserProfile;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestCampaignGroupMapper;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignGroupInfo;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;

public class UpdateTestCampaignGroupCommand implements Command<TestCampaignGroupInfo> {

    @Inject
    private TestCampaignGroupRepository testCampaignGroupRepository;

    @Inject
    private TestCampaignGroupMapper testCampaignGroupMapper;

    @Inject
    private UserHelper userHelper;

    @Inject
    private UserProfileService userProfileService;

    @Override
    public Response apply(TestCampaignGroupInfo input) {
        TestCampaignGroup group = testCampaignGroupRepository.findNameAndProduct(input.getName(), input.getProduct().getId());
        if (input.getName().length() <= 0) {
            throw new BadRequestException(Responses.badRequest("Test campaign group name is empty"));
        }
        if (group != null) {
            throw new BadRequestException(Responses.badRequest("Test campaign group already exists"));
        }
        TestCampaignGroup entity = testCampaignGroupRepository.find(input.getId());
        UserInfo currentUser = userHelper.getCurrentUser();
        if (entity == null) {
            throw new BadRequestException(Responses.notFound());
        }
        UserProfile userProfile = userProfileService.findUserProfileByUserId(currentUser.getId());
        if (!userProfile.isAdministrator()) {
            if (entity.getUser() != null && !entity.getUser().getId().equals(currentUser.getId())) {
                throw new BadRequestException(
                        Responses.badRequest("Only the author " + entity.getUser().getExternalId() + " can perform this action"));
            }
        }

        testCampaignGroupMapper.mapDto(input, entity);
        testCampaignGroupRepository.save(entity);
        return Responses.ok();
    }

}
