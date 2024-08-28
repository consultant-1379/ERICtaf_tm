package com.ericsson.cifwk.tm.fun.rest.tms;

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
        createRestOperators();
        checkTestStep(loginOperator.login());

        InputStream testCaseJson = getFullTestCaseJson();

        CreateEditTestCaseResult result = tmRestOperator.createTestCase(testCaseJson, ProductType.ENM.getProductInfo());

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
        createRestOperators();
        checkTestStep(loginOperator.login());

        checkTestStep(tmRestOperator.selectUserProfileProduct(ProductType.ENM.getProductInfo()));

        InputStream testCaseJson = getFullTestCaseJson();
        CreateEditTestCaseResult result = tmRestOperator.createTestCase(testCaseJson, null);

        checkTestStep(result);
        assertNotNull(result.getSavedTestCaseInfo().getId());

        TestCaseInfo expected = toTestCaseInfo(getFullTestCaseJson());
        checkSavedTestCaseData(expected, result.getSavedTestCaseInfo(), false);

        checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "CIP-4176_Func_6", title = "Create two Test Cases")
    public void createTwoTestCases() {
        createRestOperators();
        checkTestStep(loginOperator.login());

        InputStream testCaseJson = getFullTestCaseJson();
        CreateEditTestCaseResult result = tmRestOperator.createTestCase(testCaseJson, ProductType.ENM.getProductInfo());
        checkTestStep(result);
        assertNotNull(result.getSavedTestCaseInfo().getId());

        TestCaseInfo expected = toTestCaseInfo(getFullTestCaseJson());
        checkSavedTestCaseData(expected, result.getSavedTestCaseInfo(), false);

        testCaseJson = getFullTestCaseJson();
        checkTestStep(tmRestOperator.createTestCaseFromTestCaseView(testCaseJson, null));

        checkSavedTestCaseData(expected, result.getSavedTestCaseInfo(), false);

        checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "CIP-4176_Func_3", title = "Create a Test Case without mandatory fields")
    public void tryToCreateNotFullTestCase() {
        createRestOperators();
        checkTestStep(loginOperator.login());

        InputStream testCaseJson = getClass().getClassLoader().getResourceAsStream("data/createTestCase_notFullData.json");

        checkTestStepFailed(tmRestOperator.createTestCase(testCaseJson, ProductType.ENM.getProductInfo()));

        checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "DURACI-2294_Func_1", title = "Create a manual Test Case")
    public void createManualTestCase() {
        createRestOperators();
        checkTestStep(loginOperator.login());

        InputStream testCaseJson = getManualTestCaseJson();
        CreateEditTestCaseResult result = tmRestOperator.createTestCase(testCaseJson, ProductType.ENM.getProductInfo());
        checkTestStep(result);
        assertNotNull(result.getSavedTestCaseInfo().getId());

        TestCaseInfo expected = toTestCaseInfo(getManualTestCaseJson());
        checkSavedTestCaseData(expected, result.getSavedTestCaseInfo(), false);

        checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "DURACI-2700_Func_1", title = "List global test case groups")
    public void listNewTestCaseGroups() {
        createRestOperators();
        checkTestStep(loginOperator.login());

        Result<List<ReferenceGroup>> referenceGroupResult =
                tmRestOperator.getNewTestCaseGroups(ProductType.OSSRC.getProductInfo());
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
