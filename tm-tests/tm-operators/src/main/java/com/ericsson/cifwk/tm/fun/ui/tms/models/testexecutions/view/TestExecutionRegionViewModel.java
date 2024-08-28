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
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;

import java.util.List;

public class TestExecutionRegionViewModel extends GenericViewModel {

    public static final String MULTI_SELECT_ITEM = ".ebMultiSelectList-itemTitle";

    // Duplicated from TestCaseFormRegionViewModel
    public static final String TEST_STEP_SELECTOR = ".eaTM-TestStep";
    public static final String VERIFY_STEP_SELECTOR = ".eaTM-Verify";
    public static final String TEST_STEP_PASS_RESULT_SELECTED = ".eaTM-Result-pass_selected";
    public static final String TEST_STEP_FAIL_RESULT_SELECTED = ".eaTM-Result-fail_selected";

    @UiComponentMapping(".eaTM-CreateTestExecutionContentRegion_loaded")
    private UiComponent testExecutionRegion;

    @UiComponentMapping("#TMS_TestExecution_TestExecutionResult-resultSelect")
    private UiComponent resultSelect;

    @UiComponentMapping("#TMS_TestExecution_TestExecutionResult-resultSelect > .ebSelect-value")
    private UiComponent resultSelectValue;

    @UiComponentMapping("#TMS_TestExecution_TestExecutionResultWidget-comment")
    private TextBox commentBox;

    @UiComponentMapping("#TMS_TestExecution_TestExecutionResultWidget-executionTime")
    private TextBox executionTime;

    @UiComponentMapping(".eaTM-TestExecutionResult-defectIdMultiSelect")
    private TextBox defectIdMultiSelect;

    @UiComponentMapping("#TMS_ViewTestSteps_block")
    private UiComponent testStepsHolder;

    @UiComponentMapping("#TMS_TestExecution_TestExecutionResultWidget-defectIdMultiSelect .ebMultiSelect-textarea")
    private TextBox defectIdTextarea;

    @UiComponentMapping("#TMS_TestExecution_TestExecutionResultWidget-requirementIdMultiSelect .ebMultiSelect-textarea")
    private TextBox requirementIdTextarea;

    @UiComponentMapping(".eaTM-Result-pass")
    private List<UiComponent> passIcons;

    @UiComponentMapping(".eaTM-Result-fail")
    private List<UiComponent> failIcons;

    @UiComponentMapping("#TMS_TestCampaignExecution_createBugButton .ebBtn")
    private UiComponent createBugButton;

    @UiComponentMapping("#TMS_TestExecution_CreateDefect-createButton")
    private UiComponent createFlyoutBugButton;

    @UiComponentMapping("#TMS_CreateTestExecution_CreateTestExecutionBar-projectSelect")
    private UiComponent projectSelect;

    public UiComponent getTestExecutionRegion() {
        return testExecutionRegion;
    }

    public UiComponent getResultSelect() {
        return resultSelect;
    }

    public UiComponent getResultSelectValue() {
        return resultSelectValue;
    }

    public TextBox getCommentBox() {
        return commentBox;
    }

    public TextBox getDefectIdMultiSelect() {
        return defectIdMultiSelect;
    }

    public TextBox getDefectIdTextarea() {
        return defectIdTextarea;
    }

    public UiComponent getTestStepsHolder() {
        return testStepsHolder;
    }

    public List<UiComponent> getPassIcons() {
        return passIcons;
    }

    public List<UiComponent> getFailIcons() {
        return failIcons;
    }

    public UiComponent getCreateBugButton() {
        return createBugButton;
    }

    public UiComponent getCreateFlyoutBugButton() {
        return createFlyoutBugButton;
    }

    public TextBox getExecutionTime() {
        return executionTime;
    }

    public TextBox getRequirementIdTextarea() {
        return requirementIdTextarea;
    }

    public UiComponent getProjectSelect() {
        return projectSelect;
    }
}
