package com.ericsson.cifwk.tm.fun.ui.tms.models.admin;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;

public class ReviewGroupWidgetViewModel extends GenericViewModel {

    public static final String REVIEW_GROUP_TABLE = "#TMS_Admin_ReviewGroupTable";

    @UiComponentMapping(".eaTM-ReviewGroupWidget-nameLabel .ebInput")
    private TextBox name;

    @UiComponentMapping(".eaTM-ReviewGroupWidget-groupInput")
    private TextBox groupName;

    @UiComponentMapping(".eaTM-ReviewGroupWidget-groupTableHolder .ebTable")
    private UiComponent reviewGroupTable;

    @UiComponentMapping(".eaTM-ReviewGroupWidget-userTableHolder .ebTable")
    private UiComponent userTable;

    @UiComponentMapping(".eaTM-ReviewGroupWidget-createButton")
    private Button addButton;

    @UiComponentMapping(".eaTM-ReviewGroupWidget-editButton")
    private Button editButton;


    public TextBox getName() {
        return name;
    }

    public TextBox getGroup() {
        return groupName;
    }

    public UiComponent getReviewGroupTable() {
        return reviewGroupTable;
    }

    public UiComponent getUserTable() {
        return userTable;
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getEditButton() {
        return editButton;
    }

}
