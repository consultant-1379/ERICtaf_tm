package com.ericsson.cifwk.tm.fun.ui.tms.models.admin;

import com.ericsson.cds.uisdk.compositecomponents.UiSdkSelectBox;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;

public class GenericWidgetViewModel extends GenericViewModel {

    @UiComponentMapping(".eaTM-GenericAdminWidget-input")
    private TextBox name;

    @UiComponentMapping(".eaTM-GenericAdminWidget-tableHolder .ebTable")
    private UiComponent table;

    @UiComponentMapping(".eaTM-GenericAdminWidget-productHolder")
    private UiSdkSelectBox productSelect;

    @UiComponentMapping(".eaTM-GenericAdminWidget-featureHolder")
    private UiSdkSelectBox featureSelect;

    @UiComponentMapping(".eaTM-GenericAdminWidget-createButton")
    private Button addButton;

    @UiComponentMapping(".eaTM-GenericAdminWidget-editButton")
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

    public UiSdkSelectBox getFeatureSelect() {
        return featureSelect;
    }
}
