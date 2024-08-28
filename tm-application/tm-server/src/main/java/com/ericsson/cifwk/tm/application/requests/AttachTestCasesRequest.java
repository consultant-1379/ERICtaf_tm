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

import com.ericsson.cifwk.tm.presentation.dto.TestInfoList;

public final class AttachTestCasesRequest {

    private final long testPlanId;
    private final TestInfoList testInfoList;

    public AttachTestCasesRequest(long testPlanId, TestInfoList testInfoList) {
        this.testPlanId = testPlanId;
        this.testInfoList = testInfoList;
    }

    public long getTestPlanId() {
        return testPlanId;
    }

    public TestInfoList getTestInfoList() {
        return testInfoList;
    }

}
