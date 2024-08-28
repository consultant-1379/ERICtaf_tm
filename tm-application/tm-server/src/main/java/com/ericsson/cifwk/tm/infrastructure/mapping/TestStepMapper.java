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

import com.ericsson.cifwk.tm.domain.model.testdesign.TestStep;
import com.ericsson.cifwk.tm.domain.model.testdesign.VerifyStep;
import com.ericsson.cifwk.tm.presentation.dto.TestStepInfo;
import com.ericsson.cifwk.tm.presentation.dto.VerifyStepInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

import static com.ericsson.cifwk.tm.infrastructure.mapping.Mapping.newInstance;

public class TestStepMapper
        implements EntityMapper<TestStep, TestStepInfo>, DtoMapper<TestStepInfo, TestStep> {

    private final VerifyStepMapper verifyStepMapper;

    @Inject
    public TestStepMapper(VerifyStepMapper verifyStepMapper) {
        this.verifyStepMapper = verifyStepMapper;
    }

    @Override
    public TestStepInfo mapEntity(TestStep entity, Class<? extends TestStepInfo> dtoClass) {
        return mapEntity(entity, newInstance(dtoClass));
    }

    @Override
    public TestStepInfo mapEntity(TestStep entity, TestStepInfo dto) {
        if (entity == null) {
            return null;
        }
        Preconditions.checkNotNull(dto);

        dto.setId(entity.getId());
        dto.setName(entity.getTitle());
        dto.setComment(entity.getComment());
        dto.setData(entity.getData());

        List<VerifyStep> verifySteps = Lists.newArrayList(entity.getVerifications());
        Collections.sort(verifySteps);
        for (VerifyStep verifyStep : verifySteps) {
            dto.addVerify(verifyStepMapper.mapEntity(verifyStep, VerifyStepInfo.class));
        }

        return dto;
    }

    @Override
    public TestStepInfo mapEntity(
            TestStep entity,
            Class<? extends TestStepInfo> dtoClass,
            Class<? extends DtoView<TestStepInfo>> view) {
        return mapEntity(entity, dtoClass);
    }

    @Override
    public TestStepInfo mapEntity(
            TestStep entity,
            TestStepInfo dto,
            Class<? extends DtoView<TestStepInfo>> view) {
        return mapEntity(entity, dto);
    }

    @Override
    public TestStep mapDto(TestStepInfo dto, Class<? extends TestStep> entityClass) {
        return mapDto(dto, newInstance(entityClass));
    }

    @Override
    public TestStep mapDto(TestStepInfo dto, TestStep entity) {
        if (dto == null) {
            return null;
        }
        Preconditions.checkNotNull(entity);

        entity.setId(dto.getId());
        entity.setTitle(dto.getName());
        entity.setComment(dto.getComment());
        entity.setData(dto.getData());

        Diff<VerifyStep> verifyStepDiff = Mapping.mapDiffAuditedAndOrdered(
                entity.getVerifications(),
                dto.getVerifies(),
                verifyStepMapper,
                VerifyStep.class
        );
        entity.clearVerifications();
        for (VerifyStep verifyStep : verifyStepDiff.getAdded()) {
            entity.addVerification(verifyStep);
        }
        return entity;
    }

}
