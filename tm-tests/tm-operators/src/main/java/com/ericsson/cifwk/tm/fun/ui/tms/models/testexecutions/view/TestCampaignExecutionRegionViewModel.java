/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.tms.models.testexecutions.view;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;

import java.util.List;

public class TestCampaignExecutionRegionViewModel extends GenericViewModel {

    public static final String TMS_TE_REGION = "#TMS_TestCampaignExecution_ContentRegion";
    public static final String TMS_TE_USERNAME_ID = "#TMS_TestCampaignExecution_TestExecution-username";
    public static final String TMS_TE_DATE_ID = "#TMS_TestCampaignExecution_TestExecution-date";
    public static final String TMS_TE_COMMENT_BLOCK_ID = "#TMS_TestCampaignExecution_TestExecution-commentBlock";
    public static final String TMS_TE_RESULT_TEXT_ID = "#TMS_TestCampaignExecution_TestExecution-resultText";
    public static final String TMS_TE_REQUIREMENT_LINK = "#TMS_TestCampaignExecution_TestExecution-requirementsHolder .eaTM-TestExecution-requirementLink";
    public static final String TMS_TE_DEFECT_LINK = "#TMS_TestCampaignExecution_TestExecution-defectsHolder .eaTM-TestExecution-defectLink";
    public static final String TMS_TE_TEST_CASE_BUTTON = ".eaTM-ButtonCell-button";
    public static final String TMS_TE_TEST_CASES_TABLE_ID = "#TMS_TestCampaignExecution_TestCaseTable";
    public static final String TMS_TE_FILTER_CONTENT_CLASSNAME = ".eaTM-TestCampaignExecutionContentRegion-filterContent";
    public static final String TMS_TE_MULTISELECT_TABLE_ID = "#TMS_TestCampaignExecution_TestExecutionMultiSelectTable";
    public static final String TMS_TE_CREATE_DEFECT = "#TMS_TestExecution_CreateDefect-";

    @UiComponentMapping(TMS_TE_REGION + ".eb-animatedPage_current")
    private UiComponent testCampaignExecutionRegion;

    @UiComponentMapping("#TMS_TestExecutionCampaign_TestCampaignDetailsBody-name")
    private UiComponent testCampaignName;

    @UiComponentMapping("#TMS_TestExecutionCampaign_TestCampaignDetailsBody-description")
    private UiComponent testCampaignDescription;

    @UiComponentMapping("#TMS_TestExecutionCampaign_TestCampaignDetailsBody-release")
    private UiComponent testCampaignRelease;

    @UiComponentMapping("#TMS_TestExecutionCampaign_TestCampaignDetailsBody-environment")
    private UiComponent testCampaignEnvironment;

    @UiComponentMapping(TMS_TE_TEST_CASES_TABLE_ID + " > tbody > tr.ebTableRow")
    private List<UiComponent> testCasesRows;

    @UiComponentMapping(TMS_TE_TEST_CASES_TABLE_ID)
    private UiComponent testCasesTable;

    @UiComponentMapping("#TMS_TestCampaignExecution_TestExecutions > .eaTM-TestExecution")
    private List<UiComponent> testExecutions;

    @UiComponentMapping("#TMS_TestCampaignExecution_TestExecutions")
    private UiComponent testExecutionsContent;

    @UiComponentMapping(".eaTM-TestCampaignExecutionContentRegion-filterPanel")
    private UiComponent filterPanel;

    @UiComponentMapping(".eaTM-TestCampaignExecutionContentRegion-filterButton")
    private UiComponent filterButton;

    @UiComponentMapping(".eaTM-TestCampaignExecutionContentRegion-hideButton")
    private UiComponent hideSearchButton;

    @UiComponentMapping("#TMS_requirements_DetailsBlock-progressBlock")
    private UiComponent progressBlock;

    @UiComponentMapping("#TMS_TestCampaignExecution_TestCampaignExecutionContentRegion-executeMultipleButton")
    private Button addResults;

    @UiComponentMapping("#TMS_TestCampaignExecution_TestExecutionMultiSelectWidget-createButton")
    private Button createButton;

    @UiComponentMapping(TMS_TE_MULTISELECT_TABLE_ID)
    private UiComponent testCasesMultiSelectTable;

    @UiComponentMapping("#TMS_TestCampaignExecution_TestExecutionMultiSelectWidget-resultSelect")
    private UiComponent resultSelect;

    public UiComponent getTestCampaignExecutionRegion() {
        return testCampaignExecutionRegion;
    }

    public UiComponent getTestCampaignName() {
        return testCampaignName;
    }

    public UiComponent getTestCampaignDescription() {
        return testCampaignDescription;
    }

    public UiComponent getTestCampaignRelease() {
        return testCampaignRelease;
    }

    public UiComponent getTestCampaignEnvironment() {
        return testCampaignEnvironment;
    }

    public List<UiComponent> getTestCasesRows() {
        return testCasesRows;
    }

    public List<UiComponent> getTestExecutions() {
        return testExecutions;
    }

    public UiComponent getProgressBlock() {
        return progressBlock;
    }

    public UiComponent getTestCasesTable() {
        return testCasesTable;
    }

    public UiComponent getFilterPanel() {
        return filterPanel;
    }

    public UiComponent getFilterButton() {
        return filterButton;
    }

    public UiComponent getHideSearchButton() {
        return hideSearchButton;
    }

    public Button getAddResults() {
        return addResults;
    }

    public UiComponent getResultSelect() {
        return resultSelect;
    }

    public UiComponent getTestCasesMultiSelectTable() {
        return testCasesMultiSelectTable;
    }

    public Button getCreateButton() {
        return createButton;
    }

    public UiComponent getTestExecutionsContent() {
        return testExecutionsContent;
    }
}
