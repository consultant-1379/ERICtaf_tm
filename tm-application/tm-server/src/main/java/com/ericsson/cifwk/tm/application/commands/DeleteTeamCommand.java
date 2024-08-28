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
import com.ericsson.cifwk.tm.domain.model.testdesign.TestTeam;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestTeamRepository;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

public class DeleteTeamCommand implements Command<Long> {

    @Inject
    private TestTeamRepository teamRepository;

    @Override
    public Response apply(Long input) {

        TestTeam testTeam = teamRepository.find(input);
        if (testTeam == null || testTeam.isDeleted()) {
            throw new NotFoundException(Responses.notFound());
        }
        testTeam.delete();
        return Responses.noContent();
    }

}
