package com.ericsson.cifwk.tm.fun.ui.tms.models.requirements.tree;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;

public class TreeViewModel extends GenericViewModel {

    public static final String TREE_WIDGET_EXPANDABLE_CLASS = "ebTreeItem ebTreeItem_expandable";
    public static final String EXPANDED_ICON_CLASS = "ebIcon_downArrow";
    public static final String EXPANDABLE_ICON_SELECTOR = ".ebTreeItem-expandButton .ebIcon";

    @UiComponentMapping("#TMS_Requirements_RequirementsTreeWidget-treeHolder .ebTree")
    private UiComponent treeWidget;

    public UiComponent getTreeWidget() {
        return treeWidget;
    }

}
