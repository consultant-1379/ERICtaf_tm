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

public class ChangeLockStateRequest {

    private final long testPlanId;
    private final boolean lock;

    public ChangeLockStateRequest(long testPlanId, boolean lock) {
        this.testPlanId = testPlanId;
        this.lock = lock;
    }

    public long getTestPlanId() {
        return testPlanId;
    }

    public boolean shouldLock() {
        return lock;
    }

}
