package com.ericsson.cifwk.tm.fun.common.tms;

import com.beust.jcommander.internal.Lists;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;

import java.util.List;

/**
 * Created by egergle on 04/01/2017.
 */
public class TestCaseInfoHelper {

    private TestCaseInfoHelper() {
        // empty
    }

    public static TestCaseInfo getTestCaseInfo(Long id, String testCaseId, String testCaseTitle) {
        TestCaseInfo testCaseInfo = new TestCaseInfo();
        testCaseInfo.setTestCaseId(testCaseId);
        testCaseInfo.setTitle(testCaseTitle);
        testCaseInfo.setId(id);
        return testCaseInfo;
    }

    public static TestCaseInfo getTestCaseInfo(String id) {
        TestCaseInfo testCaseInfo = new TestCaseInfo();
        testCaseInfo.setTestCaseId(id);
        List<TestCampaignInfo> infos = Lists.newArrayList();
        infos.add(getTestPlanInfo(1L, "Test Plan #1"));
        infos.add(getTestPlanInfo(2L, "Test Plan #2"));
        testCaseInfo.setAssociatedTestCampaigns(infos);
        return testCaseInfo;
    }

    public static TestCampaignInfo getTestPlanInfo(long id, String name) {
        TestCampaignInfo testCampaignInfo = new TestCampaignInfo();
        testCampaignInfo.setId(id);
        testCampaignInfo.setName(name);
        return testCampaignInfo;
    }
}
