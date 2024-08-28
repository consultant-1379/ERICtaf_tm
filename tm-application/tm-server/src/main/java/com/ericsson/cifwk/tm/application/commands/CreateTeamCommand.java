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
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeatureRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestTeam;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestTeamRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestTeamMapper;
import com.ericsson.cifwk.tm.presentation.dto.TeamInfo;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.util.Optional;

public class CreateTeamCommand implements Command<TeamInfo> {

    @Inject
    private TestTeamRepository teamRepository;

    @Inject
    private ProductFeatureRepository featureRepository;

    @Inject
    private TestTeamMapper teamMapper;


    @Override
    public Response apply(TeamInfo input) {
        teamRepository.disableFilter();
        TestTeam entity = teamMapper.mapDto(input, TestTeam.class);
        Optional<TestTeam> testTeam = teamRepository.findByNameAndFeatureId(input.getName(), input.getFeature().getId());
        if (testTeam.isPresent()) {
            if (!testTeam.get().isDeleted()) {
                teamRepository.enableFilter();
                throw new BadRequestException(Responses.badRequest("Team already exists"));
            } else {
                testTeam.get().undelete();
                teamRepository.enableFilter();
                TeamInfo dto = teamMapper.mapEntity(entity, new TeamInfo());
                return Responses.created(dto);
            }
        }
        entity.setFeature(featureRepository.find(entity.getFeature().getId()));
        teamRepository.persist(entity);
        TeamInfo dto = teamMapper.mapEntity(entity, new TeamInfo());
        teamRepository.enableFilter();
        return Responses.created(dto);
    }

}
