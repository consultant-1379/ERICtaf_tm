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

import com.ericsson.cifwk.tm.application.services.ExcelObject;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeatureRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.domain.model.requirements.RequirementRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.Context;
import com.ericsson.cifwk.tm.domain.model.testdesign.Priority;
import com.ericsson.cifwk.tm.domain.model.testdesign.Scope;
import com.ericsson.cifwk.tm.domain.model.testdesign.ScopeRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseStatus;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestExecutionType;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestStepInfo;
import com.ericsson.cifwk.tm.presentation.dto.VerifyStepInfo;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import javax.inject.Inject;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ericsson.cifwk.tm.application.services.ReportingConstants.CONTEXT;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.DELIMITER;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.DESCRIPTION;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.EXECUTION_TYPE;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.FEATURE;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.COMPONENT;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.GROUPS;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.PRECONDITION;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.PRIORITY;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.REQUIREMENTS_IDS;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.STATUS;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.TEST_CASE_ID;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.TEST_DATA_REGEX;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.TEST_STEPS;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.TEST_STEP_REGEX;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.TEST_STEP_SPLITTER;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.TITLE;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.TYPE;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.VERIFY_STEP_REGEX;


public final class TestCaseImportMapper {


    @Inject
    private ScopeRepository scopeRepository;

    @Inject
    private RequirementRepository requirementRepository;

    @Inject
    private ProductFeatureRepository featureRepository;

    @Inject
    private ProductMapper productMapper;

    public List<TestCaseInfo> mapTestCases(ExcelObject excelObject) {

        List<TestCaseInfo> testCaseInfos = Lists.newArrayList();
        Long productId = null;

        for (LinkedHashMap<String, String> columns : excelObject.getRows()) {
            TestCaseInfo testCaseInfo = new TestCaseInfo();

            testCaseInfo.setTestCaseId(columns.get(TEST_CASE_ID));

            LinkedHashSet<String> requirements = getRequirements(columns.get(REQUIREMENTS_IDS), DELIMITER);


            Optional<Requirement> requirement = Optional.empty();
            if (requirements.iterator().hasNext()) {
                String next = requirements.iterator().next();
                requirement = Optional.ofNullable(
                        requirementRepository.findByExternalId(next));
            }


            testCaseInfo.setRequirementIds(requirements);
            testCaseInfo.setTitle(columns.get(TITLE));
            testCaseInfo.setDescription(columns.get(DESCRIPTION));
            testCaseInfo.setPrecondition(columns.get(PRECONDITION));
            testCaseInfo.setTechnicalComponents(getReferenceDataItems(columns.get(COMPONENT), DELIMITER));
            testCaseInfo.setType(new ReferenceDataItem(null, columns.get(TYPE)));

            Optional<Priority> priority = Priority.getEnum(columns.get(PRIORITY));
            if (priority.isPresent()) {
                testCaseInfo.setPriority(new ReferenceDataItem(
                        priority.get().getId().toString(),
                        priority.get().getName()));
            }

            if (requirement.isPresent()) {
                Requirement requirementParam = requirement.get();
                Optional<Project> project = Optional.ofNullable(requirementParam.getProject());
                if (project.isPresent()) {
                    productId = requirementParam.getProject().getProduct().getId();
                    testCaseInfo.setGroups(getGroups(columns.get(GROUPS), productId, DELIMITER));
                }
            }
            testCaseInfo.setContexts(getContexts(columns.get(CONTEXT), DELIMITER));

            if (productId != null) {
                Optional<ProductFeature> productFeature = Optional.ofNullable(
                        featureRepository.findByProductAndName(productId, columns.get(FEATURE)));

                if (productFeature.isPresent()) {
                    FeatureInfo featureInfo = new FeatureInfo(productFeature.get().getId(),
                            productFeature.get().getName());
                    testCaseInfo.setFeature(featureInfo);
                    featureInfo.setProduct(productMapper.mapEntity(productFeature.get().getProduct(), ProductInfo.class));
                }
            }

            Optional<TestExecutionType> executionType = TestExecutionType.getEnum(columns.get(EXECUTION_TYPE));
            if (executionType.isPresent()) {
                testCaseInfo.setExecutionType(new ReferenceDataItem(
                        executionType.get().getId().toString(),
                        executionType.get().getName()));
            }


            Optional<TestCaseStatus> testCaseStatus = TestCaseStatus.getByName(columns.get(STATUS));
            if (testCaseStatus.isPresent()) {
                testCaseInfo.setTestCaseStatus(new ReferenceDataItem(
                        testCaseStatus.get().getId().toString(),
                        testCaseStatus.get().getName()));
            }

            testCaseInfo.setTestSteps(getTestSteps(columns.get(TEST_STEPS)));
            testCaseInfos.add(testCaseInfo);

        }

        return testCaseInfos;

    }

    public LinkedHashSet<String> getRequirements(String rawRequirements, String delimiter) {
        Optional<String> valueCheck = Optional.ofNullable(rawRequirements);
        if (valueCheck.isPresent()) {
            List<String> requirements = Lists.newArrayList(Splitter.on(delimiter).split(rawRequirements));
            LinkedHashSet<String> result = new LinkedHashSet<>(requirements);
            return result;
        }
        return new LinkedHashSet<>();
    }

    public LinkedHashSet<ReferenceDataItem> getContexts(String values, String delimiter) {
        Optional<String> valueCheck = Optional.ofNullable(values);
        LinkedHashSet<ReferenceDataItem> result = new LinkedHashSet<>();
        if (valueCheck.isPresent()) {
            List<String> splitValues = Lists.newArrayList(Splitter.on(delimiter).split(values));
            for (String item : splitValues) {
                Optional<Context> context = Context.getEnum(item);
                if (context.isPresent()) {
                    result.add(new ReferenceDataItem(context.get().getId().toString(), context.get().getName()));
                }
            }
        }
        return result;
    }

    public Set<ReferenceDataItem> getReferenceDataItems(String values, String delimiter) {
        Optional<String> valueCheck = Optional.ofNullable(values);
        Set<ReferenceDataItem> result = new LinkedHashSet<>();
        if (valueCheck.isPresent()) {
            List<String> splitValues = Lists.newArrayList(Splitter.on(delimiter).split(values));
            for (String item : splitValues) {
                result.add(new ReferenceDataItem(null, item.trim()));

            }
        }
        return result;
    }

    public LinkedHashSet<ReferenceDataItem> getGroups(String values, Long productId, String delimiter) {
        Optional<String> valueCheck = Optional.ofNullable(values);
        LinkedHashSet<ReferenceDataItem> result = new LinkedHashSet<>();
        if (valueCheck.isPresent()) {
            List<String> splitValues = Lists.newArrayList(Splitter.on(delimiter).split(values));
            for (String item : splitValues) {
                Scope group = scopeRepository.findByNameAndProduct(item.trim(), productId);
                if (group != null) {
                    result.add(new ReferenceDataItem(group.getId().toString(), group.getName()));
                }
            }
        }

        return result;
    }

    public List<TestStepInfo> getTestSteps(String content) {
        List<TestStepInfo> testStepInfos = Lists.newArrayList();
        Optional<String> optionalContent = Optional.ofNullable(content);

        if (optionalContent.isPresent()) {
            List<String> testSteps = Lists.newArrayList(Splitter.on(TEST_STEP_SPLITTER).split(content));
            addTestSteps(testStepInfos, testSteps);
        }
        return testStepInfos;
    }

    private void addTestSteps(List<TestStepInfo> testStepInfos, List<String> testSteps) {
        for (String testStep : testSteps) {
            if (testStep.length() == 0) {
                break;
            }
            TestStepInfo testStepInfo = new TestStepInfo();
            testStepInfo.setName(getPattern(TEST_STEP_REGEX, testStep));
            Optional<String> name = Optional.ofNullable(testStepInfo.getName());
            if (name.isPresent()) {
                testStepInfo.setData(getPattern(TEST_DATA_REGEX, testStep));
                getVerifySteps(testStepInfo, testStep);
                testStepInfos.add(testStepInfo);
            }
        }
    }

    private String getPattern(String regex, String content) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }

    }

    private void getVerifySteps(TestStepInfo testStepInfo, String content) {
        Pattern pattern = Pattern.compile(VERIFY_STEP_REGEX);
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            VerifyStepInfo verifyStepInfo = new VerifyStepInfo();
            verifyStepInfo.setName(matcher.group(1));
            testStepInfo.addVerify(verifyStepInfo);
        }
    }
}

