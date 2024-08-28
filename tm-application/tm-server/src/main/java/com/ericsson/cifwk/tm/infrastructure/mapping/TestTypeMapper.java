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
import com.ericsson.cifwk.tm.domain.model.testdesign.TestType;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.TestTypeInfo;
import com.google.common.base.Preconditions;

import javax.inject.Inject;

import static com.ericsson.cifwk.tm.infrastructure.mapping.Mapping.newInstance;

public class TestTypeMapper extends ReferenceMapper implements DtoMapper<ReferenceDataItem, TestType> {

    @Inject
    private ProductMapper productMapper;

    @Override
    public TestType mapDto(ReferenceDataItem dto, Class<? extends TestType> entityClass) {
        return mapDto(dto, newInstance(entityClass));
    }

    public TestType mapDto(TestTypeInfo dto, Class<? extends TestType> entityClass) {
        return mapDto(dto, newInstance(entityClass));
    }

    @Override
    public TestType mapDto(ReferenceDataItem dto, TestType entity) {
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

    public TestType mapDto(TestTypeInfo dto, TestType entity) {
        if (dto == null) {
            return null;
        }
        Preconditions.checkNotNull(entity);

        entity.setId(dto.getId());
        entity.setName(dto.getName());
        Product product = productMapper.mapDto(dto.getProduct(), new Product());
        entity.setProduct(product);
        return entity;

    }

    public TestTypeInfo mapEntity(TestTypeInfo dto, TestType entity) {
        if (dto == null) {
            return null;
        }
        Preconditions.checkNotNull(entity);

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        ProductInfo productInfo = productMapper.mapEntity(entity.getProduct(), new ProductInfo());
        dto.setProduct(productInfo);
        return dto;

    }


}
