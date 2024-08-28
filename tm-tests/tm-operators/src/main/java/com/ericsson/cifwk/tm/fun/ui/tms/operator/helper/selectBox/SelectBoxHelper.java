package com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.selectBox;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;

import java.util.List;

/**
 * Created by egergle on 22/09/2016.
 */
public class SelectBoxHelper {

    private SelectBoxHelper() {
        // no constructor
    }

    public static void setSelectBox(UiComponent selectComponent, ViewModel view, String name) {
        selectComponent.click();

        UiComponent itemsBox = view.getViewComponent(".ebComponentList");
        if(!itemsBox.isDisplayed()) {
            selectComponent.click();
        }

        List<UiComponent> componentItems = view.getViewComponents(".ebComponentList .ebComponentList-item", UiComponent.class);
        for (UiComponent componentItem : componentItems) {
            if (componentItem.getText().equals(name)) {
                componentItem.click();
                break;
            }
        }
    }
}
