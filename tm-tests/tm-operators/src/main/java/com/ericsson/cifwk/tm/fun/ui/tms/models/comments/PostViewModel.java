package com.ericsson.cifwk.tm.fun.ui.tms.models.comments;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;


public class PostViewModel extends GenericViewModel {

    @UiComponentMapping("#TMS_TestCaseDetails_ContentRegion.eaTM-ViewTestCaseRegion_loaded")
    private UiComponent postAccordion;

    @UiComponentMapping(".eaTM-AssociatedComments")
    private UiComponent userLogin;

    @UiComponentMapping(".eaTM-AssociatedCommentsNew-editButtonsHolder")
    private UiComponent createdAt;

    @UiComponentMapping(".eaTM-AssociatedCommentsNew-text")
    private UiComponent commentText;

    public UiComponent getPostAccordion() {
        return postAccordion;
    }

    public UiComponent getUserLogin() {
        return userLogin;
    }

    public UiComponent getCreatedAt() {
        return createdAt;
    }

    public UiComponent getCommentText() {
        return commentText;
    }
}
