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
import com.ericsson.cifwk.tm.domain.model.testdesign.TestTeam;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestTeamRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestTeamMapper;
import com.ericsson.cifwk.tm.presentation.dto.TeamInfo;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.Optional;

public class UpdateTeamCommand implements Command<TeamInfo> {

    @Inject
    private TestTeamRepository teamRepository;

    @Inject
    private TestTeamMapper teamMapper;

    @Override
    public Response apply(TeamInfo input) {
        teamRepository.disableFilter();
        Optional<TestTeam> testTeam = teamRepository.findByNameAndFeatureId(input.getName(), input.getFeature().getId());
        if (testTeam.isPresent()) {
            if (testTeam.get().isDeleted()) {
                teamRepository.enableFilter();
                throw new BadRequestException(Responses.badRequest("Deleted team of the same name still exists." +
                        " Please try again using a different name"));
            } else if (!testTeam.get().getId().equals(input.getId())) {
                teamRepository.enableFilter();
                throw new BadRequestException(Responses.badRequest("Team already exists"));
            }
        }
        TestTeam entity = teamRepository.find(input.getId());
        if (entity == null) {
            teamRepository.enableFilter();
            throw new NotFoundException(Responses.notFound());
        }
        teamRepository.enableFilter();
        teamMapper.mapDto(input, entity);
        teamRepository.save(entity);
        return Responses.ok();
    }
}
