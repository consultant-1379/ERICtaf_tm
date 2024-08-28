package com.ericsson.cifwk.tm.application.requests;

/**
 *
 */
public class UpdateTestCaseVersionRequest {

    private final Long testPlanId;
    private final Long testCaseId;

    public UpdateTestCaseVersionRequest(Long testPlanId, Long testCaseId) {
        this.testPlanId = testPlanId;
        this.testCaseId = testCaseId;
    }

    public Long getTestPlanId() {
        return testPlanId;
    }

    public Long getTestCaseId() {
        return testCaseId;
    }

}
