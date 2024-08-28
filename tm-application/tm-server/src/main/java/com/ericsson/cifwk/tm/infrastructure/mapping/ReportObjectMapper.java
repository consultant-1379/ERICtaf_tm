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

import com.ericsson.cifwk.tm.application.services.ReportObject;
import com.ericsson.cifwk.tm.application.services.TestCaseService;
import com.ericsson.cifwk.tm.application.services.impl.SearchMapping;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItem;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItemRepository;
import com.ericsson.cifwk.tm.domain.model.shared.Paginated;
import com.ericsson.cifwk.tm.domain.model.shared.search.Query;
import com.ericsson.cifwk.tm.domain.model.shared.search.field.QueryField;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCampaignGroup;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCampaignGroupRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestStep;
import com.ericsson.cifwk.tm.domain.model.testdesign.VerifyStep;
import com.ericsson.cifwk.tm.files.FileCategory;
import com.ericsson.cifwk.tm.integration.fileStorage.FileManager;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignGroupInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignItemInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.ReportView;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCaseView;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.xml.XmlEscapers;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.nio.MappedByteBuffer;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


import static com.ericsson.cifwk.tm.infrastructure.mapping.ReportMapping.getTitle;
import static com.ericsson.cifwk.tm.infrastructure.mapping.ReportMapping.mapTitles;
import static java.lang.Math.ceil;

public final class ReportObjectMapper {

    public static final double PER_PAGE = 20.0;

    private static final String HEADING_SEPARATOR = ", ";

    @Inject
    private TestCampaignGroupRepository testCampaignGroupRepository;

    @Inject
    private TestCampaignGroupMapper testCampaignGroupMapper;

    @Inject
    private TestCaseService testCaseService;

    @Inject
    private TestCampaignItemRepository testCampaignItemRepository;

    @Inject
    private TestCaseVersionMapper testCaseVersionMapper;

    @Inject
    private SearchMapping searchMapping;

    @Inject
    private FileManager fileManager;


    private Search getSearch(long testPlanId, String q) {
        Map<String, QueryField> testPlanItemFields = searchMapping.getDetailedTestPlanItemFields();
        Query query = Query.fromQueryString(q);
        Search search = query.convertToSearch(TestCampaignItem.class, testPlanItemFields);
        search.addFilterEqual("testCampaign.id", testPlanId);

        return search;
    }

    public List<ReportObject> mapTestCasesForCampaigns(String queryString, Class view, long testPlanId) {

        List<ReportObject> reportObjects = Lists.newArrayList();
        Search search = getSearch(testPlanId, queryString);
        List<TestCampaignItem> filteredEntities = testCampaignItemRepository.search(search);

        for (TestCampaignItem testCase : filteredEntities) {
            ReportObject reportTestCase = new ReportObject();
            TestCaseVersion testCaseVersion = testCase.getTestCaseVersion();
            LinkedHashMap<String, String> variables = new LinkedHashMap<>();
            variables.put("title", prepareString(testCaseVersion.getTitle()));
            variables.put("Objective", prepareString(testCaseVersion.getDescription()));
            variables.put("Pass/Fail Criteria", "");

            List<TestStep> testSteps = Lists.newArrayList(testCaseVersion.getTestSteps());
            Collections.sort(testSteps);
            variables.put("Parameter Setting", prepareString(StringUtils.join(getTestData(testSteps), "\n")));
            variables.put("Impact on Network", "");
            variables.put("Precondition", prepareString(testCaseVersion.getPrecondition()));
            variables.put("Test Execution", "");

            reportTestCase.addHeaderInfo(variables);

            ReportObject testStep = createTestStep(testSteps, reportTestCase, "Action", "Result", false);

            ReportObject reportComment = new ReportObject();
            reportComment.setName("Comment: ");
            reportComment.setValue("No Comment");
            testStep.addChild(reportComment);

            if (ReportView.Detailed.class.equals(view)) {
                getFiles(testCaseVersion.getProductFeature().getProduct().getExternalId(),
                            testCase.getId(),
                            reportTestCase);
            }

            reportObjects.add(reportTestCase);
        }

        return reportObjects;
    }


    public List<ReportObject> mapTestCases(String queryString, Class view) {

        Query query = Query.fromQueryString(queryString);
        Paginated<TestCase> paginated = testCaseService.customSearch(query, 1, (int) PER_PAGE);

        List<ReportObject> reportObjects = Lists.newArrayList();
        int pages = (int) ceil(paginated.getTotal() / PER_PAGE);

        for (int i = 1; i <= pages; i++) {
            for (TestCase testCase : paginated.getResults()) {
                ReportObject reportTestCase = new ReportObject();
                TestCaseVersion testCaseVersion = testCase.getCurrentVersion();

                LinkedHashMap<String, String> variables = new LinkedHashMap<>();
                variables.put("title", prepareString(testCaseVersion.getTitle()));
                variables.put("Objective", prepareString(testCaseVersion.getDescription()));
                variables.put("Pass/Fail Criteria", "");

                List<TestStep> testSteps = Lists.newArrayList(testCaseVersion.getTestSteps());
                Collections.sort(testSteps);

                variables.put("Parameter Setting", prepareString(StringUtils.join(getTestData(testSteps), "\n")));

                variables.put("Impact on Network", "");
                variables.put("Precondition", prepareString(testCaseVersion.getPrecondition()));
                variables.put("Test Execution", "");

                reportTestCase.addHeaderInfo(variables);

                ReportObject testStep = createTestStep(testSteps, reportTestCase, "Action", "Result", false);

                ReportObject reportComment = new ReportObject();
                reportComment.setName("Comment: ");
                reportComment.setValue("No Comment");
                testStep.addChild(reportComment);

                if (ReportView.Detailed.class.equals(view)) {
                    getFiles(testCaseVersion.getProductFeature().getProduct().getExternalId(),
                            testCase.getId(),
                            reportTestCase);
                }

                reportObjects.add(reportTestCase);
            }
            if (i < pages) {
                paginated = testCaseService.customSearch(query, i + 1, (int) PER_PAGE);
            }

        }
        return reportObjects;
    }

    public List<ReportObject> mapSpecificTestCases(String queryString, Class view) {

        Query query = Query.fromQueryString(queryString);
        Paginated<TestCase> paginated = testCaseService.customSearch(query, 1, (int) PER_PAGE);

        Joiner joiner = Joiner.on(HEADING_SEPARATOR).skipNulls();

        List<ReportObject> reportObjects = Lists.newArrayList();
        int pages = (int) ceil(paginated.getTotal() / PER_PAGE);

        for (int i = 1; i <= pages; i++) {
            for (TestCase testCase : paginated.getResults()) {
                ReportObject reportTestCase = new ReportObject();
                TestCaseVersion testCaseVersion = testCase.getCurrentVersion();

                TestCaseInfo testCaseInfo = testCaseVersionMapper.mapEntity(testCaseVersion,
                        TestCaseInfo.class,
                        TestCaseView.Detailed.class);

                LinkedHashMap<String, String> variables = new LinkedHashMap<>();
                variables.put("title", prepareString(testCase.getTestCaseId()));
                variables.put("Requirement Id's", prepareString(joiner.join(testCaseInfo.getRequirementIds())));
                variables.put("Title", prepareString(testCaseInfo.getTitle()));
                variables.put("Description", prepareString(testCaseInfo.getDescription()));
                variables.put("Type", prepareString(getTitle(testCaseInfo.getType())));
                variables.put("Component", prepareString(joiner.join(mapTitles(testCaseInfo.getTechnicalComponents()))));
                variables.put("Priority", prepareString(getTitle(testCaseInfo.getPriority())));
                variables.put("Groups", prepareString(joiner.join(mapTitles(testCaseInfo.getGroups()))));
                variables.put("Context", prepareString(joiner.join(mapTitles(testCaseInfo.getContexts()))));
                variables.put("Precondition", prepareString(testCaseInfo.getPrecondition()));
                variables.put("Execution Type", prepareString(getTitle(testCaseInfo.getExecutionType())));
                variables.put("Status", prepareString(getTitle(testCaseInfo.getTestCaseStatus())));
                variables.put("Test Steps", "");

                List<TestStep> testSteps = Lists.newArrayList(testCaseVersion.getTestSteps());
                Collections.sort(testSteps);

                reportTestCase.addHeaderInfo(variables);

                createTestStep(testSteps, reportTestCase, "Test Step", "Verify Step", true);

                if (ReportView.Detailed.class.equals(view)) {
                    getFiles(testCaseVersion.getProductFeature().getProduct().getExternalId(),
                            testCase.getId(),
                            reportTestCase);
                }

                reportObjects.add(reportTestCase);
            }
            if (i < pages) {
                paginated = testCaseService.customSearch(query, i + 1, (int) PER_PAGE);
            }

        }
        return reportObjects;
    }

    public static String prepareString(String s) {
        return XmlEscapers.xmlContentEscaper().escape(Strings.nullToEmpty(s));
    }

    public ReportObject createTestStep(List<TestStep> testSteps,
                                       ReportObject reportObject,
                                       String testStepName,
                                       String verifyName,
                                       boolean testData) {

        for (int testStepIndex = 0; testStepIndex < testSteps.size(); testStepIndex++) {
            ReportObject reportTestStep = new ReportObject();
            TestStep testStep = testSteps.get(testStepIndex);


            reportTestStep.setName(String.format(testStepName + " %d: ", testStepIndex + 1));
            reportTestStep.setValue(testStep.getTitle());
            if (testData) {
                addTestData(reportTestStep, testStep);
            }
            List<VerifyStep> verifications = Lists.newArrayList(testStep.getVerifications());
            Collections.sort(verifications);
            String verifyStepTitle = ((testSteps.size() > 1 || verifications.size() > 1) ? "\t" : "")
                    + verifyName + " %d.%d: ";

            createVerifyStep(verifications, testStepIndex, verifyStepTitle, reportTestStep);
            reportObject.addChild(reportTestStep);
        }
        return reportObject;
    }

    private void addTestData(ReportObject reportTestStep, TestStep testStep) {
        ReportObject reportTestData = new ReportObject();
        reportTestData.setName("Test Data: ");
        if (testStep.getData() != null) {
            reportTestData.setValue(testStep.getData());
        } else {
            reportTestData.setValue("");
        }
        reportTestStep.addChild(reportTestData);
    }

    public ReportObject createVerifyStep(List<VerifyStep> verifications, int testStepIndex,
                                         String verifyStepTitle, ReportObject reportObject) {

        for (int verifyStepIndex = 0; verifyStepIndex < verifications.size(); verifyStepIndex++) {
            VerifyStep verifyStep = verifications.get(verifyStepIndex);
            ReportObject reportVerifyStep = new ReportObject();
            String text = String.format(verifyStepTitle, testStepIndex + 1,
                    verifyStepIndex + 1);

            reportVerifyStep.setName(text);
            reportVerifyStep.setValue(verifyStep.getVerifyStep());
            reportObject.addChild(reportVerifyStep);
        }

        return reportObject;
    }

    public ReportObject getFiles(String product, Long entityId, ReportObject reportObject) {
        Map<String, MappedByteBuffer> files = fileManager.findFileBufferForEntity(product, FileCategory.TEST_CASE_FILE, entityId);
        if (files != null) {
            reportObject.setFiles(files);
        }
        return reportObject;
    }

    public List<String> getTestData(List<TestStep> testSteps) {
        List<String> result = Lists.newArrayList();

        for (TestStep testStep : testSteps) {
            if (StringUtils.isNotEmpty(testStep.getData())) {
                result.add(testStep.getData());
            }
        }
        return result;
    }

    public List<ReportObject> getTestCampaignGroupInfo(Long id) {
        Optional<TestCampaignGroup> testCampaignGroup = Optional.ofNullable(testCampaignGroupRepository.find(id));
        if (!testCampaignGroup.isPresent()) {
            throw new NotFoundException();
        }
        TestCampaignGroupInfo testCampaignGroupInfo = testCampaignGroupMapper.mapEntity(testCampaignGroup.get(),
                TestCampaignGroupInfo.class);
        List<ReportObject> reportObjects = Lists.newArrayList();
        int pages = (int) ceil(testCampaignGroupInfo.getTestCampaigns().size() / PER_PAGE);
        for (int i = 1; i <= pages; i++) {
            for (TestCampaignInfo testCampaignInfo : testCampaignGroupInfo.getTestCampaigns()) {
                ReportObject reportTestCampaign = new ReportObject();
                List<String> values = Lists.newArrayList();
                values.add(testCampaignInfo.getName());
                values.add(testCampaignInfo.getEnvironment());
                values.add(testCampaignInfo.getPsFrom());
                values.add(testCampaignInfo.getPsTo());
                values.add(testCampaignInfo.getGuideRevision());
                values.add(testCampaignInfo.getSedRevision());
                values.add(testCampaignInfo.getOtherDependentSW());
                values.add(testCampaignInfo.getNodeTypeVersion());
                List<TestCampaignItemInfo> testSteps = Lists.newArrayList(testCampaignInfo.getTestCampaignItems());
                reportTestCampaign.addTestCampaignInfo(values);
                reportTestCampaign.addTestCasesList(createTestCase(testSteps));
                reportObjects.add(reportTestCampaign);
            }
        }
        return reportObjects;
    }

    public List<ReportObject> createTestCase(List<TestCampaignItemInfo> testSteps) {
        Joiner joiner = Joiner.on(HEADING_SEPARATOR).skipNulls();
        List<ReportObject> reportObjects = Lists.newArrayList();
        int pages = (int) ceil(testSteps.size() / PER_PAGE);
        for (int i = 1; i <= pages; i++) {
            for (TestCampaignItemInfo testCampaignItemInfo : testSteps) {
                ReportObject reportTestCampaign = new ReportObject();
                List<String> values = Lists.newArrayList();
                values.add(testCampaignItemInfo.getTestCase().getTestCaseId());
                values.add(testCampaignItemInfo.getTestCase().getTitle());
                if (testCampaignItemInfo.getResult() == null) {
                    values.add("");
                } else {
                    values.add(testCampaignItemInfo.getResult().getTitle());
                }
                values.add(testCampaignItemInfo.getComment());
                values.add(joiner.join(testCampaignItemInfo.getDefectIds()));
                reportTestCampaign.addTestCaseInfo(values);
                reportObjects.add(reportTestCampaign);
            }
        }
        return reportObjects;
    }

    public List<ReportObject> getTestCampaignSubGroupInfo(Long id, String testCampaignId) {
        Optional<TestCampaignGroup> testCampaignGroup = Optional.ofNullable(testCampaignGroupRepository.find(id));
        if (!testCampaignGroup.isPresent()) {
            throw new NotFoundException();
        }
        TestCampaignGroupInfo testCampaignGroupInfo = testCampaignGroupMapper.mapEntity(testCampaignGroup.get(),
                TestCampaignGroupInfo.class);

        List<ReportObject> reportObjects = Lists.newArrayList();
        int pages = (int) ceil(testCampaignGroupInfo.getTestCampaigns().size() / PER_PAGE);
        for (int i = 1; i <= pages; i++) {
            Set<TestCampaignInfo> tci = Sets.newLinkedHashSet(testCampaignGroupInfo.getTestCampaigns());
            String[] testCampaignIdList = testCampaignId.split(",");
            for (String idNew : testCampaignIdList) {
                for (TestCampaignInfo testCampaignInfo : tci) {
                    if ((idNew).equals(testCampaignInfo.getId().toString())) {
                        ReportObject reportTestCampaign = new ReportObject();
                        List<String> values = Lists.newArrayList();
                        values.add(testCampaignInfo.getName());
                        values.add(testCampaignInfo.getEnvironment());
                        values.add(testCampaignInfo.getPsFrom());
                        values.add(testCampaignInfo.getPsTo());
                        values.add(testCampaignInfo.getGuideRevision());
                        values.add(testCampaignInfo.getSedRevision());
                        values.add(testCampaignInfo.getOtherDependentSW());
                        values.add(testCampaignInfo.getNodeTypeVersion());
                        List<TestCampaignItemInfo> testSteps = Lists.newArrayList(testCampaignInfo.getTestCampaignItems());
                        reportTestCampaign.addTestCampaignInfo(values);
                        reportTestCampaign.addTestCasesList(createTestCase(testSteps));
                        reportObjects.add(reportTestCampaign);
                    }
                }
            }
        }
        return reportObjects;
    }
}

