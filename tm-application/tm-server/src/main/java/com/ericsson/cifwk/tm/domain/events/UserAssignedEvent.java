/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */
package com.ericsson.cifwk.tm.domain.events;

import java.util.List;

public class UserAssignedEvent {
    private final Long testPlanId;
    private final String userId;
    private final String hostname;
    private final List<String> testCases;

    public UserAssignedEvent(Long testPlanId, String userId, List<String> testCases, String hostname) {
        this.testPlanId = testPlanId;
        this.userId = userId;
        this.testCases = testCases;
        this.hostname = hostname;
    }

    public Long getTestPlanId() {
        return testPlanId;
    }

    public String getUserId() {
        return userId;
    }

    public String getHostName() {
        return hostname;
    }

    public List<String> getTestCases() {
        return testCases;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAssignedEvent)) return false;

        UserAssignedEvent that = (UserAssignedEvent) o;

        if (testCases != null ? !testCases.equals(that.testCases) : that.testCases != null) return false;
        if (testPlanId != null ? !testPlanId.equals(that.testPlanId) : that.testPlanId != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (hostname != null ? !hostname.equals(that.hostname) : that.hostname != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = testPlanId != null ? testPlanId.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (testCases != null ? testCases.hashCode() : 0);
        return result;
    }
}
