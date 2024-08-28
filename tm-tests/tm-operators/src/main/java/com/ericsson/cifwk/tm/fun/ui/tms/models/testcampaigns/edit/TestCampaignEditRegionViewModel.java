/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.tms.models.testcampaigns.edit;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;

import java.util.List;

public class TestCampaignEditRegionViewModel extends GenericViewModel {

    public static final String TMS_TP_TEST_CASE_TABLE_ID = "#TMS_TestCampaignList_testCaseTable";
    public static final String TMS_ACTION_LINK = ".eaTM-ActionLink-link";
    public static final String TMS_TEST_CAMPAIGN_EDIT_FILTER_HOLDER_ID = "#TMS_TestCampaignEdit_TestCampaignFormWidget-filterHolder";

    @UiComponentMapping("#TMS_TestCampaignEdit_ContentRegion")
    private UiComponent testCampaignEditRegion;

    @UiComponentMapping("#TMS_TestCampaignEdit_TestCampaignFormWidget-name")
    private TextBox nameInput;

    @UiComponentMapping("#TMS_TestCampaignEdit_TestCampaignFormWidget-description")
    private TextBox descriptionTextarea;

    @UiComponentMapping("#TMS_TestCampaignEdit_TestCampaignFormWidget-environment")
    private TextBox environmentName;

    @UiComponentMapping(".eaTM-AutocompleteInput-input")
    private TextBox testCaseInput;

    @UiComponentMapping("#TMS_TestCampaignEdit_TestCampaignTestCases-linkButton")
    private Button testCaseAddButton;

    @UiComponentMapping("#TMS_TestCampaignEdit_TestCampaignTestCases-addMultipleButton")
    private Button testCaseMultipleAddButton;

    @UiComponentMapping(TMS_TP_TEST_CASE_TABLE_ID)
    private UiComponent testCaseTable;

    @UiComponentMapping(TMS_TP_TEST_CASE_TABLE_ID + " > tbody > tr.ebTableRow > td:first-child")
    private List<UiComponent> testCaseIdCells;

    @UiComponentMapping(TMS_TP_TEST_CASE_TABLE_ID + " > tbody > tr.ebTableRow > th > i")
    private List<UiComponent> testCaseRemoveCells;

    @UiComponentMapping(TMS_TP_TEST_CASE_TABLE_ID + " > tbody > tr.ebTableRow")
    private List<UiComponent> tableRows;

    @UiComponentMapping("#TMS_TestCampaignEdit_TestCampaignTestCases-assignUpdateButtons")
    private List<UiComponent> assignUpdateButtons;

    @UiComponentMapping(".ebDialogBox")
    private UiComponent userDialogBox;

    @UiComponentMapping (".ebComponentList")
    private UiComponent multiselectList;

    public UiComponent getTestCampaignEditRegion() {
        return testCampaignEditRegion;
    }

    public TextBox getNameInput() {
        return nameInput;
    }

    public TextBox getDescriptionTextarea() {
        return descriptionTextarea;
    }

    public TextBox getEnvironmentName() {
        return environmentName;
    }

    public TextBox getTestCaseInput() {
        return testCaseInput;
    }

    public Button getTestCaseAddButton() {
        return testCaseAddButton;
    }

    public List<UiComponent> getTestCaseIdCells() {
        return testCaseIdCells;
    }

    public List<UiComponent> getTableRows() {
        return tableRows;
    }

    public List<UiComponent> getTestCaseRemoveCells() {
        return testCaseRemoveCells;
    }

    public Button getTestCaseMultipleAddButton() {
        return testCaseMultipleAddButton;
    }

    public UiComponent getTestCaseTable() {
        return testCaseTable;
    }

    public List<UiComponent> getAssignUpdateButtons() {
        return assignUpdateButtons;
    }

    public UiComponent getUserDialogBox() {
        return userDialogBox;
    }

    public UiComponent getMultiselectList() {
        return multiselectList;
    }
}
