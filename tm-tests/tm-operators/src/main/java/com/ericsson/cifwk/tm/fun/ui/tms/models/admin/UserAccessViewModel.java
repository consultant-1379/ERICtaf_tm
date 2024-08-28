package com.ericsson.cifwk.tm.fun.ui.tms.models.admin;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;

public class UserAccessViewModel extends GenericViewModel {

    public static final String COMPONENT_ITEM = ".ebComponentList-item";

    @UiComponentMapping(".eaTM-AutocompleteInput input")
    private UiComponent name;

    @UiComponentMapping("#TMS_Admin_UserAccessTable")
    private UiComponent table;

    @UiComponentMapping (".ebComponentList")
    private UiComponent multiselectList;

    @UiComponentMapping (".eaTM-UserAccessWidget-addButton")
    private Button addButton;

    public UiComponent getName() {
        return name;
    }

    public UiComponent getTable() {
        return table;
    }

    public UiComponent getMultiselectList() {
        return multiselectList;
    }

    public Button getAddButton() {
        return addButton;
    }
}
