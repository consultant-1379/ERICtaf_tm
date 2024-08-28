package com.ericsson.cifwk.tm.infrastructure.mapping;

import com.ericsson.cifwk.tm.domain.model.domain.Drop;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.ProductRepository;
import com.ericsson.cifwk.tm.presentation.dto.DropInfo;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;

import javax.inject.Inject;

import static com.ericsson.cifwk.tm.infrastructure.mapping.Mapping.newInstance;

public class DropMapper implements EntityMapper<Drop, DropInfo>, DtoMapper<DropInfo, Drop> {

    private ProductMapper productMapper;

    @Inject
    public DropMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Inject
    private ProductRepository productRepository;

    @Override
    public Drop mapDto(DropInfo dto, Class<? extends Drop> entityClass) {
        Drop drop = newInstance(entityClass);
        return mapDto(dto, drop);
    }

    @Override
    public Drop mapDto(DropInfo dto, Drop entity) {
        if (dto == null) {
            return null;
        }
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setProduct(productMapper.mapDto(dto.getProduct(), Product.class));

        return entity;
    }

    @Override
    public DropInfo mapEntity(Drop entity, Class<? extends DropInfo> dtoClass) {
        return mapEntity(entity, newInstance(dtoClass));
    }

    @Override
    public DropInfo mapEntity(Drop entity, DropInfo dto) {
        if (entity == null) {
            return null;
        }
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setProduct(productMapper.mapEntity(entity.getProduct(), ProductInfo.class));

        dto.setDefaultDrop(entity.isDefaultDrop());
        return dto;
    }

    @Override
    public DropInfo mapEntity(Drop entity, Class<? extends DropInfo> dtoClass, Class<? extends DtoView<DropInfo>> view) {
        return mapEntity(entity, newInstance(dtoClass), view);
    }

    @Override
    public DropInfo mapEntity(Drop entity, DropInfo dto, Class<? extends DtoView<DropInfo>> view) {
        if (entity == null) {
            return null;
        }
        dto.setId(entity.getId());
        entity.setName(entity.getName());
        return dto;
    }
}
