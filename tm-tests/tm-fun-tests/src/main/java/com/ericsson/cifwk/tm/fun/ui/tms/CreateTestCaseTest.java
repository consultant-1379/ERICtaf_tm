package com.ericsson.cifwk.tm.fun.ui.tms;

import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.tm.fun.common.BaseFuncTest;
import com.ericsson.cifwk.tm.fun.ui.testcases.jsonobjects.references.ReferenceGroup;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.ProductType;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.references.Reference;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.Result;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.CreateEditTestCaseResult;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import static com.ericsson.cifwk.tm.fun.common.CustomAsserts.checkTestStep;
import static com.ericsson.cifwk.tm.fun.common.CustomAsserts.checkTestStepFailed;
import static com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.JsonHelper.toTestCaseInfo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.testng.Assert.assertNotNull;

public class CreateTestCaseTest extends BaseFuncTest {

    @Test
    @TestId(id = "CIP-4176_Func_1", title = "Create a Test Case")
    public void createTestCase() {
        createUiOperators();
        checkTestStep(loginOperator.login());

        InputStream testCaseJson = getFullTestCaseJson();

        CreateEditTestCaseResult result = tmUiOperator.createTestCase(testCaseJson, ProductType.ENM.getProductInfo());

        checkTestStep(result);
        assertNotNull(result.getSavedTestCaseInfo().getId());

        TestCaseInfo expected = toTestCaseInfo(getFullTestCaseJson());
        checkSavedTestCaseData(expected, result.getSavedTestCaseInfo(), false);

        // TODO: VOV: Remove created TestCase.

        checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "CIP-3956_Func_1", title = "Create a Test Case with preselected ProductInfo")
    public void createTestCaseWithPreselectedProject() {
        createUiOperators();
        checkTestStep(loginOperator.login());

        checkTestStep(tmUiOperator.selectUserProfileProduct(ProductType.ENM.getProductInfo()));

        InputStream testCaseJson = getFullTestCaseJson();
        CreateEditTestCaseResult result = tmUiOperator.createTestCase(testCaseJson, null);

        checkTestStep(result);
        assertNotNull(result.getSavedTestCaseInfo().getId());

        TestCaseInfo expected = toTestCaseInfo(getFullTestCaseJson());
        checkSavedTestCaseData(expected, result.getSavedTestCaseInfo(), false);

        // TODO: VOV: Remove created TestCase.

        checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "CIP-4176_Func_6", title = "Create two Test Cases")
    public void createTwoTestCases() {
        createUiOperators();
        checkTestStep(loginOperator.login());

        InputStream testCaseJson = getFullTestCaseJson();
        CreateEditTestCaseResult result = tmUiOperator.createTestCase(testCaseJson, ProductType.ENM.getProductInfo());
        checkTestStep(result);
        assertNotNull(result.getSavedTestCaseInfo().getId());

        TestCaseInfo expected = toTestCaseInfo(getFullTestCaseJson());
        checkSavedTestCaseData(expected, result.getSavedTestCaseInfo(), false);

        testCaseJson = getFullTestCaseJson();
        checkTestStep(tmUiOperator.createTestCaseFromTestCaseView(testCaseJson, null));

        checkSavedTestCaseData(expected, result.getSavedTestCaseInfo(), false);

        // TODO: VOV: Remove created TestCases.

        checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "CIP-4176_Func_2", title = "Create/Update a Test Case with ID from Eclipse Plugin")
    public void createAndUpdateTestCaseLinksForEclipsePlugin() {
        createUiOperators();
        checkTestStep(loginOperator.login());

        // create
        InputStream testCaseJson = getFullTestCaseJson();
        String testCaseId = UUID.randomUUID().toString();
        CreateEditTestCaseResult result =
                tmUiOperator.createTestCaseWithGivenId(testCaseId, testCaseJson, ProductType.ENM.getProductInfo());
        checkTestStep(result);
        assertNotNull(result.getSavedTestCaseInfo().getTestCaseId());

        TestCaseInfo expected = toTestCaseInfo(getFullTestCaseJson());
        checkSavedTestCaseData(expected, result.getSavedTestCaseInfo(), false);

        // update
        result = tmUiOperator.updateTestCaseDescription(testCaseId, "updatedDescription");
        expected.setDescription("updatedDescription");
        checkTestStep(result);
        assertNotNull(result.getSavedTestCaseInfo().getTestCaseId());

        checkSavedTestCaseData(expected, result.getSavedTestCaseInfo(), false);

        // TODO: VOV: Remove created TestCases.

        checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "CIP-4176_Func_3", title = "Create a Test Case without mandatory fields")
    public void tryToCreateNotFullTestCase() {
        createUiOperators();
        checkTestStep(loginOperator.login());

        InputStream testCaseJson = getClass().getClassLoader().getResourceAsStream("data/createTestCase_notFullData.json");

        checkTestStepFailed(tmUiOperator.createTestCase(testCaseJson, ProductType.ENM.getProductInfo()));

        checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "DURACI-2294_Func_1", title = "Create a manual Test Case")
    public void createManualTestCase() {
        createUiOperators();
        checkTestStep(loginOperator.login());

        InputStream testCaseJson = getManualTestCaseJson();
        CreateEditTestCaseResult result = tmUiOperator.createTestCase(testCaseJson, ProductType.ENM.getProductInfo());
        checkTestStep(result);
        assertNotNull(result.getSavedTestCaseInfo().getId());

        TestCaseInfo expected = toTestCaseInfo(getManualTestCaseJson());
        checkSavedTestCaseData(expected, result.getSavedTestCaseInfo(), false);

        // TODO: VOV: Remove created TestCases.

        checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "DURACI-2700_Func_1", title = "List global test case groups")
    public void listNewTestCaseGroups() {
        createUiOperators();
        checkTestStep(loginOperator.login());

        Result<List<ReferenceGroup>> referenceGroupResult =
                tmUiOperator.getNewTestCaseGroups(ProductType.OSSRC.getProductInfo());
        checkTestStep(referenceGroupResult);

        List<ReferenceGroup> groups = referenceGroupResult.getValue();
        assertThat(groups, hasSize(1));

        ReferenceGroup referenceGroup = groups.get(0);
        assertThat(referenceGroup.getId(), equalTo("group"));
        List<Reference> items = referenceGroup.getItems();
        assertThat(items, hasSize(5));
        assertThat(items.get(0).getTitle(), equalTo("GAT"));
        assertThat(items.get(1).getTitle(), equalTo("KGB"));
        assertThat(items.get(2).getTitle(), equalTo("RNCDB"));
        assertThat(items.get(3).getTitle(), equalTo("STCDB"));
        assertThat(items.get(4).getTitle(), equalTo("VCDB"));

        checkTestStep(loginOperator.logout());
    }
}
