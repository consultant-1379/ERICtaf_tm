package com.ericsson.cifwk.tm.fun.rest.tms;


import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.tm.fun.common.BaseFuncTest;
import com.ericsson.cifwk.tm.fun.common.CustomAsserts;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.ProductType;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.search.Condition;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.table.FilterInfo;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.table.TestCasesColumn;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.CreateEditTestCaseResult;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.TestCasesResult;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.google.common.collect.Lists;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.List;

import static com.ericsson.cifwk.tm.fun.common.tms.TestCaseInfoHelper.getTestCaseInfo;

public class CopyTestCaseTest extends BaseFuncTest {

    private static final String TEST_CASE_ID = "1d7938e9-2f60-4d13-a20a-905aed30e128";

    private static final String TEST_CASE_TITLE = "TORRV TEST: Add a node using CM CLI";

    @Test
    @TestId(id = "DURACI-2790_Func_1", title = "Copy Test Case")
    public void copyTestCase() {
        createRestOperators();

        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmRestOperator.selectUserProfileProduct(ProductType.ENM.getProductInfo()));

        List<FilterInfo> filters = Lists.newArrayList();
        filters.add(new FilterInfo(TestCasesColumn.TEST_CASE_ID, Condition.EQUALS, TEST_CASE_ID));

        TestCasesResult result = tmRestOperator.getTestCasesByFilter(filters);
        CustomAsserts.checkTestStep(result);

        TestCaseInfo testCaseInfo = getTestCaseInfo(8L, TEST_CASE_ID, TEST_CASE_TITLE);
        InputStream testCaseJson = getCopyTestCaseJson();
        CreateEditTestCaseResult copyResult = tmRestOperator.copyTestCase(testCaseInfo, testCaseJson);

        TestCaseInfo expected = tmRestOperator.getExpectedResult(copyResult);

        CustomAsserts.checkTestStep(copyResult);
        checkSavedTestCaseData(expected, copyResult.getSavedTestCaseInfo(), true);

        CustomAsserts.checkTestStep(loginOperator.logout());
    }
}
