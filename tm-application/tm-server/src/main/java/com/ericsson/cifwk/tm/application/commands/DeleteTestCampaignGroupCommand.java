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
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

public class DeleteTestCampaignGroupCommand implements Command<Long> {

    @Inject
    private TestCampaignGroupRepository testCampaignGroupRepository;

    @Inject
    private UserHelper userHelper;

    @Inject
    private UserProfileService userProfileService;

    @Override
    public Response apply(Long input) {
        TestCampaignGroup testCampaignGroup = testCampaignGroupRepository.find(input);
        if (testCampaignGroup == null || testCampaignGroup.isDeleted()) {
            throw new NotFoundException(Responses.notFound());
        }
        UserInfo currentUser = userHelper.getCurrentUser();
        UserProfile userProfile = userProfileService.findUserProfileByUserId(currentUser.getId());
        if (!userProfile.isAdministrator()) {
            if (testCampaignGroup.getUser() != null && !testCampaignGroup.getUser().getId().equals(currentUser.getId())) {
                throw new BadRequestException(
                        Responses.badRequest("Only the author " + testCampaignGroup.getUser().getExternalId() + " can perform this action"));
            }
        }

        testCampaignGroup.delete();
        return Responses.noContent();
    }

}
