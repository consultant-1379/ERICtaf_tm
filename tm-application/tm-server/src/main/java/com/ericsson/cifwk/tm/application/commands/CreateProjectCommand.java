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
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.ProductRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.requirements.ProjectRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.ProjectMapper;
import com.ericsson.cifwk.tm.presentation.dto.ProjectInfo;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;

public class CreateProjectCommand implements Command<ProjectInfo> {

    @Inject
    private ProjectRepository projectRepository;

    @Inject
    private ProductRepository productRepository;

    @Inject
    private ProjectMapper projectMapper;

    @Override
    public Response apply(ProjectInfo input) {
        projectRepository.disableFilter();
        String externalId = input.getExternalId();
        Project project = projectRepository.findByExternalId(externalId);
        Project entity = projectMapper.mapDto(input, Project.class);
        Project otherProject = projectRepository.findByExternalIdAndName(externalId, input.getName());
        if (otherProject != null) {
            if (otherProject.isDeleted()) {
                project.undelete();
                ProjectInfo dto = projectMapper.mapEntity(entity, ProjectInfo.class);
                projectRepository.enableFilter();
                return Responses.created(dto);
            } else if (!project.getId().equals(input.getId())) {
                projectRepository.enableFilter();
                throw new BadRequestException(Responses.badRequest("Project already exists"));
            }
        }
        if (project != null) {
            if (!project.isDeleted()) {
                projectRepository.enableFilter();
                throw new BadRequestException(Responses.badRequest("Project with this identifier already exists: "
                        + externalId));
            } else {
                if (project.getName().equals(input.getName())) {
                    project.undelete();
                    ProjectInfo dto = projectMapper.mapEntity(entity, ProjectInfo.class);
                    projectRepository.enableFilter();
                    return Responses.created(dto);
                } else {
                    throw new BadRequestException(Responses.badRequest("Deleted project with the same external id but different name." +
                            "To undo delete please try again using the same external id and " + project.getName() + "as the name"));
                }
            }
        }

        if (input.getProduct() != null) {
            Product product = productRepository.find(input.getProduct().getId());
            if (product == null) {
                projectRepository.enableFilter();
                throw new BadRequestException(Responses.badRequest("Product with this identifier does not exists: "
                        + input.getProduct().getId()));
            }
            product.addProject(entity);
            entity.setProduct(product);
        } else {
            Product defaultProduct = productRepository.getDefault();
            defaultProduct.addProject(entity);
        }
        projectRepository.save(entity);
        ProjectInfo dto = projectMapper.mapEntity(entity, ProjectInfo.class);
        projectRepository.enableFilter();
        return Responses.created(dto);
    }

}
