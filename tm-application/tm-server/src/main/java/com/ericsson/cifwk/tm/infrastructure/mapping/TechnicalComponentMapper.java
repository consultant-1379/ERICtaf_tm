package com.ericsson.cifwk.tm.infrastructure.mapping;

import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.TechnicalComponentInfo;
import com.google.common.base.Preconditions;

import javax.inject.Inject;

import static com.ericsson.cifwk.tm.infrastructure.mapping.Mapping.newInstance;

public class TechnicalComponentMapper extends ReferenceMapper implements DtoMapper<ReferenceDataItem, TechnicalComponent> {

    @Inject
    private FeatureMapper featureMapper;

    @Override
    public TechnicalComponent mapDto(ReferenceDataItem dto, Class<? extends TechnicalComponent> entityClass) {
        return mapDto(dto, newInstance(entityClass));
    }

    @Override
    public TechnicalComponent mapDto(ReferenceDataItem dto, TechnicalComponent entity) {
        if (dto == null) {
            return null;
        }
        Preconditions.checkNotNull(entity);
        try {
            String idString = dto.getId();
            long id = Long.parseLong(idString);
            entity.setId(id);
        } catch (NumberFormatException e) {
            entity.setId(null);
        }
        entity.setName(dto.getTitle());
        return entity;
    }

    public TechnicalComponent mapDto(TechnicalComponentInfo dto, TechnicalComponent entity) {
        if (dto == null) {
            return null;
        }
        Preconditions.checkNotNull(entity);

        entity.setId(dto.getId());
        entity.setName(dto.getName());
        ProductFeature feature = featureMapper.mapDto(dto.getFeature(), new ProductFeature());
        entity.setFeature(feature);
        return entity;
    }

    public TechnicalComponentInfo mapEntity(TechnicalComponentInfo dto, TechnicalComponent entity) {
        if (dto == null) {
            return null;
        }
        Preconditions.checkNotNull(entity);

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        FeatureInfo featureInfo = featureMapper.mapEntity(entity.getFeature(), FeatureInfo.class);
        dto.setFeature(featureInfo);
        return dto;

    }

    public TechnicalComponentInfo mapEntity(TechnicalComponent entity) {
        if (entity == null) {
            return null;
        }
        TechnicalComponentInfo dto = new TechnicalComponentInfo();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setFeatureName(entity.getFeature().getName());
        return dto;
    }
}
