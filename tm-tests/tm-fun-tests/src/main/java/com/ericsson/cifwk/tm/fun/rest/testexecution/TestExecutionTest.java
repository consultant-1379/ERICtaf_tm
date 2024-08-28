/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.rest.testexecution;

import com.beust.jcommander.internal.Lists;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.tm.fun.common.BaseFuncTest;
import com.ericsson.cifwk.tm.fun.common.CustomAsserts;
import com.ericsson.cifwk.tm.fun.ui.testcases.jsonobjects.references.TestCaseExecutions;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.DropType;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.FeatureType;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.ProductType;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.testexecution.references.TestExecutionResultType;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.testplan.TestCampaignExecutionsInfo;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.JsonHelper;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.search.Condition;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.search.Criterion;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.search.Field;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.Result;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testexecutions.TestCampaignExecutionResult;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testexecutions.TestExecutionResult;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignItemInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestExecutionInfo;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.ericsson.cifwk.tm.fun.common.testexecution.TestExecutionTestHelper.assertPrepopulatedTestExecutionInfo;
import static com.ericsson.cifwk.tm.fun.common.testexecution.TestExecutionTestHelper.assertTestExecution;
import static com.ericsson.cifwk.tm.fun.common.testexecution.TestExecutionTestHelper.createTestExecutionInfo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isIn;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TestExecutionTest extends BaseFuncTest {

    @Test
    @TestId(id = "DURACI-2269_Func_1", title = "View Test Plan Execution")
    public void viewTestExecution() {
        createRestOperators();
        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmRestOperator.navigateToTestPlans());
        TestCampaignInfo testCampaignInfo = createTestCampaign("Test Plan #1", ProductType.ENM, DropType.ENM_OTHER, getFeatures(FeatureType.ENM_OTHER));
        testCampaignInfo.setId(1L);
        CustomAsserts.checkTestStep(tmRestOperator.viewTestPlan(testCampaignInfo));

        CustomAsserts.checkTestStep(tmRestOperator.navigateToTestPlanExecution(testCampaignInfo));
        TestCampaignExecutionResult testCampaignExecutionResult = tmRestOperator.viewTestPlanExecution(testCampaignInfo);
        CustomAsserts.checkTestStep(testCampaignExecutionResult);

        TestCampaignInfo testCampaignInfoFromResult = testCampaignExecutionResult.getTestCaseExecutionsInfo().getTestCampaignInfo();
        assertEquals(testCampaignInfo.getName(), testCampaignInfoFromResult.getName());
        assertEquals("test", testCampaignInfoFromResult.getEnvironment());
        assertEquals("Test plan from V6_1 Migration (No project)", testCampaignInfoFromResult.getDescription());

        assertTestExecutions(testCampaignExecutionResult.getTestCaseExecutionsInfo());

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "DURACI-2269_Func_2", title = "CASE 1: Create Test Execution: standard test execution creation with existing defect")
    public void createTestExecution() {
        createRestOperators();
        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmRestOperator.navigateToTestPlans());

        TestCampaignInfo testCampaignInfo = createTestCampaign("Test Plan #2", ProductType.ENM, DropType.ENM_OTHER, getFeatures(FeatureType.ENM_OTHER));
        testCampaignInfo.setId(2L);

        CustomAsserts.checkTestStep(tmRestOperator.viewTestPlan(testCampaignInfo));
        CustomAsserts.checkTestStep(tmRestOperator.navigateToTestPlanExecution(testCampaignInfo));

        TestCampaignExecutionResult testCampaignExecutionResult = tmRestOperator.viewTestPlanExecution(testCampaignInfo);
        CustomAsserts.checkTestStep(testCampaignExecutionResult);

        Set<TestCampaignItemInfo> assignments = testCampaignExecutionResult.getTestCaseExecutionsInfo().getTestCampaignInfo().getTestCampaignItems();
        assertTestExecutions(testCampaignExecutionResult.getTestCaseExecutionsInfo());

        final TestCampaignItemInfo assignmentInfo = assignments.iterator().next();

        TestExecutionInfo testExecutionInfo = createTestExecutionInfo(TestExecutionResultType.WIP, "WIP comment",
                Sets.newHashSet("TORF-1234"),
                Sets.newHashSet("CIP-1234_1"),
                "23:45:00");
        testExecutionInfo.setTestPlan(testCampaignInfo.getId());

        CustomAsserts.checkTestStep(tmRestOperator.createTestExecution(testCampaignInfo, assignmentInfo, testExecutionInfo));

        // Check results after new test execution is added.
        testCampaignExecutionResult = tmRestOperator.viewTestPlanExecution(testCampaignInfo);
        CustomAsserts.checkTestStep(testCampaignExecutionResult);

        assignments = testCampaignExecutionResult.getTestCaseExecutionsInfo().getTestCampaignInfo().getTestCampaignItems();
        assertTestExecutions(testCampaignExecutionResult.getTestCaseExecutionsInfo());

        Optional<TestCampaignItemInfo> testCase = Iterables.tryFind(assignments, new Predicate<TestCampaignItemInfo>() {
            @Override
            public boolean apply(TestCampaignItemInfo input) {
                return assignmentInfo.getTestCase().getTestCaseId().equals(input.getTestCase().getTestCaseId());
            }
        });
        assertTrue(testCase.isPresent());
        TestCampaignItemInfo updatedTestCaseInfo = testCase.get();

        assertEquals(TestExecutionResultType.WIP.getTitle(), updatedTestCaseInfo.getResult().getTitle());

        List<TestCaseExecutions> testCaseExecutions = testCampaignExecutionResult.getTestCaseExecutionsInfo().getTestCaseExecutions();
        for (TestCaseExecutions testCaseExecutionsList : testCaseExecutions) {
            if(testCaseExecutionsList.getTestCaseId().equals(updatedTestCaseInfo.getTestCase().getTestCaseId())) {
                assertThat("TORF-1234", isIn(testCaseExecutionsList.getTestExecutions().get(0).getDefectIds()));
                assertThat("CIP-1234_1", isIn(testCaseExecutionsList.getTestExecutions().get(0).getRequirementIds()));
            }
        }

        assertTrue(updatedTestCaseInfo.getDefectIds().contains("TORF-1234"));

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "DURACI-2269_Func_3", title = "CASE 3: Create Test Execution: No Defects allowed for Passed execution")
    public void createTestExecution_WhenResultIsPass_NoDefectAllowed() {
        createRestOperators();
        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmRestOperator.navigateToTestPlans());

        TestCampaignInfo testCampaignInfo = createTestCampaign("Test Plan #3", ProductType.ENM, DropType.ENM_OTHER, getFeatures(FeatureType.ENM_OTHER));
        testCampaignInfo.setId(3L);

        CustomAsserts.checkTestStep(tmRestOperator.viewTestPlan(testCampaignInfo));
        CustomAsserts.checkTestStep(tmRestOperator.navigateToTestPlanExecution(testCampaignInfo));

        TestCampaignExecutionResult testCampaignExecutionResult = tmRestOperator.viewTestPlanExecution(testCampaignInfo);
        CustomAsserts.checkTestStep(testCampaignExecutionResult);

        Set<TestCampaignItemInfo> assignments = testCampaignExecutionResult.getTestCaseExecutionsInfo().getTestCampaignInfo().getTestCampaignItems();
        assertTestExecutions(testCampaignExecutionResult.getTestCaseExecutionsInfo());

        final TestCampaignItemInfo assignmentInfo = assignments.iterator().next();

        TestExecutionInfo testExecutionInfo = createTestExecutionInfo(TestExecutionResultType.PASS, "Passed comment", Sets.newHashSet("TORF-1234"), new HashSet<String>(), "15:45:05");
        CustomAsserts.checkTestStepFailed(tmRestOperator.createTestExecution(testCampaignInfo, assignmentInfo, testExecutionInfo));

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "DURACI-2269_Func_4", title = "CASE 2: Create Test Execution: defects are validated")
    public void createTestExecution_WhenDefectNotExists_ShouldRestrictExecutionCreate() {
        createRestOperators();
        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmRestOperator.navigateToTestPlans());

        TestCampaignInfo testCampaignInfo = createTestCampaign("Test Plan #4", ProductType.ENM, DropType.ENM_OTHER, getFeatures(FeatureType.ENM_OTHER));
        testCampaignInfo.setId(4L);

        CustomAsserts.checkTestStep(tmRestOperator.viewTestPlan(testCampaignInfo));
        CustomAsserts.checkTestStep(tmRestOperator.navigateToTestPlanExecution(testCampaignInfo));

        TestCampaignExecutionResult testCampaignExecutionResult = tmRestOperator.viewTestPlanExecution(testCampaignInfo);
        CustomAsserts.checkTestStep(testCampaignExecutionResult);

        Set<TestCampaignItemInfo> assignments = testCampaignExecutionResult.getTestCaseExecutionsInfo().getTestCampaignInfo().getTestCampaignItems();
        assertTestExecutions(testCampaignExecutionResult.getTestCaseExecutionsInfo());

        final TestCampaignItemInfo assignmentInfo = assignments.iterator().next();

        TestExecutionInfo testExecutionInfo = createTestExecutionInfo(TestExecutionResultType.NOT_STARTED, "Not started comment", Sets.newHashSet("BANANA-777"), new HashSet<String>(), "01:45:05");
        CustomAsserts.checkTestStepFailed(tmRestOperator.createTestExecution(testCampaignInfo, assignmentInfo, testExecutionInfo));

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "DURACI-2269_Func_5", title = "CASE 4: Create Test Execution: Execution with Result Passed is created without Defect Ids")
    public void createTestExecution_WhenResultIsPass_ShouldCreateWithoutDefects() {
        createRestOperators();
        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmRestOperator.navigateToTestPlans());

        TestCampaignInfo testCampaignInfo = createTestCampaign("Test Plan #7", ProductType.ASSURE, DropType.ASSURE_OTHER, getFeatures(FeatureType.ENM_OTHER));
        testCampaignInfo.setId(7L);

        CustomAsserts.checkTestStep(tmRestOperator.viewTestPlan(testCampaignInfo));
        CustomAsserts.checkTestStep(tmRestOperator.navigateToTestPlanExecution(testCampaignInfo));

        TestCampaignExecutionResult testCampaignExecutionResult = tmRestOperator.viewTestPlanExecution(testCampaignInfo);
        CustomAsserts.checkTestStep(testCampaignExecutionResult);

        Set<TestCampaignItemInfo> assignments = testCampaignExecutionResult.getTestCaseExecutionsInfo().getTestCampaignInfo().getTestCampaignItems();
        assertTestExecutions(testCampaignExecutionResult.getTestCaseExecutionsInfo());

        final TestCampaignItemInfo assignmentInfo = assignments.iterator().next();

        TestExecutionInfo testExecutionInfo = createTestExecutionInfo(TestExecutionResultType.PASS, "Passed comment", new HashSet<String>(), new HashSet<String>(), "23:20:05");
        testExecutionInfo.setTestPlan(testCampaignInfo.getId());
        CustomAsserts.checkTestStep(tmRestOperator.createPassedTestExecution(testCampaignInfo, assignmentInfo, testExecutionInfo));

        // Check results after new test execution is added.
        testCampaignExecutionResult = tmRestOperator.viewTestPlanExecution(testCampaignInfo);
        CustomAsserts.checkTestStep(testCampaignExecutionResult);

        assignments = testCampaignExecutionResult.getTestCaseExecutionsInfo().getTestCampaignInfo().getTestCampaignItems();
        assertTestExecutions(testCampaignExecutionResult.getTestCaseExecutionsInfo());

        Optional<TestCampaignItemInfo> assignmentInfoOptional = Iterables.tryFind(assignments, new Predicate<TestCampaignItemInfo>() {
            @Override
            public boolean apply(TestCampaignItemInfo input) {
                return assignmentInfo.getTestCase().getTestCaseId().equals(input.getTestCase().getTestCaseId());
            }
        });
        assertTrue(assignmentInfoOptional.isPresent());
        TestCampaignItemInfo updatedTestCaseInfo = assignmentInfoOptional.get();

        assertEquals(TestExecutionResultType.PASS.getTitle(), updatedTestCaseInfo.getResult().getTitle());
        assertTrue(updatedTestCaseInfo.getDefectIds().isEmpty());

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "DURACI-3005_Func_1", title = "Update actual Test Step result in Test Execution")
    public void updateTestExecutionTestSteps() {
        createRestOperators();
        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmRestOperator.navigateToTestPlans());

        TestCampaignInfo testCampaignInfo = createTestCampaign("Test Plan #6", ProductType.ASSURE, DropType.ASSURE_OTHER, getFeatures(FeatureType.ENM_OTHER));
        testCampaignInfo.setId(6L);

        CustomAsserts.checkTestStep(tmRestOperator.viewTestPlan(testCampaignInfo));
        CustomAsserts.checkTestStep(tmRestOperator.navigateToTestPlanExecution(testCampaignInfo));

        TestCampaignExecutionResult testCampaignExecutionResult = tmRestOperator.viewTestPlanExecution(testCampaignInfo);
        CustomAsserts.checkTestStep(testCampaignExecutionResult);

        Set<TestCampaignItemInfo> assignments = testCampaignExecutionResult.getTestCaseExecutionsInfo().getTestCampaignInfo().getTestCampaignItems();
        assertTestExecutions(testCampaignExecutionResult.getTestCaseExecutionsInfo());

        Optional<TestCampaignItemInfo> assignmentInfoOptional = Iterables.tryFind(assignments, new Predicate<TestCampaignItemInfo>() {
            @Override
            public boolean apply(TestCampaignItemInfo assignmentInfo) {
                return "test1".equals(assignmentInfo.getTestCase().getTestCaseId());
            }
        });
        assertTrue(assignmentInfoOptional.isPresent());

        InputStream testExecutionJson = getClass().getClassLoader().getResourceAsStream("data/testExecution_fullData.json");
        TestExecutionInfo testExecutionInfo = JsonHelper.toTestExecutionInfo(testExecutionJson);

        TestCampaignItemInfo assignmentInfo = assignmentInfoOptional.get();
        CustomAsserts.checkTestStep(tmRestOperator.updateTestExecutionInfo(testCampaignInfo, assignmentInfo, testExecutionInfo));

        TestExecutionResult testExecutionResult = tmRestOperator.getPrepopulatedTestExecutionInfo(testCampaignInfo, assignmentInfo);
        CustomAsserts.checkTestStep(testExecutionResult);
        assertPrepopulatedTestExecutionInfo(testExecutionInfo, testExecutionResult.getTestExecutionInfo());

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id="DURACI-3352_Func_1", title="Execute Advanced Search for Test Executions")
    public void executeAdvancedSearchForTestExecutions() {
        createRestOperators();
        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmRestOperator.navigateToTestPlans());

        TestCampaignInfo testCampaignInfo = createTestCampaign("Test Plan #5", ProductType.ENM, DropType.ENM_OTHER, getFeatures(FeatureType.ENM_OTHER));
        testCampaignInfo.setId(5L);
        CustomAsserts.checkTestStep(tmRestOperator.viewTestPlan(testCampaignInfo));
        CustomAsserts.checkTestStep(tmRestOperator.navigateToTestPlanExecution(testCampaignInfo));

        List<Criterion> criteria = Lists.newArrayList();
        criteria.add(new Criterion(Field.GROUP, Condition.EQUALS, "KPI"));

        Result<List<TestCampaignItemInfo>> result = tmRestOperator.searchForTestExecutions(testCampaignInfo, criteria);
        CustomAsserts.checkTestStep(result);
        assertThat(result.getValue(), hasSize(1));
        assertEquals("CIP-2638_Func_1", result.getValue().get(0).getTestCase().getTestCaseId());

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    private void assertTestExecutions(TestCampaignExecutionsInfo testCampaignExecutionsInfo) {
        Set<TestCampaignItemInfo> testCases = testCampaignExecutionsInfo.getTestCampaignInfo().getTestCampaignItems();
        testCampaignExecutionsInfo.getTestCaseExecutions();

        for (TestCampaignItemInfo testCase : testCases) {
            for(TestCaseExecutions testCaseExecutions : testCampaignExecutionsInfo.getTestCaseExecutions()) {
                if(testCaseExecutions.getTestCaseId().equals(testCase.getTestCase().getTestCaseId())) {
                    assertTestExecution(testCase, testCaseExecutions);
                }
            }
        }
    }

    private Set<FeatureType> getFeatures(FeatureType... featureType) {
        Set<FeatureType> featureTypes = Sets.newHashSet(featureType);
        return featureTypes;
    }

}
