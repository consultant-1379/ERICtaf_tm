package com.ericsson.cifwk.tm.fun.ui.testcampaigns;

import com.beust.jcommander.internal.Lists;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.tm.fun.common.BaseFuncTest;
import com.ericsson.cifwk.tm.fun.common.CustomAsserts;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.DropType;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.FeatureType;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.Paginated;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.ProductType;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.TechnicalComponentType;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.search.Condition;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.search.Criterion;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.search.Field;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.table.FilterInfo;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.table.TestCampaignsColumn;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.Result;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testplans.AutoCompleteResult;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testplans.CreateEditTestCampaignResult;
import com.ericsson.cifwk.tm.presentation.dto.DropInfo;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignItemInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import com.google.common.collect.Sets;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ericsson.cifwk.tm.fun.common.testcampaigns.TestCampaignsListTestHelper.checkThatTestPlanDetailsWereSavedCorrectly;
import static com.ericsson.cifwk.tm.fun.common.testcampaigns.TestCampaignsListTestHelper.getTestCampaignFilterSelection;
import static com.ericsson.cifwk.tm.fun.common.testcampaigns.TestCampaignsListTestHelper.setTestCampaignAttributes;
import static com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.JsonHelper.toTestPlanInfo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isIn;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class TestCampaignsListTest extends BaseFuncTest {

    @Test
    @TestId(id = "DURACI-2526_Func_1", title = "Create/Delete Test Campaign without release")
    public void createDeleteTestCampaign() {
        createUiOperators();
        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmUiOperator.navigateToTestPlans());

        TestCampaignInfo testCampaignInfo = toTestPlanInfo(getEnmTestCampaignWithTestCases());
        testCampaignInfo.setName(UUID.randomUUID().toString().substring(0, 13));

        CreateEditTestCampaignResult result = tmUiOperator.createTestPlan(testCampaignInfo);
        CustomAsserts.checkTestStep(result);

        checkThatTestPlanDetailsWereSavedCorrectly(testCampaignInfo, result.getTestCampaignInfo());
        checkThatCorrectTestCasesArePresent(testCampaignInfo.getTestCampaignItems(),
                result.getTestCampaignInfo().getTestCampaignItems());

        CustomAsserts.checkTestStep(tmUiOperator.deleteTestPlan(result.getTestCampaignInfo()));
        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "DURACI-2526_Func_3", title = "Create/Edit/Delete Test Campaign")
    public void createEditDeleteTestCampaign() {
        createUiOperators();
        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmUiOperator.navigateToTestPlans());

        TestCampaignInfo testCampaignInfo = toTestPlanInfo(getEnmTestCampaignWithTestCases());
        testCampaignInfo.setName(UUID.randomUUID().toString().substring(0, 13));

        CreateEditTestCampaignResult createTestPlanResult = tmUiOperator.createTestPlan(testCampaignInfo);
        CustomAsserts.checkTestStep(createTestPlanResult);

        checkThatTestPlanDetailsWereSavedCorrectly(testCampaignInfo, createTestPlanResult.getTestCampaignInfo());
        checkThatCorrectTestCasesArePresent(testCampaignInfo.getTestCampaignItems(),
                createTestPlanResult.getTestCampaignInfo().getTestCampaignItems());

        TestCampaignInfo createdTestCampaignInfo = createTestPlanResult.getTestCampaignInfo();

        // update test campaign change description and environment
        setTestCampaignAttributes(createdTestCampaignInfo);
        CreateEditTestCampaignResult editTestPlanResult = tmUiOperator.editTestPlan(createdTestCampaignInfo);
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

        editTestPlanResult = tmUiOperator.editTestPlan(createdTestCampaignInfo);
        CustomAsserts.checkTestStep(editTestPlanResult);

        editedTestCampaignInfo = editTestPlanResult.getTestCampaignInfo();

        checkThatTestPlanDetailsWereSavedCorrectly(createdTestCampaignInfo, editTestPlanResult.getTestCampaignInfo());
        checkThatCorrectTestCasesArePresent(createdTestCampaignInfo.getTestCampaignItems(),
                editedTestCampaignInfo.getTestCampaignItems());

        CustomAsserts.checkTestStep(tmUiOperator.deleteTestPlan(createdTestCampaignInfo));
        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "DURACI-2786_Func_1", title = "Create/Copy Test Campaign")
    public void createCopyTestCampaign() {
        createUiOperators();
        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmUiOperator.navigateToTestPlans());

        TestCampaignInfo testCampaignInfo = toTestPlanInfo(getEnmTestCampaignWithTestCases());
        testCampaignInfo.setName(UUID.randomUUID().toString().substring(0, 13));

        CreateEditTestCampaignResult createResult = tmUiOperator.createTestPlan(testCampaignInfo);
        CustomAsserts.checkTestStep(createResult);

        TestCampaignInfo createdTestCampaignInfo = createResult.getTestCampaignInfo();

        checkThatTestPlanDetailsWereSavedCorrectly(testCampaignInfo, createdTestCampaignInfo);
        checkThatCorrectTestCasesArePresent(testCampaignInfo.getTestCampaignItems(),
                createdTestCampaignInfo.getTestCampaignItems());

        CreateEditTestCampaignResult copyResult = tmUiOperator.copyTestPlan(createdTestCampaignInfo);
        CustomAsserts.checkTestStep(copyResult);

        TestCampaignInfo copiedTestCampaignInfo = copyResult.getTestCampaignInfo();

        assertThat(copiedTestCampaignInfo.getName(), equalTo(testCampaignInfo.getName() + " - COPY"));
        CustomAsserts.checkTestStep(tmUiOperator.deleteTestPlan(copiedTestCampaignInfo));
        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "DURACI-3315_Func_1", title = "Filter Test Campaigns")
    public void filterTestCampaigns() {
        createUiOperators();
        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmUiOperator.navigateToTestPlans());

        Set<FeatureInfo> featureInfos = Sets.newHashSet(FeatureType.ASSURE_OTHER.getFeatureInfo());

        TestCampaignInfo filterSelection = getTestCampaignFilterSelection(ProductType.ASSURE.getProductInfo(), Optional.empty(),
                featureInfos, Optional.empty());

        List<FilterInfo> tableFilters = Lists.newArrayList();
        tableFilters.add(new FilterInfo(TestCampaignsColumn.NAME, Condition.CONTAINS, "#"));
        tableFilters.add(new FilterInfo(TestCampaignsColumn.DESCRIPTION, Condition.CONTAINS, "to test defects"));
        tableFilters.add(new FilterInfo(TestCampaignsColumn.ENVIRONMENT, Condition.EQUALS, "test"));

        Result<Paginated<TestCampaignInfo>> result = tmUiOperator.filterTestPlans(filterSelection, tableFilters);
        CustomAsserts.checkTestStep(result);
        List<TestCampaignInfo> items = result.getValue().getItems();
        assertThat(items, hasSize(1));
        assertThat(items.get(0).getName(), equalTo("Test Plan #6"));
        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "DURACI-3248_Func_1", title = "Add multiple Test Cases to Test Campaign")
    public void addMultipleTestCasesToTestCampaign() {
        createUiOperators();
        String identifier = UUID.randomUUID().toString();
        prepareTestCases(identifier);

        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmUiOperator.navigateToTestPlans());

        TestCampaignInfo testCampaignInfo = toTestPlanInfo(getEnmTestCampaign());
        testCampaignInfo.setName(UUID.randomUUID().toString().substring(0, 13));

        List<Criterion> criterions = Lists.newArrayList();
        criterions.add(new Criterion(Field.ANY, Condition.CONTAINS, identifier));

        CreateEditTestCampaignResult testPlanResult = tmUiOperator.createTestPlanWithMultipleTestCases(testCampaignInfo, criterions);
        CustomAsserts.checkTestStep(testPlanResult);
        checkThatTestPlanDetailsWereSavedCorrectly(testCampaignInfo, testPlanResult.getTestCampaignInfo());

        Set<TestCampaignItemInfo> assignments = testPlanResult.getTestCampaignInfo().getTestCampaignItems();

        assertThat(assignments.size(), equalTo(3));

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "DURACI-3248_Func_2", title = "Create Test Campaign but delete Test Cases added to Test Campaign")
    public void deleteTestCaseFromTestCampaign() {
        createUiOperators();
        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmUiOperator.navigateToTestPlans());

        TestCampaignInfo testCampaignInfo = toTestPlanInfo(getEnmTestCampaignWithTestCases());
        testCampaignInfo.setName(UUID.randomUUID().toString().substring(0, 13));

        CreateEditTestCampaignResult result = tmUiOperator.createTestPlanButDeleteTestCases(testCampaignInfo);
        CustomAsserts.checkTestStep(result);
        checkThatTestPlanDetailsWereSavedCorrectly(testCampaignInfo, result.getTestCampaignInfo());
        checkThatTestCasesWereDeleted(result.getTestCampaignInfo());

        Set<TestCampaignItemInfo> assignments = result.getTestCampaignInfo().getTestCampaignItems();

        assertThat(assignments.size(), equalTo(0));
        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "DURACI-3873_Func_1", title = "Check, that fields are empty after Created/Edited Test Campaign")
    public void createAndCheckFieldForNewCreate() {
        createUiOperators();
        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmUiOperator.navigateToTestPlans());

        TestCampaignInfo testCampaignInfo = toTestPlanInfo(getEnmTestCampaignWithTestCases());
        testCampaignInfo.setName(UUID.randomUUID().toString().substring(0, 13));

        CreateEditTestCampaignResult result = tmUiOperator.createTestPlan(testCampaignInfo);
        CustomAsserts.checkTestStep(result);

        checkThatTestPlanDetailsWereSavedCorrectly(testCampaignInfo, result.getTestCampaignInfo());
        checkThatCorrectTestCasesArePresent(testCampaignInfo.getTestCampaignItems(), result.getTestCampaignInfo().getTestCampaignItems());

        CustomAsserts.checkTestStep(tmUiOperator.deleteTestPlan(result.getTestCampaignInfo()));

        TestCampaignInfo emptyTestCampaignInfo = new TestCampaignInfo();
        emptyTestCampaignInfo.setProduct(ProductType.DE.getProductInfo());
        Set<FeatureInfo> featureInfos = Sets.newHashSet(FeatureType.TAF.getFeatureInfo());
        emptyTestCampaignInfo.setFeatures(featureInfos);
        CustomAsserts.checkTestStepFailed(tmUiOperator.createTestPlan(emptyTestCampaignInfo));

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "DURACI-3918_Func_1", title = "Create Test Campaign and Assign Test Case to User")
    public void assignTestCampaignTestCaseToUser() {
        createUiOperators();
        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmUiOperator.navigateToTestPlans());

        final String username = "tafUser2 tafSurname2";
        List<TestCampaignItemInfo> testCaseInfosWithUser = createTestCaseInfoWithUser(username);

        TestCampaignInfo testCampaignInfo = toTestPlanInfo(getEnmTestCampaignWithTestCases());
        testCampaignInfo.setName(UUID.randomUUID().toString().substring(0, 13));

        CreateEditTestCampaignResult createTestPlanResult = tmUiOperator.createTestPlanAndAssignUser(testCampaignInfo, username);
        CustomAsserts.checkTestStep(createTestPlanResult);

        checkThatTestPlanDetailsWereSavedCorrectly(testCampaignInfo, createTestPlanResult.getTestCampaignInfo());
        compareTestCampaignItems(createTestPlanResult.getTestCampaignInfo().getTestCampaignItems(), testCaseInfosWithUser);
        checkThatCorrectTestCasesArePresent(testCampaignInfo.getTestCampaignItems(), createTestPlanResult.getTestCampaignInfo().getTestCampaignItems());

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "DURACI-3248_Func_3", title = "Navigate to Test Campaign using old url")
    public void navigateToOldTestPlanUrl() {
        createUiOperators();
        CustomAsserts.checkTestStep(loginOperator.login());
        String oldUrl = "#tm/viewTestPlan/2";
        tmUiOperator.navigateTo(oldUrl);
        CustomAsserts.checkTestStep(tmUiOperator.viewTestPlan());

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "DURACI-3248_Func_4", title = "Assign Test Cases to Test campaign from opening search screen")
    public void assignTestCasesToTestCampaign() {
        createUiOperators();
        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmUiOperator.selectUserProfileProduct(ProductType.ENM.getProductInfo()));

        List<String> testcaseIds = new ArrayList();
        testcaseIds.add("CIP-939_Func111");
        testcaseIds.add("CIP-939");

        String testCampaignName = "Test Plan #1";
        Set<FeatureType> featureInfoLists = Sets.newHashSet();
        featureInfoLists.add(FeatureType.ENM_OTHER);
        TestCampaignInfo testCampaign = createTestCampaign(testCampaignName, ProductType.ENM, DropType.ENM_OTHER, featureInfoLists);

        CreateEditTestCampaignResult createEditTestCampaignResult = tmUiOperator.addTestCasesToTestCampaign(testcaseIds, testCampaign, "CIP-9");
        Set<TestCampaignItemInfo> testCampaignItems = createEditTestCampaignResult.getTestCampaignInfo().getTestCampaignItems();

        List<String> actualTestcaseIds = new ArrayList();
        for (TestCampaignItemInfo testCampaignItemInfo : testCampaignItems) {
            actualTestcaseIds.add(testCampaignItemInfo.getTestCase().getTestCaseId());
        }
        assertThat(testcaseIds, everyItem(isIn(actualTestcaseIds)));

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "CIP-13004", title = "Copy Test Campaigns between drops")
    public void copyTestCampaignsBetweenDrops() {
        createUiOperators();
        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmUiOperator.navigateToTestPlans());
        TestCampaignInfo filterOptions = toTestPlanInfo(getEnmTestCampaignDrop());

        DropInfo copyToDrop = new DropInfo(3L, "ENM", "2.0.ENM");
        Paginated<TestCampaignInfo> testCampaignsCopiedToEmptyDrop =
                tmUiOperator.copyTestCampaignsBetweenDrops(filterOptions, copyToDrop);

        assertThat(testCampaignsCopiedToEmptyDrop.getItems().size(), equalTo(2));

        List<String> testCampaignNames = testCampaignsCopiedToEmptyDrop.getItems()
                .stream()
                .map(t -> t.getName())
                .collect(Collectors.toList());

        assertThat(testCampaignNames, containsInAnyOrder("ENM Test Plan #1", "ENM Test Plan #2"));

        testCampaignsCopiedToEmptyDrop.getItems().forEach(t -> {
            tmUiOperator.viewTestPlan(t);
            tmUiOperator.deleteTestPlan(t);
        });
        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "CIP-15343", title = "Test case autocomplete test")
    public void testCaseAutoComplete() {
        createUiOperators();
        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmUiOperator.navigateToTestPlans());
        tmUiOperator.navigateToCreateTestCampaignPage();

        TestCampaignInfo testCampaignInfo = toTestPlanInfo(getEnmTestCampaignWithTestCases());
        tmUiOperator.setContext(testCampaignInfo);

        AutoCompleteResult result = tmUiOperator.setTestCaseAutoCompleteText("CIP-3517");
        List<String> items = result.getAutocompleteItems();
        assertThat(items.size(), equalTo(1));
        assertThat(items, containsInAnyOrder("CIP-3517"));

        result = tmUiOperator.setTestCaseAutoCompleteText("CIP-939");
        items = result.getAutocompleteItems();
        assertThat(items.size(), equalTo(2));
        assertThat(items, containsInAnyOrder("CIP-939", "CIP-939_Func111"));
        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "CIP-24093_Func_2", title = "showTestCampaignsAfterSearch")
    public void showTestCampaignsAfterSearch() {
        createUiOperators();
        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmUiOperator.selectUserProfileProduct(ProductType.ENM.getProductInfo()));
        CustomAsserts.checkTestStep(tmUiOperator.navigateToTestPlans());

        Set<FeatureInfo> featureInfos = Sets.newHashSet(FeatureType.OSS_CN.getFeatureInfo());

        TestCampaignInfo filterSelection = getTestCampaignFilterSelection(ProductType.OSSRC.getProductInfo(), Optional.of(DropType.OSS_2.getDropInfo()),
                featureInfos, Optional.of(Sets.newHashSet(TechnicalComponentType.OSS_CN.getTechnicalComponentInfo())));

        List<FilterInfo> tableFilters = Lists.newArrayList();
        Result<Paginated<TestCampaignInfo>> result = tmUiOperator.filterTestPlans(filterSelection, tableFilters);
        CustomAsserts.checkTestStep(result);
        List<TestCampaignInfo> items = result.getValue().getItems();
        assertThat(items, hasSize(1));

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

    private void compareTestCampaignItems(Set<TestCampaignItemInfo> actual, List<TestCampaignItemInfo> compareTestCampaignInfo) {

        int index = 0;

        for (TestCampaignItemInfo actualTestCampaignInfo : actual) {
            TestCampaignItemInfo testCampaignItemInfo = compareTestCampaignInfo.get(index);
            TestCaseInfo testCase = testCampaignItemInfo.getTestCase();

            assertThat(actualTestCampaignInfo.getUser().getUserName(), equalTo(testCampaignItemInfo.getUser().getUserName()));
            assertThat(actualTestCampaignInfo.getTestCase().getTitle(), equalTo(testCase.getTitle()));
            assertThat(actualTestCampaignInfo.getTestCase().getTestCaseId(), equalTo(testCase.getTestCaseId()));
            assertThat(actualTestCampaignInfo.getFeature().getName(), equalTo(testCase.getFeature().getName()));
            assertThat(actualTestCampaignInfo.getTestCase().getVersion(), equalTo(testCase.getVersion()));
            assertThat(actualTestCampaignInfo.getTestCase().getExecutionType().getTitle(), equalTo(testCase.getExecutionType().getTitle()));

            index++;
        }
    }

    private void checkThatTestCasesWereDeleted(TestCampaignInfo testCampaignInfo) {
        assertTrue(testCampaignInfo.getTestCampaignItems().isEmpty());
    }

    private List<TestCampaignItemInfo> createTestCaseInfoWithUser(String username) {
        List<TestCampaignItemInfo> testCampaignItemInfos = Lists.newArrayList();

        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(username);

        TestCampaignItemInfo testCampaignItemInfo1 = new TestCampaignItemInfo();
        testCampaignItemInfo1.setUser(userInfo);
        testCampaignItemInfo1.getTestCase().setTitle("CLI Test Case");
        testCampaignItemInfo1.getTestCase().setTestCaseId("CIP-2638_Func_1");
        testCampaignItemInfo1.getTestCase().setVersion("1.1");
        testCampaignItemInfo1.getTestCase().setExecutionType(new ReferenceDataItem(null, "Functional"));
        testCampaignItemInfo1.getTestCase().setFeature(new FeatureInfo(null, "Other"));


        TestCampaignItemInfo testCampaignItemInfo2 = new TestCampaignItemInfo();
        testCampaignItemInfo2.setUser(userInfo);
        testCampaignItemInfo2.getTestCase().setTitle("efjefj");
        testCampaignItemInfo2.getTestCase().setTestCaseId("CIP-2638_Func_2");
        testCampaignItemInfo2.getTestCase().setVersion("1.1");
        testCampaignItemInfo2.getTestCase().setExecutionType(new ReferenceDataItem(null, "Functional"));
        testCampaignItemInfo2.getTestCase().setFeature(new FeatureInfo(null, "Other"));

        testCampaignItemInfos.add(testCampaignItemInfo1);
        testCampaignItemInfos.add(testCampaignItemInfo2);

        return testCampaignItemInfos;
    }

}
