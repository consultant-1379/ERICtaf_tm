package com.ericsson.cifwk.tm.fun.ui.tms.models.common;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.FileSelector;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;

import java.util.List;

public class ComponentModel extends GenericViewModel {

    @UiComponentMapping(".ebComponentList")
    private UiComponent componentList;

    @UiComponentMapping(".ebComponentList .ebComponentList-item")
    private List<UiComponent> componentItems;

    public List<UiComponent> getComponentItems() {
        return componentItems;
    }

    public UiComponent getComponentList() {
        return componentList;
    }
}


