package com.ericsson.cifwk.tm.infrastructure.mapping;

import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestSuite;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.SuiteInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;

import javax.inject.Inject;

import static com.ericsson.cifwk.tm.infrastructure.mapping.Mapping.newInstance;

public class TestSuiteMapper implements EntityMapper<TestSuite, SuiteInfo>, DtoMapper<SuiteInfo, TestSuite> {

    @Inject
    private FeatureMapper featureMapper;

    @Override
    public TestSuite mapDto(SuiteInfo dto, Class<? extends TestSuite> entityClass) {
        return mapDto(dto, newInstance(entityClass));
    }

    @Override
    public TestSuite mapDto(SuiteInfo dto, TestSuite entity) {
        if (dto == null) {
            return null;
        }
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setFeature(featureMapper.mapDto(dto.getFeature(), ProductFeature.class));

        return entity;
    }

    @Override
    public SuiteInfo mapEntity(TestSuite entity, Class<? extends SuiteInfo> dtoClass) {
        return mapEntity(entity, newInstance(dtoClass));
    }

    @Override
    public SuiteInfo mapEntity(TestSuite entity, SuiteInfo dto) {
        if (entity == null) {
            return null;
        }
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setFeature(featureMapper.mapEntity(entity.getFeature(), FeatureInfo.class));
        return dto;
    }

    @Override
    public SuiteInfo mapEntity(TestSuite entity, Class<? extends SuiteInfo> dtoClass, Class<? extends DtoView<SuiteInfo>> view) {
        return mapEntity(entity, newInstance(dtoClass));
    }

    @Override
    public SuiteInfo mapEntity(TestSuite entity, SuiteInfo dto, Class<? extends DtoView<SuiteInfo>> view) {
        return mapEntity(entity, dto);
    }
}
