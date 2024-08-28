package com.ericsson.cifwk.tm.fun.ui.tms;

import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.tm.fun.common.BaseFuncTest;
import com.ericsson.cifwk.tm.fun.common.CustomAsserts;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.ProductType;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.CreateEditTestCaseResult;
import org.testng.annotations.Test;

import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ReviewTestCaseTest extends BaseFuncTest {

    @Test
    @TestId(id = "CIP-15727_Func_1", title = "Create and review a Test Case")
    public void createReviewTestCase() {
        createUiOperators();

        CustomAsserts.checkTestStep(loginOperator.loginWithDefaultUser());

        InputStream testCaseJson = getFullTestCaseJson();

        CreateEditTestCaseResult result = tmUiOperator.createTestCase(testCaseJson, ProductType.ENM.getProductInfo());

        CustomAsserts.checkTestStep(result);

        CreateEditTestCaseResult review = tmUiOperator.reviewTestCase(result.getSavedTestCaseInfo(), "TMS Review Group");

        CustomAsserts.checkTestStep(review);

        assertThat(review.getSavedTestCaseInfo().getTestCaseStatus().getTitle(), equalTo("Review"));

        CreateEditTestCaseResult approve = tmUiOperator.approveTestCase(result.getSavedTestCaseInfo(), "Major");
        CustomAsserts.checkTestStep(approve);

        assertThat(approve.getSavedTestCaseInfo().getTestCaseStatus().getTitle(), equalTo("Approved"));

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "CIP-16580_Func_1", title = "Edit/Review the Test Case and compare versions")
    public void compareTestCase() {
        createUiOperators();
        CustomAsserts.checkTestStep(loginOperator.loginWithDefaultUser());

        InputStream createTestCaseJson = getFullTestCaseJson();
        CreateEditTestCaseResult createResult = tmUiOperator.createTestCase(createTestCaseJson, ProductType.ENM.getProductInfo());
        CustomAsserts.checkTestStep(createResult);

        CreateEditTestCaseResult review = tmUiOperator.reviewTestCase(createResult.getSavedTestCaseInfo(), "TMS Review Group");
        CustomAsserts.checkTestStep(review);

        CreateEditTestCaseResult approve = tmUiOperator.approveTestCase(review.getSavedTestCaseInfo(), "Minor");
        CustomAsserts.checkTestStep(approve);

        InputStream editTestCaseJson = getEditTestCaseJson();

        // problem uicomponent is not found when new test case version is created. This resolves it.
        tmUiOperator.navigateToTestCaseEditAndView(createResult.getSavedTestCaseInfo());

        CreateEditTestCaseResult editResult = tmUiOperator.editTestCase(createResult.getSavedTestCaseInfo(), editTestCaseJson);
        CustomAsserts.checkTestStep(editResult);

        CreateEditTestCaseResult compareTestCase = tmUiOperator.compareTestCase();
        CustomAsserts.checkTestStep(compareTestCase);

        assertThat(compareTestCase.getSavedTestCaseInfo().getTestCaseStatus().getTitle(),
                equalTo("Approved"));

        assertThat(compareTestCase.getSavedTestCaseInfo().getTitle(),
                equalTo(createResult.getSavedTestCaseInfo().getTitle()));

        assertThat(compareTestCase.getSavedTestCaseInfo().getTestSteps().get(0).getName(),
                equalTo(createResult.getSavedTestCaseInfo().getTestSteps().get(0).getName()));

        assertThat(compareTestCase.getSavedTestCaseInfo().getTestSteps().get(0).getVerifies().get(0).getName(),
                equalTo(createResult.getSavedTestCaseInfo().getTestSteps().get(0).getVerifies().get(0).getName()));

        CustomAsserts.checkTestStep(loginOperator.logout());
    }
}
