package com.ericsson.cifwk.tm.fun.rest.tms;

import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.tm.fun.common.BaseFuncTest;
import com.ericsson.cifwk.tm.fun.common.CustomAsserts;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.ProductType;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.CreateEditTestCaseResult;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import org.testng.annotations.Test;

import java.io.InputStream;

import static com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.JsonHelper.toTestCaseInfo;

public class EditTestCaseTest extends BaseFuncTest {

    @Test
    @TestId(id="CIP-4176_Func_4", title="Edit the Test Case")
    public void editTestCase() {
        createRestOperators();
        CustomAsserts.checkTestStep(loginOperator.login());

        InputStream createTestCaseJson = getFullTestCaseJson();
        CreateEditTestCaseResult createResult = tmRestOperator.createTestCase(createTestCaseJson, ProductType.ENM.getProductInfo());
        CustomAsserts.checkTestStep(createResult);
        TestCaseInfo expected = toTestCaseInfo(getFullTestCaseJson());
        checkSavedTestCaseData(expected, createResult.getSavedTestCaseInfo(), false);

        InputStream editTestCaseJson = getEditTestCaseJson();
        CreateEditTestCaseResult editResult = tmRestOperator.editTestCase(createResult.getSavedTestCaseInfo(), editTestCaseJson);
        CustomAsserts.checkTestStep(editResult);

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id="CIP-4176_Func_5", title="Try to edit the Test Case without mandatory fields")
    public void tryToEditNotFullTestCase() {
        createRestOperators();
        CustomAsserts.checkTestStep(loginOperator.login());

        InputStream createTestCaseJson = getFullTestCaseJson();
        CreateEditTestCaseResult createResult = tmRestOperator.createTestCase(createTestCaseJson, ProductType.ENM.getProductInfo());
        CustomAsserts.checkTestStep(createResult);
        TestCaseInfo expected = toTestCaseInfo(getFullTestCaseJson());
        checkSavedTestCaseData(expected, createResult.getSavedTestCaseInfo(), false);

        InputStream partialTestCaseJson = getPartialTestCaseJson();
        CustomAsserts.checkTestStepFailed(tmRestOperator.editTestCase(createResult.getSavedTestCaseInfo(), partialTestCaseJson));

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

}
