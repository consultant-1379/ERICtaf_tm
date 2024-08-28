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

import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.google.common.base.Preconditions;

import static com.ericsson.cifwk.tm.infrastructure.mapping.Mapping.newInstance;

public class ProductMapper implements EntityMapper<Product, ProductInfo>, DtoMapper<ProductInfo, Product> {

    @Override
    public ProductInfo mapEntity(Product entity, Class<? extends ProductInfo> dtoClass) {
        return mapEntity(entity, newInstance(dtoClass));
    }

    @Override
    public ProductInfo mapEntity(Product entity, ProductInfo dto) {
        if (entity == null) {
            return null;
        }
        Preconditions.checkNotNull(dto);
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setExternalId(entity.getExternalId());
        dto.setDropCapable(entity.isDropCabable());
        return dto;
    }

    @Override
    public ProductInfo mapEntity(
            Product entity,
            Class<? extends ProductInfo> dtoClass,
            Class<? extends DtoView<ProductInfo>> view) {
        return mapEntity(entity, dtoClass);
    }

    @Override
    public ProductInfo mapEntity(
            Product entity,
            ProductInfo dto,
            Class<? extends DtoView<ProductInfo>> view) {
        return mapEntity(entity, dto);
    }

    @Override
    public Product mapDto(ProductInfo dto, Class<? extends Product> entityClass) {
        return mapDto(dto, newInstance(entityClass));
    }

    @Override
    public Product mapDto(ProductInfo dto, Product entity) {
        if (dto == null) {
            return null;
        }
        Preconditions.checkNotNull(entity);
        entity.setId(dto.getId());
        entity.setExternalId(dto.getExternalId());
        entity.setName(dto.getName());
        return entity;
    }
}
