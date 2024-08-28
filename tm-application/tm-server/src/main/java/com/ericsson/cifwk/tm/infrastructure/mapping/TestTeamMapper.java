package com.ericsson.cifwk.tm.infrastructure.mapping;

import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestTeam;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.TeamInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;

import javax.inject.Inject;

import static com.ericsson.cifwk.tm.infrastructure.mapping.Mapping.newInstance;

public class TestTeamMapper implements EntityMapper<TestTeam, TeamInfo>, DtoMapper<TeamInfo, TestTeam> {

    @Inject
    private FeatureMapper featureMapper;

    @Override
    public TestTeam mapDto(TeamInfo dto, Class<? extends TestTeam> entityClass) {
        return mapDto(dto, newInstance(entityClass));
    }

    @Override
    public TestTeam mapDto(TeamInfo dto, TestTeam entity) {
        if (dto == null) {
            return null;
        }
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setFeature(featureMapper.mapDto(dto.getFeature(), ProductFeature.class));

        return entity;
    }

    @Override
    public TeamInfo mapEntity(TestTeam entity, Class<? extends TeamInfo> dtoClass) {
        return mapEntity(entity, newInstance(dtoClass));
    }

    @Override
    public TeamInfo mapEntity(TestTeam entity, TeamInfo dto) {
        if (entity == null) {
            return null;
        }
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setFeature(featureMapper.mapEntity(entity.getFeature(), FeatureInfo.class));
        return dto;
    }

    @Override
    public TeamInfo mapEntity(TestTeam entity, Class<? extends TeamInfo> dtoClass, Class<? extends DtoView<TeamInfo>> view) {
        return mapEntity(entity, newInstance(dtoClass));
    }

    @Override
    public TeamInfo mapEntity(TestTeam entity, TeamInfo dto, Class<? extends DtoView<TeamInfo>> view) {
        return mapEntity(entity, dto);
    }
}
