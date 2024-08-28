package com.ericsson.cifwk.tm.fun.ui.tms.models.testcampaigns.create;

import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.Link;

public class QuickActionBarCreateTPViewModel  extends GenericViewModel {

    @UiComponentMapping("#TMS_CreateTestCampaign_CreateTestCampaignBar-createLinkHolder .eaTM-ActionLink-link")
    private Link createActionLink;

    public Link getCreateActionLink() {
        return createActionLink;
    }

}
