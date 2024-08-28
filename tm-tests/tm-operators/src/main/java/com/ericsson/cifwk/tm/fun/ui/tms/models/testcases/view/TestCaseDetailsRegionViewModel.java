package com.ericsson.cifwk.tm.fun.ui.tms.models.testcases.view;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;

import java.util.Arrays;
import java.util.List;

public class TestCaseDetailsRegionViewModel extends GenericViewModel {
    public static final String TEST_STEP_BLOCK = ".eaTM-TestStep";
    public static final String TEST_STEP_NAME = ".eaTM-TestStep-title";
    public static final String TEST_STEP_NAME_COMPARE = ".eaTM-TestStep-titleCompare";
    public static final String TEST_STEP_DATA_COMPARE = ".eaTM-TestStep-dataFieldCompare";
    public static final String TEST_STEP_DATA = ".eaTM-TestStep-dataField";
    public static final String VERIFY_STEPS = ".eaTM-TestStep-verifiesList";
    public static final String VERIFY_STEP_NAME = ".eaTM-Verify-title";
    public static final String VERIFY_STEP_NAME_COMPARE = ".eaTM-Verify-titleCompare";
    public static final String REQUIREMENTS_LINKS = ".eaTM-RequirementLinkWidget-requirementId";

    @UiComponentMapping("#TMS_TestCaseDetails_ContentRegion.eaTM-ViewTestCaseRegion_loaded")
    private UiComponent testCaseDetailsRegion;

    @UiComponentMapping(".eaTM-AssociatedComments")
    private UiComponent commentsHolder;

    @UiComponentMapping(".eaTM-AssociatedComment")
    private UiComponent associatedComment;

    @UiComponentMapping(".eaTM-AssociatedCommentsNew-editButtonsHolder .eaTM-ActionLink-link")
    private UiComponent commentAddLink;

    @UiComponentMapping(".eaTM-AssociatedCommentsNew-text")
    private UiComponent commentNewText;

    @UiComponentMapping(".eaTM-TestPlanLinks-link")
    private List<UiComponent> testPlanLinks;

    @UiComponentMapping(".eaFlyout-panelCloseIcon")
    private UiComponent flyoutCloseIcon;

    @UiComponentMapping(".eaTM-ViewTestCase-requirements")
    private UiComponent requirementsBlock;

    @UiComponentMapping("eaTM-RequirementLinkWidget-requirementId")
    private List<UiComponent> requirements;

    @UiComponentMapping(".eaTM-ViewTestCase-testCaseId")
    private UiComponent testCaseId;

    @UiComponentMapping(".eaTM-ViewTestCase-title")
    private UiComponent testCaseTitle;

    @UiComponentMapping(".eaTM-ViewTestCase-description")
    private UiComponent testCaseDescription;

    @UiComponentMapping(".eaTM-ViewTestCase-type")
    private UiComponent testCaseType;

    @UiComponentMapping(".eaTM-ViewTestCase-component")
    private UiComponent testCaseComponent;

    @UiComponentMapping(".eaTM-ViewTestCase-feature")
    private UiComponent testCaseFeature;

    @UiComponentMapping(".eaTM-ViewTestCase-priority")
    private UiComponent testCasePriority;

    @UiComponentMapping(".eaTM-ViewTestCase-group")
    private UiComponent testCaseGroup;

    @UiComponentMapping(".eaTM-ViewTestCase-context")
    private UiComponent testCaseContext;

    @UiComponentMapping(".eaTM-ViewTestCase-preCondition")
    private UiComponent testCasePrecondition;

    @UiComponentMapping(".eaTM-ViewTestCase-executionType")
    private UiComponent testCaseExecType;

    @UiComponentMapping(".eaTM-ViewTestCase-testCaseStatus")
    private UiComponent testCaseStatus;

    @UiComponentMapping(".eaTM-ViewTestSteps-block")
    private UiComponent testStepBlock;

    @UiComponentMapping(".eaTM-TestStep")
    private List<UiComponent> testSteps;

    @UiComponentMapping(".ebComponentList")
    private UiComponent multiselectList;

    @UiComponentMapping(".eaTM-ViewTestCaseRegion-difference .eaTM-ViewTestCaseRegion-compareCheckBox")
    private UiComponent compareVersionsCheckBox;

    @UiComponentMapping(".eaTM-ViewTestCase-titleCompare")
    private UiComponent testCaseTitleCompare;

    @UiComponentMapping(".eaTM-ViewTestCase-descriptionCompare")
    private UiComponent testCaseDescriptionCompare;

    @UiComponentMapping(".eaTM-ViewTestCase-typeCompare")
    private UiComponent testCaseTypeCompare;

    @UiComponentMapping(".eaTM-ViewTestCase-featureCompare")
    private UiComponent testCaseFeatureCompare;

    @UiComponentMapping(".eaTM-ViewTestCase-componentCompare")
    private UiComponent testCaseComponentCompare;

    @UiComponentMapping(".eaTM-ViewTestCase-priorityCompare")
    private UiComponent testCasePriorityCompare;

    @UiComponentMapping(".eaTM-ViewTestCase-groupCompare")
    private UiComponent testCaseGroupCompare;

    @UiComponentMapping(".eaTM-ViewTestCase-contextCompare")
    private UiComponent testCaseContextCompare;

    @UiComponentMapping(".eaTM-ViewTestCase-preConditionCompare")
    private UiComponent testCasePreconditionCompare;

    @UiComponentMapping(".eaTM-ViewTestCase-executionTypeCompare")
    private UiComponent testCaseExecTypeCompare;

    @UiComponentMapping(".eaTM-ViewTestCase-requirementsCompare")
    private UiComponent testCaseRequirementsCompare;

    @UiComponentMapping(".eaTM-ViewTestCase-testCaseStatusCompare")
    private UiComponent testCaseStatusCompare;


    public UiComponent getTestCaseDetailsRegion() {
        return testCaseDetailsRegion;
    }

    public UiComponent getCommentsHolder() {
        return commentsHolder;
    }

    public UiComponent getCommentAddLink() {
        return commentAddLink;
    }

    public UiComponent getCommentNewText() {
        return commentNewText;
    }

    public List<UiComponent> getTestPlanLinks() {
        return testPlanLinks;
    }

    public UiComponent getFlyoutCloseIcon() {
        return flyoutCloseIcon;
    }

    public UiComponent getTestCaseRequirementBlock() {
        return this.requirementsBlock;
    }

    public UiComponent getTestCaseId() {
        return testCaseId;
    }

    public UiComponent getTestCaseTitle() {
        return testCaseTitle;
    }

    public UiComponent getTestCaseDescription() {
        return testCaseDescription;
    }

    public UiComponent getTestCaseType() {
        return testCaseType;
    }

    public UiComponent getTestCaseComponent() {
        return testCaseComponent;
    }

    public UiComponent getTestCaseFeature() {
        return testCaseFeature;
    }

    public UiComponent getTestCasePriority() {
        return testCasePriority;
    }

    public List<String> getTestCaseGroups() {
        return Arrays.asList(testCaseGroup.getText().split(","));
    }

    public UiComponent getTestCaseGroup() {
        return testCaseGroup;
    }

    public UiComponent getTestCaseContext() {
        return testCaseContext;
    }

    public List<String> getTestCaseContexts() {
        return Arrays.asList(testCaseContext.getText().split(","));
    }

    public UiComponent getTestCasePrecondition() {
        return testCasePrecondition;
    }

    public UiComponent getTestCaseExecType() {
        return testCaseExecType;
    }

    public UiComponent getTestCaseStatus() {
        return testCaseStatus;
    }

    public UiComponent getTestStepBlock() {
        return testStepBlock;
    }

    public List<UiComponent> getTestSteps() {
        return testSteps;
    }

    public UiComponent getAssociatedComment() {
        return associatedComment;
    }

    public UiComponent getMultiselectList() {
        return multiselectList;
    }

    public UiComponent getCompareVersionsCheckBox() {
        return compareVersionsCheckBox;
    }

    public UiComponent getTestCaseTitleCompare() { return testCaseTitleCompare; }

    public UiComponent getTestCaseDescriptionCompare() {
        return testCaseDescriptionCompare;
    }

    public UiComponent getTestCaseTypeCompare() {
        return testCaseTypeCompare;
    }

    public UiComponent getTestCaseFeatureCompare() {
        return testCaseFeatureCompare;
    }

    public UiComponent getTestCaseComponentCompare() {
        return testCaseComponentCompare;
    }

    public UiComponent getTestCasePriorityCompare() {
        return testCasePriorityCompare;
    }

    public UiComponent getTestCaseGroupCompare() {
        return testCaseGroupCompare;
    }

    public UiComponent getTestCaseContextCompare() {
        return testCaseContextCompare;
    }

    public UiComponent getTestCasePreconditionCompare() {
        return testCasePreconditionCompare;
    }

    public UiComponent getTestCaseExecTypeCompare() {
        return testCaseExecTypeCompare;
    }

    public UiComponent getTestCaseStatusCompare() {
        return testCaseStatusCompare;
    }

    public UiComponent getTestCaseRequirementsCompare() {
        return testCaseRequirementsCompare;
    }

}
