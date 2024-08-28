package com.ericsson.cifwk.tm.fun.ui.tms.models.testcases.list;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.Link;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;

public class TestCaseSearchViewModel extends GenericViewModel {

    @UiComponentMapping("#TMS_TestCaseSearch_DefaultBar-searchInput")
    private TextBox searchInput;

    @UiComponentMapping("#TMS_TestCaseSearch_DefaultBar-searchButton")
    private Button searchButton;

    @UiComponentMapping("#TMS_TestCaseSearch_DefaultBar-moreButton")
    private Button moreButton;

    @UiComponentMapping("#TMS_TestCaseSearch_DefaultBar-createTestCaseLinkHolder a")
    private Link createTestCaseLink;

    @UiComponentMapping("#TMS_TestCaseSearch_DefaultBar-testPlansLinkHolder a")
    private Link testPlansLink;

    @UiComponentMapping("#TMS_TestCaseSearch_DefaultBar-requirementsLinkHolder a")
    private Link requirementsLink;

    @UiComponentMapping(".eaTM-Results-addToTestPlanLinkHolder a")
    private Link addToTestCampaignLink;

    @UiComponentMapping("#TMS_TestCaseSearch_ContentRegion-resultsPanel")
    private UiComponent resultsPanel;

    @UiComponentMapping("#TMS_TestCaseSearch_AddToTestCampaign")
    private UiComponent testCampaignSelect;

    @UiComponentMapping(".ebDialogBox-actionBlock ebBtn_color_green span")
    private UiComponent testCampaignSelectAddButton;

    @UiComponentMapping("#TMS_ContextSearchMenu-productSelect")
    private UiComponent productSelect;

    @UiComponentMapping("#TMS_ContextSearchMenu-productSelect .ebSelect-value")
    private UiComponent productSelectValue;

    @UiComponentMapping(".eaTM-Menu-button")
    private UiComponent contextMenuButton;

    @UiComponentMapping("#TMS_TestCaseSearch_ListResults")
    private UiComponent resultsTable;

    public TextBox getSearchInput() {
        return searchInput;
    }

    public Button getSearchButton() {
        return searchButton;
    }

    public Button getMoreButton() {
        return moreButton;
    }

    public Link getCreateTestCaseLink() {
        return createTestCaseLink;
    }

    public Link getTestPlansLink() {
        return testPlansLink;
    }

    public Link getRequirementsLink() {
        return requirementsLink;
    }

    public UiComponent getResultsPanel() {
        return resultsPanel;
    }

    public Link getAddToTestCampaignLink() {
        return addToTestCampaignLink;
    }

    public UiComponent getTestCampaignSelect() {
        return testCampaignSelect;
    }

    public UiComponent getTestCampaignSelectAddButton() {
        return testCampaignSelectAddButton;
    }

    public UiComponent getProductSelect() {
        return productSelect;
    }

    public UiComponent getProductSelectValue() {
        return productSelectValue;
    }

    public UiComponent getContextMenuButton() {
        return contextMenuButton;
    }

    public UiComponent getResultsTable() {
        return resultsTable;
    }
}
