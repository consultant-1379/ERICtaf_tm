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
import com.ericsson.cifwk.tm.domain.helper.UserHelper;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCampaignGroup;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCampaignGroupRepository;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestCampaignGroupMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.UserMapper;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignGroupInfo;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;

public class CreateTestCampaignGroupCommand implements Command<TestCampaignGroupInfo> {

    @Inject
    private TestCampaignGroupRepository testCampaignGroupRepository;

    @Inject
    private TestCampaignGroupMapper testCampaignGroupMapper;

    @Inject
    private UserHelper userHelper;

    @Inject
    private UserMapper userMapper;

    @Override
    public Response apply(TestCampaignGroupInfo input) {
        testCampaignGroupRepository.disableFilter();
        if (input.getName().length() <= 0) {
            throw new BadRequestException(Responses.badRequest("Test campaign group name is empty"));
        }
        if (input.getProduct() == null) {
            throw new BadRequestException(Responses.badRequest("Product has not been selected"));
        }
        TestCampaignGroup entity = testCampaignGroupMapper.mapDto(input, TestCampaignGroup.class);

        TestCampaignGroup group = testCampaignGroupRepository.findNameAndProduct(input.getName(), input.getProduct().getId());
        if (group != null) {
            if (!group.isDeleted()) {
                testCampaignGroupRepository.enableFilter();
                throw new BadRequestException(Responses.badRequest("Test campaign group already exists"));
            } else {
                group.undelete();
                TestCampaignGroupInfo dto = testCampaignGroupMapper.mapEntity(entity, new TestCampaignGroupInfo());
                testCampaignGroupRepository.enableFilter();
                return Responses.created(dto);
            }
        }

        entity.setTestCampaigns(null);
        UserInfo currentUser = userHelper.getCurrentUser();
        entity.setUser(userMapper.mapDto(currentUser, User.class));
        testCampaignGroupRepository.persist(entity);
        TestCampaignGroupInfo dto = testCampaignGroupMapper.mapEntity(entity, new TestCampaignGroupInfo());
        testCampaignGroupRepository.enableFilter();
        return Responses.created(dto);
    }

}
