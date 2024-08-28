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



import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.HasResult;
import com.ericsson.cifwk.tm.presentation.dto.TestExecutionInfo;

public class TestExecutionResult implements HasResult {

    private final boolean success;
    private final TestExecutionInfo testExecutionInfo;
    private final String errorMessage;

    private TestExecutionResult(boolean success, TestExecutionInfo testExecutionInfo, String errorMessage) {
        this.success = success;
        this.testExecutionInfo = testExecutionInfo;
        this.errorMessage = errorMessage;
    }

    public static TestExecutionResult success(TestExecutionInfo testExecutionInfo) {
        return new TestExecutionResult(true, testExecutionInfo, null);
    }

    public static TestExecutionResult error(String errorMessage) {
        return new TestExecutionResult(false, null, errorMessage);
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    public TestExecutionInfo getTestExecutionInfo() {
        return testExecutionInfo;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

}
