package com.ericsson.cifwk.tm.fun.ui.tms.models.admin;

import com.ericsson.cds.uisdk.compositecomponents.UiSdkSelectBox;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;

public class ProductWidgetExternalIdViewModel extends GenericViewModel {

    @UiComponentMapping(".eaTM-GenericProductWidgetExternalId-productHolder")
    private UiSdkSelectBox productSelect;

    @UiComponentMapping(".eaTM-GenericProductWidgetExternalId-input")
    private TextBox name;

    @UiComponentMapping(".eaTM-GenericProductWidgetExternalId-externalInput")
    private TextBox externalId;

    @UiComponentMapping(".eaTM-GenericProductWidgetExternalId-tableHolder .ebTable")
    private UiComponent table;

    @UiComponentMapping(".eaTM-GenericProductWidgetExternalId-createButton")
    private Button addButton;

    @UiComponentMapping(".eaTM-GenericProductWidgetExternalId-editButton")
    private Button editButton;


    public TextBox getName() {
        return name;
    }

    public TextBox getExternalId() {
        return externalId;
    }

    public UiSdkSelectBox getProductSelect() {
        return productSelect;
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

}
