package com.ericsson.cifwk.tm.fun.ui.tms.models.admin;

import com.ericsson.cds.uisdk.compositecomponents.UiSdkSelectBox;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;

public class GenericProductWidgetViewModel extends GenericViewModel {

    @UiComponentMapping(".eaTM-GenericProductWidget-input")
    private TextBox name;

    @UiComponentMapping(".eaTM-GenericProductWidget-tableHolder .ebTable")
    private UiComponent table;

    @UiComponentMapping(".eaTM-GenericProductWidget-productHolder")
    private UiSdkSelectBox productSelect;


    @UiComponentMapping(".eaTM-GenericProductWidget-createButton")
    private Button addButton;

    @UiComponentMapping(".eaTM-GenericProductWidget-editButton")
    private Button editButton;

    public TextBox getName() {
        return name;
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

    public UiSdkSelectBox getProductSelect() {
        return productSelect;
    }
}
