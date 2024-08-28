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

import com.ericsson.cifwk.tm.domain.model.requirements.Defect;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.presentation.dto.DefectInfo;
import com.ericsson.cifwk.tm.presentation.dto.ProjectInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.DefectView;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.google.inject.Inject;

import static com.ericsson.cifwk.tm.infrastructure.mapping.Mapping.newInstance;

public class DefectMapper implements EntityMapper<Defect, DefectInfo>, DtoMapper<DefectInfo, Defect> {

    private final ProjectMapper projectMapper;

    @Inject
    public DefectMapper(ProjectMapper projectMapper) {
        this.projectMapper = projectMapper;
    }

    @Override
    public Defect mapDto(DefectInfo dto, Class<? extends Defect> entityClass) {
        return mapDto(dto, newInstance(entityClass));
    }

    @Override
    public Defect mapDto(DefectInfo dto, Defect entity) {
        if (dto == null) {
            return null;
        }
        entity.setId(dto.getId());
        entity.setExternalId(dto.getExternalId());
        entity.setExternalTitle(dto.getExternalTitle());
        entity.setExternalSummary(dto.getExternalSummary());
        entity.setProject(projectMapper.mapDto(dto.getProject(), Project.class));
        entity.setExternalStatusName(dto.getExternalStatusName());

        return entity;
    }

    @Override
    public DefectInfo mapEntity(Defect entity, Class<? extends DefectInfo> dtoClass) {
        return mapEntity(entity, newInstance(dtoClass));
    }

    @Override
    public DefectInfo mapEntity(Defect entity, DefectInfo dto) {
        if (entity == null) {
            return null;
        }

        dto.setId(entity.getId());
        dto.setExternalId(entity.getExternalId());
        dto.setExternalTitle(entity.getExternalTitle());
        dto.setExternalSummary(entity.getExternalSummary());
        dto.setProject(projectMapper.mapEntity(entity.getProject(), ProjectInfo.class));
        dto.setExternalStatusName(entity.getExternalStatusName());

        return dto;
    }

    @Override
    public DefectInfo mapEntity(Defect entity,
                                Class<? extends DefectInfo> dtoClass, Class<? extends DtoView<DefectInfo>> view) {

        if (DefectView.Detailed.class.equals(view)) {
            return mapEntity(entity, dtoClass);
        }
        return mapSimple(entity, newInstance(dtoClass));
    }

    private DefectInfo mapSimple(Defect entity, DefectInfo defectInfo) {
        defectInfo.setId(entity.getId());
        defectInfo.setExternalId(entity.getExternalId());
        defectInfo.setExternalTitle(entity.getExternalTitle());
        defectInfo.setExternalSummary(entity.getExternalSummary());
        defectInfo.setExternalStatusName(entity.getExternalStatusName());
        return defectInfo;
    }

    @Override
    public DefectInfo mapEntity(Defect entity, DefectInfo dto, Class<? extends DtoView<DefectInfo>> view) {
        if (DefectView.Detailed.class.equals(view)) {
            return mapEntity(entity, dto);
        }
        return mapSimple(entity, dto);
    }
}
