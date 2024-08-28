package com.ericsson.cifwk.tm.fun.ui.tms.models.common;

import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.Select;

public class CopyTestCampaignsDialogModel extends GenericViewModel {

    @UiComponentMapping(".ebDialog .ebDialogBox-actionBlock > .ebBtn.ebBtn_color_green")
    private Button copyButton;

    @UiComponentMapping(".ebDialog .ebDialogBox-contentBlock .ebDialogBox-thirdText .ebSelect")
    private Select dropSelect;

    public Button getCopyButton() {
        return copyButton;
    }

    public Select getDropSelect() {
        return dropSelect;
    }
}
