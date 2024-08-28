package com.ericsson.cifwk.tm.infrastructure.mapping;

import com.ericsson.cifwk.tm.domain.model.execution.TestExecution;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecutionRepository;
import com.ericsson.cifwk.tm.domain.model.execution.VerifyStepExecution;
import com.ericsson.cifwk.tm.domain.model.testdesign.VerifyStep;
import com.ericsson.cifwk.tm.domain.model.testdesign.VerifyStepRepository;
import com.ericsson.cifwk.tm.presentation.dto.VerifyStepExecutionInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.google.common.base.Preconditions;

import javax.inject.Inject;

import static com.ericsson.cifwk.tm.infrastructure.mapping.Mapping.newInstance;

public class VerifyStepExecutionMapper
        implements EntityMapper<VerifyStepExecution, VerifyStepExecutionInfo>,
        DtoMapper<VerifyStepExecutionInfo, VerifyStepExecution> {

    private final VerifyStepRepository verifyStepRepository;

    private final TestExecutionRepository testExecutionRepository;

    @Inject
    public VerifyStepExecutionMapper(
            VerifyStepRepository verifyStepRepository,
            TestExecutionRepository testExecutionRepository) {

        this.verifyStepRepository = verifyStepRepository;
        this.testExecutionRepository = testExecutionRepository;
    }

    @Override
    public VerifyStepExecutionInfo mapEntity(
            VerifyStepExecution entity,
            Class<? extends VerifyStepExecutionInfo> dtoClass) {

        return mapEntity(entity, newInstance(dtoClass));
    }

    @Override
    public VerifyStepExecutionInfo mapEntity(
            VerifyStepExecution entity,
            VerifyStepExecutionInfo dto) {

        if (entity == null) {
            return null;
        }
        Preconditions.checkNotNull(dto);

        dto.setId(entity.getId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setActualResult(entity.getActualResult());
        dto.setVerifyStep(entity.getVerifyStep().getId());
        dto.setTestStep(entity.getVerifyStep().getTestStep().getId());
        dto.setTestExecution(entity.getTestExecution().getId());

        return dto;
    }

    @Override
    public VerifyStepExecutionInfo mapEntity(
            VerifyStepExecution entity,
            Class<? extends VerifyStepExecutionInfo> dtoClass,
            Class<? extends DtoView<VerifyStepExecutionInfo>> view) {

        return mapEntity(entity, dtoClass);
    }

    @Override
    public VerifyStepExecutionInfo mapEntity(
            VerifyStepExecution entity,
            VerifyStepExecutionInfo dto,
            Class<? extends DtoView<VerifyStepExecutionInfo>> view) {

        return mapEntity(entity, dto);
    }

    @Override
    public VerifyStepExecution mapDto(
            VerifyStepExecutionInfo dto,
            Class<? extends VerifyStepExecution> entityClass) {

        return mapDto(dto, newInstance(entityClass));
    }

    @Override
    public VerifyStepExecution mapDto(
            VerifyStepExecutionInfo dto,
            VerifyStepExecution entity) {

        if (dto == null) {
            return null;
        }
        Preconditions.checkNotNull(entity);

        entity.setId(dto.getId());
        entity.setActualResult(dto.getActualResult());
        VerifyStep verifyStep = verifyStepRepository.find(dto.getVerifyStep());
        entity.setVerifyStep(verifyStep);

        if (dto.getTestExecution() != null) {
            TestExecution testExecution = testExecutionRepository.find(dto.getTestExecution());
            entity.setTestExecution(testExecution);
        }

        return entity;
    }

}
