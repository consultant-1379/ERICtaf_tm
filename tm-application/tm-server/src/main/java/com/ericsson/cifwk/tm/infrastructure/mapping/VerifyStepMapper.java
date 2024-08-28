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

import com.ericsson.cifwk.tm.domain.model.testdesign.VerifyStep;
import com.ericsson.cifwk.tm.presentation.dto.VerifyStepInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.google.common.base.Preconditions;

import static com.ericsson.cifwk.tm.infrastructure.mapping.Mapping.newInstance;

public class VerifyStepMapper
        implements EntityMapper<VerifyStep, VerifyStepInfo>, DtoMapper<VerifyStepInfo, VerifyStep> {

    @Override
    public VerifyStepInfo mapEntity(VerifyStep entity, Class<? extends VerifyStepInfo> dtoClass) {
        return mapEntity(entity, newInstance(dtoClass));
    }

    @Override
    public VerifyStepInfo mapEntity(VerifyStep entity, VerifyStepInfo dto) {
        if (entity == null) {
            return null;
        }
        Preconditions.checkNotNull(dto);

        dto.setId(entity.getId());
        dto.setName(entity.getVerifyStep());
        return dto;
    }

    @Override
    public VerifyStepInfo mapEntity(
            VerifyStep entity,
            Class<? extends VerifyStepInfo> dtoClass,
            Class<? extends DtoView<VerifyStepInfo>> view) {
        return mapEntity(entity, dtoClass);
    }

    @Override
    public VerifyStepInfo mapEntity(
            VerifyStep entity,
            VerifyStepInfo dto,
            Class<? extends DtoView<VerifyStepInfo>> view) {
        return mapEntity(entity, dto);
    }

    @Override
    public VerifyStep mapDto(VerifyStepInfo dto, Class<? extends VerifyStep> entityClass) {
        return mapDto(dto, newInstance(entityClass));
    }

    @Override
    public VerifyStep mapDto(VerifyStepInfo dto, VerifyStep entity) {
        if (dto == null) {
            return null;
        }
        Preconditions.checkNotNull(entity);

        entity.setId(dto.getId());
        entity.setVerifyStep(dto.getName());
        return entity;
    }

}
