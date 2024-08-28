/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.requests;

import com.ericsson.cifwk.tm.presentation.dto.TestExecutionInfo;

public final class CreateTestExecutionRequest {

    private final long testPlanId;
    private final String testCaseId;
    private final TestExecutionInfo testExecutionInfo;

    public CreateTestExecutionRequest(long testPlanId, String testCaseId, TestExecutionInfo testExecutionInfo) {
        this.testPlanId = testPlanId;
        this.testCaseId = testCaseId;
        this.testExecutionInfo = testExecutionInfo;
    }

    public long getTestPlanId() {
        return testPlanId;
    }

    public String getTestCaseId() {
        return testCaseId;
    }

    public TestExecutionInfo getTestExecutionInfo() {
        return testExecutionInfo;
    }

}
