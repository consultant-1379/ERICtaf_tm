package com.ericsson.cifwk.tm.fun.ui.tms.models.testcampaigns.list;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.Link;

import java.util.List;

public class TestCampaignsRegionViewModel extends GenericViewModel {

    public static final String TMS_TEST_PLANS_ID = "#TMS_TestCampaignList_TestCampaigns";
    public static final String TMS_TEST_PLANS_TABLE_ID = "#TMS_TestCampaignList_TestCampaignTable";
    public static final String TMS_TEST_PLANS_CONTENT_ID = "#TMS_TestCampaignList_TestCampaigns-content";
    public static final String TMS_TEST_CAMPAIGNS_LIST_FILTER_HOLDER_ID = "#TMS_TestCampaignList_ViewTestCampaignsBar-filterHolder";

    @UiComponentMapping("#TMS_TestCampaignList_ContentRegion")
    private UiComponent testCampaignsRegion;

    @UiComponentMapping("#TMS_TestCampaignList_ViewTestCampaignsBar-createLinkHolder a")
    private Link createLink;

    @UiComponentMapping("#TMS_TestCampaignList_ViewTestCampaignsBar-copyLinkHolder a")
    private Link copyLink;

    @UiComponentMapping(".eaTM-ViewTestCampaignsBar-actions .ebQuickActionBar-back")
    private UiComponent search;

    @UiComponentMapping(TMS_TEST_PLANS_TABLE_ID)
    private UiComponent table;

    @UiComponentMapping(TMS_TEST_PLANS_TABLE_ID + " > tbody > tr.ebTableRow")
    private List<UiComponent> tableRows;

    public UiComponent getTestCampaignsRegion() {
        return testCampaignsRegion;
    }

    public Link getCreateLink() {
        return createLink;
    }

    public Link getCopyLink() {
        return copyLink;
    }

    public UiComponent getSearch() {
        return search;
    }

    public UiComponent getTable() {
        return table;
    }

    public List<UiComponent> getTableRows() {
        return tableRows;
    }
}
