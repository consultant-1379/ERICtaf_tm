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

import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.domain.model.testdesign.AutomationCandidate;
import com.ericsson.cifwk.tm.domain.model.testdesign.Context;
import com.ericsson.cifwk.tm.domain.model.testdesign.Priority;
import com.ericsson.cifwk.tm.domain.model.testdesign.ReviewGroup;
import com.ericsson.cifwk.tm.domain.model.testdesign.Scope;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseStatus;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersionRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestExecutionType;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestField;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestStep;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.RequirementInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReviewGroupInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestStepInfo;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCampaignView;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCaseView;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCaseViewFactory;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ericsson.cifwk.tm.domain.model.shared.ReferenceHelper.parseEnumByName;
import static com.ericsson.cifwk.tm.infrastructure.mapping.Mapping.newInstance;

public class TestCaseVersionMapper implements
        EntityMapper<TestCaseVersion, TestCaseInfo>, DtoMapper<TestCaseInfo, TestCaseVersion> {

    private final EnumReferenceMapper referenceMapper;
    private final TestStepMapper testStepMapper;
    private final RequirementMapper requirementMapper;
    private final TestCaseViewFactory testCaseViewFactory;
    private final TestCampaignRepository testCampaignRepository;
    private final TestCampaignMapper testCampaignMapper;
    private final FeatureMapper featureMapper;
    private final ReviewGroupMapper reviewGroupMapper;
    private final UserMapper userMapper;
    private final TestCaseVersionRepository testCaseVersionRepository;
    public static final String MAJOR = "major";
    public static final String MINOR = "minor";

    @Inject
    public TestCaseVersionMapper(
            EnumReferenceMapper referenceMapper,
            TestStepMapper testStepMapper,
            TestCaseViewFactory testCaseViewFactory,
            TestCampaignRepository testCampaignRepository,
            TestCampaignMapper testCampaignMapper,
            ProjectMapper projectMapper,
            FeatureMapper featureMapper,
            ReviewGroupMapper reviewGroupMapper,
            UserMapper userMapper,
            TestCaseVersionRepository testCaseVersionRepository) {
        this.referenceMapper = referenceMapper;
        this.testStepMapper = testStepMapper;
        this.testCaseViewFactory = testCaseViewFactory;
        this.testCampaignRepository = testCampaignRepository;
        this.testCampaignMapper = testCampaignMapper;
        this.featureMapper = featureMapper;
        this.reviewGroupMapper = reviewGroupMapper;
        this.userMapper = userMapper;
        this.testCaseVersionRepository = testCaseVersionRepository;

        TestCaseMapper testCaseMapper = new TestCaseMapper(this, testCaseViewFactory);
        this.requirementMapper = new RequirementMapper(testCaseMapper, projectMapper);
    }

    @Override
    public TestCaseInfo mapEntity(TestCaseVersion entity, Class<? extends TestCaseInfo> dtoClass) {
        return mapEntity(entity, newInstance(dtoClass));
    }

    @Override
    public TestCaseInfo mapEntity(TestCaseVersion entity, TestCaseInfo dto) {
        return mapEntity(entity, dto, testCaseViewFactory.getDefault());
    }

    @Override
    public TestCaseInfo mapEntity(
            TestCaseVersion entity,
            Class<? extends TestCaseInfo> dtoClass,
            Class<? extends DtoView<TestCaseInfo>> view) {
        return mapEntity(entity, newInstance(dtoClass), view);
    }

    @Override
    public TestCaseInfo mapEntity(
            TestCaseVersion entity,
            TestCaseInfo dto,
            Class<? extends DtoView<TestCaseInfo>> view) {
        if (entity == null) {
            return null;
        }
        Preconditions.checkNotNull(dto);

        dto.setId(entity.getId());
        dto.setVersion(entity.getMajorVersion() + "." + entity.getMinorVersion());
        dto.setTestCaseId(entity.getTestCase().getTestCaseId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setComment(entity.getComment());
        dto.setFeature(featureMapper.mapEntity(entity.getProductFeature(), FeatureInfo.class));
        dto.setType(referenceMapper.mapEntity(entity.getType(), new ReferenceDataItem()));
        dto.setTestSuite(referenceMapper.mapEntity(entity.getTestSuite(), new ReferenceDataItem()));
        dto.setTestTeam(referenceMapper.mapEntity(entity.getTestTeam(), new ReferenceDataItem()));
        dto.setReviewGroup(reviewGroupMapper.mapEntity(entity.getReviewGroup(), ReviewGroupInfo.class));
        dto.setReviewUser(userMapper.mapEntity(entity.getReviewUser(), UserInfo.class));
        dto.setExecutionType(referenceMapper.mapEntity(entity.getExecutionType(), new ReferenceDataItem()));
        mapComponentFields(entity, dto);
        addGroups(entity, dto);


        if (TestCaseView.SimpleRequirements.class.equals(view) ||
                TestCaseView.Detailed.class.equals(view)) {

            Set<Requirement> requirements = entity.getRequirements();
            dto.setRequirements(mapRequirements(requirements));

            List<String> requirementIds = Lists.newArrayList();
            for (Requirement requirement : requirements) {
                requirementIds.add(requirement.getExternalId());
            }
            Collections.sort(requirementIds);
            dto.setRequirementIds(Sets.newLinkedHashSet(requirementIds));

        }

        if (TestCaseView.Version.class.equals(view)) {
            List<TestCaseVersion> testCaseVersions = testCaseVersionRepository.findByTestCase(
                    entity.getTestCase().getTestCaseId());

            Set<ReferenceDataItem> mappedTestCases = testCaseVersions.stream()
                    .map(item -> new ReferenceDataItem(item.getId().toString(), item.getVersion()))
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            dto.setTestCaseVersions(mappedTestCases);
        }

        if (TestCaseView.Detailed.class.equals(view)) {
            return mapDetailedEntity(entity, dto);
        } else {
            return dto;
        }
    }

    private void addGroups(TestCaseVersion entity, TestCaseInfo dto) {
        for (Scope scope : entity.getScopes()) {
            ReferenceDataItem group = referenceMapper.mapEntity(scope, new ReferenceDataItem());
            dto.addGroup(group);
        }
    }

    private TestCaseInfo mapDetailedEntity(TestCaseVersion entity, TestCaseInfo dto) {
        dto.setPriority(referenceMapper.mapEntity(entity.getPriority(), new ReferenceDataItem()));
        dto.setPrecondition(entity.getPrecondition());
        dto.setAutomationCandidate(referenceMapper.mapEntity(entity.getAutomationCandidate(), new ReferenceDataItem()));
        dto.setTestCaseStatus(referenceMapper.mapEntity(entity.getTestCaseStatus(), new ReferenceDataItem()));
        dto.setComment(entity.getComment());

        dto.clearTestSteps();

        ImmutableList<TestStep> testSteps = FluentIterable.from(entity.getTestSteps())
                .toSortedList(new Comparator<TestStep>() {
                    @Override
                    public int compare(TestStep testStep, TestStep testStep2) {
                        return testStep.getSequenceOrder().compareTo(testStep2.getSequenceOrder());
                    }
                });

        for (TestStep testStep : testSteps) {
            dto.addTestStep(testStepMapper.mapEntity(testStep, TestStepInfo.class));
        }

        Map<String, TestField> fields = entity.getOptionalFieldsAsMap();
        mapEntityOptionalFields(dto, fields);

        dto.setAssociatedTestCampaigns(getAssociatedTestPlans(entity.getId()));
        dto.setIntrusive(entity.isIntrusive());
        dto.setIntrusiveComment(entity.getIntrusiveComment());

        return dto;
    }

    private void mapComponentFields(TestCaseVersion entity, TestCaseInfo dto) {
        Set<TechnicalComponent> technicalComponents = entity.getTechnicalComponents();
        technicalComponents.forEach(c -> {
                ReferenceDataItem componentDto = referenceMapper.mapEntity(c, new ReferenceDataItem());
                dto.addTechnicalComponent(componentDto);
            });
    }

    private void mapEntityOptionalFields(TestCaseInfo dto, Map<String, TestField> fields) {
        TestField packageField = fields.get(TestField.PACKAGE);
        if (packageField != null) {
            dto.setPackageName(packageField.getValue());
        }

        TestField contextField = fields.get(TestField.CONTEXT);
        if (contextField != null) {
            List<String> contexts = Arrays.asList(contextField.getValue().split(", "));
            for (String context : contexts) {
                Context referenceEnum = parseEnumByName(context, Context.class);
                dto.addContext(referenceMapper.mapEntity(referenceEnum, new ReferenceDataItem()));
            }
        }
    }

    private List<TestCampaignInfo> getAssociatedTestPlans(long id) {
        List<TestCampaignInfo> dtos = Lists.newArrayList();
        List<TestCampaign> testCampaigns = testCampaignRepository.findByTestCaseVersionId(id);
        for (TestCampaign testCampaign : testCampaigns) {
            TestCampaignInfo dto = testCampaignMapper.mapEntity(testCampaign, new TestCampaignInfo(),
                    TestCampaignView.Minimal.class);
            dtos.add(dto);
        }
        return dtos;
    }

    private List<RequirementInfo> mapRequirements(Set<Requirement> requirements) {
        List<RequirementInfo> dtos = new ArrayList<>(requirements.size());
        for (Requirement requirement : requirements) {
            RequirementInfo dto = requirementMapper.mapEntity(requirement, RequirementInfo.class);
            dtos.add(dto);
        }
        Collections.sort(dtos, new Comparator<RequirementInfo>() {
            @Override
            public int compare(RequirementInfo requirementInfo1, RequirementInfo requirementInfo2) {
                return requirementInfo1.getExternalId().compareTo(requirementInfo2.getExternalId());
            }
        });
        return dtos;
    }

    @Override
    public TestCaseVersion mapDto(TestCaseInfo dto, Class<? extends TestCaseVersion> entityClass) {
        return mapDto(dto, newInstance(entityClass));
    }

    @Override
    public TestCaseVersion mapDto(TestCaseInfo dto, TestCaseVersion entity) {
        if (dto == null) {
            return null;
        }
        Preconditions.checkNotNull(entity);

        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setExecutionType(referenceMapper.mapCastDto(dto.getExecutionType(), TestExecutionType.class));
        entity.setAutomationCandidate(referenceMapper.mapCastDto(
                dto.getAutomationCandidate(), AutomationCandidate.class));
        entity.setDescription(dto.getDescription());
        entity.setTestCaseStatus(referenceMapper.mapCastDto(dto.getTestCaseStatus(), TestCaseStatus.class));
        entity.setPriority(referenceMapper.mapCastDto(dto.getPriority(), Priority.class));
        entity.setPrecondition(dto.getPrecondition());
        entity.setComment(dto.getComment());
        entity.setProductFeature(featureMapper.mapDto(dto.getFeature(), ProductFeature.class));
        entity.setIntrusive(dto.isIntrusive());
        entity.setIntrusiveComment(dto.getIntrusiveComment());
        entity.setReviewGroup(reviewGroupMapper.mapDto(dto.getReviewGroup(), ReviewGroup.class));
        entity.setReviewUser(userMapper.mapDto(dto.getReviewUser(), User.class));

        Diff<TestStep> testStepDiff = Mapping.mapDiffAuditedAndOrdered(
                entity.getTestSteps(),
                dto.getTestSteps(),
                testStepMapper,
                TestStep.class
        );
        entity.clearTestSteps();
        for (TestStep testStep : testStepDiff.getAdded()) {
            entity.addTestStep(testStep);
        }

        mapDtoOptionalFields(dto, entity);

        return entity;
    }

    private void mapDtoOptionalFields(TestCaseInfo dto, TestCaseVersion entity) {
        entity.clearOptionalFields();
        entity.addOptionalField(new TestField(TestField.PACKAGE, dto.getPackageName()));

        Set<ReferenceDataItem> contexts = dto.getContexts();
        if (!contexts.isEmpty()) {
            entity.addOptionalField(new TestField(TestField.CONTEXT, Joiner.on(", ")
                    .join(Iterables.transform(contexts, new Function<ReferenceDataItem, String>() {
                        @Override
                        public String apply(ReferenceDataItem input) {
                            return input.getTitle();
                        }
                    }))));
        }
    }

    public Map<String, Long> getVersion(String version) {
        Map<String, Long> mappedVersion = new HashMap();
        if (version == null || version.isEmpty() || !version.contains(".")) {
            return mappedVersion;
        }
        String[] split = version.split("\\.");

        mappedVersion.put(MAJOR, Long.parseLong(split[0]));
        mappedVersion.put(MINOR, Long.parseLong(split[1]));
        return mappedVersion;
    }

}
