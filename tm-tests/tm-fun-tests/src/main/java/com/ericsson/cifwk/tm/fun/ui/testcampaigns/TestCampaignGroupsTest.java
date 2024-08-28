package com.ericsson.cifwk.tm.fun.ui.testcampaigns;

import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.tm.fun.common.BaseFuncTest;
import org.testng.annotations.Test;

import static com.ericsson.cifwk.tm.fun.common.CustomAsserts.checkTestStep;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestCampaignGroupsTest extends BaseFuncTest {

    @Test
    @TestId(id = "CIP-186046_Func_1", title = "Navigate to page and get statistics of test campaign group")
    public void getTestCampaignGroupPercentage() {
        createUiOperators();
        checkTestStep(loginOperator.login());
        String url = "#tm/testCampaignGroups/?product=ENM&name=TestCampaignGroup";
        tmUiOperator.navigateTo(url);
        tmUiOperator.refreshPage();
        String percentageOfTestCampaignGroup = tmUiOperator.getPercentageOfTestCampaignGroup();
        assertThat(percentageOfTestCampaignGroup, containsString("13"));

        checkTestStep(loginOperator.logout());
    }
}
