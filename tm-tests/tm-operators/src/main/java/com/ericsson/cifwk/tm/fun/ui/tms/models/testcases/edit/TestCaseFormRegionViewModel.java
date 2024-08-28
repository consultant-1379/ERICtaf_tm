package com.ericsson.cifwk.tm.fun.ui.tms.models.testcases.edit;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.Link;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;

import java.util.List;

public class TestCaseFormRegionViewModel extends GenericViewModel {

    public static final String TEST_STEP_EDIT_WIDGET = ".eaTM-TestStepEdit";
    public static final String TEST_STEP_DATA_TEXTBOX = ".eaTM-TestStepEdit-dataField";
    public static final String TEST_STEP_EDIT_TITLE = ".eaTM-TestStepEdit-title";
    public static final String TEST_STEP_EDIT_ADDLINK = ".eaTM-TestStepEdit-addLink";
    public static final String TEST_STEP_EDIT_VERIFIES_LIST = ".eaTM-TestStepEdit-verifiesList";
    public static final String TEST_STEP_EDIT_VERIFY_EDIT_TITLE = ".eaTM-VerifyEdit-title";
    public static final String TEST_STEP_STYLE = ".eaTM-TestStep";
    public static final String TEST_STEP_PASS_RESULT = ".eaTM-Result-pass";
    public static final String TEST_STEP_PASS_RESULT_SELECTED = ".eaTM-Result-pass_selected";
    public static final String TEST_STEP_FAIL_RESULT = ".eaTM-Result-fail";
    public static final String TEST_STEP_FAIL_RESULT_SELECTED = ".eaTM-Result-fail_selected";
    public static final String TEST_STEP_COPY_ICON = ".eaTM-TestStepEdit-iconsBlock .ebIcon_copy";
    public static final String VERIFY_STEP_DELETE_ACTUAL_RESULT_ICON = ".eaTM-ActionIcon.ebIcon_delete";
    public static final String VERIFY_STEP_ADD_ACTUAL_RESULT_LINK = ".eaTM-Verify-addLink";
    public static final String VERIFY_STEP_ACTUAL_RESULT_TEXT_BOX = ".eaTM-Verify-actualResultBox";
    public static final String EA_TM_VERIFY_STEPS = ".eaTM-Verify";

    @UiComponentMapping("#TMS_CreateTestCase_ContentRegion")
    private UiComponent testCaseFormRegion;

    @UiComponentMapping("#TMS_CreateTestCase_DialogBox-productSelect")
    private TextBox productSelectFromDialog;

    @UiComponentMapping("#TMS_CreateTestCase_DialogBox-projectSelect .ebSelect-value")
    private UiComponent projectsSelectValueFromDialog;

    @UiComponentMapping("#TMS_CreateTestCase_TestCaseForm-projectSelect")
    private TextBox projectSelectFromForm;

    @UiComponentMapping("#TMS_CreateTestCase_TestCaseForm-projectSelect .ebSelect-value")
    private UiComponent projectsSelectValueFromForm;

    @UiComponentMapping("#TMS_CreateTestCase_ContentRegion .ebMultiSelect-textarea")
    private TextBox requirementIdTextarea;

    @UiComponentMapping("#TMS_CreateTestCase_TestCaseForm-testCaseId")
    private TextBox testCaseId;

    @UiComponentMapping("#TMS_CreateTestCase_TestCaseForm-title")
    private TextBox title;

    @UiComponentMapping("#TMS_CreateTestCase_TestCaseForm-description")
    private TextBox description;

    @UiComponentMapping("#TMS_CreateTestCase_TestCaseForm-preCondition")
    private TextBox preCondition;

    @UiComponentMapping("#TMS_CreateTestCase_TestCaseForm-componentSelect")
    private TextBox componentSelect;

    @UiComponentMapping("#TMS_CreateTestCase_TestCaseForm-productSelect")
    private TextBox productSelect;

    @UiComponentMapping("#TMS_CreateTestCase_TestCaseForm-featureSelect")
    private TextBox featureSelect;

    @UiComponentMapping("#TMS_CreateTestCase_TestCaseForm-typeSelect")
    private UiComponent typeSelect;

    @UiComponentMapping("#TMS_CreateTestCase_TestCaseForm-prioritySelect")
    private UiComponent prioritySelect;

    @UiComponentMapping("#TMS_CreateTestCase_TestCaseForm-executionTypeSelect")
    private UiComponent executionTypeSelect;

    @UiComponentMapping("#TMS_CreateTestCase_TestCaseForm-automationCandidateBlock")
    private UiComponent automationCandidateBlock;

    @UiComponentMapping(".eaTM-TestCaseForm-automationCandidate-switcher > .ebSwitcher")
    private UiComponent automationCandidateSwitcher;

    @UiComponentMapping(".eaTM-TestCaseForm-intrusive-switcher > .ebSwitcher")
    private UiComponent intrusiveSwitcher;

    @UiComponentMapping(".eaTM-TestCaseForm-intrusiveComment")
    private TextBox intrusiveComment;

    @UiComponentMapping("#TMS_CreateTestCase_TestCaseForm-testCaseStatusSelect")
    private UiComponent testCaseStatusSelect;

    @UiComponentMapping("#TMS_CreateTestCase_TestCaseForm-groupSelect")
    private UiComponent groupSelect;

    @UiComponentMapping(".ebComponentList .ebComponentList-item")
    private List<UiComponent> groupSelectItems;

    @UiComponentMapping("#TMS_CreateTestCase_TestCaseForm-contextSelect")
    private UiComponent contextSelect;

    // TestSteps block
    @UiComponentMapping("#TMS_TestStepsForm_addLink")
    private Link addTestStepLink;

    @UiComponentMapping("#TMS_TestStepsForm_block")
    private UiComponent testStepsHolder;

    @UiComponentMapping(".eaTM-TestStepEdit")
    private List<UiComponent> testSteps;

    @UiComponentMapping(".eaTM-TestStepEdit-iconsBlock .ebIcon_delete")
    private List<UiComponent> testStepDeleteIcons;

    public UiComponent getTestCaseFormRegion() {
        return testCaseFormRegion;
    }

    public TextBox getProductSelectFromDialog() {
        return productSelectFromDialog;
    }

    public UiComponent getProjectsSelectValueFromDialog() {
        return projectsSelectValueFromDialog;
    }

    public TextBox getProjectSelectFromForm() {
        return projectSelectFromForm;
    }

    public UiComponent getProjectsSelectValueFromForm() {
        return projectsSelectValueFromForm;
    }

    public TextBox getRequirementIdTextarea() {
        return requirementIdTextarea;
    }

    public TextBox getTestCaseId() {
        return testCaseId;
    }

    public TextBox getTitle() {
        return title;
    }

    public TextBox getDescription() {
        return description;
    }

    public TextBox getPreCondition() {
        return preCondition;
    }

    public TextBox getComponentSelect() {
        return componentSelect;
    }

    public UiComponent getTypeSelect() {
        return typeSelect;
    }

    public UiComponent getPrioritySelect() {
        return prioritySelect;
    }

    public UiComponent getExecutionTypeSelect() {
        return executionTypeSelect;
    }

    public UiComponent getAutomationCandidateBlock() {
        return automationCandidateBlock;
    }

    public UiComponent getAutomationCandidateSwitcher() {
        return automationCandidateSwitcher;
    }

    public UiComponent getIntrusiveSwitcher() {
        return intrusiveSwitcher;
    }

    public TextBox getIntrusiveComment() {
        return intrusiveComment;
    }

    public UiComponent getTestCaseStatusSelect() {
        return testCaseStatusSelect;
    }

    public UiComponent getGroupSelect() {
        return groupSelect;
    }

    public UiComponent getContextSelect() {
        return contextSelect;
    }

    public Link getAddTestStepLink() {
        return addTestStepLink;
    }

    public UiComponent getTestStepsHolder() {
        return testStepsHolder;
    }

    public List<UiComponent> getGroupSelectItems() {
        return groupSelectItems;
    }

    public void setGroupSelectItems(List<UiComponent> groupSelectItems) {
        this.groupSelectItems = groupSelectItems;
    }

    public List<UiComponent> getTestSteps() {
        return testSteps;
    }

    public List<UiComponent> getTestStepDeleteIcons() {
        return testStepDeleteIcons;
    }

    public TextBox getFeatureSelect() {
        return featureSelect;
    }

    public TextBox getProductSelect() {
        return productSelect;
    }
}
