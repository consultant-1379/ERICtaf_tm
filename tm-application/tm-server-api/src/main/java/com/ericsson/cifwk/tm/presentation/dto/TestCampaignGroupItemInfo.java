/*
 * COPYRIGHT Ericsson (c) 2017.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.dto;

public class TestCampaignGroupItemInfo {

    private String testCampaignName;

    private String drop;

    private String environment;

    private TestCampaignItemInfo testCampaignItemInfo;

    public TestCampaignGroupItemInfo(String testCampaignName, String drop, String environment,
                                     TestCampaignItemInfo testCampaignItemInfo) {
        this.testCampaignName = testCampaignName;
        this.drop = drop;
        this.environment = environment;
        this.testCampaignItemInfo = testCampaignItemInfo;
    }


    public String getTestCampaignName() {
        return testCampaignName;
    }

    public void setTestCampaignName(String testCampaignName) {
        this.testCampaignName = testCampaignName;
    }

    public String getDrop() {
        return drop;
    }

    public TestCampaignItemInfo getTestCampaignItemInfo() {
        return testCampaignItemInfo;
    }

    public void setTestCampaignItemInfo(TestCampaignItemInfo testCampaignItemInfo) {
        this.testCampaignItemInfo = testCampaignItemInfo;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}
