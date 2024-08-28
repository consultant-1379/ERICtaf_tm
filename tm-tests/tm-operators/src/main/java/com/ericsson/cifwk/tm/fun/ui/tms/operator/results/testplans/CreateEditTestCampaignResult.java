package com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testplans;


import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.HasResult;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;

public final class CreateEditTestCampaignResult implements HasResult {
    
    private final boolean success;
    private final TestCampaignInfo testCampaignInfo;
    private final String errorMessage;

    private CreateEditTestCampaignResult(boolean success, TestCampaignInfo testCampaignInfo, String errorMessage) {
        this.success = success;
        this.testCampaignInfo = testCampaignInfo;
        this.errorMessage = errorMessage;
    }

    public static CreateEditTestCampaignResult success(TestCampaignInfo testCampaignInfo) {
        return new CreateEditTestCampaignResult(true, testCampaignInfo, null);
    }

    public static CreateEditTestCampaignResult error(String errorMessage) {
        return new CreateEditTestCampaignResult(false, null, errorMessage);
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    public TestCampaignInfo getTestCampaignInfo() {
        return testCampaignInfo;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
    
}
