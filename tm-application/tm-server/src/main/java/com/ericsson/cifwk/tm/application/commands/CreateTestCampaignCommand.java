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
import com.ericsson.cifwk.tm.application.services.TestCampaignService;
import com.ericsson.cifwk.tm.application.services.validation.ServiceValidationException;
import com.ericsson.cifwk.tm.domain.helper.UserHelper;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignRepository;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestCampaignMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.UserMapper;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCampaignView;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

public class CreateTestCampaignCommand implements Command<TestCampaignInfo> {

    @Inject
    private TestCampaignService testCampaignService;

    @Inject
    private TestCampaignRepository testCampaignRepository;

    @Inject
    private TestCampaignMapper testCampaignMapper;

    @Inject
    private UserHelper userHelper;

    @Inject
    private UserMapper userMapper;

    @Override
    public Response apply(TestCampaignInfo input) {
        TestCampaign entity = new TestCampaign();
        Map<String, List<String>> usersToNotify = testCampaignService.getUsersToNotify(entity, input);
        try {
            testCampaignService.populate(entity, input);
        } catch (ServiceValidationException e) {
            throw new BadRequestException(Responses.badRequest(e.getMessage(), e.getDeveloperMessage()));
        }
        UserInfo currentUser = userHelper.getCurrentUser();
        entity.setAuthor(userMapper.mapDto(currentUser, User.class));

        testCampaignRepository.persist(entity);
        TestCampaignInfo dto = testCampaignMapper.mapEntity(entity, TestCampaignInfo.class,
                TestCampaignView.Detailed.class);

        testCampaignService.notifyAboutAssignment(usersToNotify,
                entity.getId(),
                input.getHostname());

        return Responses.created(dto);
    }

}
