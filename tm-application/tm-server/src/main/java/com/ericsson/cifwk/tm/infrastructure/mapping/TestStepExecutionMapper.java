package com.ericsson.cifwk.tm.infrastructure.mapping;

import com.ericsson.cifwk.tm.domain.model.execution.TestExecution;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecutionRepository;
import com.ericsson.cifwk.tm.domain.model.execution.TestStepExecution;
import com.ericsson.cifwk.tm.domain.model.execution.TestStepResult;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestStep;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestStepRepository;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.TestStepExecutionInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.google.common.base.Preconditions;

import javax.inject.Inject;

import static com.ericsson.cifwk.tm.infrastructure.mapping.Mapping.newInstance;

public class TestStepExecutionMapper
        implements EntityMapper<TestStepExecution, TestStepExecutionInfo>,
        DtoMapper<TestStepExecutionInfo, TestStepExecution> {

    private final EnumReferenceMapper referenceMapper;

    private final TestStepRepository testStepRepository;

    private final TestExecutionRepository testExecutionRepository;

    @Inject
    public TestStepExecutionMapper(
            EnumReferenceMapper referenceMapper,
            TestStepRepository testStepRepository,
            TestExecutionRepository testExecutionRepository) {
        this.referenceMapper = referenceMapper;
        this.testStepRepository = testStepRepository;
        this.testExecutionRepository = testExecutionRepository;
    }

    @Override
    public TestStepExecutionInfo mapEntity(TestStepExecution entity,
                                           Class<? extends TestStepExecutionInfo> dtoClass) {

        return mapEntity(entity, newInstance(dtoClass));
    }

    @Override
    public TestStepExecutionInfo mapEntity(TestStepExecution entity,
                                           TestStepExecutionInfo dto) {

        if (entity == null) {
            return null;
        }
        Preconditions.checkNotNull(dto);

        dto.setId(entity.getId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setTestStep(entity.getTestStep().getId());
        dto.setTestExecution(entity.getTestExecution().getId());
        dto.setResult(referenceMapper.mapEntity(entity.getResult(), new ReferenceDataItem()));
        dto.setData(entity.getData());

        return dto;
    }

    @Override
    public TestStepExecutionInfo mapEntity(TestStepExecution entity,
                                           Class<? extends TestStepExecutionInfo> dtoClass,
                                           Class<? extends DtoView<TestStepExecutionInfo>> view) {

        return mapEntity(entity, dtoClass);
    }

    @Override
    public TestStepExecutionInfo mapEntity(TestStepExecution entity,
                                           TestStepExecutionInfo dto,
                                           Class<? extends DtoView<TestStepExecutionInfo>> view) {

        return mapEntity(entity, dto);
    }

    @Override
    public TestStepExecution mapDto(TestStepExecutionInfo dto,
                                    Class<? extends TestStepExecution> entityClass) {

        return mapDto(dto, newInstance(entityClass));
    }

    @Override
    public TestStepExecution mapDto(TestStepExecutionInfo dto,
                                    TestStepExecution entity) {

        if (dto == null) {
            return null;
        }
        Preconditions.checkNotNull(entity);

        entity.setId(dto.getId());
        entity.setResult(referenceMapper.mapCastDto(dto.getResult(), TestStepResult.class));
        entity.setData(dto.getData());
        TestStep testStep = testStepRepository.find(dto.getTestStep());
        entity.setTestStep(testStep);

        if (dto.getTestExecution() != null) {
            TestExecution testExecution = testExecutionRepository.find(dto.getTestExecution());
            entity.setTestExecution(testExecution);
        }

        return entity;
    }

}
