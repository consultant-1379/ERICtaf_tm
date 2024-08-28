/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testexecutions;


import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.testplan.TestCampaignExecutionsInfo;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.HasResult;

public class TestCampaignExecutionResult implements HasResult {

    private final boolean success;

    private final TestCampaignExecutionsInfo testCaseExecutionsInfo;

    private final String errorMessage;

    private TestCampaignExecutionResult(boolean success,
                                        TestCampaignExecutionsInfo testCaseExecutionsInfo,
                                        String errorMessage) {
        this.success = success;
        this.testCaseExecutionsInfo = testCaseExecutionsInfo;
        this.errorMessage = errorMessage;
    }
    public static TestCampaignExecutionResult success(TestCampaignExecutionsInfo testCaseExecutions) {
        return new TestCampaignExecutionResult(true, testCaseExecutions, null);
    }

    public static TestCampaignExecutionResult error(String errorMessage) {
        return new TestCampaignExecutionResult(false, null, errorMessage);
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }


    public TestCampaignExecutionsInfo getTestCaseExecutionsInfo() {
        return testCaseExecutionsInfo;
    }

}
