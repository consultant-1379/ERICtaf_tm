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

import com.ericsson.cifwk.tm.common.NamedWithId;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.google.common.base.Preconditions;

import static com.ericsson.cifwk.tm.infrastructure.mapping.Mapping.newInstance;

public class ReferenceMapper implements EntityMapper<NamedWithId, ReferenceDataItem> {

    @Override
    public ReferenceDataItem mapEntity(NamedWithId entity, Class<? extends ReferenceDataItem> dtoClass) {
        return mapEntity(entity, newInstance(dtoClass));
    }

    @Override
    public ReferenceDataItem mapEntity(NamedWithId entity, ReferenceDataItem dto) {
        if (entity == null) {
            return null;
        }
        Preconditions.checkNotNull(dto);
        Object id = entity.getId();
        dto.setId(id != null ? id.toString() : "");
        dto.setTitle(entity.getName());
        return dto;
    }

    @Override
    public ReferenceDataItem mapEntity(
            NamedWithId entity,
            Class<? extends ReferenceDataItem> dtoClass,
            Class<? extends DtoView<ReferenceDataItem>> view) {
        return mapEntity(entity, dtoClass);
    }

    @Override
    public ReferenceDataItem mapEntity(
            NamedWithId entity,
            ReferenceDataItem dto,
            Class<? extends DtoView<ReferenceDataItem>> view) {
        return mapEntity(entity, dto);
    }

}
