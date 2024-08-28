package com.ericsson.cifwk.tm.infrastructure.mapping;

import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.ProjectInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.google.common.base.Preconditions;

import javax.inject.Inject;

import static com.ericsson.cifwk.tm.infrastructure.mapping.Mapping.newInstance;

public class ProjectMapper implements EntityMapper<Project, ProjectInfo>, DtoMapper<ProjectInfo, Project> {

    private final ProductMapper productMapper;

    @Inject
    public ProjectMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }


    @Override
    public ProjectInfo mapEntity(Project entity, Class<? extends ProjectInfo> dtoClass) {
        return mapEntity(entity, newInstance(dtoClass));
    }

    @Override
    public ProjectInfo mapEntity(Project entity, ProjectInfo dto) {
        if (entity == null) {
            return null;
        }
        Preconditions.checkNotNull(dto);
        dto.setId(entity.getId());
        dto.setExternalId(entity.getExternalId());
        dto.setName(entity.getName());
        dto.setProduct(productMapper.mapEntity(entity.getProduct(), new ProductInfo()));
        return dto;
    }

    @Override
    public ProjectInfo mapEntity(
            Project entity,
            Class<? extends ProjectInfo> dtoClass,
            Class<? extends DtoView<ProjectInfo>> view) {
        return mapEntity(entity, dtoClass);
    }

    @Override
    public ProjectInfo mapEntity(
            Project entity,
            ProjectInfo dto,
            Class<? extends DtoView<ProjectInfo>> view) {
        return mapEntity(entity, dto);
    }

    @Override
    public Project mapDto(ProjectInfo dto, Class<? extends Project> entityClass) {
        return mapDto(dto, newInstance(entityClass));
    }

    @Override
    public Project mapDto(ProjectInfo dto, Project entity) {
        if (dto == null) {
            return null;
        }
        Preconditions.checkNotNull(entity);
        entity.setId(dto.getId());
        entity.setExternalId(dto.getExternalId());
        entity.setName(dto.getName());
        entity.setProduct(productMapper.mapDto(dto.getProduct(), new Product()));
        return entity;
    }

}
