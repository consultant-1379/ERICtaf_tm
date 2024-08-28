package com.ericsson.cifwk.tm.fun.ui.tms;


import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.tm.fun.common.BaseFuncTest;
import com.ericsson.cifwk.tm.fun.common.CustomAsserts;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.ProductType;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.search.Condition;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.table.FilterInfo;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.table.TestCasesColumn;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.CopyTestStepResult;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.CreateEditTestCaseResult;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.DeleteTestStepsResult;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.SearchResult;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.TestCasesResult;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.google.common.collect.Lists;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.List;

import static com.ericsson.cifwk.tm.fun.common.tms.TestCaseInfoHelper.getTestCaseInfo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class CopyTestCaseTest extends BaseFuncTest {

    private static final String TEST_CASE_ID = "1d7938e9-2f60-4d13-a20a-905aed30e128";
    private static final String TEST_CASE_ID2 = "ddc198c3-d228-49e0-83b8-3278a23ba246";

    private static final String TEST_CASE_TITLE = "TORRV TEST: Add a node using CM CLI";

    @Test
    @TestId(id = "DURACI-2790_Func_1", title = "Copy Test Case")
    public void copyTestCase() {
        createUiOperators();

        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmUiOperator.selectUserProfileProduct(ProductType.ENM.getProductInfo()));

        List<FilterInfo> filters = Lists.newArrayList();
        filters.add(new FilterInfo(TestCasesColumn.TEST_CASE_ID, Condition.EQUALS, TEST_CASE_ID));

        TestCasesResult result = tmUiOperator.getTestCasesByFilter(filters);
        CustomAsserts.checkTestStep(result);

        TestCaseInfo testCaseInfo = getTestCaseInfo(8L, TEST_CASE_ID, TEST_CASE_TITLE);
        InputStream testCaseJson = getCopyTestCaseJson();
        CreateEditTestCaseResult copyResult = tmUiOperator.copyTestCase(testCaseInfo, testCaseJson);

        TestCaseInfo expected = tmUiOperator.getExpectedResult(copyResult);

        CustomAsserts.checkTestStep(copyResult);
        checkSavedTestCaseData(expected, copyResult.getSavedTestCaseInfo(), true);

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "DURACI-2805_Func_1", title = "Copy Test Steps and delete test Steps")
    public void copyTestSteps() {
        createUiOperators();

        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmUiOperator.selectUserProfileProduct(ProductType.ENM.getProductInfo()));

        SearchResult searchResult = tmUiOperator.executeQuickSearch(TEST_CASE_ID2);
        CustomAsserts.checkTestStep(searchResult);

        TestCaseInfo testCaseInfo = getTestCaseInfo(5L, TEST_CASE_ID2, TEST_CASE_TITLE);

        List<String> testStepsToCopy = Lists.newArrayList("0");
        CopyTestStepResult copyResult = tmUiOperator.copyTestSteps(testCaseInfo, testStepsToCopy);
        CustomAsserts.checkTestStep(copyResult);

        assertThat(copyResult.getTestStepsAfterCopy(), equalTo(copyResult.getTestStepsBeforeCopy() + testStepsToCopy.size()));
        for (int i = 0; i < copyResult.getCopyTitles().size(); i++) {
            assertThat(copyResult.getCopyTitles().get(i),
                    equalTo("COPY - " + copyResult.getTestStepTitles().get(i)));
        }

        DeleteTestStepsResult deleteResult = tmUiOperator.deleteTestSteps(copyResult.getCopyIndices());

        assertThat(deleteResult.getTestStepsAfterDelete(),
                equalTo(deleteResult.getTestStepsBeforeDelete() - copyResult.getCopyIndices().size()));
        CustomAsserts.checkTestStep(loginOperator.logout());
    }
}
