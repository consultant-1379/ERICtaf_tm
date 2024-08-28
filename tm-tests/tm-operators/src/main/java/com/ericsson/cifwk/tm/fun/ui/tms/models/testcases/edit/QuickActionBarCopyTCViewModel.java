package com.ericsson.cifwk.tm.fun.ui.tms.models.testcases.edit;

import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.Link;

public class QuickActionBarCopyTCViewModel extends GenericViewModel {

    @UiComponentMapping("#TMS_CopyTestCase_CopyTestCaseBar-createLinkHolder .eaTM-ActionLink-link")
    private Link createActionLink;

    public Link getCreateActionLink() {
        return createActionLink;
    }
}
