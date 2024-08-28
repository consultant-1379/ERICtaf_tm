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
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.requirements.ProjectRepository;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

public class DeleteProjectCommand implements Command<Long> {

    @Inject
    private ProjectRepository projectRepository;

    @Override
    public Response apply(Long id) {

        Project project = projectRepository.find(id);
        if (project == null) {
            throw new NotFoundException(Responses.notFound());
        }

        project.delete();
        projectRepository.save(project);
        return Responses.noContent();
    }

}
