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
import com.ericsson.cifwk.tm.domain.model.testdesign.ReviewGroup;
import com.ericsson.cifwk.tm.domain.model.testdesign.ReviewGroupRepository;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.domain.model.users.UserRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.ReviewGroupMapper;
import com.ericsson.cifwk.tm.presentation.dto.ReviewGroupInfo;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class CreateReviewGroupCommand implements Command<ReviewGroupInfo> {

    @Inject
    private UserRepository userRepository;

    @Inject
    private ReviewGroupRepository reviewGroupRepository;

    @Inject
    private ReviewGroupMapper reviewGroupMapper;


    @Override
    public Response apply(ReviewGroupInfo input) {
        reviewGroupRepository.disableFilter();
        ReviewGroup entity = reviewGroupMapper.mapDto(input, ReviewGroup.class);
        Optional<ReviewGroup> reviewGroup = Optional.ofNullable(reviewGroupRepository.findByGroupName(input.getName()));
        if (reviewGroup.isPresent()) {
            if (!reviewGroup.get().isDeleted()) {
                reviewGroupRepository.enableFilter();
                throw new BadRequestException(Responses.badRequest("Review Group already exists"));
            } else {
                reviewGroup.get().undelete();
                ReviewGroupInfo dto = reviewGroupMapper.mapEntity(entity, new ReviewGroupInfo());
                reviewGroupRepository.enableFilter();
                return Responses.created(dto);
            }
        }
        Set<User> users = input.getUsers().stream().map(item -> userRepository.findByExternalId(item.getUserId())).collect(Collectors.toSet());
        entity.setUsers(users);
        reviewGroupRepository.persist(entity);
        ReviewGroupInfo dto = reviewGroupMapper.mapEntity(entity, new ReviewGroupInfo());
        reviewGroupRepository.enableFilter();
        return Responses.created(dto);
    }

}
