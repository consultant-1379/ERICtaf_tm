package com.ericsson.cifwk.tm.infrastructure.mapping;

import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCaseViewFactory;

import javax.inject.Inject;

import static com.ericsson.cifwk.tm.infrastructure.mapping.Mapping.newInstance;

/**
 *
 */
public class TestCaseMapper implements EntityMapper<TestCase, TestCaseInfo>, DtoMapper<TestCaseInfo, TestCase> {

    private final TestCaseVersionMapper testCaseVersionMapper;
    private final TestCaseViewFactory testCaseViewFactory;

    @Inject
    public TestCaseMapper(
            TestCaseVersionMapper testCaseVersionMapper,
            TestCaseViewFactory testCaseViewFactory) {
        this.testCaseVersionMapper = testCaseVersionMapper;
        this.testCaseViewFactory = testCaseViewFactory;
    }

    @Override
    public TestCase mapDto(
            TestCaseInfo dto,
            Class<? extends TestCase> entityClass) {
        TestCase testCase = newInstance(entityClass);
        return mapDto(dto, testCase);
    }

    @Override
    public TestCase mapDto(
            TestCaseInfo dto,
            TestCase entity) {
        TestCaseVersion currentVersion = entity.getCurrentVersion();

        Long preservedId = currentVersion.getId();
        testCaseVersionMapper.mapDto(dto, currentVersion);
        currentVersion.setId(preservedId);

        entity.setId(dto.getId());
        entity.setTestCaseId(dto.getTestCaseId());

        return entity;
    }

    @Override
    public TestCaseInfo mapEntity(
            TestCase entity,
            Class<? extends TestCaseInfo> dtoClass) {
        if (entity == null) {
            return null;
        }
        TestCaseInfo dto = newInstance(dtoClass);
        return mapEntity(entity, dto);
    }

    @Override
    public TestCaseInfo mapEntity(
            TestCase entity,
            TestCaseInfo dto) {
        return mapEntity(entity, dto, testCaseViewFactory.getDefault());
    }

    @Override
    public TestCaseInfo mapEntity(
            TestCase entity,
            Class<? extends TestCaseInfo> dtoClass,
            Class<? extends DtoView<TestCaseInfo>> view) {
        return mapEntity(entity, newInstance(dtoClass), view);
    }

    @Override
    public TestCaseInfo mapEntity(
            TestCase entity,
            TestCaseInfo dto,
            Class<? extends DtoView<TestCaseInfo>> view) {

        TestCaseVersion currentVersion = entity.getCurrentVersion();
        TestCaseInfo result = testCaseVersionMapper.mapEntity(currentVersion, dto, view);

        result.setId(entity.getId());
        result.setTestCaseId(entity.getTestCaseId());

        return result;
    }

}
