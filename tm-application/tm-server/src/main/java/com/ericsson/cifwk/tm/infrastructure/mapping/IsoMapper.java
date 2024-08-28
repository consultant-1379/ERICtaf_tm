package com.ericsson.cifwk.tm.infrastructure.mapping;

import com.ericsson.cifwk.tm.domain.model.domain.ISO;
import com.ericsson.cifwk.tm.presentation.dto.IsoInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;

import static com.ericsson.cifwk.tm.infrastructure.mapping.Mapping.newInstance;

public class IsoMapper implements EntityMapper<ISO, IsoInfo>, DtoMapper<IsoInfo, ISO> {

    @Override
    public ISO mapDto(IsoInfo dto, Class<? extends ISO> entityClass) {
        return mapDto(dto, newInstance(entityClass));
    }

    @Override
    public ISO mapDto(IsoInfo dto, ISO entity) {
        if (dto == null) {
            return null;
        }
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setVersion(dto.getVersion());

        return entity;
    }

    @Override
    public IsoInfo mapEntity(ISO entity, Class<? extends IsoInfo> dtoClass) {
        return mapEntity(entity, newInstance(dtoClass));
    }

    @Override
    public IsoInfo mapEntity(ISO entity, IsoInfo dto) {
        if (entity == null) {
            return null;
        }
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setVersion(entity.getVersion());

        return dto;
    }

    @Override
    public IsoInfo mapEntity(ISO entity, Class<? extends IsoInfo> dtoClass, Class<? extends DtoView<IsoInfo>> view) {
        return mapEntity(entity, newInstance(dtoClass));
    }

    @Override
    public IsoInfo mapEntity(ISO entity, IsoInfo dto, Class<? extends DtoView<IsoInfo>> view) {
        return mapEntity(entity, dto);
    }
}
