package com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases;

import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.HasResult;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;

public final class CreateEditTestCaseResult implements HasResult {

    private final boolean success;
    private final TestCaseInfo savedTestCaseInfo;
    private final String errorMessage;
    private TestCaseInfo expectedTestCaseInfo;

    private CreateEditTestCaseResult(boolean success, TestCaseInfo savedTestCaseInfo, String errorMessage) {
        this.success = success;
        this.savedTestCaseInfo = savedTestCaseInfo;
        this.errorMessage = errorMessage;
    }

    private CreateEditTestCaseResult(boolean success, TestCaseInfo savedTestCaseInfo,
                                     String errorMessage, TestCaseInfo expectedTestCaseInfo) {
        this.success = success;
        this.savedTestCaseInfo = savedTestCaseInfo;
        this.errorMessage = errorMessage;
        this.expectedTestCaseInfo = expectedTestCaseInfo;
    }

    public static CreateEditTestCaseResult success(TestCaseInfo savedTestCaseInfo) {
        return new CreateEditTestCaseResult(true, savedTestCaseInfo, null);
    }

    public static CreateEditTestCaseResult success(TestCaseInfo savedTestCaseInfo, TestCaseInfo expectedTestCaseInfo) {
        return new CreateEditTestCaseResult(true, savedTestCaseInfo, null, expectedTestCaseInfo);
    }

    public static CreateEditTestCaseResult error(String errorMessage) {
        return new CreateEditTestCaseResult(false, null, errorMessage);
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    public TestCaseInfo getSavedTestCaseInfo() {
        return savedTestCaseInfo;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    public TestCaseInfo getExpectedTestCaseInfo() {
        return expectedTestCaseInfo;
    }
}
