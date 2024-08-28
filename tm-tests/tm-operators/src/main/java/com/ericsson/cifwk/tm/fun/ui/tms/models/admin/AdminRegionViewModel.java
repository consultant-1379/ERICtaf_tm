package com.ericsson.cifwk.tm.fun.ui.tms.models.admin;

import com.ericsson.cds.uisdk.compositecomponents.tabs.Tabs;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;

public class AdminRegionViewModel extends GenericViewModel {

    public static final String USER_ACCESS = "User Access";
    public static final String TEAM = "Team";
    public static final String SUITE = "Suite";

    @UiComponentMapping("#TMS_Admin_ContentRegion")
    private UiComponent adminRegion;

    @UiComponentMapping(".eaTM-ViewAdminRegion-details")
    private Tabs tabs;

    public UiComponent getAdminRegion() {
        return adminRegion;
    }

    public void clickTab(String name) {
        tabs.openTab(name);
    }

}
