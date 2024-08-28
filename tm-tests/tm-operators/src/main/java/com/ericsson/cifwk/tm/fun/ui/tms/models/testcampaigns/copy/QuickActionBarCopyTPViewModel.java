package com.ericsson.cifwk.tm.fun.ui.tms.models.testcampaigns.copy;

import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.Link;

public class QuickActionBarCopyTPViewModel extends GenericViewModel {

    @UiComponentMapping("#TMS_TestCampaignCopy_CopyTestCampaignBar-createLinkHolder .eaTM-ActionLink-link")
    private Link createActionLink;

    public Link getCreateActionLink() {
        return createActionLink;
    }

}
