package com.ericsson.cifwk.tm.fun.ui.tms.models.testcampaigns.view;

import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.Link;

public class QuickActionBarViewTPViewModel extends GenericViewModel {

    @UiComponentMapping("#TMS_TestCampaignDetails_TestCampaignDetailsBar-editLinkHolder .eaTM-ActionLink-link")
    private Link editLink;

    @UiComponentMapping("#TMS_TestCampaignDetails_TestCampaignDetailsBar-removeLinkHolder .eaTM-ActionLink-link")
    private Link removeLink;

    @UiComponentMapping("#TMS_TestCampaignDetails_TestCampaignDetailsBar-statusChangeBlock .eaTM-ActionLink-link")
    private Link lockUnlockLink;

    @UiComponentMapping("#TMS_TestCampaignDetails_TestCampaignDetailsBar-copyLinkHolder .eaTM-ActionLink-link")
    private Link copyLink;

    @UiComponentMapping("#TMS_TestCampaignDetails_TestCampaignDetailsBar-executionLinkHolder .eaTM-ActionLink-link")
    private Link executionLink;

    public Link getEditLink() {
        return editLink;
    }

    public Link getRemoveLink() {
        return removeLink;
    }

    public Link getLockUnlockLink() {
        return lockUnlockLink;
    }

    public Link getCopyLink() {
        return copyLink;
    }

    public Link getExecutionLink() {
        return executionLink;
    }

}
