package com.ericsson.cifwk.tm.fun.ui.tms.models.testcases.edit;

import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.Link;

public class QuickActionBarEditTCViewModel extends GenericViewModel {

    @UiComponentMapping("#TMS_EditTestCase_EditTestCaseBar-saveLinkHolder .eaTM-ActionLink-link")
    private Link saveActionLink;

    public Link getSaveActionLink() {
        return saveActionLink;
    }

}
