package com.ericsson.cifwk.tm.fun.ui.tms.models.admin;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;

public class ProductWidgetViewModel extends GenericViewModel {

    @UiComponentMapping(".eaTM-ProductWidget-input")
    private TextBox name;

    @UiComponentMapping(".eaTM-ProductWidget-externalInput")
    private TextBox externalId;

    @UiComponentMapping(".eaTM-ProductWidget-tableHolder .ebTable")
    private UiComponent table;

    @UiComponentMapping(".eaTM-ProductWidget-createButton")
    private Button addButton;

    @UiComponentMapping(".eaTM-ProductWidget-editButton")
    private Button editButton;

    @UiComponentMapping(".eaTM-ProductWidget-refreshButton")
    private Button refreshButton;

    public TextBox getName() {
        return name;
    }

    public TextBox getExternalId() {
        return externalId;
    }

    public UiComponent getTable() {
        return table;
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getEditButton() {
        return editButton;
    }

    public Button getRefreshButton() {
        return refreshButton;
    }

}
