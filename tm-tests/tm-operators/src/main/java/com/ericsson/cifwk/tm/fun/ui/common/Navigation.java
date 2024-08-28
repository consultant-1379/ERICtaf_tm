/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.common;

public class Navigation {

    private String appUrl;

    public void setup(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getLoginUrl() {
        return this.appUrl + "/login";
    }

    public String getTestPlanExecutionUrl(String testPlanId) {
        return this.appUrl + "/#tm/testCampaignExecution/" + testPlanId;
    }

    public String getTestExecutionUrl(String testPlanId, String testCaseId) {
        return getTestPlanExecutionUrl(testPlanId) + "/testCase/" + testCaseId;
    }
}
