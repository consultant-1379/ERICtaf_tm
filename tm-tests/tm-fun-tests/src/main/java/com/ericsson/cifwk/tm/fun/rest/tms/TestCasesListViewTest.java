/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.rest.tms;

import com.beust.jcommander.internal.Lists;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.tm.fun.common.BaseFuncTest;
import com.ericsson.cifwk.tm.fun.common.CustomAsserts;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.ProductType;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.search.Condition;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.table.FilterInfo;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.table.TestCasesColumn;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.TestCasesResult;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Set;

import static org.testng.Assert.assertEquals;


public class TestCasesListViewTest extends BaseFuncTest {

    @Test
    @TestId(id="CIP-4168_Func_1", title="Show Test Cases table with results")
    public void showTestCases() {
        createRestOperators();
        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmRestOperator.selectUserProfileProduct(ProductType.ENM.getProductInfo()));

        TestCasesResult result = tmRestOperator.getTestCasesByFilter(Lists.<FilterInfo>newArrayList());
        CustomAsserts.checkTestStep(result);
        assertEquals(false, result.getTestCases().isEmpty());

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id="CIP-4168_Func_2", title="Show Test Cases by Search")
    public void showTestCasesBySearch() {
        createRestOperators();
        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmRestOperator.selectUserProfileProduct(ProductType.ENM.getProductInfo()));

        List<FilterInfo> filters = Lists.newArrayList();
        filters.add(new FilterInfo(TestCasesColumn.TEST_CASE_ID, Condition.EQUALS, "CIP-3517"));

        TestCasesResult result = tmRestOperator.getTestCasesByFilter(filters);
        CustomAsserts.checkTestStep(result);
        List<TestCaseInfo> testInfos = result.getTestCases();
        assertEquals(false, testInfos.isEmpty());
        assertEquals(1, testInfos.size());

        TestCaseInfo testInfo = testInfos.get(0);
        assertEquals("CIP-3517", testInfo.getTestCaseId());
        assertEquals("test dm", testInfo.getTitle());
        assertEquals("test dm", testInfo.getDescription());

        Set<String> requirements = testInfo.getRequirementIds();
        assertEquals(1, requirements.size());
        assertEquals("CIP-4960", requirements.iterator().next());

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

}
