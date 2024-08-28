package com.ericsson.cifwk.tm.fun.rest.testcampaigns;

import com.beust.jcommander.internal.Lists;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.tm.fun.common.BaseFuncTest;
import com.ericsson.cifwk.tm.fun.common.CustomAsserts;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.FeatureType;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.Paginated;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.ProductType;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.search.Condition;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.search.Criterion;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.search.Field;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.table.FilterInfo;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.table.TestCampaignsColumn;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.Result;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testplans.CreateEditTestCampaignResult;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignItemInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.google.common.collect.Sets;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.ericsson.cifwk.tm.fun.common.testcampaigns.TestCampaignsListTestHelper.checkThatTestPlanDetailsWereSavedCorrectly;
import static com.ericsson.cifwk.tm.fun.common.testcampaigns.TestCampaignsListTestHelper.getTestCampaignFilterSelection;
import static com.ericsson.cifwk.tm.fun.common.testcampaigns.TestCampaignsListTestHelper.setTestCampaignAttributes;
import static com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.JsonHelper.toTestPlanInfo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.testng.Assert.assertTrue;

public class TestCampaignsListTest extends BaseFuncTest {

    @Test
    @TestId(id = "DURACI-2526_Func_1", title = "Create/Delete Test Campaign without release")
    public void createDeleteTestCampaign() {
        createRestOperators();
        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmRestOperator.navigateToTestPlans());

        TestCampaignInfo testCampaignInfo = toTestPlanInfo(getEnmTestCampaignWithTestCases());
        testCampaignInfo.setName(UUID.randomUUID().toString().substring(0, 13));

        CreateEditTestCampaignResult result = tmRestOperator.createTestPlan(testCampaignInfo);
        CustomAsserts.checkTestStep(result);

        checkThatTestPlanDetailsWereSavedCorrectly(testCampaignInfo, result.getTestCampaignInfo());
        checkThatCorrectTestCasesArePresent(testCampaignInfo.getTestCampaignItems(),
                result.getTestCampaignInfo().getTestCampaignItems());

        CustomAsserts.checkTestStep(tmRestOperator.deleteTestPlan(result.getTestCampaignInfo()));
        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "DURACI-2526_Func_3", title = "Create/Edit/Delete Test Campaign")
    public void createEditDeleteTestCampaign() {
        createRestOperators();
        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmRestOperator.navigateToTestPlans());

        TestCampaignInfo testCampaignInfo = toTestPlanInfo(getEnmTestCampaignWithTestCases());
        testCampaignInfo.setName(UUID.randomUUID().toString().substring(0, 13));

        CreateEditTestCampaignResult createTestPlanResult = tmRestOperator.createTestPlan(testCampaignInfo);
        CustomAsserts.checkTestStep(createTestPlanResult);

        checkThatTestPlanDetailsWereSavedCorrectly(testCampaignInfo, createTestPlanResult.getTestCampaignInfo());
        checkThatCorrectTestCasesArePresent(testCampaignInfo.getTestCampaignItems(),
                createTestPlanResult.getTestCampaignInfo().getTestCampaignItems());

        TestCampaignInfo createdTestCampaignInfo = createTestPlanResult.getTestCampaignInfo();

        // update test campaign change description and environment
        setTestCampaignAttributes(createdTestCampaignInfo);
        CreateEditTestCampaignResult editTestPlanResult = tmRestOperator.editTestPlan(createdTestCampaignInfo);
        CustomAsserts.checkTestStep(editTestPlanResult);

        TestCampaignInfo editedTestCampaignInfo = editTestPlanResult.getTestCampaignInfo();

        checkThatTestPlanDetailsWereSavedCorrectly(createdTestCampaignInfo, editedTestCampaignInfo);
        checkThatCorrectTestCasesArePresent(createdTestCampaignInfo.getTestCampaignItems(),
                editedTestCampaignInfo.getTestCampaignItems());

        // update test campaign - add test case
        TestCaseInfo testCaseInfo = new TestCaseInfo();
        testCaseInfo.setId(18L);
        testCaseInfo.setTestCaseId("CIP-2638_Func_3");
        testCaseInfo.setVersion("1.1");

        TestCampaignItemInfo assignmentInfo = new TestCampaignItemInfo();
        assignmentInfo.setTestCase(testCaseInfo);
        createdTestCampaignInfo.getTestCampaignItems().add(assignmentInfo);

        editTestPlanResult = tmRestOperator.editTestPlan(createdTestCampaignInfo);
        CustomAsserts.checkTestStep(editTestPlanResult);

        editedTestCampaignInfo = editTestPlanResult.getTestCampaignInfo();

        checkThatTestPlanDetailsWereSavedCorrectly(createdTestCampaignInfo, editTestPlanResult.getTestCampaignInfo());
        checkThatCorrectTestCasesArePresent(createdTestCampaignInfo.getTestCampaignItems(),
                editedTestCampaignInfo.getTestCampaignItems());

        CustomAsserts.checkTestStep(tmRestOperator.deleteTestPlan(createdTestCampaignInfo));
        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "DURACI-2786_Func_1", title = "Create/Copy Test Campaign")
    public void createCopyTestCampaign() {
        createRestOperators();
        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmRestOperator.navigateToTestPlans());

        TestCampaignInfo testCampaignInfo = toTestPlanInfo(getEnmTestCampaignWithTestCases());
        testCampaignInfo.setName(UUID.randomUUID().toString().substring(0, 13));

        CreateEditTestCampaignResult createResult = tmRestOperator.createTestPlan(testCampaignInfo);
        CustomAsserts.checkTestStep(createResult);

        TestCampaignInfo createdTestCampaignInfo = createResult.getTestCampaignInfo();

        checkThatTestPlanDetailsWereSavedCorrectly(testCampaignInfo, createdTestCampaignInfo);
        checkThatCorrectTestCasesArePresent(testCampaignInfo.getTestCampaignItems(),
                createdTestCampaignInfo.getTestCampaignItems());

        CreateEditTestCampaignResult copyResult = tmRestOperator.copyTestPlan(createdTestCampaignInfo);
        CustomAsserts.checkTestStep(copyResult);

        TestCampaignInfo copiedTestCampaignInfo = copyResult.getTestCampaignInfo();

        assertThat(copiedTestCampaignInfo.getName(), equalTo(testCampaignInfo.getName() + " - COPY"));
        CustomAsserts.checkTestStep(tmRestOperator.deleteTestPlan(copiedTestCampaignInfo));
        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "DURACI-3315_Func_1", title = "Filter Test Campaigns")
    public void filterTestCampaigns() {
        createRestOperators();
        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmRestOperator.navigateToTestPlans());

        Set<FeatureInfo> featureInfos = Sets.newHashSet(FeatureType.ASSURE_OTHER.getFeatureInfo());

        TestCampaignInfo filterSelection = getTestCampaignFilterSelection(ProductType.ASSURE.getProductInfo(), Optional.empty(),
                featureInfos, Optional.empty());

        List<FilterInfo> tableFilters = Lists.newArrayList();
        tableFilters.add(new FilterInfo(TestCampaignsColumn.NAME, Condition.CONTAINS, "#"));
        tableFilters.add(new FilterInfo(TestCampaignsColumn.DESCRIPTION, Condition.CONTAINS, "to test defects"));
        tableFilters.add(new FilterInfo(TestCampaignsColumn.ENVIRONMENT, Condition.EQUALS, "test"));

        Result<Paginated<TestCampaignInfo>> result = tmRestOperator.filterTestPlans(filterSelection, tableFilters);
        CustomAsserts.checkTestStep(result);
        List<TestCampaignInfo> items = result.getValue().getItems();
        assertThat(items, hasSize(1));
        assertThat(items.get(0).getName(), equalTo("Test Plan #6"));
        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "DURACI-3248_Func_1", title = "Add multiple Test Cases to Test Campaign")
    public void addMultipleTestCasesToTestCampaign() {
        createRestOperators();
        String identifier = UUID.randomUUID().toString();
        prepareTestCases(identifier);

        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmRestOperator.navigateToTestPlans());

        TestCampaignInfo testCampaignInfo = toTestPlanInfo(getEnmTestCampaign());
        testCampaignInfo.setName(UUID.randomUUID().toString().substring(0, 13));

        List<Criterion> criterions = Lists.newArrayList();
        criterions.add(new Criterion(Field.ANY, Condition.CONTAINS, identifier));

        CreateEditTestCampaignResult testPlanResult = tmRestOperator.createTestPlanWithMultipleTestCases(testCampaignInfo, criterions);
        CustomAsserts.checkTestStep(testPlanResult);
        checkThatTestPlanDetailsWereSavedCorrectly(testCampaignInfo, testPlanResult.getTestCampaignInfo());

        Set<TestCampaignItemInfo> assignments = testPlanResult.getTestCampaignInfo().getTestCampaignItems();

        assertThat(assignments.size(), equalTo(3));

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    private void prepareTestCases(String description) {
        InputStream testCaseJson = getClass().getClassLoader().getResourceAsStream("data/multiple/searchMultipleTestCase_fullData.json");
        InputStream testCaseJson2 = getClass().getClassLoader().getResourceAsStream("data/multiple/searchMultipleTestCase2_fullData.json");
        InputStream testCaseJson3 = getClass().getClassLoader().getResourceAsStream("data/multiple/searchMultipleTestCase3_fullData.json");
        CustomAsserts.checkTestStep(this.testCaseFixturesOperator.login());
        CustomAsserts.checkTestStep(this.testCaseFixturesOperator.createTestCase(testCaseJson, description));
        CustomAsserts.checkTestStep(this.testCaseFixturesOperator.createTestCase(testCaseJson2, description));
        CustomAsserts.checkTestStep(this.testCaseFixturesOperator.createTestCase(testCaseJson3, description));
        CustomAsserts.checkTestStep(this.testCaseFixturesOperator.logout());
    }

    private void checkThatCorrectTestCasesArePresent(Set<TestCampaignItemInfo> expected, Set<TestCampaignItemInfo> actual) {
        Set<String> expectedIds = getTestCaseIds(expected);
        Set<String> actualIds = getTestCaseIds(actual);
        assertTrue(expectedIds.size() == actualIds.size() && actualIds.containsAll(expectedIds));
    }

    private Set<String> getTestCaseIds(Set<TestCampaignItemInfo> assignmentInfos) {
        Set<String> testCasesIds = Sets.newHashSet();
        for (TestCampaignItemInfo assignmentInfo : assignmentInfos) {
            testCasesIds.add(assignmentInfo.getTestCase().getTestCaseId());
        }
        return testCasesIds;
    }
}
