package com.ericsson.cifwk.tm.application.requests;

import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;

/**
 *
 */
public final class CreateTestCaseVersionRequest {

    private String testCaseId;
    private TestCaseInfo testCaseVersion;

    public CreateTestCaseVersionRequest(String testCaseId, TestCaseInfo testCaseVersion) {
        this.testCaseId = testCaseId;
        this.testCaseVersion = testCaseVersion;
    }

    public TestCaseInfo getTestCaseVersion() {
        return testCaseVersion;
    }

    public String getTestCaseId() {
        return testCaseId;
    }

}
