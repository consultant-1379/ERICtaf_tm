package com.ericsson.cifwk.tm.fun.ui.tms;

import com.beust.jcommander.internal.Lists;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.tm.fun.common.BaseFuncTest;
import com.ericsson.cifwk.tm.fun.common.CustomAsserts;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.ProductType;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.search.Condition;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.table.FilterInfo;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.table.TestCasesColumn;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.Result;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.TestCasesResult;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class HideShowTestCaseButtonsTest extends BaseFuncTest {

    private static final String TEST_CASE_ID = "TestCase_with_TwoVersions";

    private static final String TEST_CASE_REF = "22";

    @Test
    @TestId(id="DURACI-3636_Func_1", title="Test Edit and Remove button visibility on Test Case view")
    public void changeTestCaseVersion() {
        createUiOperators();

        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmUiOperator.selectUserProfileProduct(ProductType.ENM.getProductInfo()));

        List<FilterInfo> filters = Lists.newArrayList();
        filters.add(new FilterInfo(TestCasesColumn.TEST_CASE_ID, Condition.EQUALS, TEST_CASE_ID));

        TestCasesResult searchResult = tmUiOperator.getTestCasesByFilter(filters);
        CustomAsserts.checkTestStep(searchResult);

        assertEquals(1, searchResult.getTestCases().size());
        assertEquals(TEST_CASE_ID, searchResult.getTestCases().iterator().next().getTestCaseId());

        Result<TestCaseInfo> testCasesResult = tmUiOperator.viewTestCase(TEST_CASE_REF);
        CustomAsserts.checkTestStep(testCasesResult);

        Result<Boolean> checkBtnResult = tmUiOperator.checkTestCaseButtons();
        CustomAsserts.checkTestStep(checkBtnResult);

        CustomAsserts.checkTestStep(loginOperator.logout());
    }
}
