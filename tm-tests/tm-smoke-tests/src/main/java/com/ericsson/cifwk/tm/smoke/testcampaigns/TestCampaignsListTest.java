package com.ericsson.cifwk.tm.smoke.testcampaigns;

import com.ericsson.cifwk.taf.TafTestBase;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.tm.fun.ui.login.operator.RestLoginOperator;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.RestTestManagementOperator;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testplans.CreateEditTestCampaignResult;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static com.ericsson.cifwk.tm.smoke.common.CustomAsserts.checkTestStep;

public class TestCampaignsListTest extends TafTestBase {

    private Host host;

    @Inject
    RestTestManagementOperator tmOperator;

    @Inject
    RestLoginOperator loginOperator;

    @BeforeClass(alwaysRun = true)
    public final void setUp() {
        host = DataHandler.getHostByName("tm");
        loginOperator.start(host);
        tmOperator.start(host);
    }

    @Test
    @TestId(id = "DURACI-2526_Smoke_1", title = "View Test Campaign")
    public void viewTestCampaign() {
        TestCampaignInfo testCampaignInfo = new TestCampaignInfo();
        testCampaignInfo.setId(10l);
        CreateEditTestCampaignResult result = tmOperator.viewTestPlan(testCampaignInfo);
        checkTestStep(result);
    }
}
