/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.resources.impl;

import com.ericsson.cifwk.tm.application.CommandProcessor;
import com.ericsson.cifwk.tm.application.commands.CreateTeamCommand;
import com.ericsson.cifwk.tm.application.commands.DeleteTeamCommand;
import com.ericsson.cifwk.tm.application.commands.UpdateTeamCommand;
import com.ericsson.cifwk.tm.application.queries.TeamQuerySet;
import com.ericsson.cifwk.tm.domain.model.shared.search.Query;
import com.ericsson.cifwk.tm.presentation.annotations.Controller;
import com.ericsson.cifwk.tm.presentation.dto.TeamInfo;
import com.ericsson.cifwk.tm.presentation.resources.TeamResource;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Controller
public class TeamController implements TeamResource {

    @Inject
    private CommandProcessor commandProcessor;

    @Inject
    private CreateTeamCommand createTeamCommand;

    @Inject
    private UpdateTeamCommand updateTeamCommand;

    @Inject
    private DeleteTeamCommand deleteTeamCommand;

    @Inject
    private TeamQuerySet teamQuerySet;

    @Context
    private UriInfo uriInfo;

    @Override
    public Response getTeams(String q, int page, int perPage, String orderBy, String orderMode) {
        Query query = Query.fromQueryString(q);
        query.sortBy(orderBy, orderMode);
        return teamQuerySet.getTeamsByQuery(query, page, perPage, uriInfo);
    }

    @Override
    public Response create(TeamInfo teamInfo) {
        return commandProcessor.process(createTeamCommand, teamInfo);
    }

    @Override
    public Response update(Long id, TeamInfo teamInfo) {
        return commandProcessor.process(updateTeamCommand, teamInfo);
    }

    @Override
    public Response delete(Long id) {
        return commandProcessor.process(deleteTeamCommand, id);
    }
}
