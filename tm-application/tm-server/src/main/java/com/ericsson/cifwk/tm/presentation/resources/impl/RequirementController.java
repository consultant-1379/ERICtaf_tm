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

import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.domain.model.requirements.RequirementRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.RequirementMapper;
import com.ericsson.cifwk.tm.presentation.annotations.Controller;
import com.ericsson.cifwk.tm.presentation.dto.CompletionInfo;
import com.ericsson.cifwk.tm.presentation.dto.RequirementInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.ericsson.cifwk.tm.presentation.dto.view.RequirementView;
import com.ericsson.cifwk.tm.presentation.dto.view.RequirementViewFactory;
import com.ericsson.cifwk.tm.presentation.resources.RequirementResource;
import com.ericsson.cifwk.tm.presentation.responses.CompletionHelper;
import com.ericsson.cifwk.tm.presentation.responses.Responses;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Controller
public class RequirementController implements RequirementResource {

    @Inject
    private RequirementRepository requirementRepository;

    @Inject
    private RequirementMapper requirementMapper;

    @Inject
    private RequirementViewFactory requirementViewFactory;

    @Context
    private UriInfo uriInfo;

    @Override
    public Response getRequirement(String requirementId, String projectId, String view) {
        Requirement requirement = requirementRepository.findByExternalIdAndProjectId(requirementId, projectId);

        Class<? extends DtoView<RequirementInfo>> dtoView = requirementViewFactory.getByName(view);
        RequirementInfo dto;

        if (RequirementView.ReverseTree.class.equals(dtoView) && requirement != null) {
            Requirement rootParent = requirement.getRootParent();
            dto = requirementMapper.mapEntity(rootParent, RequirementInfo.class, RequirementView.Tree.class);
            return Responses.nullable(dto);
        }
        dto = requirementMapper.mapEntity(requirement, RequirementInfo.class, dtoView);
        return Responses.nullable(dto);

    }

    @Override
    public Response getRequirements(String projectId, String view) {
        List<Requirement> parents = requirementRepository.findTopLevel(projectId);
        Class<? extends DtoView<RequirementInfo>> dtoView = requirementViewFactory.getByName(view);
        List<RequirementInfo> dtos = Lists.newArrayList();
        for (Requirement parent : parents) {
            RequirementInfo dto = requirementMapper.mapEntity(parent, RequirementInfo.class, dtoView);
            if (dto != null) {
                dtos.add(dto);
            }
        }
        return Responses.ok(dtos);
    }

    @Override
    public CompletionInfo getCompletion(String search, List<String> type, int limit) {
        if (search.isEmpty()) {
            return CompletionInfo.empty();
        }
        List<Requirement> requirements = requirementRepository.findMatchingExternalId(search, type, limit);

        return CompletionHelper.completion(search, requirements, new Function<Requirement, String>() {
            @Override
            public String apply(Requirement requirement) {
                return requirement.getExternalId();
            }
        });
    }

}
