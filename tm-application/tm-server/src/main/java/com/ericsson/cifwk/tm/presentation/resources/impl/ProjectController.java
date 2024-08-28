/*
 * COPYRIGHT Ericsson (c) 2017.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.resources.impl;

import com.ericsson.cifwk.tm.application.CommandProcessor;
import com.ericsson.cifwk.tm.application.commands.CreateProjectCommand;
import com.ericsson.cifwk.tm.application.commands.DeleteProjectCommand;
import com.ericsson.cifwk.tm.application.commands.UpdateProjectCommand;
import com.ericsson.cifwk.tm.application.queries.ProjectQuerySet;
import com.ericsson.cifwk.tm.domain.model.shared.search.Query;
import com.ericsson.cifwk.tm.presentation.annotations.Controller;
import com.ericsson.cifwk.tm.presentation.dto.ProjectInfo;
import com.ericsson.cifwk.tm.presentation.resources.ProjectResource;
import com.google.common.base.Preconditions;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Controller
public class ProjectController implements ProjectResource {

    @Inject
    private CommandProcessor commandProcessor;

    @Inject
    private CreateProjectCommand createProjectCommand;

    @Inject
    private UpdateProjectCommand updateProjectCommand;

    @Inject
    private DeleteProjectCommand deleteProjectCommand;

    @Inject
    private ProjectQuerySet projectQuerySet;

    @Context
    private UriInfo uriInfo;

    @Override
    public Response getProjects(String q, int page, int perPage, String orderBy, String orderMode) {
        Query query = Query.fromQueryString(q);
        query.sortBy(orderBy, orderMode);
        return projectQuerySet.getProjectsByQuery(query, page, perPage, uriInfo);
    }

    @Override
    public Response getProject(String projectId) {
        return projectQuerySet.getProject(projectId);
    }

    @Override
    public Response createProject(ProjectInfo projectInfo) {
        return commandProcessor.process(createProjectCommand, projectInfo);
    }

    @Override
    public Response updateProject(Long projectId, ProjectInfo projectInfo) {
        Preconditions.checkArgument(
                projectId.equals(projectInfo.getId()),
                "Project ids in URL path and request body do not match"
        );
        return commandProcessor.process(updateProjectCommand, projectInfo);
    }

    @Override
    public Response deleteProject(Long projectId) {
        return commandProcessor.process(deleteProjectCommand, projectId);
    }

}
