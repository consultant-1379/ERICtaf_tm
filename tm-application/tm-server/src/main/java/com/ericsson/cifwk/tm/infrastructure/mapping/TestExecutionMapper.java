package com.ericsson.cifwk.tm.infrastructure.mapping;

import com.ericsson.cifwk.tm.domain.model.execution.TestExecution;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecutionResult;
import com.ericsson.cifwk.tm.domain.model.execution.TestStepExecution;
import com.ericsson.cifwk.tm.domain.model.execution.VerifyStepExecution;
import com.ericsson.cifwk.tm.domain.model.requirements.Defect;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.presentation.dto.IsoInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.TestExecutionInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestStepExecutionInfo;
import com.ericsson.cifwk.tm.presentation.dto.VerifyStepExecutionInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.ericsson.cifwk.tm.presentation.dto.view.TestExecutionView;
import com.ericsson.cifwk.tm.presentation.dto.view.TestExecutionViewFactory;
import com.google.common.base.Preconditions;

import javax.inject.Inject;
import java.util.List;

import static com.ericsson.cifwk.tm.infrastructure.mapping.Mapping.newInstance;

public class TestExecutionMapper
        implements EntityMapper<TestExecution, TestExecutionInfo>, DtoMapper<TestExecutionInfo, TestExecution> {

    private final EnumReferenceMapper referenceMapper;

    private final TestStepExecutionMapper testStepExecutionMapper;

    private final IsoMapper isoMapper;

    private final VerifyStepExecutionMapper verifyStepExecutionMapper;

    private final TestExecutionViewFactory testExecutionViewFactory;

    @Inject
    public TestExecutionMapper(EnumReferenceMapper referenceMapper,
                               TestStepExecutionMapper testStepExecutionMapper,
                               VerifyStepExecutionMapper verifyStepExecutionMapper,
                               IsoMapper isoMapper,
                               TestExecutionViewFactory testExecutionViewFactory) {
        this.referenceMapper = referenceMapper;
        this.testStepExecutionMapper = testStepExecutionMapper;
        this.verifyStepExecutionMapper = verifyStepExecutionMapper;
        this.isoMapper = isoMapper;
        this.testExecutionViewFactory = testExecutionViewFactory;
    }

    @Override
    public TestExecution mapDto(TestExecutionInfo dto, Class<? extends TestExecution> entityClass) {
        return mapDto(dto, newInstance(entityClass));
    }

    @Override
    public TestExecution mapDto(TestExecutionInfo dto, TestExecution entity) {
        if (dto == null) {
            return null;
        }
        Preconditions.checkNotNull(entity);
        entity.setId(dto.getId());
        entity.setCreatedAt(dto.getCreatedAt());
        TestExecutionResult result = referenceMapper.mapCastDto(dto.getResult(), TestExecutionResult.class);
        entity.setResult(result);
        entity.setComment(dto.getComment());
        entity.setExecutionTime(dto.getExecutionTime());
        entity.setKpiMeasurement(dto.getKpiMeasurement());

        List<TestStepExecutionInfo> testStepExecutionDtos = dto.getTestStepExecutions();
        for (TestStepExecutionInfo testStepExecutionInfo : testStepExecutionDtos) {
            TestStepExecution testStepExecution =
                    testStepExecutionMapper.mapDto(testStepExecutionInfo, new TestStepExecution());
            entity.addTestStepExecution(testStepExecution);
        }

        List<VerifyStepExecutionInfo> verifyStepExecutionDtos = dto.getVerifyStepExecutions();
        for (VerifyStepExecutionInfo verifyStepExecutionInfo : verifyStepExecutionDtos) {
            VerifyStepExecution verifyStepExecution =
                    verifyStepExecutionMapper.mapDto(verifyStepExecutionInfo, new VerifyStepExecution());
            entity.addVerifyStepExecution(verifyStepExecution);
        }

        return entity;
    }

    @Override
    public TestExecutionInfo mapEntity(TestExecution entity, Class<? extends TestExecutionInfo> dtoClass) {
        return mapEntity(entity, newInstance(dtoClass));
    }

    @Override
    public TestExecutionInfo mapEntity(TestExecution entity, TestExecutionInfo dto) {
        return mapEntity(entity, dto, testExecutionViewFactory.getDefault());
    }

    @Override
    public TestExecutionInfo mapEntity(
            TestExecution entity,
            Class<? extends TestExecutionInfo> dtoClass,
            Class<? extends DtoView<TestExecutionInfo>> view) {
        return mapEntity(entity, newInstance(dtoClass), view);
    }

    @Override
    public TestExecutionInfo mapEntity(
            TestExecution entity,
            TestExecutionInfo dto,
            Class<? extends DtoView<TestExecutionInfo>> view) {

        if (entity == null) {
            return null;
        }
        Preconditions.checkNotNull(dto);
        dto.setId(entity.getId());
        Product product = entity.getTestCampaign().getFeatures().stream().findFirst().get().getProduct();
        dto.setProduct(product.getName());
        dto.setTestPlan(entity.getTestCampaign().getId());
        dto.setTestCase(entity.getTestCaseVersion().getTestCase().getId());
        dto.setCreatedAt(entity.getCreatedAt());

        User author = entity.getAuthor();
        if (author != null) {
            dto.setAuthor(author.getUserName());
        }

        ReferenceDataItem result = referenceMapper.mapEntity(entity.getResult(), new ReferenceDataItem());
        dto.setResult(result);
        dto.setExecutionTime(entity.getExecutionTime());
        dto.setKpiMeasurement(entity.getKpiMeasurement());
        dto.setComment(entity.getComment());

        for (Defect defect : entity.getDefects()) {
            dto.getDefectIds().add(defect.getExternalId());
        }

        for (Requirement requirement : entity.getRequirements()) {
            dto.getRequirementIds().add(requirement.getExternalId());
        }

        if (TestExecutionView.Detailed.class.equals(view)) {
            for (TestStepExecution testStepExecution : entity.getTestStepExecutions()) {
                TestStepExecutionInfo info =
                        testStepExecutionMapper.mapEntity(testStepExecution, new TestStepExecutionInfo());
                dto.addTestStepExecution(info);
            }

            for (VerifyStepExecution verifyStepExecution : entity.getVerifyStepExecutions()) {
                VerifyStepExecutionInfo info =
                        verifyStepExecutionMapper.mapEntity(verifyStepExecution, new VerifyStepExecutionInfo());
                dto.addVerifyStepExecution(info);
            }
        }

        dto.setIso(isoMapper.mapEntity(entity.getIso(), IsoInfo.class));

        return dto;
    }


}
