/*
 * COPYRIGHT Ericsson (c) 2016.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.commands;

import com.ericsson.cifwk.tm.application.Command;
import com.ericsson.cifwk.tm.domain.model.testdesign.ReviewGroup;
import com.ericsson.cifwk.tm.domain.model.testdesign.ReviewGroupRepository;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.domain.model.users.UserRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.ReviewGroupMapper;
import com.ericsson.cifwk.tm.presentation.dto.ReviewGroupInfo;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


public class UpdateReviewGroupCommand implements Command<ReviewGroupInfo> {

    @Inject
    private ReviewGroupRepository reviewGroupRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private ReviewGroupMapper reviewGroupMapper;

    @Override
    public Response apply(ReviewGroupInfo input) {

        Optional<ReviewGroup> entity = Optional.ofNullable(reviewGroupRepository.find(input.getId()));

        if (!entity.isPresent()) {
            throw new NotFoundException(Responses.notFound());
        }
        ReviewGroup reviewGroup = entity.get();
        reviewGroupMapper.mapDto(input, reviewGroup);
        Set<User> users = input.getUsers().stream().map(item -> userRepository.findByExternalId(item.getUserId())).collect(Collectors.toSet());
        reviewGroup.setUsers(users);
        reviewGroupRepository.save(reviewGroup);
        ReviewGroupInfo reviewGroupInfo = reviewGroupMapper.mapEntity(reviewGroup, ReviewGroupInfo.class);
        return Responses.ok(reviewGroupInfo);
    }
}
