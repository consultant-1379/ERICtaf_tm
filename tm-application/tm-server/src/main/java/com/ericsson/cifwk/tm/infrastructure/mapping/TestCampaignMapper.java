/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.infrastructure.mapping;

import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecution;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecutionResult;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItem;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.Defect;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCampaignGroup;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersionRepository;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.presentation.dto.DefectInfo;
import com.ericsson.cifwk.tm.presentation.dto.DropInfo;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.ProjectInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.TechnicalComponentInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignItemInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestExecutionResultInfo;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCampaignItemView;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCampaignView;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCampaignViewFactory;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCaseViewFactory;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ericsson.cifwk.tm.infrastructure.mapping.Mapping.newInstance;

public class TestCampaignMapper implements EntityMapper<TestCampaign, TestCampaignInfo>,
        DtoMapper<TestCampaignInfo, TestCampaign> {

    private final ProjectMapper projectMapper;
    private final TestCampaignViewFactory testCampaignViewFactory;
    private final TestExecutionResultMapper testExecutionResultMapper;
    private final TestCaseVersionMapper testCaseVersionMapper;
    private final TestCampaignItemMapper testCampaignItemMapper;
    private final DefectMapper defectMapper;
    private final DropMapper dropMapper;
    private final FeatureMapper featureMapper;
    private final ProductMapper productMapper;
    private final ReviewGroupMapper reviewGroupMapper;
    private final UserMapper userMapper;
    private final EnumReferenceMapper enumReferenceMapper;
    private TestCaseVersionRepository testCaseVersionRepository;

    @Inject
    public TestCampaignMapper(
            ProjectMapper projectMapper,
            TestCampaignViewFactory testCampaignViewFactory,
            TestExecutionResultMapper testExecutionResultMapper,
            DefectMapper defectMapper,
            EnumReferenceMapper referenceMapper,
            TestStepMapper testStepMapper,
            TestCaseViewFactory testCaseViewFactory,
            TestCampaignRepository testCampaignRepository,
            TestCaseVersionRepository testCaseVersionRepository,
            DropMapper dropMapper,
            ProductMapper productMapper,
            FeatureMapper featureMapper,
            UserMapper userMapper,
            ReviewGroupMapper reviewGroupMapper) {
        this.projectMapper = projectMapper;
        this.testCampaignViewFactory = testCampaignViewFactory;
        this.testCaseVersionRepository = testCaseVersionRepository;
        this.testExecutionResultMapper = testExecutionResultMapper;
        this.defectMapper = defectMapper;
        this.enumReferenceMapper = referenceMapper;
        this.dropMapper = dropMapper;
        this.productMapper = productMapper;
        this.featureMapper = featureMapper;
        this.reviewGroupMapper = reviewGroupMapper;
        this.userMapper = userMapper;

        testCaseVersionMapper = new TestCaseVersionMapper(
                referenceMapper,
                testStepMapper,
                testCaseViewFactory,
                testCampaignRepository,
                this,
                projectMapper,
                featureMapper,
                reviewGroupMapper,
                userMapper,
                testCaseVersionRepository
        );
        this.testCampaignItemMapper = new TestCampaignItemMapper(
                testCaseVersionMapper,
                new EnumReferenceMapper(),
                new UserMapper()
        );
    }

    @VisibleForTesting
    public TestCaseVersionMapper getTestCaseVersionMapper() {
        return testCaseVersionMapper;
    }

    @Override
    public TestCampaignInfo mapEntity(TestCampaign entity, Class<? extends TestCampaignInfo> dtoClass) {
        return mapEntity(entity, newInstance(dtoClass));
    }

    @Override
    public TestCampaignInfo mapEntity(TestCampaign entity, TestCampaignInfo dto) {
        return mapEntity(entity, dto, testCampaignViewFactory.getDefault());
    }

    @Override
    public TestCampaignInfo mapEntity(
            TestCampaign entity,
            Class<? extends TestCampaignInfo> dtoClass,
            Class<? extends DtoView<TestCampaignInfo>> view) {
        return mapEntity(entity, newInstance(dtoClass), view);
    }

    @Override
    public TestCampaignInfo mapEntity(
            TestCampaign entity,
            TestCampaignInfo dto,
            Class<? extends DtoView<TestCampaignInfo>> view) {
        if (entity == null) {
            return null;
        }
        Preconditions.checkNotNull(dto);

        dto.setId(entity.getId());
        dto.setParentId(entity.getParentId());
        dto.setName(entity.getName());
        if (TestCampaignView.Minimal.class.equals(view)) {
            return dto;
        }
        dto.setDescription(entity.getDescription());
        dto.setEnvironment(entity.getEnvironment());
        dto.setDrop(dropMapper.mapEntity(entity.getDrop(), DropInfo.class));
        if (!entity.getFeatures().isEmpty()) {
            Product product = entity.getFeatures().stream().findFirst().get().getProduct();
            dto.setProduct(productMapper.mapEntity(product, ProductInfo.class));
            dto.setFeatures(mapFeatures(entity.getFeatures()));
            dto.setComponents(mapComponents(entity.getComponents()));
        }
        dto.setLocked(entity.isLocked());
        dto.setSystemVersion(entity.getSystemVersion());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setAutoCreate(entity.isAutoCreate());
        dto.setAuthor(userMapper.mapEntity(entity.getAuthor(), UserInfo.class));
        addGroups(entity, dto);

        if (TestCampaignView.Detailed.class.equals(view) ||
                TestCampaignView.DetailedItems.class.equals(view)) {
            dto.setProject(projectMapper.mapEntity(entity.getProject(), new ProjectInfo()));
            dto.setTestCampaignItems(mapTestPlanItems(entity, view));
        }

        dto.setPsFrom(entity.getPsFrom());
        dto.setPsTo(entity.getPsTo());
        dto.setGuideRevision(entity.getGuideRevision());
        dto.setSedRevision(entity.getSedRevision());
        dto.setOtherDependentSW(entity.getOtherDependentSW());
        dto.setNodeTypeVersion(entity.getNodeTypeVersion());
        dto.setSovStatus(entity.getSovStatus());
        dto.setComment(entity.getComment());

        return dto;
    }

    public List<TestCampaignInfo> mapEntities(List<TestCampaign> entities) {
        List<TestCampaignInfo> dtos = entities.stream()
                .map(entity -> mapEntity(entity, TestCampaignInfo.class))
                .collect(Collectors.toList());

        return dtos;
    }

    private Set<FeatureInfo> mapFeatures(Set<ProductFeature> entities) {
        Set<FeatureInfo> featureInfos = entities.stream()
                .map(entity -> featureMapper.mapEntity(entity, FeatureInfo.class))
                .collect(Collectors.toSet());

        return featureInfos;
    }

    private Set<TechnicalComponentInfo> mapComponents(Set<TechnicalComponent> entities) {
        Set<TechnicalComponentInfo> dtos = Sets.newHashSet();
        for (TechnicalComponent entity : entities) {
            dtos.add(new TechnicalComponentInfo(entity.getId(), entity.getName(), entity.getFeature().getName()));
        }
        return dtos;
    }

    private Set<TestCampaignItemInfo> mapTestPlanItems(
            TestCampaign entity,
            Class<? extends DtoView<TestCampaignInfo>> view) {

        Set<TestCampaignItemInfo> result = Sets.newTreeSet(new Comparator<TestCampaignItemInfo>() {
            @Override
            public int compare(TestCampaignItemInfo testPlanItemInfo1, TestCampaignItemInfo testPlanItemInfo2) {
                return testPlanItemInfo1.getTestCase().getId().compareTo(testPlanItemInfo2.getTestCase().getId());
            }
        });

        final Map<Long, TestExecution> lastResults = Maps.newHashMap();
        for (TestExecution execution : entity.getExecutions()) {
            Long testCaseId = execution.getTestCaseVersion().getTestCase().getId();
            TestExecution lastResult = lastResults.get(testCaseId);
            if (lastResult == null || lastResult.getCreatedAt().before(execution.getCreatedAt())) {
                lastResults.put(testCaseId, execution);
            }
        }

        for (TestCampaignItem testCampaignItem : entity.getTestCampaignItems()) {
            if (testCampaignItem.getTestCaseVersion().isDeleted()) {
                continue;
            }
            TestCampaignItemInfo testCampaignItemInfo =
                    testCampaignItemMapper.mapEntity(testCampaignItem, TestCampaignItemInfo.class,
                            getTestPlanItemView(view));

            TestCase testCase = testCampaignItem.getTestCaseVersion().getTestCase();
            List<TestCaseVersion> testCaseVersions = testCaseVersionRepository.findByTestCase(testCase.getId().toString());
            Set<ReferenceDataItem> mappedTestCases = testCaseVersions.stream()
                    .map(item -> new ReferenceDataItem(item.getId().toString(), item.getVersion()))
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            testCampaignItemInfo.setTestCaseVersions(mappedTestCases);

            TestExecution testExecution = lastResults.get(testCampaignItemInfo.getTestCase().getId());
            if (testExecution != null) {
                TestExecutionResult testResult = testExecution.getResult();
                ReferenceDataItem resultDto = testExecutionResultMapper.mapEntity(testResult,
                        new TestExecutionResultInfo());
                testCampaignItemInfo.setResult(resultDto);
                testCampaignItemInfo.setResultAuthor(userMapper.mapEntity(testExecution.getAuthor(), UserInfo.class));
                testCampaignItemInfo.getTestCase().setComment(testExecution.getComment());
                testCampaignItemInfo.setExecutionTime(testExecution.getExecutionTime());
                testCampaignItemInfo.setKpiMeasurement(testExecution.getKpiMeasurement());
                testCampaignItemInfo.setComment(testExecution.getComment());

                for (Defect defect : testExecution.getDefects()) {
                    DefectInfo defectInfo = defectMapper.mapEntity(defect, new DefectInfo());
                    testCampaignItemInfo.addDefect(defectInfo);
                }

                for (Requirement requirement : testExecution.getRequirements()) {
                    testCampaignItemInfo.addRequirementIds(requirement.getExternalId());
                }
            }
            result.add(testCampaignItemInfo);
        }
        return result;
    }

    private Class<? extends DtoView<TestCampaignItemInfo>> getTestPlanItemView(
            Class<? extends DtoView<TestCampaignInfo>> view) {
        if (TestCampaignView.DetailedItems.class.equals(view)) {
            return TestCampaignItemView.DetailedTestCase.class;
        } else {
            return TestCampaignItemView.Simple.class;
        }
    }

    @Override
    public TestCampaign mapDto(TestCampaignInfo dto, Class<? extends TestCampaign> entityClass) {
        return mapDto(dto, newInstance(entityClass));
    }

    @Override
    public TestCampaign mapDto(TestCampaignInfo dto, TestCampaign entity) {
        if (dto == null) {
            return null;
        }
        Preconditions.checkNotNull(entity);

        entity.setId(dto.getId());
        entity.setParentId(dto.getParentId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setEnvironment(dto.getEnvironment());
        entity.setSystemVersion(dto.getSystemVersion());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setAutoCreate(dto.isAutoCreate());
        entity.setAuthor(userMapper.mapDto(dto.getAuthor(), User.class));
        entity.setPsFrom(dto.getPsFrom());
        entity.setPsTo(dto.getPsTo());
        entity.setGuideRevision(dto.getGuideRevision());
        entity.setSedRevision(dto.getSedRevision());
        entity.setOtherDependentSW(dto.getOtherDependentSW());
        entity.setNodeTypeVersion(dto.getNodeTypeVersion());
        entity.setSovStatus(dto.getSovStatus());
        entity.setComment(dto.getComment());
        return entity;
    }

    private void addGroups(TestCampaign entity, TestCampaignInfo dto) {
        for (TestCampaignGroup item : entity.getGroups()) {
            ReferenceDataItem group = enumReferenceMapper.mapEntity(item, new ReferenceDataItem());
            dto.addGroup(group);
        }
    }

}
