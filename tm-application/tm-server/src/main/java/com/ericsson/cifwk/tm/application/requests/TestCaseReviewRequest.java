package com.ericsson.cifwk.tm.application.requests;

/**
 * @author egergle
 */
public final class TestCaseReviewRequest {

    private Long testCaseId;
    private String status;
    private String type;
    private long reviewGroupId;
    private String reviewUserId;

    public TestCaseReviewRequest(Long testCaseId, String status, String type, long reviewGroupId, String reviewUserId) {
        this.testCaseId = testCaseId;
        this.status = status;
        this.type = type;
        this.reviewGroupId = reviewGroupId;
        this.reviewUserId = reviewUserId;
    }

    public Long getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(Long testCaseId) {
        this.testCaseId = testCaseId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getReviewGroupId() {
        return reviewGroupId;
    }

    public void setReviewGroupId(long reviewGroupId) {
        this.reviewGroupId = reviewGroupId;
    }

    public String getReviewUserId() {
        return reviewUserId;
    }

    public void setReviewUserId(String reviewUserId) {
        this.reviewUserId = reviewUserId;
    }
}
