package com.ericsson.cifwk.tm.fun.ui.tms.models.testcases.view;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.Link;

public class QuickActionBarViewTCViewModel extends GenericViewModel {

    @UiComponentMapping("#TMS_TestCaseDetails_ViewTestCaseBar-createNewTestCaseLinkHolder .eaTM-ActionLink-link")
    private Link createLink;

    @UiComponentMapping("#TMS_TestCaseDetails_ViewTestCaseBar-editLinkHolder .eaTM-ActionLink-link")
    private Link editLink;

    @UiComponentMapping("#TMS_TestCaseDetails_ViewTestCaseBar-removeLinkHolder .eaTM-ActionLink-link")
    private Link removeLink;

    @UiComponentMapping("#TMS_TestCaseDetails_ViewTestCaseBar-copyLinkHolder .eaTM-ActionLink-link")
    private Link copyLink;

    @UiComponentMapping("#TMS_TestCaseDetails_ViewTestCaseBar-backText")
    private Link backLink;

    @UiComponentMapping("#TMS_TestCaseDetails_ViewTestCaseBar-versionSelect")
    private UiComponent versionSelect;

    @UiComponentMapping("#TMS_TestCaseDetails_ViewTestCaseBar-showTestPlansLinkHolder .eaTM-ActionLink-link")
    private Link showTestPlansLink;

    @UiComponentMapping("#TMS_TestCaseDetails_ViewTestCaseBar-sendForReview .eaTM-ActionLink-link")
    private Link sendForReviewLink;

    @UiComponentMapping("#TMS_TestCaseDetails_ViewTestCaseBar-approvalHolder .eaTM-ActionLink-link")
    private Link approveLink;

    @UiComponentMapping("#TMS_TestCaseDetails_ViewTestCaseBar-showCommentsLinkHolder")
    private Link showCommentsLinkHolder;

    public Link getCreateLink() {
        return createLink;
    }

    public Link getEditLink() {
        return editLink;
    }

    public Link getRemoveLink() {
        return removeLink;
    }

    public Link getCopyLink() {
        return copyLink;
    }

    public Link getBackLink() {
        return backLink;
    }

    public UiComponent getVersionSelect() {
        return versionSelect;
    }

    public Link getShowTestPlansLink() {
        return showTestPlansLink;
    }

    public Link getShowCommentsLinkHolder() {
        return showCommentsLinkHolder;
    }

    public Link getSendForReviewLink() {
        return sendForReviewLink;
    }

    public Link getApproveLink() {
        return approveLink;
    }
}
