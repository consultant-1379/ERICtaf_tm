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

import com.ericsson.cifwk.tm.domain.model.shared.AuditRevisionEntity;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.presentation.dto.Modification;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;

import java.util.Date;

import static com.ericsson.cifwk.tm.infrastructure.mapping.Mapping.newInstance;

public class AuditRevisionMapper implements EntityMapper<AuditRevisionEntity, Modification> {

    @Override
    public Modification mapEntity(AuditRevisionEntity entity, Class<? extends Modification> dtoClass) {
        return mapEntity(entity, newInstance(dtoClass));
    }

    @Override
    public Modification mapEntity(AuditRevisionEntity entity, Modification dto) {
        User user = entity.getUser();
        dto.setUsername(user == null ? null : user.getUserName());
        dto.setTimestamp(new Date(entity.getTimestamp()));
        return dto;
    }

    @Override
    public Modification mapEntity(
            AuditRevisionEntity entity,
            Class<? extends Modification> dtoClass,
            Class<? extends DtoView<Modification>> view) {
        return mapEntity(entity, dtoClass);
    }

    @Override
    public Modification mapEntity(
            AuditRevisionEntity entity,
            Modification dto,
            Class<? extends DtoView<Modification>> view) {
        return mapEntity(entity, dto);
    }

}
