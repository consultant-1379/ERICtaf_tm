package com.ericsson.cifwk.tm.fun.ui.tms.models.userinbox;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;

public class UserInboxViewModel extends GenericViewModel {

    @UiComponentMapping("#TMS_UserInbox_ContentRegion")
    private UiComponent userInboxRegion;

    public UiComponent getUserInboxRegion() {
        return userInboxRegion;
    }
}
