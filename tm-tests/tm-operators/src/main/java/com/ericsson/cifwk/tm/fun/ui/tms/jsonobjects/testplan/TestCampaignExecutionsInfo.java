package com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.testplan;

import com.ericsson.cifwk.tm.fun.ui.testcases.jsonobjects.references.TestCaseExecutions;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;

import java.util.List;

public class TestCampaignExecutionsInfo {

    private TestCampaignInfo testCampaignInfo;
    private List<TestCaseExecutions> testCaseExecutions;

    public TestCampaignExecutionsInfo(TestCampaignInfo testCampaignInfo, List<TestCaseExecutions> testCaseExecutions) {
        this.testCampaignInfo = testCampaignInfo;
        this.testCaseExecutions = testCaseExecutions;
    }

    public List<TestCaseExecutions> getTestCaseExecutions() {
        return testCaseExecutions;
    }

    public TestCampaignInfo getTestCampaignInfo() {
        return testCampaignInfo;
    }
}
