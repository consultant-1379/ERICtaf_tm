package com.ericsson.cifwk.tm.presentation.dto;

public class TestCaseSubscriptionInfo {

    private String testCaseId;

    private String userId;

    private boolean subscribed;

    public TestCaseSubscriptionInfo() {
        // default constructor
    }

    public TestCaseSubscriptionInfo(String testCaseId, String userId, boolean subscribed) {
        this.testCaseId = testCaseId;
        this.userId = userId;
        this.subscribed = subscribed;
    }

    public String getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(String testCaseId) {
        this.testCaseId = testCaseId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }
}
