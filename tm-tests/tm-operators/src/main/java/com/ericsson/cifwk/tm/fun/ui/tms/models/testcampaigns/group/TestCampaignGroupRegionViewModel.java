/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.tms.models.testcampaigns.group;

import com.ericsson.cifwk.taf.ui.core.SelectorType;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;

import java.util.List;

public class TestCampaignGroupRegionViewModel extends GenericViewModel {

    @UiComponentMapping("#TMS_TestCampaignGroup_ContentRegion")
    private UiComponent testCampaignGroupRegion;

    @UiComponentMapping(".eaTM-TestCampaignGroup-totalTestCases")
    private UiComponent totalTestCases;

    public UiComponent getTestCampaignGroupRegion() {
        return testCampaignGroupRegion;
    }

    public UiComponent getTotalTestCases() {
        return totalTestCases;
    }

    public UiComponent getTreeNode(String itemName) {
        return testCampaignGroupRegion.getDescendantsBySelector(SelectorType.XPATH,
                "//div[contains(text(), '" + itemName + "')]").get(0);
    }
}
