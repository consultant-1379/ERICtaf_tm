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
import com.ericsson.cifwk.tm.domain.model.requirements.ProductRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.requirements.ProjectRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.ProjectMapper;
import com.ericsson.cifwk.tm.presentation.dto.ProjectInfo;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

public class UpdateProjectCommand implements Command<ProjectInfo> {

    @Inject
    private ProjectRepository projectRepository;

    @Inject
    private ProjectMapper projectMapper;

    @Inject
    private ProductRepository productRepository;

    @Override
    public Response apply(ProjectInfo input) {
        projectRepository.disableFilter();
        String externalId = input.getExternalId();
        String name = input.getName();
        Project project = projectRepository.findByExternalIdAndName(externalId, name);
        if (project != null) {
            if (project.isDeleted()) {
                projectRepository.enableFilter();
                throw new BadRequestException(Responses.badRequest("Deleted project of the same external Id and name still exists." +
                        " Please try again using a different external Id and name"));
            } else if (!project.getId().equals(input.getId())) {
                projectRepository.enableFilter();
                throw new BadRequestException(Responses.badRequest("Project already exists"));
            }
        }
        Project otherProject = projectRepository.findByExternalId(externalId);
        if (otherProject != null) {
            if (otherProject.isDeleted()) {
                projectRepository.enableFilter();
                throw new BadRequestException(Responses.badRequest("Deleted project of the same external Id  still exists." +
                        " Please try again using a different external Id"));
            } else if (!otherProject.getId().equals(input.getId())) {
                projectRepository.enableFilter();
                throw new BadRequestException(Responses.badRequest("Another project has the same identifier: "
                        + externalId));
            }
        }
        Project entity = projectRepository.find(input.getId());
        if (entity == null) {
            projectRepository.enableFilter();
            throw new NotFoundException(Responses.notFound());
        }
        projectRepository.enableFilter();
        projectMapper.mapDto(input, entity);
        entity.setProduct(productRepository.find(input.getProduct().getId()));
        projectRepository.save(entity);
        return Responses.ok();
    }
}
