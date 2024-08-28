package com.ericsson.cifwk.tm.infrastructure.mapping;

import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;

import javax.inject.Inject;

import static com.ericsson.cifwk.tm.infrastructure.mapping.Mapping.newInstance;

public class FeatureMapper implements EntityMapper<ProductFeature, FeatureInfo>, DtoMapper<FeatureInfo, ProductFeature> {

    private ProductMapper productmapper;

    @Inject
    public FeatureMapper(ProductMapper productmapper) {
        this.productmapper = productmapper;
    }

    @Override
    public ProductFeature mapDto(FeatureInfo dto, Class<? extends ProductFeature> entityClass) {
        ProductFeature productFeature = newInstance(entityClass);
        return mapDto(dto, productFeature);
    }

    @Override
    public ProductFeature mapDto(FeatureInfo dto, ProductFeature entity) {
        if (dto == null) {
            return null;
        }
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setProduct(productmapper.mapDto(dto.getProduct(), Product.class));

        return entity;
    }

    @Override
    public FeatureInfo mapEntity(ProductFeature entity, Class<? extends FeatureInfo> dtoClass) {
        return mapEntity(entity, newInstance(dtoClass));
    }

    @Override
    public FeatureInfo mapEntity(ProductFeature entity, FeatureInfo dto) {
        if (entity == null) {
            return null;
        }

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setProduct(productmapper.mapEntity(entity.getProduct(), ProductInfo.class));
        return dto;
    }

    @Override
    public FeatureInfo mapEntity(ProductFeature entity, Class<? extends FeatureInfo> dtoClass, Class<? extends DtoView<FeatureInfo>> view) {
        return mapEntity(entity, newInstance(dtoClass), view);
    }

    @Override
    public FeatureInfo mapEntity(ProductFeature entity, FeatureInfo dto, Class<? extends DtoView<FeatureInfo>> view) {
        if (entity == null) {
            return null;
        }

        dto.setId(entity.getId());
        dto.setName(entity.getName());

        return dto;
    }
}
