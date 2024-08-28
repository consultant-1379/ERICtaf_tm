package com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases;

import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.HasResult;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;

import java.util.List;

public final class TestCasesResult implements HasResult {

    private final boolean success;
    private final String requirementId;
    private final List<TestCaseInfo> testCases;
    private final String errorMessage;

    private TestCasesResult(boolean success, String requirementId, List<TestCaseInfo> testCases, String errorMessage) {
        this.success = success;
        this.requirementId = requirementId;
        this.testCases = testCases;
        this.errorMessage = errorMessage;
    }

    public static TestCasesResult success(String requirementId) {
        return new TestCasesResult(true, requirementId, null, null);
    }

    public static TestCasesResult success(List<TestCaseInfo> testCases) {
        return new TestCasesResult(true, null, testCases, null);
    }

    public static TestCasesResult success(String requirementId, List<TestCaseInfo> testCases) {
        return new TestCasesResult(true, requirementId, testCases, null);
    }

    public static TestCasesResult error(String errorMessage) {
        return new TestCasesResult(false, null, null, errorMessage);
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    public String getRequirementId() {
        return requirementId;
    }

    public List<TestCaseInfo> getTestCases() {
        return testCases;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

}
