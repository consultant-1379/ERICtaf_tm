package com.ericsson.cifwk.tm.infrastructure.mapping;

import com.ericsson.cifwk.tm.domain.model.testdesign.SavedSearch;
import com.ericsson.cifwk.tm.presentation.dto.SavedSearchInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.google.common.base.Preconditions;

import static com.ericsson.cifwk.tm.infrastructure.mapping.Mapping.newInstance;

public class SavedSearchMapper implements EntityMapper<SavedSearch, SavedSearchInfo>, DtoMapper<SavedSearchInfo, SavedSearch> {

    @Override
    public SavedSearchInfo mapEntity(SavedSearch entity, Class<? extends SavedSearchInfo> dtoClass) {
        return mapEntity(entity, newInstance(dtoClass));
    }

    @Override
    public SavedSearchInfo mapEntity(SavedSearch entity, SavedSearchInfo dto) {
        if (entity == null) {
            return null;
        }
        Preconditions.checkNotNull(dto);
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setQuery(entity.getQuery());
        return dto;
    }

    @Override
    public SavedSearchInfo mapEntity(
            SavedSearch entity,
            Class<? extends SavedSearchInfo> dtoClass,
            Class<? extends DtoView<SavedSearchInfo>> view) {
        return mapEntity(entity, dtoClass);
    }

    @Override
    public SavedSearchInfo mapEntity(
            SavedSearch entity,
            SavedSearchInfo dto,
            Class<? extends DtoView<SavedSearchInfo>> view) {
        return mapEntity(entity, dto);
    }

    @Override
    public SavedSearch mapDto(SavedSearchInfo dto, Class<? extends SavedSearch> entityClass) {
        return mapDto(dto, newInstance(entityClass));
    }

    @Override
    public SavedSearch mapDto(SavedSearchInfo dto, SavedSearch entity) {
        if (dto == null) {
            return null;
        }
        Preconditions.checkNotNull(entity);
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setQuery(dto.getQuery());
        return entity;
    }

}
