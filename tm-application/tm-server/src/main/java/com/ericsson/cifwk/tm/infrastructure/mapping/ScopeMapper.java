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
import com.ericsson.cifwk.tm.domain.model.testdesign.Scope;
import com.ericsson.cifwk.tm.presentation.dto.GroupInfo;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.google.common.base.Preconditions;

import javax.inject.Inject;

import static com.ericsson.cifwk.tm.infrastructure.mapping.Mapping.newInstance;

public class ScopeMapper extends ReferenceMapper implements DtoMapper<ReferenceDataItem, Scope> {

    @Inject
    private ProductMapper productMapper;

    @Override
    public Scope mapDto(ReferenceDataItem dto, Class<? extends Scope> entityClass) {
        return mapDto(dto, newInstance(entityClass));
    }

    public Scope mapDto(GroupInfo dto, Class<? extends Scope> entityClass) {
        return mapDto(dto, newInstance(entityClass));
    }

    @Override
    public Scope mapDto(ReferenceDataItem dto, Scope entity) {
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

    public Scope mapDto(GroupInfo dto, Scope entity) {
        if (dto == null) {
            return null;
        }
        Preconditions.checkNotNull(entity);

        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setEnabled(dto.isEnabled());
        Product product = productMapper.mapDto(dto.getProduct(), new Product());
        entity.setProduct(product);
        return entity;

    }

    public GroupInfo mapEntity(GroupInfo dto, Scope entity) {
        if (dto == null) {
            return null;
        }
        Preconditions.checkNotNull(entity);

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEnabled(entity.isEnabled());
        ProductInfo productInfo = productMapper.mapEntity(entity.getProduct(), new ProductInfo());
        dto.setProduct(productInfo);
        return dto;

    }


}
