/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.infrastructure.mapping;

import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.presentation.dto.ProjectInfo;
import com.ericsson.cifwk.tm.presentation.dto.RequirementInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.ericsson.cifwk.tm.presentation.dto.view.RequirementView;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.ericsson.cifwk.tm.infrastructure.mapping.Mapping.newInstance;

public class RequirementMapper
        implements EntityMapper<Requirement, RequirementInfo> {

    private final TestCaseMapper testCaseMapper;
    private final ProjectMapper projectMapper;

    @Inject
    public RequirementMapper(
            TestCaseMapper testCaseMapper,
            ProjectMapper projectMapper) {
        this.testCaseMapper = testCaseMapper;
        this.projectMapper = projectMapper;
    }

    @Override
    public RequirementInfo mapEntity(Requirement entity, Class<? extends RequirementInfo> dtoClass) {
        return mapEntity(entity, newInstance(dtoClass));
    }

    @Override
    public RequirementInfo mapEntity(Requirement entity, RequirementInfo dto) {
        return mapEntity(entity, dto, RequirementView.Simple.class);
    }

    @Override
    public RequirementInfo mapEntity(
            Requirement entity,
            Class<? extends RequirementInfo> dtoClass,
            Class<? extends DtoView<RequirementInfo>> view) {
        return mapEntity(entity, newInstance(dtoClass), view);
    }

    @Override
    public RequirementInfo mapEntity(
            Requirement entity,
            RequirementInfo dto,
            Class<? extends DtoView<RequirementInfo>> view) {
        if (entity == null) {
            return null;
        }
        Preconditions.checkNotNull(dto);

        dto.setId(entity.getId());
        dto.setExternalId(entity.getExternalId());
        dto.setType(entity.getExternalType());
        dto.setLabel(entity.getExternalLabel());
        dto.setSummary(entity.getExternalSummary());
        dto.setExternalStatusName(entity.getExternalStatusName());
        dto.setProject(projectMapper.mapEntity(entity.getProject(), ProjectInfo.class));
        dto.setDeliveredIn(entity.getDeliveredIn());

        if (RequirementView.Tree.class.equals(view)) {
            dto.setChildren(mapChildren(entity, dto.getClass(), view));
            dto.setTestCaseCount(getTestCases(entity).size());
        }
        if (RequirementView.Detailed.class.equals(view)) {
            Requirement rootParent = entity.getRootParent();
            dto.setRootTitle(rootParent == null ? null : rootParent.getExternalLabel());
            dto.setTestCases(mapTestCases(getTestCases(entity)));
        }
        return dto;
    }

    private Set<RequirementInfo> mapChildren(
            Requirement parent,
            Class<? extends RequirementInfo> dtoClass,
            Class<? extends DtoView<RequirementInfo>> view) {
        Set<Requirement> children = parent.getChildren();
        String projectId = getProjectId(parent);

        List<RequirementInfo> childDtos = Lists.newArrayList();
        for (Requirement child : children) {
            if (child.isDeleted() || !Objects.equals(projectId, getProjectId(child))) {
                continue;
            }
            RequirementInfo childDto = mapEntity(child, dtoClass, view);
            childDtos.add(childDto);
        }

        List<RequirementInfo> orderedDtos = Ordering.natural()
                .nullsFirst()
                .onResultOf(new Function<RequirementInfo, String>() {
                    @Override
                    public String apply(RequirementInfo input) {
                        return input.getExternalId();
                    }
                })
                .sortedCopy(childDtos);

        return Sets.newLinkedHashSet(orderedDtos);
    }

    private String getProjectId(Requirement requirement) {
        Project project = requirement.getProject();
        if (project != null) {
            return project.getExternalId();
        } else {
            return null;
        }
    }

    private Set<TestCase> getTestCases(Requirement entity) {
        return FluentIterable.from(entity.getTestCaseVersions())
                .transform(new Function<TestCaseVersion, TestCase>() {
                    @Override
                    public TestCase apply(TestCaseVersion input) {
                        return input.getTestCase();
                    }
                }).toSet();
    }

    private Set<TestCaseInfo> mapTestCases(Set<TestCase> testCases) {
        Set<TestCaseInfo> dtos = Sets.newLinkedHashSet();
        for (TestCase testCase : testCases) {
            TestCaseInfo dto = testCaseMapper.mapEntity(testCase, TestCaseInfo.class);
            dtos.add(dto);
        }
        return dtos;
    }

}
