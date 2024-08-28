package com.ericsson.cifwk.tm.infrastructure.mapping;

import com.ericsson.cifwk.tm.common.NamedWithId;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;

import static com.ericsson.cifwk.tm.domain.model.shared.ReferenceHelper.*;
import static com.ericsson.cifwk.tm.infrastructure.mapping.Mapping.cast;

public class EnumReferenceMapper
        extends ReferenceMapper
        implements DtoMapper<ReferenceDataItem, NamedWithId> {

    @Override
    public NamedWithId mapDto(ReferenceDataItem dto, Class<? extends NamedWithId> entityClass) {
        if (dto == null) {
            return null;
        }
        try {
            String idString = dto.getId();
            int id = Integer.parseInt(idString);
            return parseEnumById(id, entityClass);
        } catch (NumberFormatException e) {
            String title = dto.getTitle();
            return parseEnumByName(title, entityClass);
        }
    }

    @Override
    public NamedWithId mapDto(ReferenceDataItem dto, NamedWithId entity) {
        throw new UnsupportedOperationException();
    }

    public <T extends Enum<T> & NamedWithId> T mapCastDto(ReferenceDataItem dto, Class<T> entityClass) {
        NamedWithId reference = mapDto(dto, entityClass);
        return cast(reference, entityClass);
    }

}
