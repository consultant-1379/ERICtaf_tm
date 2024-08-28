/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.smoke.tms;

import com.beust.jcommander.internal.Lists;
import com.ericsson.cifwk.taf.TafTestBase;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.tm.fun.ui.login.operator.RestLoginOperator;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.RestTestManagementOperator;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.search.Condition;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.table.FilterInfo;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.table.TestCasesColumn;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.TestCasesResult;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.List;

import static com.ericsson.cifwk.tm.smoke.common.CustomAsserts.checkTestStep;
import static org.testng.AssertJUnit.assertEquals;

public class TestCasesListViewTest extends TafTestBase {

    private Host host;

    @Inject
    RestTestManagementOperator tmOperator;

    @Inject
    RestLoginOperator loginOperator;

    @BeforeClass(alwaysRun = true)
    public final void setUp() {
        host = DataHandler.getHostByName("tm");
        loginOperator.start(host);
        tmOperator.start(host);
    }

    @Test
    @TestId(id="CIP-4168_Smoke_1", title="Show Test Cases table with results")
    public void showTestCases() {
        TestCasesResult result = tmOperator.getTestCasesByFilter(Lists.<FilterInfo>newArrayList());
        checkTestStep(result);
        assertEquals(false, result.getTestCases().isEmpty());

    }

    @Test
    @TestId(id="CIP-4168_Smoke_2", title="Show Test Cases by Search")
    public void showTestCasesBySearch() {

        List<FilterInfo> filters = Lists.newArrayList();
        filters.add(new FilterInfo(TestCasesColumn.TEST_CASE_ID, Condition.EQUALS, "MR35035-WF-09-005"));

        TestCasesResult result = tmOperator.getTestCasesByFilter(filters);
        checkTestStep(result);
        List<TestCaseInfo> testInfos = result.getTestCases();
        assertEquals(false, testInfos.isEmpty());
        assertEquals(1, testInfos.size());

        TestCaseInfo testInfo = testInfos.get(0);
        assertEquals("MR35035-WF-09-005", testInfo.getTestCaseId());

    }

}
