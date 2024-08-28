package com.ericsson.cifwk.tm.fun.ui.tms.models.testcampaigns.edit;

import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.Link;

public class QuickActionBarEditTPViewModel extends GenericViewModel {

    @UiComponentMapping("#TMS_TestCampaignEdit_EditTestCampaignBar-saveLinkHolder .eaTM-ActionLink-link")
    private Link saveActionLink;

    @UiComponentMapping("#TMS_TestCampaignEdit_EditTestCampaignBar-cancelLinkHolder .eaTM-ActionLink-link")
    private Link cancelActionLink;

    public Link getSaveActionLink() {
        return saveActionLink;
    }

    public Link getCancelActionLink() {
        return cancelActionLink;
    }
}
