package com.ericsson.cifwk.tm.infrastructure.mapping;

import com.ericsson.cifwk.tm.domain.model.execution.TestExecutionResult;
import com.ericsson.cifwk.tm.presentation.dto.TestExecutionResultInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.google.common.base.Preconditions;

import static com.ericsson.cifwk.tm.infrastructure.mapping.Mapping.newInstance;

public class TestExecutionResultMapper implements EntityMapper<TestExecutionResult, TestExecutionResultInfo> {

    @Override
    public TestExecutionResultInfo mapEntity(
            TestExecutionResult entity,
            Class<? extends TestExecutionResultInfo> dtoClass) {

        return mapEntity(entity, newInstance(dtoClass));
    }

    @Override
    public TestExecutionResultInfo mapEntity(TestExecutionResult entity, TestExecutionResultInfo dto) {
        if (entity == null) {
            return null;
        }
        Preconditions.checkNotNull(dto);
        dto.setId(entity.getId().toString());
        dto.setTitle(entity.getName());
        dto.setSortOrder(entity.getSortOrder());
        return dto;
    }

    @Override
    public TestExecutionResultInfo mapEntity(
            TestExecutionResult entity,
            Class<? extends TestExecutionResultInfo> dtoClass,
            Class<? extends DtoView<TestExecutionResultInfo>> view) {

        return mapEntity(entity, dtoClass);
    }

    @Override
    public TestExecutionResultInfo mapEntity(
            TestExecutionResult entity,
            TestExecutionResultInfo dto,
            Class<? extends DtoView<TestExecutionResultInfo>> view) {

        return mapEntity(entity, dto);
    }
}
