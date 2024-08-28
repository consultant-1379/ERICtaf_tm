package com.ericsson.cifwk.tm.fun.ui.tms;

import com.beust.jcommander.internal.Lists;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.tm.fun.common.BaseFuncTest;
import com.ericsson.cifwk.tm.fun.common.CustomAsserts;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.ProductType;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.search.Condition;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.table.FilterInfo;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.table.TestCasesColumn;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.TestCasesResult;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import org.testng.annotations.Test;

import java.util.List;

import static com.ericsson.cifwk.tm.fun.common.tms.TestCaseInfoHelper.getTestCaseInfo;
import static org.testng.Assert.assertEquals;

public class ShowAssociatedTestCampaignsTest extends BaseFuncTest {

    private static final String TEST_CASE_ID = "ddc198c3-d228-49e0-83b8-3278a23ba246";

    @Test
    @TestId(id="DURACI-3219_Func_1", title="Show Test Plans that Test Case is included in")
    public void showAssociatedTestPlans() {
        createUiOperators();
        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmUiOperator.selectUserProfileProduct(ProductType.ENM.getProductInfo()));

        List<FilterInfo> filters = Lists.newArrayList();
        filters.add(new FilterInfo(TestCasesColumn.TEST_CASE_ID, Condition.EQUALS, TEST_CASE_ID));

        TestCasesResult result = tmUiOperator.getTestCasesByFilter(filters);
        CustomAsserts.checkTestStep(result);
        assertEquals(1, result.getTestCases().size());

        TestCaseInfo testCaseInfo = getTestCaseInfo(TEST_CASE_ID);
        CustomAsserts.checkTestStep(tmUiOperator.showAssociatedTestPlans(testCaseInfo));

        CustomAsserts.checkTestStep(loginOperator.logout());
    }
}
