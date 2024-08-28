package com.ericsson.cifwk.tm.fun.ui.tms.operator;

import com.beust.jcommander.internal.Sets;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.annotations.TestStep;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.ui.UI;
import com.ericsson.cifwk.taf.ui.core.GenericPredicate;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentNotFoundException;
import com.ericsson.cifwk.taf.ui.core.UiComponentPredicates;
import com.ericsson.cifwk.taf.ui.core.UiProperties;
import com.ericsson.cifwk.taf.ui.core.WaitTimedOutException;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.CheckBox;
import com.ericsson.cifwk.taf.ui.sdk.Link;
import com.ericsson.cifwk.taf.ui.sdk.Select;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;
import com.ericsson.cifwk.tm.fun.ui.common.TableHelper;
import com.ericsson.cifwk.tm.fun.ui.common.operator.UiCommonOperator;
import com.ericsson.cifwk.tm.fun.ui.testcases.jsonobjects.references.ReferenceGroup;
import com.ericsson.cifwk.tm.fun.ui.testcases.jsonobjects.references.TestCaseExecutions;
import com.ericsson.cifwk.tm.fun.ui.testcases.models.common.FlyoutViewModel;
import com.ericsson.cifwk.tm.fun.ui.testcases.models.testcases.search.TestCaseSearchWidgetViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.Paginated;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.references.NotificationType;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.references.ReferenceType;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.testexecution.references.TestExecutionResultType;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.testexecution.references.TestStepResultType;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.testplan.TestCampaignExecutionsInfo;
import com.ericsson.cifwk.tm.fun.ui.tms.models.common.ComponentModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.common.CopyTestCampaignsDialogModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.common.DialogViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.common.FlyoutPanelViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.common.ProductSelectorViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.common.TopBarViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.requirements.tree.DetailsBlockModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.requirements.tree.RequirementsRegionViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.requirements.tree.TreeViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.testcampaigns.copy.QuickActionBarCopyTPViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.testcampaigns.create.QuickActionBarCreateTPViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.testcampaigns.edit.QuickActionBarEditTPViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.testcampaigns.edit.TestCampaignEditRegionViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.testcampaigns.group.TestCampaignGroupRegionViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.testcampaigns.list.TestCampaignsRegionViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.testcampaigns.view.NotificationRegionViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.testcampaigns.view.QuickActionBarViewTPViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.testcampaigns.view.TestCampaignDetailsRegionViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.testcases.edit.QuickActionBarCopyTCViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.testcases.edit.QuickActionBarCreateTCViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.testcases.edit.QuickActionBarEditTCViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.testcases.edit.TestCaseFormRegionViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.testcases.list.AdvancedSearchBlockViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.testcases.list.TableResultsViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.testcases.list.TestCaseSearchViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.testcases.view.QuickActionBarViewTCViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.testcases.view.TestCaseDetailsRegionViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.testexecutions.view.CreateDefectViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.testexecutions.view.QuickActionBarCreateTEViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.testexecutions.view.TestCampaignExecutionRegionViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.testexecutions.view.TestExecutionRegionViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.userinbox.AssignmentsRegionViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.userinbox.UserInboxViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.JsonHelper;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.ObjectMapper;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.TreeVisitor;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.comments.CommentComponentHelper;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.search.Condition;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.search.Criterion;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.search.Field;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.table.FilterInfo;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.Result;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.CopyTestStepResult;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.CreateEditTestCaseResult;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.DeleteTestStepsResult;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.ExportResult;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.SearchResult;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.TestCasesResult;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testexecutions.TestCampaignExecutionResult;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testexecutions.TestExecutionResult;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testplans.AutoCompleteResult;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testplans.CreateEditTestCampaignResult;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testplans.NavigationResult;
import com.ericsson.cifwk.tm.presentation.dto.DefectInfo;
import com.ericsson.cifwk.tm.presentation.dto.DropInfo;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.NotificationInfo;
import com.ericsson.cifwk.tm.presentation.dto.PostInfo;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.ProjectInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.TechnicalComponentInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignItemInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestExecutionInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestStepExecutionInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestStepInfo;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import com.ericsson.cifwk.tm.presentation.dto.VerifyStepExecutionInfo;
import com.ericsson.cifwk.tm.presentation.dto.VerifyStepInfo;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.openqa.selenium.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.collections.Maps;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.ericsson.cifwk.tm.fun.ui.common.TimingHelper.sleep;

@Operator
public class UiTestManagementOperator extends UiCommonOperator {

    private static final Logger logger = LoggerFactory.getLogger(UiTestManagementOperator.class);

    public static final String PRODUCT_ID_PARAM = "productName";

    public static final String TABLE_ROW = ".elTablelib-Table-body tr";

    private String appUrl;

    private static final ReferenceType automationCandidateYes = new ReferenceType("1", "Yes");

    private ObjectMapper mapper = new ObjectMapper();

    @TestStep(id = "start", description = "start TM operator on host {0}")
    public void start(Host host) {
        screenShotDirPath = createScreenshotDir();

        appUrl = getAppUrl(host);
        navigation.setup(appUrl);
        getBrowserTab().maximize();
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }


    @TestStep(id = "getTestCasesByFilter", description = "Gets Test Cases by Table filters")
    public TestCasesResult getTestCasesByFilter(List<FilterInfo> filters) {
        waitForTestCaseSearchView();

        TableResultsViewModel tableResultsViewModel = getBrowserTab().getView(TableResultsViewModel.class);
        waitForTableUpdate(tableResultsViewModel, TableResultsViewModel.TMS_TEST_CASES_TABLE_ID);

        if (!filters.isEmpty()) {
            clearTableFilters(tableResultsViewModel, TableResultsViewModel.TMS_TEST_CASES_LIST_RESULTS_ID);
            applyTableFilters(tableResultsViewModel, TableResultsViewModel.TMS_TEST_CASES_LIST_RESULTS_ID, filters);

            waitForTableUpdate(tableResultsViewModel, TableResultsViewModel.TMS_TEST_CASES_TABLE_ID);
        }

        Paginated<TestCaseInfo> testCases = getTestCases(tableResultsViewModel);
        return TestCasesResult.success(testCases.getItems());
    }

    @TestStep(id = "showAssociatedTestPlans")
    public NavigationResult showAssociatedTestPlans(TestCaseInfo testCaseInfo) {
        clickTestCaseIdInResultsTable(testCaseInfo);

        TestCaseDetailsRegionViewModel testCaseDetailsRegionViewModel = waitForTestCasePage();
        QuickActionBarViewTCViewModel quickActionBarViewTCViewModel = getBrowserTab().getView(QuickActionBarViewTCViewModel.class);
        getBrowserTab().waitUntilComponentIsDisplayed(quickActionBarViewTCViewModel.getBackLink(), WAIT_LONG_TIMEOUT);
        quickActionBarViewTCViewModel.getShowTestPlansLink().click();

        waitForFlyoutPanel();

        List<UiComponent> testPlanLinks = testCaseDetailsRegionViewModel.getTestPlanLinks();
        for (UiComponent testPlanLink : testPlanLinks) {
            testPlanLink.click();

            try {
                waitForTestCampaignDetailsPageOrException();
            } catch (WaitTimedOutException e) {
                createScreenshot("showAssociatedTestPlans", "Test Plan details page was not displayed for " + testPlanLink.getText());
                return NavigationResult.error("Test Plan details page was not displayed.");
            }

            getBrowserTab().back();
            waitForTestCasePage();
        }

        UiComponent flyoutCloseIcon = testCaseDetailsRegionViewModel.getFlyoutCloseIcon();
        waitForComponent(flyoutCloseIcon, WAIT_SHORT_TIMEOUT, "flyoutCloseIcon", "Flyout Close Icon was not found on screen!");
        flyoutCloseIcon.click();

        return NavigationResult.success();
    }

    @TestStep(id = "getAssociatedComments")
    public Result<List<PostInfo>> getAssociatedComments(TestCaseInfo testCaseInfo) {
        TestCaseDetailsRegionViewModel testCaseDetailsRegionViewModel = waitForTestCasePage();
        QuickActionBarViewTCViewModel quickActionBarViewTCViewModel = getBrowserTab().getView(QuickActionBarViewTCViewModel.class);

        CommentComponentHelper componentHelper = new CommentComponentHelper(getBrowserTab());
        componentHelper.setCommentsHolder(testCaseDetailsRegionViewModel.getCommentsHolder());

        Link showCommentsLinkHolder = quickActionBarViewTCViewModel.getShowCommentsLinkHolder();
        UiComponent commentsLink = showCommentsLinkHolder.getDescendantsBySelector(".eaTM-ActionLink-link").get(0);
        commentsLink.click();

        try {
            waitForFlyoutPanel();
        } catch (WaitTimedOutException e) {
            createScreenshot("getAssociatedComments", "Test case comments page was not displayed for " + testCaseInfo.getId());
            return Result.error("Test case comments page was not displayed.");
        }

        List<PostInfo> result = componentHelper.getDtosFromComponents();
        return Result.success(result);
    }

    @TestStep(id = "hideAssociatedComments")
    public NavigationResult hideAssociatedComments() {
        TestCaseDetailsRegionViewModel testCaseDetailsRegionViewModel = waitForTestCasePage();
        testCaseDetailsRegionViewModel.getFlyoutCloseIcon().click();
        return NavigationResult.success();
    }

    @TestStep(id = "createAssociatedComment")
    public Result<PostInfo> createAssociatedComment(TestCaseInfo testCaseInfo, PostInfo newComment) {
        TestCaseDetailsRegionViewModel testCaseDetailsRegionViewModel = waitForTestCasePage();

        CommentComponentHelper componentHelper = new CommentComponentHelper(getBrowserTab());
        componentHelper.setCommentsHolder(testCaseDetailsRegionViewModel.getCommentsHolder());

        int commentsCount = componentHelper.getCommentsCount();

        UiComponent commentNewText = testCaseDetailsRegionViewModel.getCommentNewText();
        commentNewText.sendKeys(newComment.getMessage());

        UiComponent commentAddLink = testCaseDetailsRegionViewModel.getCommentAddLink();
        commentAddLink.focus();
        if (!commentAddLink.hasFocus()) {
            commentAddLink.focus();
        }
        commentAddLink.click();

        try {
            componentHelper.waitForEachMessageShow(commentsCount + 1);
        } catch (Exception e) {
            createScreenshot("createAssociatedComment", "Newly created disappeared in UI.");
            return Result.error("Newly created disappeared in UI.");
        }

        int expectedCount = commentsCount + 1;
        int actualCount = componentHelper.getCommentsCount();
        if (expectedCount != actualCount) {
            createScreenshot("createAssociatedComment", "Newly created comment not found. Expected count to be " + expectedCount + " but was " + actualCount);
            return Result.error("Newly created comment not found. Expected count to be " + expectedCount + " but was " + actualCount);
        }

        UiComponent commentComponent = componentHelper.getLatestCommentComponent();
        try {
            componentHelper.waitForMessageShow(commentComponent);
        } catch (Exception e) {
            createScreenshot("createAssociatedComment", "Newly created not displayed.");
            return Result.error("Newly created not displayed.");
        }
        PostInfo createdComment = componentHelper.getDtoFromComponent(commentComponent);
        return Result.success(createdComment);
    }

    @TestStep(id = "deleteAssociatedComment")
    public Result<PostInfo> deleteAssociatedComment(TestCaseInfo testCaseInfo, PostInfo comment) {
        TestCaseDetailsRegionViewModel testCaseDetailsRegionViewModel = waitForTestCasePage();

        CommentComponentHelper componentHelper = new CommentComponentHelper(getBrowserTab());
        UiComponent associatedComments = testCaseDetailsRegionViewModel.getCommentsHolder();
        getBrowserTab().waitUntilComponentIsDisplayed(associatedComments, WAIT_TIMEOUT);
        componentHelper.setCommentsHolder(testCaseDetailsRegionViewModel.getCommentsHolder());

        int commentsCount = componentHelper.getCommentsCount();
        UiComponent commentComponentForDelete = componentHelper.findCommentComponentByMessage(comment.getMessage());
        if (commentComponentForDelete == null) {
            createScreenshot("deleteAssociatedComment", "Message '" + comment.getMessage() + "' for delete not found.");
            return Result.error("Message '" + comment.getMessage() + "' for delete not found. Delete failed.");
        }
        PostInfo postInfo = componentHelper.getDtoFromComponent(commentComponentForDelete);

        UiComponent deleteButton = componentHelper.getRemoveButton(commentComponentForDelete);
        getBrowserTab().waitUntilComponentIsDisplayed(deleteButton, WAIT_TIMEOUT);
        deleteButton.focus();
        deleteButton.click();

        try {
            componentHelper.waitForDeleteConfirmShow(commentComponentForDelete);
        } catch (Exception e) {
            createScreenshot("deleteAssociatedComment", "Remove buttons not shown.");
            return Result.error("Remove buttons not shown.");
        }

        UiComponent confirmRemoveButton = componentHelper.getRemoveButtonConfirm(commentComponentForDelete);
        if (confirmRemoveButton == null) {
            createScreenshot("deleteAssociatedComment", "Remove confirmation button not shown.");
            return Result.error("Remove confirmation button not shown.");
        }
        getBrowserTab().waitUntilComponentIsDisplayed(confirmRemoveButton, WAIT_TIMEOUT);
        confirmRemoveButton.focus();
        createScreenshot("confirmCommentDeletion", "Confirm delete button not shown");
        confirmRemoveButton.click();

        sleep(4); //required to make sure request has been processed
        int expectedCount = commentsCount - 1;
        int actualCount = componentHelper.getCommentsCount();
        if (expectedCount != actualCount) {
            createScreenshot("deleteAssociatedComment", "Deletion of comments failed. Comment count was " + actualCount + " but was expecting " + expectedCount);
            testCaseDetailsRegionViewModel.getFlyoutCloseIcon().click();
            return Result.error("Deletion of comments failed.Comment count was " + actualCount + " but was expecting " + expectedCount);
        }
        return Result.success(postInfo);
    }

    @TestStep(id = "checkAssociatedCommentDeleteIsRestricted")
    public Result<PostInfo> checkAssociatedCommentDeleteIsRestricted(TestCaseInfo testCaseInfo, PostInfo comment) {
        TestCaseDetailsRegionViewModel testCaseDetailsRegionViewModel =
                getBrowserTab().getView(TestCaseDetailsRegionViewModel.class);

        waitForComponent(testCaseDetailsRegionViewModel.getAssociatedComment());

        CommentComponentHelper componentHelper = new CommentComponentHelper(getBrowserTab());
        componentHelper.setCommentsHolder(testCaseDetailsRegionViewModel.getCommentsHolder());

        UiComponent commentComponentForDelete = componentHelper.findCommentComponentByMessage(comment.getMessage());
        if (commentComponentForDelete == null) {
            createScreenshot("checkAssociatedCommentDeleteIsRestricted", "Comment for deletion not found.");
            return Result.error("Message '" + comment.getMessage() + "' for deletion not found.");
        }
        PostInfo postInfo = componentHelper.getDtoFromComponent(commentComponentForDelete);

        if (componentHelper.visibleRemoveButton(commentComponentForDelete)) {
            createScreenshot("checkAssociatedCommentDeleteIsRestricted", "Delete comments not owned by user is not available.");
            testCaseDetailsRegionViewModel.getFlyoutCloseIcon().click();
            return Result.error("Delete comments not owned by user is not available.");
        }
        return Result.success(postInfo);
    }

    @TestStep(id = "checkCommentsCount")
    public Result<String> checkCommentsCount(TestCaseInfo testCaseInfo) {
        waitForTestCasePage();
        QuickActionBarViewTCViewModel quickActionBarViewTCViewModel =
                getBrowserTab().getView(QuickActionBarViewTCViewModel.class);
        UiComponent linkHolder = quickActionBarViewTCViewModel.getShowCommentsLinkHolder();
        UiComponent link = linkHolder.getDescendantsBySelector(".eaTM-ActionLink-link").get(0);
        String label = link.getText();
        return Result.success(label);
    }

    @TestStep(id = "getTestCasesByRequirement", description = "Get Test Cases by Requirement {0}")
    public TestCasesResult getTestCasesByRequirement(String requirementId) {
        TestCaseSearchViewModel mainPage = waitForTestCaseSearchView();
        RequirementsRegionViewModel requirementsRegion = navigateToRequirementsTree(mainPage);

        requirementsRegion.getRequirementsFilterInput().setText(requirementId);
        requirementsRegion.getRequirementsFilterInput().sendKeys(Keys.RETURN);

        DetailsBlockModel detailsBlock = getBrowserTab().getView(DetailsBlockModel.class);
        getBrowserTab().waitUntilComponentIsDisplayed(detailsBlock.getDetailsBlock(), WAIT_TIMEOUT);

        TreeViewModel treeView;
        try {
            treeView = waitForTreeViewWidget();
            detailsBlock = selectRequirementFromTree(requirementId, treeView);
        } catch (WaitTimedOutException e) {
            createScreenshot("timeoutWaitingForRequirement", "");
            return TestCasesResult.error("Cannot Select Requirement from tree");
        }

        return getTestCaseResult(requirementId, detailsBlock);
    }

    private TestCasesResult getTestCaseResult(String requirementId, DetailsBlockModel detailsBlock) {
        if (detailsBlock.getRequirementTitle().getText().isEmpty()) {
            String detailsInfoMessage = detailsBlock.getDetailsInfoMessage().getText();
            createScreenshot("getTestCasesByRequirement_detailsBlock_Error", detailsInfoMessage);
            return TestCasesResult.success(requirementId, Lists.<TestCaseInfo>newArrayList());
        }
        sleep(2); //required to allow time for table to load with test cases-cannot do wait for table as it does not render if test cases is zero
        if (!detailsBlock.getTableBlock().isDisplayed()) {
            String detailsInfoMessage = detailsBlock.getDetailsInfoMessage().getText();
            createScreenshot("getTestCasesByRequirement_NoTestCases_Warning", detailsInfoMessage);
            return TestCasesResult.success(requirementId, Lists.<TestCaseInfo>newArrayList());
        }

        Paginated<TestCaseInfo> testCases = getTestCasesFromDetailsBlock(detailsBlock);
        return TestCasesResult.success(requirementId, testCases.getItems());
    }

    @TestStep(id = "navigateToTestCaseFromRequirementsAndBack", description = "Checks back navigation from Test Case View")
    public TestCasesResult navigateToTestCaseFromRequirementsAndBack(TestCaseInfo testInfo) {
        DetailsBlockModel detailsBlock = getBrowserTab().getView(DetailsBlockModel.class);
        getBrowserTab().waitUntilComponentIsDisplayed(detailsBlock.getTableBlock(), WAIT_TIMEOUT);

        for (UiComponent testCaseLine : detailsBlock.getTestCasesLines()) {
            UiComponent titleLink = testCaseLine.getDescendantsBySelector(".eaTM-DetailsLine-title").get(0);
            if (titleLink.getText().startsWith(testInfo.getTestCaseId())) {
                titleLink.click();
                break;
            }
        }

        waitForTestCasePage();
        QuickActionBarViewTCViewModel quickActionBarViewTCViewModel = getBrowserTab().getView(QuickActionBarViewTCViewModel.class);
        getBrowserTab().waitUntilComponentIsDisplayed(quickActionBarViewTCViewModel.getBackLink(), WAIT_TIMEOUT);
        quickActionBarViewTCViewModel.getBackLink().click();

        getBrowserTab().waitUntilComponentIsDisplayed(detailsBlock.getDetailsBlock(), WAIT_TIMEOUT);
        sleep(1);

        return getTestCaseResult(detailsBlock.getRequirementId().getText(), detailsBlock);
    }

    @TestStep(id = "createTestCase", description = "Creates Test Case with JSON InputStream and ProjectInfo")
    public CreateEditTestCaseResult createTestCase(InputStream testCaseJson, ProductInfo productInfo) {
        TestCaseSearchViewModel testCaseSearchViewModel = waitForTestCaseSearchView();
        TestCaseFormRegionViewModel testCaseFormRegionViewModel = navigateToCreateTestCasePage(testCaseSearchViewModel);
        selectProductInfoFromDialog(testCaseFormRegionViewModel, productInfo);
        return getCreateEditTestCaseResult(testCaseJson, testCaseFormRegionViewModel);
    }

    @TestStep(id = "sendForReview")
    public CreateEditTestCaseResult reviewTestCase(TestCaseInfo testCaseInfo, String reviewGroup) {

        TestCaseDetailsRegionViewModel testCaseDetailsRegionViewModel = waitForTestCasePage();

        QuickActionBarViewTCViewModel quickActionBarViewTCViewModel = getBrowserTab().getView(QuickActionBarViewTCViewModel.class);
        getBrowserTab().waitUntilComponentIsDisplayed(quickActionBarViewTCViewModel.getBackLink(), WAIT_TIMEOUT);
        quickActionBarViewTCViewModel.getSendForReviewLink().click();

        DialogViewModel dialogScreenView = getBrowserTab().getView(DialogViewModel.class);
        getBrowserTab().waitUntilComponentIsDisplayed(dialogScreenView.getDialog(), WAIT_TIMEOUT);

        TextBox selectReviewGroupBox = dialogScreenView.getDialog().getDescendantsBySelector(".ebInput").get(0).as(TextBox.class);
        selectReviewGroupBox.setText(reviewGroup);

        UiComponent multiselectList = testCaseDetailsRegionViewModel.getMultiselectList();
        getBrowserTab().waitUntilComponentIsDisplayed(multiselectList, WAIT_TIMEOUT);
        multiselectList.getDescendantsBySelector(".ebComponentList-item").get(0).click();
        getBrowserTab().waitUntilComponentIsHidden(multiselectList, WAIT_TIMEOUT);

        dialogScreenView.getSubmitBlueButton().click(); // blue button

        TestCaseInfo savedTestCase = getTestCaseDetails(testCaseDetailsRegionViewModel);
        return CreateEditTestCaseResult.success(savedTestCase);
    }

    @TestStep(id = "approveTestCase")
    public CreateEditTestCaseResult approveTestCase(TestCaseInfo testCaseInfo, String reviewType) {
        TestCaseDetailsRegionViewModel testCaseDetailsRegionViewModel = waitForTestCasePage();

        QuickActionBarViewTCViewModel quickActionBarViewTCViewModel = getBrowserTab().getView(QuickActionBarViewTCViewModel.class);
        getBrowserTab().waitUntilComponentIsDisplayed(quickActionBarViewTCViewModel.getBackLink(), WAIT_TIMEOUT);
        quickActionBarViewTCViewModel.getApproveLink().click();

        DialogViewModel dialogScreenView = getBrowserTab().getView(DialogViewModel.class);
        getBrowserTab().waitUntilComponentIsDisplayed(dialogScreenView.getDialog(), WAIT_TIMEOUT);

        if ("Major".equalsIgnoreCase(reviewType)) {
            dialogScreenView.getSubmitBlueButton().click();
        } else if ("Minor".equalsIgnoreCase(reviewType)) {
            dialogScreenView.getSubmitButton().click();
        } else if ("Rejected".equalsIgnoreCase(reviewType)) {
            dialogScreenView.getRemoveButton().click();
        } else {
            dialogScreenView.getCancelButton().click();
        }

        TestCaseInfo savedTestCase = getTestCaseDetails(testCaseDetailsRegionViewModel);
        return CreateEditTestCaseResult.success(savedTestCase);
    }

    @TestStep(id = "createTestCaseFromTestCaseView", description = "Creates Test Case with JSON InputStream and ProductInfo")
    public CreateEditTestCaseResult createTestCaseFromTestCaseView(InputStream testCaseJson, ProductInfo productInfo) {
        QuickActionBarViewTCViewModel quickActionBar = getBrowserTab().getView(QuickActionBarViewTCViewModel.class);
        TestCaseFormRegionViewModel testCaseFormRegionViewModel = navigateToCreateTestCasePage(quickActionBar);
        selectProductInfoFromDialog(testCaseFormRegionViewModel, productInfo);
        return getCreateEditTestCaseResult(testCaseJson, testCaseFormRegionViewModel);
    }

    private void selectProductInfoFromDialog(TestCaseFormRegionViewModel testCaseFormRegionViewModel, ProductInfo productInfo) {
        if (productInfo != null) {
            DialogViewModel dialogScreenView = getBrowserTab().getView(DialogViewModel.class);
            try {
                getBrowserTab().waitUntilComponentIsDisplayed(dialogScreenView.getDialog(), WAIT_TIMEOUT);

                setSelectBoxValue(testCaseFormRegionViewModel.getProductSelectFromDialog(), productInfo.getName());
                dialogScreenView.getSubmitButton().click();


            } catch (WaitTimedOutException e) {
                logger.debug("No product dialog was opened", e);
            }

            getBrowserTab().waitUntilComponentIsHidden(dialogScreenView.getDialog(), WAIT_TIMEOUT);
            getBrowserTab().waitUntilComponentIsDisplayed(testCaseFormRegionViewModel.getTestCaseFormRegion(), WAIT_TIMEOUT);
        }
    }

    private LinkedHashSet<String> getTestCaseRequirements(TestCaseDetailsRegionViewModel testCaseDetailsRegionViewModel) {
        List<UiComponent> requirements = testCaseDetailsRegionViewModel
                .getTestCaseRequirementBlock()
                .getDescendantsBySelector(TestCaseDetailsRegionViewModel.REQUIREMENTS_LINKS);
        LinkedHashSet<String> requirementIds = new LinkedHashSet();
        for (UiComponent requirement : requirements) {
            requirementIds.add(requirement.getText());
        }
        return requirementIds;
    }

    private LinkedHashSet<String> getTestCaseRequirementsCompare(TestCaseDetailsRegionViewModel testCaseDetailsRegionViewModel) {
        Iterable<String> requirements = Splitter.on(",").split(testCaseDetailsRegionViewModel.getTestCaseRequirementsCompare().getText());

        LinkedHashSet<String> requirementIds = new LinkedHashSet();
        for (String requirement : requirements) {
            requirementIds.add(requirement.trim());
        }
        return requirementIds;
    }

    private TestCaseInfo getTestCaseDetails(TestCaseDetailsRegionViewModel testCaseDetailsRegionViewModel) {
        TestCaseInfo testCase = new TestCaseInfo();
        testCase.setTestCaseId(testCaseDetailsRegionViewModel.getTestCaseId().getText());
        testCase.setTitle(testCaseDetailsRegionViewModel.getTestCaseTitle().getText());
        testCase.setRequirementIds(getTestCaseRequirements(testCaseDetailsRegionViewModel));
        testCase.setDescription(testCaseDetailsRegionViewModel.getTestCaseDescription().getText());

        FeatureInfo feature = new FeatureInfo(null, testCaseDetailsRegionViewModel.getTestCaseFeature().getText());
        testCase.setFeature(feature);

        ReferenceDataItem type = new ReferenceDataItem("typeId", testCaseDetailsRegionViewModel.getTestCaseType().getText());
        testCase.setType(type);

        String text = testCaseDetailsRegionViewModel.getTestCaseComponent().getText();
        testCase.addTechnicalComponent(new ReferenceDataItem("", text));

        ReferenceDataItem priority = new ReferenceDataItem("priorityId", testCaseDetailsRegionViewModel.getTestCasePriority().getText());
        testCase.setPriority(priority);

        Set<ReferenceDataItem> groups = new HashSet<>();
        for (int i = 0; i < testCaseDetailsRegionViewModel.getTestCaseGroups().size(); i++) {
            String id = "group" + i;
            ReferenceDataItem group = new ReferenceDataItem(id, testCaseDetailsRegionViewModel.getTestCaseGroups().get(i));
            groups.add(group);
        }
        testCase.setGroups(groups);

        Set<ReferenceDataItem> contexts = new HashSet<>();
        for (int i = 0; i < testCaseDetailsRegionViewModel.getTestCaseContexts().size(); i++) {
            String id = "context" + i;
            ReferenceDataItem context = new ReferenceDataItem(id, testCaseDetailsRegionViewModel.getTestCaseContexts().get(i));
            contexts.add(context);
        }
        testCase.setContexts(contexts);

        testCase.setPrecondition(testCaseDetailsRegionViewModel.getTestCasePrecondition().getText());

        ReferenceDataItem executionType = new ReferenceDataItem("executionId", testCaseDetailsRegionViewModel.getTestCaseExecType().getText());
        testCase.setExecutionType(executionType);

        ReferenceDataItem status = new ReferenceDataItem("statusId", testCaseDetailsRegionViewModel.getTestCaseStatus().getText());
        testCase.setTestCaseStatus(status);
        getTestStepDetails(testCase, testCaseDetailsRegionViewModel);

        return testCase;
    }

    private void getTestStepDetails(TestCaseInfo testCase, TestCaseDetailsRegionViewModel testCaseDetailsRegionViewModel) {
        List<UiComponent> testSteps = testCaseDetailsRegionViewModel.getTestStepBlock().getDescendantsBySelector(TestCaseDetailsRegionViewModel.TEST_STEP_BLOCK);
        List<TestStepInfo> testStepInfoList = Lists.newArrayList();
        for (UiComponent testStep : testSteps) {
            TestStepInfo testStepInfo = new TestStepInfo();
            testStepInfo.setName(testStep.getDescendantsBySelector(TestCaseDetailsRegionViewModel.TEST_STEP_NAME).get(0).getText());
            testStepInfo.setData(testStep.getDescendantsBySelector(TestCaseDetailsRegionViewModel.TEST_STEP_DATA).get(0).getText());

            List<UiComponent> verifySteps = testStep.getDescendantsBySelector(TestCaseDetailsRegionViewModel.VERIFY_STEPS);
            VerifyStepInfo verifyStepInfo = new VerifyStepInfo();
            for (UiComponent verifyStep : verifySteps) {
                verifyStepInfo.setName(verifyStep.getDescendantsBySelector(TestCaseDetailsRegionViewModel.VERIFY_STEP_NAME).get(0).getText());
                testStepInfo.addVerify(verifyStepInfo);
            }
            testStepInfoList.add(testStepInfo);
        }
        testCase.setTestSteps(testStepInfoList);
    }

    private void getTestStepCompareDetails(TestCaseInfo testCase, TestCaseDetailsRegionViewModel testCaseDetailsRegionViewModel) {
        List<UiComponent> testSteps = testCaseDetailsRegionViewModel.getTestStepBlock().getDescendantsBySelector(TestCaseDetailsRegionViewModel.TEST_STEP_BLOCK);
        List<TestStepInfo> testStepInfoList = Lists.newArrayList();
        for (UiComponent testStep : testSteps) {
            TestStepInfo testStepInfo = new TestStepInfo();
            testStepInfo.setName(testStep.getDescendantsBySelector(TestCaseDetailsRegionViewModel.TEST_STEP_NAME_COMPARE).get(0).getText());
            testStepInfo.setData(testStep.getDescendantsBySelector(TestCaseDetailsRegionViewModel.TEST_STEP_DATA_COMPARE).get(0).getText());

            List<UiComponent> verifySteps = testStep.getDescendantsBySelector(TestCaseDetailsRegionViewModel.VERIFY_STEPS);
            VerifyStepInfo verifyStepInfo = new VerifyStepInfo();
            for (UiComponent verifyStep : verifySteps) {
                verifyStepInfo.setName(verifyStep.getDescendantsBySelector(TestCaseDetailsRegionViewModel.VERIFY_STEP_NAME_COMPARE).get(0).getText());
                testStepInfo.addVerify(verifyStepInfo);
            }
            testStepInfoList.add(testStepInfo);
        }
        testCase.setTestSteps(testStepInfoList);
    }

    private TestCaseInfo getTestCaseDetailsComparison(TestCaseDetailsRegionViewModel testCaseDetailsRegionViewModel) {
        TestCaseInfo testCase = new TestCaseInfo();
        testCase.setTestCaseId(testCaseDetailsRegionViewModel.getTestCaseId().getText());
        testCase.setTitle(testCaseDetailsRegionViewModel.getTestCaseTitleCompare().getText());
        testCase.setRequirementIds(getTestCaseRequirementsCompare(testCaseDetailsRegionViewModel));
        testCase.setDescription(testCaseDetailsRegionViewModel.getTestCaseDescriptionCompare().getText());

        FeatureInfo feature = new FeatureInfo(null, testCaseDetailsRegionViewModel.getTestCaseFeatureCompare().getText());
        testCase.setFeature(feature);

        ReferenceDataItem type = new ReferenceDataItem("typeId", testCaseDetailsRegionViewModel.getTestCaseTypeCompare().getText());
        testCase.setType(type);

        String text = testCaseDetailsRegionViewModel.getTestCaseComponentCompare().getText();
        testCase.addTechnicalComponent(new ReferenceDataItem("", text));

        ReferenceDataItem priority = new ReferenceDataItem("priorityId", testCaseDetailsRegionViewModel.getTestCasePriorityCompare().getText());
        testCase.setPriority(priority);

        Set<ReferenceDataItem> groups = new HashSet<>();
        Iterable<String> testCaseGroups = Splitter.on(",").split(testCaseDetailsRegionViewModel.getTestCaseGroupCompare().getText());

        int i = 0;
        for (String item : testCaseGroups) {
            String id = "group" + i;
            ReferenceDataItem group = new ReferenceDataItem(id, item);
            groups.add(group);
        }
        testCase.setGroups(groups);

        Set<ReferenceDataItem> contexts = new HashSet<>();
        Iterable<String> testCaseContexts = Splitter.on(",").split(testCaseDetailsRegionViewModel.getTestCaseContextCompare().getText());

        i = 0;
        for (String item : testCaseContexts) {
            String id = "context" + i;
            ReferenceDataItem context = new ReferenceDataItem(id, item);
            contexts.add(context);
        }
        testCase.setContexts(contexts);

        testCase.setPrecondition(testCaseDetailsRegionViewModel.getTestCasePreconditionCompare().getText());

        ReferenceDataItem executionType = new ReferenceDataItem("executionId", testCaseDetailsRegionViewModel.getTestCaseExecTypeCompare().getText());
        testCase.setExecutionType(executionType);

        ReferenceDataItem status = new ReferenceDataItem("statusId", testCaseDetailsRegionViewModel.getTestCaseStatusCompare().getText());
        testCase.setTestCaseStatus(status);
        getTestStepCompareDetails(testCase, testCaseDetailsRegionViewModel);

        return testCase;
    }

    private CreateEditTestCaseResult getCreateEditTestCaseResult(
            InputStream testCaseJson,
            TestCaseFormRegionViewModel testCaseFormRegionViewModel) {

        TestCaseInfo testCaseInfo = setTestCaseId(testCaseJson, UUID.randomUUID().toString());

        populateTestCaseFormFields(testCaseFormRegionViewModel, testCaseInfo);
        populateTestCaseSelectBoxes(testCaseFormRegionViewModel, testCaseInfo);
        setAutomationCandidacy(testCaseFormRegionViewModel, testCaseInfo);
        setIntrusive(testCaseFormRegionViewModel, testCaseInfo);
        populateTestStepsAndVerifies(testCaseFormRegionViewModel, testCaseInfo);
        submitCreateTestCaseForm();

        try {
            TestCaseDetailsRegionViewModel testCaseDetailsRegionViewModel = waitForTestCasePage();
            TestCaseInfo savedTestCase = getTestCaseDetails(testCaseDetailsRegionViewModel);
            // TODO: get new Test Case Id
            savedTestCase.setId(1L);
            return CreateEditTestCaseResult.success(savedTestCase);
        } catch (Exception e) {
            createScreenshot("createTestCase_submitCreateTestCaseForm", "TestCase was not created, because of errors:", e);
            return CreateEditTestCaseResult.error(e.getMessage());
        }
    }

    @TestStep(id = "createTestCaseWithGivenId", description = "Create Test Case with ID {0} and ProductInfo")
    public CreateEditTestCaseResult createTestCaseWithGivenId(
            String testCaseId,
            InputStream testCaseJson,
            ProductInfo productInfo) {

        getBrowserTab().open(appUrl + "/#tm/createTC/" + testCaseId);
        TestCaseInfo testCaseInfo = JsonHelper.toTestCaseInfo(testCaseJson);
        testCaseInfo.setTestCaseId(null);

        TestCaseFormRegionViewModel testCaseFormViewModel = waitForTestCaseEditPage();
        selectProductInfoFromDialog(testCaseFormViewModel, productInfo);

        populateTestCaseFormFields(testCaseFormViewModel, testCaseInfo);
        populateTestCaseSelectBoxes(testCaseFormViewModel, testCaseInfo);
        populateTestStepsAndVerifies(testCaseFormViewModel, testCaseInfo);
        submitCreateTestCaseForm();
        testCaseInfo.setTestCaseId(testCaseId);

        try {
            TestCaseDetailsRegionViewModel testCaseDetailsRegionViewModel = waitForTestCasePage();
            TestCaseInfo savedTestCase = getTestCaseDetails(testCaseDetailsRegionViewModel);
            // TODO: get new Test Case Id
            testCaseInfo.setId(1L);
            return CreateEditTestCaseResult.success(savedTestCase);
        } catch (Exception e) {
            createScreenshot("createTestCaseWithGivenId_submitCreateTestCaseForm", "TestCase was not created, because of errors:", e);
            return CreateEditTestCaseResult.error(e.getMessage());
        }
    }

    @TestStep(id = "editTestCase", description = "Makes edit for Test Case")
    public CreateEditTestCaseResult editTestCase(TestCaseInfo existingTestCaseInfo, InputStream testCaseJson) {
        TestCaseFormRegionViewModel testCaseFormViewModel = clickOnEditTestCaseLink();
        TestCaseInfo testCaseInfo = setTestCaseId(testCaseJson, existingTestCaseInfo.getTestCaseId());

        populateTestCaseFormFields(testCaseFormViewModel, testCaseInfo);
        populateTestCaseSelectBoxes(testCaseFormViewModel, testCaseInfo);
        populateTestStepsAndVerifies(testCaseFormViewModel, testCaseInfo);
        submitEditTestCaseForm();

        try {
            getBrowserTab().waitUntilComponentIsHidden(testCaseFormViewModel.getTestCaseFormRegion(), WAIT_TIMEOUT);
        } catch (Exception e) {
            createScreenshot("editTestCase_submitEditTestCaseForm", "TestCase was not saved, because of errors:", e);
            return CreateEditTestCaseResult.error(e.getMessage());
        }

        return CreateEditTestCaseResult.success(testCaseInfo);
    }

    public CreateEditTestCaseResult compareTestCase() {
        TestCaseDetailsRegionViewModel testCaseDetailsRegionViewModel = waitForTestCasePage();
        testCaseDetailsRegionViewModel.getCompareVersionsCheckBox().click();

        TestCaseInfo testCaseInfo = getTestCaseDetailsComparison(testCaseDetailsRegionViewModel);

        return CreateEditTestCaseResult.success(testCaseInfo);
    }

    @TestStep(id = "copyTestCase", description = "Select and copy a Test Case")
    public CreateEditTestCaseResult copyTestCase(TestCaseInfo testCaseInfo, InputStream testCaseJson) {
        clickTestCaseIdInResultsTable(testCaseInfo);
        waitForTestCasePage();
        // get test case details for assertions
        TestCaseDetailsRegionViewModel testCaseDetailsRegionViewModel = getBrowserTab().getView(TestCaseDetailsRegionViewModel.class);
        TestCaseInfo testCaseToCopy = getTestCaseDetails(testCaseDetailsRegionViewModel);

        QuickActionBarViewTCViewModel quickActionBarViewTCViewModel = getBrowserTab().getView(QuickActionBarViewTCViewModel.class);
        quickActionBarViewTCViewModel.getCopyLink().click();
        TestCaseFormRegionViewModel testCaseFormRegionViewModel = waitForTestCaseEditPage();
        testCaseFormRegionViewModel.getTestCaseId().setText("[ Random UUID for testing ] Copy of - " + UUID.randomUUID().toString());

        for (String requirement : testCaseToCopy.getRequirementIds()) {
            testCaseFormRegionViewModel.getRequirementIdTextarea().setText(requirement);
        }
        QuickActionBarCopyTCViewModel quickActionBarCopyTCViewModel = getBrowserTab().getView(QuickActionBarCopyTCViewModel.class);
        quickActionBarCopyTCViewModel.getCreateActionLink().click();
        try {
            waitForTestCasePage();
            testCaseDetailsRegionViewModel = getBrowserTab().getView(TestCaseDetailsRegionViewModel.class);
            TestCaseInfo copiedTestCase = getTestCaseDetails(testCaseDetailsRegionViewModel);
            return CreateEditTestCaseResult.success(copiedTestCase, testCaseToCopy);
        } catch (Exception e) {
            createScreenshot("copyTestCase_showCopiedTestCase", "Could not show Test Case details after copy action");
            return CreateEditTestCaseResult.error(e.getMessage());
        }
    }

    @TestStep(id = "copyTestSteps", description = "Copy Test Steps")
    public CopyTestStepResult copyTestSteps(TestCaseInfo testCaseInfo, List<String> testStepIndices) {
        clickTestCaseIdInResultsTable(testCaseInfo);
        waitForTestCasePage();

        QuickActionBarViewTCViewModel viewBar = getBrowserTab().getView(QuickActionBarViewTCViewModel.class);
        waitForComponent(viewBar.getEditLink());
        viewBar.getEditLink().click();
        TestCaseFormRegionViewModel testCaseFormRegionViewModel = waitForTestCaseEditPage();

        int testStepsBeforeCopy = countTestStepEditWidgets(testCaseFormRegionViewModel);

        List<Integer> effectiveIndices = Lists.newArrayList();

        for (String index : testStepIndices) {
            int testStepIndex = Integer.parseInt(index);
            clickTestStepCopyIcon(testCaseFormRegionViewModel, testStepIndex);
            effectiveIndices.add(testStepIndex + 1);
        }

        QuickActionBarEditTCViewModel quickActionBarEditTCViewModel = getBrowserTab().getView(QuickActionBarEditTCViewModel.class);
        quickActionBarEditTCViewModel.getSaveActionLink().click();
        TestCaseDetailsRegionViewModel testCaseDetailsRegionViewModel = waitForTestCasePage();

        int testStepsAfterCopy = testCaseDetailsRegionViewModel.getTestSteps().size();

        List<String> testStepTitles = Lists.newArrayList();
        List<String> copyTitles = Lists.newArrayList();

        for (String index : testStepIndices) {
            int testStepIndex = Integer.parseInt(index);
            String testStepTitle = getTestStepTitle(testCaseDetailsRegionViewModel, testStepIndex);
            String copyTitle = getTestStepTitle(testCaseDetailsRegionViewModel, testStepIndex + 1);
            if (testStepTitle == null || copyTitle == null) {
                createScreenshot("copyTestSteps", "Could not copy Test Steps");
                return CopyTestStepResult.error("Could not read Test Step title");
            }
            testStepTitles.add(testStepTitle);
            copyTitles.add(copyTitle);
        }

        List<Integer> copyIndices = getTestStepCopyIndices(effectiveIndices);
        return CopyTestStepResult.success(testStepsBeforeCopy, testStepsAfterCopy, testStepTitles, copyTitles, copyIndices);

    }

    @TestStep(id = "deleteTestSteps", description = "Delete Test Steps")
    public DeleteTestStepsResult deleteTestSteps(List<Integer> testStepIndices) {
        waitForTestCasePage();

        QuickActionBarViewTCViewModel viewBar = getBrowserTab().getView(QuickActionBarViewTCViewModel.class);
        viewBar.getEditLink().click();

        TestCaseFormRegionViewModel testCaseFormRegionViewModel = waitForTestCaseEditPage();
        Integer testStepsBeforeDelete = testCaseFormRegionViewModel
                .getTestStepsHolder()
                .getDescendantsBySelector(TestCaseFormRegionViewModel.TEST_STEP_EDIT_WIDGET)
                .size();

        int maxIndex = Collections.max(testStepIndices);
        if (maxIndex > testStepsBeforeDelete) {
            createScreenshot("deleteTestSteps", "Could not delete Test Steps");
            return DeleteTestStepsResult.error("Trying to delete a nonexistent Test Step");
        }

        for (Integer index : testStepIndices) {
            clickTestStepDeleteIcon(testCaseFormRegionViewModel, index);
        }
        QuickActionBarEditTCViewModel quickActionBarEditTCViewModel = getBrowserTab().getView(QuickActionBarEditTCViewModel.class);
        quickActionBarEditTCViewModel.getSaveActionLink().click();

        TestCaseDetailsRegionViewModel testCaseDetailsRegionViewModel = waitForTestCasePage();
        Integer testStepsAfterDelete = testCaseDetailsRegionViewModel.getTestSteps().size();

        return DeleteTestStepsResult.success(testStepsBeforeDelete, testStepsAfterDelete);
    }

    private Integer countTestStepEditWidgets(TestCaseFormRegionViewModel testCaseFormRegionViewModel) {
        return testCaseFormRegionViewModel
                .getTestStepsHolder()
                .getDescendantsBySelector(TestCaseFormRegionViewModel.TEST_STEP_EDIT_WIDGET)
                .size();
    }

    private void clickTestStepCopyIcon(TestCaseFormRegionViewModel testCaseFormRegionViewModel, int testStepIndex) {
        UiComponent testStepHolder = testCaseFormRegionViewModel.getTestStepsHolder();
        List<UiComponent> copyIcons = testStepHolder.getDescendantsBySelector(TestCaseFormRegionViewModel.TEST_STEP_COPY_ICON);
        copyIcons.get(testStepIndex).click();
    }

    private void clickTestStepDeleteIcon(TestCaseFormRegionViewModel testCaseFormRegionViewModel, int testStepIndex) {
        List<UiComponent> deleteIcons = testCaseFormRegionViewModel.getTestStepDeleteIcons();
        deleteIcons.get(testStepIndex).click();
    }

    private String getTestStepTitle(TestCaseDetailsRegionViewModel testCaseDetailsRegionViewModel, int testStepIndex) {
        List<UiComponent> testSteps = testCaseDetailsRegionViewModel.getTestSteps();
        UiComponent titleTextArea = testSteps
                .get(testStepIndex)
                .getDescendantsBySelector(TestCaseDetailsRegionViewModel.TEST_STEP_NAME)
                .get(0);

        if (titleTextArea == null) {
            return null;
        }

        return titleTextArea.getText();
    }

    private List<Integer> getTestStepCopyIndices(List<Integer> testStepIndices) {
        ArrayList<Integer> copyIndices = Lists.newArrayList();
        for (Integer index : testStepIndices) {
            copyIndices.add(index + 1);
        }
        return copyIndices;
    }

    @TestStep(id = "viewTestCase", description = "Get Test Cases by the given ID {0}")
    public Result<TestCaseInfo> viewTestCase(String testCaseId) {
        getBrowserTab().open(appUrl + "/#tm/viewTC/" + testCaseId);

        waitForTestCasePage();

        TestCaseDetailsRegionViewModel tcDetailsView = getBrowserTab().getView(TestCaseDetailsRegionViewModel.class);

        return Result.success(mapper.mapNewTestCase(tcDetailsView));
    }

    @TestStep(id = "checkTestCaseButtons", description = "Check whether Edit and Hide buttons are visible or not")
    public Result<Boolean> checkTestCaseButtons() {
        Result<Boolean> result = Result.success(true);

        QuickActionBarViewTCViewModel tcViewModel = getBrowserTab().getView(QuickActionBarViewTCViewModel.class);

        if (!tcViewModel.getVersionSelect().isEnabled()) {
            return Result.error("Version select box is disabled");
        }

        List<String> versions = getSelectBoxValues(tcViewModel.getVersionSelect());
        if (versions.isEmpty()) {
            return Result.error("No any version values found in select box");
        }

        if (versions.size() < 2) {
            return Result.error("Required at least two versions");
        }
        Collections.sort(versions, String.CASE_INSENSITIVE_ORDER);

        String firstVersion = Iterables.getFirst(versions, "default none");
        String lastVersion = Iterables.getLast(versions);

        // ~ set the first version
        setSelectBoxValue(tcViewModel.getVersionSelect(), firstVersion);
        sleep(3);

        if (isEditAndRemoveBtnHidden(tcViewModel)) {
            // ~ the last version
            setSelectBoxValue(tcViewModel.getVersionSelect(), lastVersion);
            sleep(3);

            if (isEditAndRemoveBtnHidden(tcViewModel)) {
                result = Result.error("The Edit and Remove buttons are hidden");
            }
        } else {
            result = Result.error("The Edit and Remove buttons are visible");
        }
        return result;
    }

    @TestStep(id = "updateTestCaseDescription", description = "Update Test Case Description for test {0}")
    public CreateEditTestCaseResult updateTestCaseDescription(String testCaseId, String description) {
        getBrowserTab().open(appUrl + "/#tm/editTC/" + testCaseId);

        TestCaseFormRegionViewModel testCaseFormViewModel = waitForTestCaseEditPage();
        testCaseFormViewModel.getDescription().setText(description);
        submitEditTestCaseForm();

        try {
            TestCaseDetailsRegionViewModel testCaseDetailsRegionViewModel = waitForTestCasePage();
            TestCaseInfo savedTestCase = getTestCaseDetails(testCaseDetailsRegionViewModel);
            return CreateEditTestCaseResult.success(savedTestCase);
        } catch (Exception e) {
            createScreenshot("updateTestCaseDescription_submitEditTestCaseForm", "TestCase was not saved, because of errors:", e);
            return CreateEditTestCaseResult.error(e.getMessage());
        }
    }

    @TestStep(id = "selectUserProfileProduct", description = "Select User Profile Product")
    public Result<ProductInfo> selectUserProfileProduct(ProductInfo productInfo) {
        TestCaseSearchViewModel mainPage = waitForTestCaseSearchView();

        try {
            setSelectBoxValue(mainPage.getProductSelect(), productInfo.getName());
        } catch (Exception e) {
            createScreenshot("selectUserProfileProduct_setSelectBoxValue", "SelectBox for Product select probably redrawn: trying to locate again...");
            setSelectBoxValue(mainPage.getProductSelect(), productInfo.getName());
        }

        try {
            waitForTestCasesTableResults();
        } catch (WaitTimedOutException e) {
            createScreenshot("selectUserProfileProject_waitForTableResults", "TestCases were not found for selected project: " + productInfo.getExternalId());
        }

        Map<String, String> queryParams;
        try {
            queryParams = getQueryParams();
        } catch (Exception e) {
            createScreenshot("selectUserProfileProject_getQueryParams", "QueryParams were not found in the hash part of URL.", e);
            return Result.error(e.getMessage());
        }

        String productId = queryParams.containsKey(PRODUCT_ID_PARAM) ? queryParams.get(PRODUCT_ID_PARAM) : "";
        String productName = mainPage.getProductSelectValue().getText();
        ProductInfo actualProductInfo = new ProductInfo();
        actualProductInfo.setId(null);
        actualProductInfo.setExternalId(productId);
        actualProductInfo.setName(productName);
        return Result.success(actualProductInfo);
    }

    @TestStep(id = "getSelectedProduct", description = "Gets Selected Product")
    public Result<ProductInfo> getSelectedProject() {
        TestCaseSearchViewModel mainPage = waitForTestCaseSearchView();

        try {
            waitForTestCasesTableResults();
        } catch (WaitTimedOutException e) {
            createScreenshot("getSelectedProject_waitForTableResults", "TestCases were not found for selected product.");
        }

        String productName = mainPage.getProductSelectValue().getText();

        Map<String, String> queryParams;
        try {
            queryParams = getQueryParams();
        } catch (Exception e) {
            createScreenshot("getSelectedProject_getQueryParams", "QueryParams were not found in the hash part of URL.", e);
            return Result.error(e.getMessage());
        }
        String projectId = queryParams.containsKey(PRODUCT_ID_PARAM) ? queryParams.get(PRODUCT_ID_PARAM) : "";

        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(null);
        productInfo.setExternalId(projectId);
        productInfo.setName(productName);
        return Result.success(productInfo);
    }

    @TestStep(id = "executeQuickSearch", description = "Execute Quick Search {0}")
    public SearchResult executeQuickSearch(String searchValue) {
        TestCaseSearchViewModel mainPage = waitForTestCaseSearchView();

        TableResultsViewModel tableResultsViewModel = getBrowserTab().getView(TableResultsViewModel.class);
        waitForTableUpdate(tableResultsViewModel, TableResultsViewModel.TMS_TEST_CASES_TABLE_ID);

        mainPage.getSearchInput().setText(searchValue);
        mainPage.getSearchButton().click();

        waitForTableUpdate(tableResultsViewModel, TableResultsViewModel.TMS_TEST_CASES_TABLE_ID);

        Paginated<TestCaseInfo> testCases = getTestCases(tableResultsViewModel);

        List<Criterion> criterions = Lists.newArrayList();
        criterions.add(new Criterion(Field.ANY, Condition.CONTAINS, searchValue));
        try {
            checkUrlParams(criterions);
            return SearchResult.success(testCases);
        } catch (Exception e) {
            createScreenshot("executeQuickSearch_checkUrlParams", e.getMessage());
            return SearchResult.error(testCases, e.getMessage());
        }
    }

    @TestStep(id = "executeAdvancedSearch", description = "Executes Advanced Search with criterions")
    public SearchResult executeAdvancedSearch(List<Criterion> criterions, List<FilterInfo> filters) {
        TestCaseSearchViewModel mainPage = waitForTestCaseSearchView();

        TableResultsViewModel tableResultsViewModel = getBrowserTab().getView(TableResultsViewModel.class);
        waitForTableUpdate(tableResultsViewModel, TableResultsViewModel.TMS_TEST_CASES_TABLE_ID);

        clearTableFilters(tableResultsViewModel, TableResultsViewModel.TMS_TEST_CASES_LIST_RESULTS_ID);
        applyTableFilters(tableResultsViewModel, TableResultsViewModel.TMS_TEST_CASES_LIST_RESULTS_ID, filters);

        waitForTableUpdate(tableResultsViewModel, TableResultsViewModel.TMS_TEST_CASES_TABLE_ID);

        mainPage.getMoreButton().click();
        AdvancedSearchBlockViewModel advancedSearchBlockViewModel = waitForAdvancedSearchBlock(null);
        sleep(1); // CSS animation should be finished

        UiComponent searchPanel = advancedSearchBlockViewModel.getSearchPanel();

        clearAllCriterions(searchPanel);
        prepopulateDataForAdvancedSearchFromDescendants(searchPanel, criterions);

        searchPanel.getDescendantsBySelector(AdvancedSearchBlockViewModel.SEARCH_BUTTON).get(0).click();
        waitForTableUpdate(tableResultsViewModel, TableResultsViewModel.TMS_TEST_CASES_TABLE_ID);

        Paginated<TestCaseInfo> testCases = getTestCases(tableResultsViewModel);
        try {
            checkCriterionsWithUiFields(advancedSearchBlockViewModel, criterions);
        } catch (Exception e) {
            createScreenshot("executeQuickSearch_checkCriterionsWithUiFields", e.getMessage());
            return SearchResult.error(testCases, e.getMessage());
        }

        try {
            checkUrlParams(criterions);
        } catch (Exception e) {
            createScreenshot("executeQuickSearch_checkUrlParams", e.getMessage());
            return SearchResult.error(testCases, e.getMessage());
        }
        return SearchResult.success(testCases);
    }

    @TestStep(id = "executeExportAction", description = "Executes Export Action")
    public ExportResult executeExportAction(List<Criterion> criterions) {
        throw new UnsupportedOperationException();
    }

    @TestStep(id = "navigateToTestPlans", description = "Checks navigation to Test Plans page")
    public NavigationResult navigateToTestPlans() {
        TestCaseSearchViewModel mainPage = waitForTestCaseSearchView();
        try {
            navigateToTestCampaignsPage(mainPage);
            return NavigationResult.success();
        } catch (WaitTimedOutException e) {
            String errorMessage = "Test Plans Page was not opened on time.";
            createScreenshot("navigateToTestPlans", errorMessage);
            return NavigationResult.error(errorMessage);
        }
    }

    @TestStep(id = "navigateToTestCases", description = "Checks navigation to Test Cases page")
    public NavigationResult navigateToTestCases() {
        TestCampaignsRegionViewModel mainPage = waitForTestCampaignRegionView();
        try {
            navigateToTestCasesPage(mainPage);
            return NavigationResult.success();
        } catch (WaitTimedOutException e) {
            String errorMessage = "Test Cases Page was not opened on time.";
            createScreenshot("navigateToTestCases", errorMessage);
            return NavigationResult.error(errorMessage);
        }
    }

    @TestStep(id = "filterTestPlans", description = "Filters Test Plans")
    public Result<Paginated<TestCampaignInfo>> filterTestPlans(TestCampaignInfo filterSelection, List<FilterInfo> tableFilters) {
        TestCampaignsRegionViewModel testPlansModel = waitForTestCampaignsPage();
        waitForTableUpdate(testPlansModel, TestCampaignsRegionViewModel.TMS_TEST_PLANS_TABLE_ID);
        filterTestCampaigns(filterSelection);

        clearTableFilters(testPlansModel, TestCampaignsRegionViewModel.TMS_TEST_PLANS_CONTENT_ID);
        applyTableFilters(testPlansModel, TestCampaignsRegionViewModel.TMS_TEST_PLANS_CONTENT_ID, tableFilters);

        Paginated<TestCampaignInfo> testPlans = getTestPlansFromTable(testPlansModel);
        return Result.success(testPlans);
    }

    @TestStep(id = "createTestPlan", description = "Creates Test Plan with default project")
    public CreateEditTestCampaignResult createTestPlan(TestCampaignInfo testCampaignInfo) {
        TestCampaignsRegionViewModel testPlansModel = waitForTestCampaignsPage();
        waitForTableUpdate(testPlansModel, TestCampaignsRegionViewModel.TMS_TEST_PLANS_TABLE_ID);
        filterTestCampaigns(testCampaignInfo);

        testPlansModel.getCreateLink().click();
        TestCampaignEditRegionViewModel testPlanCreateViewModel = waitForTestCampaignEditPage();

        populateTestCampaignFormFields(testPlanCreateViewModel, testCampaignInfo, false);
        populateTestCampaignTestCases(testPlanCreateViewModel, testCampaignInfo);

        selectProductDropFeatureAndComponents(
                TestCampaignEditRegionViewModel.TMS_TEST_CAMPAIGN_EDIT_FILTER_HOLDER_ID, testCampaignInfo);

        QuickActionBarCreateTPViewModel createTPBarViewModel = getBrowserTab().getView(QuickActionBarCreateTPViewModel.class);
        createTPBarViewModel.getCreateActionLink().click();

        TestCampaignInfo createdTestCampaignInfo;

        try {
            TestCampaignDetailsRegionViewModel newTestPlanRegionViewModel = waitForTestCampaignDetailsPageOrException();
            createdTestCampaignInfo = getSavedTestPlanDetails(newTestPlanRegionViewModel);
        } catch (WaitTimedOutException e) {
            String errorMessage = "Test Plan Details page was not shown after create action.";
            createScreenshot("createTestPlan", errorMessage);
            return CreateEditTestCampaignResult.error(errorMessage);
        }

        return CreateEditTestCampaignResult.success(createdTestCampaignInfo);
    }


    @TestStep(id = "createTestPlan", description = "Creates Test Plan and assigns user to Test Cases")
    public CreateEditTestCampaignResult createTestPlanAndAssignUser(TestCampaignInfo testCampaignInfo, String username) {
        TestCampaignsRegionViewModel testPlansModel = waitForTestCampaignsPage();
        waitForTableUpdate(testPlansModel, TestCampaignsRegionViewModel.TMS_TEST_PLANS_TABLE_ID);

        testPlansModel.getCreateLink().click();

        TestCampaignEditRegionViewModel testPlanCreateViewModel = waitForTestCampaignEditPage();

        selectProductDropFeatureAndComponents(
                TestCampaignEditRegionViewModel.TMS_TEST_CAMPAIGN_EDIT_FILTER_HOLDER_ID, testCampaignInfo);

        populateTestCampaignFormFields(testPlanCreateViewModel, testCampaignInfo, false);
        populateTestCampaignTestCases(testPlanCreateViewModel, testCampaignInfo);
        assignTestCasesToUser(testCampaignInfo.getTestCampaignItems(), username);

        QuickActionBarCreateTPViewModel createTPBarViewModel = getBrowserTab().getView(QuickActionBarCreateTPViewModel.class);
        createTPBarViewModel.getCreateActionLink().click();

        TestCampaignInfo createdTestCampaignInfo;

        try {
            TestCampaignDetailsRegionViewModel newTestPlanRegionViewModel = waitForTestCampaignDetailsPageOrException();
            createdTestCampaignInfo = getSavedTestPlanDetails(newTestPlanRegionViewModel);
        } catch (WaitTimedOutException e) {
            String errorMessage = "Test Plan Details page was not shown after create action.";
            createScreenshot("createTestPlan", errorMessage);
            return CreateEditTestCampaignResult.error(errorMessage);
        }

        return CreateEditTestCampaignResult.success(createdTestCampaignInfo);
    }

    @TestStep(id = "createBug", description = "Create bug from Test Execution screen")
    public TestExecutionResult createBug(TestCampaignInfo testCampaignInfo, Map<String, String> bugInfo, ProjectInfo projectInfo) {
        TestCampaignExecutionRegionViewModel testPlanExecutionViewModel = getBrowserTab().getView(TestCampaignExecutionRegionViewModel.class);

        for (TestCampaignItemInfo testPlanItems : testCampaignInfo.getTestCampaignItems()) {
            String testCaseId = testPlanItems.getTestCase().getTestCaseId();

            UiComponent testCaseRow = findTableRow(testPlanExecutionViewModel.getTestCasesTable(), testCaseId);
            if (testCaseRow == null) {
                createScreenshot("createBug", "Test case was not found by title!");
                return TestExecutionResult.error("Test case was not found by title!");
            }
            Button addResults = testCaseRow.getDescendantsBySelector(TestCampaignExecutionRegionViewModel.TMS_TE_TEST_CASE_BUTTON).get(0).as(Button.class);
            addResults.focus();
            addResults.click();

            TestExecutionRegionViewModel testExecutionViewModel = waitForTestExecutionPage();
            UiComponent projectSelect = testExecutionViewModel.getProjectSelect();
            getBrowserTab().waitUntilComponentIsDisplayed(projectSelect, WAIT_TIMEOUT);
            setSelectBoxValue(projectSelect, projectInfo.getName());
            UiComponent createBugButton = testExecutionViewModel.getCreateBugButton();
            getBrowserTab().evaluate(" var windowHeight=window.innerHeight;" + " window.scrollTo(0, windowHeight);");
            createBugButton.click();

            FlyoutViewModel flyoutViewModel = getBrowserTab().getView(FlyoutViewModel.class);
            getBrowserTab().waitUntilComponentIsDisplayed(flyoutViewModel.getFlyoutContents(), WAIT_TIMEOUT);

            populateCreateBugWidget(bugInfo);
            testExecutionViewModel.getCreateFlyoutBugButton().click();

            getBrowserTab().waitUntilComponentIsHidden(flyoutViewModel.getFlyout(), WAIT_TIMEOUT);

        }

        QuickActionBarCreateTEViewModel createTEViewModel = getBrowserTab().getView(QuickActionBarCreateTEViewModel.class);
        createTEViewModel.getSaveActionLink().click();

        return TestExecutionResult.success(null);
    }

    @TestStep(id = "createTestPlanWithButDeleteTestCase", description = "Create Test Plan but delete Test Cases that are added to Test Plan")
    public CreateEditTestCampaignResult createTestPlanButDeleteTestCases(TestCampaignInfo testCampaignInfo) {
        TestCampaignsRegionViewModel testPlansModel = waitForTestCampaignsPage();
        waitForTableUpdate(testPlansModel, TestCampaignsRegionViewModel.TMS_TEST_PLANS_TABLE_ID);

        testPlansModel.getCreateLink().click();

        TestCampaignEditRegionViewModel testCampaignEditRegionViewModel = waitForTestCampaignEditPage();

        selectProductDropFeatureAndComponents(
                TestCampaignEditRegionViewModel.TMS_TEST_CAMPAIGN_EDIT_FILTER_HOLDER_ID, testCampaignInfo);

        populateTestCampaignFormFields(testCampaignEditRegionViewModel, testCampaignInfo, false);
        populateTestCampaignTestCases(testCampaignEditRegionViewModel, testCampaignInfo);

        UiComponent testCasesTable = testCampaignEditRegionViewModel.getTestCaseTable();
        deleteSpecificTestCases(testCampaignInfo, testCasesTable);

        testCampaignInfo.setTestCampaignItems(getTestPlanAssignmentData(testCampaignEditRegionViewModel.getTestCaseTable(), false));

        QuickActionBarCreateTPViewModel createTPBarViewModel = getBrowserTab().getView(QuickActionBarCreateTPViewModel.class);
        createTPBarViewModel.getCreateActionLink().click();

        TestCampaignInfo createdTestCampaignInfo;

        try {
            TestCampaignDetailsRegionViewModel createdTestPlanRegionViewModel = waitForTestCampaignDetailsPageOrException();
            createdTestCampaignInfo = getSavedTestPlanDetails(createdTestPlanRegionViewModel);
        } catch (WaitTimedOutException e) {
            String errorMessage = "Test Plan Details page was not shown after create action.";
            createScreenshot("createTestPlan", errorMessage);
            return CreateEditTestCampaignResult.error(errorMessage);
        }

        return CreateEditTestCampaignResult.success(createdTestCampaignInfo);
    }

    @TestStep(id = "createTestPlanWithMultipleTestCases", description = "Creates Test Plan with default project adding multiple TestCases at once")
    public CreateEditTestCampaignResult createTestPlanWithMultipleTestCases(TestCampaignInfo testCampaignInfo, List<Criterion> criterions) {
        TestCampaignsRegionViewModel testPlansModel = waitForTestCampaignsPage();
        waitForTableUpdate(testPlansModel, TestCampaignsRegionViewModel.TMS_TEST_PLANS_TABLE_ID);

        testPlansModel.getCreateLink().click();

        TestCampaignEditRegionViewModel testPlanCreateViewModel = waitForTestCampaignEditPage();

        selectProductDropFeatureAndComponents(
                TestCampaignEditRegionViewModel.TMS_TEST_CAMPAIGN_EDIT_FILTER_HOLDER_ID, testCampaignInfo);

        populateTestCampaignFormFields(testPlanCreateViewModel, testCampaignInfo, false);

        testPlanCreateViewModel.getTestCaseMultipleAddButton().click();

        FlyoutViewModel flyoutViewModel = getBrowserTab().getView(FlyoutViewModel.class);
        getBrowserTab().waitUntilComponentIsDisplayed(flyoutViewModel.getFlyoutContents(), WAIT_TIMEOUT);

        populateMultipleTestCampaignTestCases(flyoutViewModel.getFlyoutContents(), criterions);

        flyoutViewModel.getFlyoutCloseIcon().click();
        getBrowserTab().waitUntilComponentIsHidden(flyoutViewModel.getFlyout(), WAIT_TIMEOUT);

        waitForTableUpdate(testPlanCreateViewModel, TestCampaignEditRegionViewModel.TMS_TP_TEST_CASE_TABLE_ID);

        testCampaignInfo.setTestCampaignItems(getTestPlanAssignmentData(testPlanCreateViewModel.getTestCaseTable(), false));

        QuickActionBarCreateTPViewModel createTPBarViewModel = getBrowserTab().getView(QuickActionBarCreateTPViewModel.class);
        createTPBarViewModel.getCreateActionLink().click();

        TestCampaignDetailsRegionViewModel newTestPlanRegionViewModel = waitForTestCampaignDetailsPageOrException();

        try {
            getBrowserTab().waitUntilComponentIsDisplayed(newTestPlanRegionViewModel.getTestCampaignDetailsRegion(), WAIT_TIMEOUT);
        } catch (WaitTimedOutException e) {
            String errorMessage = "Test Plan Details page was not shown after create action.";
            createScreenshot("createTestPlan", errorMessage);
            return CreateEditTestCampaignResult.error(errorMessage);
        }

        return CreateEditTestCampaignResult.success(testCampaignInfo);

    }

    @TestStep(id = "createTestPlan", description = "Creates Test Plan with defined project")
    public CreateEditTestCampaignResult createTestPlan(TestCampaignInfo testCampaignInfo, ProjectInfo projectInfo) {
        return createTestPlan(testCampaignInfo);
    }

    @TestStep(id = "viewTestPlan", description = "View Test Plan")
    public CreateEditTestCampaignResult viewTestPlan(TestCampaignInfo testCampaignInfo) {
        TestCampaignsRegionViewModel testPlansModel = waitForTestCampaignsPage();
        waitForTableUpdate(testPlansModel, TestCampaignsRegionViewModel.TMS_TEST_PLANS_TABLE_ID);
        filterTestCampaigns(testCampaignInfo);

        String testPlanName = testCampaignInfo.getName();
        int pageCount = getPageCount(testPlansModel);
        for (int pageNumber = 1; pageNumber <= pageCount; pageNumber++) {
            openPage(testPlansModel, pageNumber, pageCount);
            waitForTableUpdate(testPlansModel, TestCampaignsRegionViewModel.TMS_TEST_PLANS_TABLE_ID);

            UiComponent testPlanRow = findTableRow(testPlansModel.getTable(), testPlanName);
            if (testPlanRow != null) {
                List<UiComponent> testPlanCells = testPlanRow.getChildren();
                Link testPlanLink = testPlanCells.get(0).getDescendantsBySelector("a").get(0).as(Link.class);
                testPlanLink.click();

                return CreateEditTestCampaignResult.success(testCampaignInfo);
            }
        }

        createScreenshot("viewTestPlan", "Test plan was not found by name!");
        return CreateEditTestCampaignResult.error("Test plan was not found by name!");
    }

    @TestStep(id = "addTestCasesToTestCampaign", description = "Add Test cases to test campaigns")
    public CreateEditTestCampaignResult addTestCasesToTestCampaign(List<String> testCaseIds, TestCampaignInfo testCampaign,
                                                                   String search) {
        TestCaseSearchViewModel mainPage = waitForTestCaseSearchView();

        mainPage.getSearchInput().setText(search);
        mainPage.getSearchButton().click();

        TableResultsViewModel tableResultsViewModel = getBrowserTab().getView(TableResultsViewModel.class);
        waitForTableUpdate(tableResultsViewModel, TableResultsViewModel.TMS_TEST_CASES_TABLE_ID);

        for (String testCase : testCaseIds) {
            UiComponent row = findTableRow(tableResultsViewModel.getTable(), testCase);
            UiComponent checkbox = row.getDescendantsBySelector(".ebCheckbox").get(0);
            checkbox.focus();
            checkbox.click();
        }

        mainPage.getAddToTestCampaignLink().click();

        UiComponent testCampaignInputField = mainPage.getTestCampaignSelect();
        getBrowserTab().waitUntil(testCampaignInputField, (d) -> d.isDisplayed());
        getBrowserTab().waitUntil(testCampaignInputField, (d) -> d.isEnabled());

        testCampaignInputField.focus();
        testCampaignInputField.sendKeys(testCampaign.getName());
        if (testCampaignInputField.getText().isEmpty()) {
            testCampaignInputField.focus();
            testCampaignInputField.sendKeys(testCampaign.getName());
        }
        UiComponent testCampaignList = getBrowserTab().getView(ComponentModel.class).getComponentList();
        if (testCampaignList.isDisplayed()) {
            mainPage.getTestCampaignSelect().focus();
            mainPage.getTestCampaignSelect().click();
        }

        DialogViewModel dialogViewModel = getBrowserTab().getView(DialogViewModel.class);
        Button addButton = dialogViewModel.getDialog().getDescendantsBySelector(".ebBtn").get(0).as(Button.class);
        addButton.focus();
        addButton.click();

        navigateToTestPlans();

        TestCampaignsRegionViewModel testPlansModel = waitForTestCampaignsPage();
        filterTestCampaigns(testCampaign);
        waitForTableUpdate(testPlansModel, TestCampaignsRegionViewModel.TMS_TEST_PLANS_TABLE_ID);

        UiComponent testCampaignRow = findTableRow(testPlansModel.getTable(), testCampaign.getName());
        UiComponent hyperlink = testCampaignRow.getDescendantsBySelector("a").get(0);
        hyperlink.click();

        return viewTestPlan();
    }

    @TestStep(id = "GetPercentageOfTestCampaignGroup", description = "Get percentage of test campaign in a group")
    public String getPercentageOfTestCampaignGroup() {
        TestCampaignGroupRegionViewModel testCampaignGroupRegionViewModel = waitForTestCampaignGroupView();
        UiComponent totalTestCases = testCampaignGroupRegionViewModel.getTotalTestCases();

        return totalTestCases.getText();
    }

    public CreateEditTestCampaignResult viewTestPlan() {
        TestCampaignDetailsRegionViewModel testPlanRegionViewModel = waitForTestCampaignDetailsPageOrException();
        TestCampaignInfo testCampaignInfo = getSavedTestPlanDetails(testPlanRegionViewModel);

        return CreateEditTestCampaignResult.success(testCampaignInfo);
    }

    public void navigateTo(String url) {
        getBrowserTab().open(appUrl + url);
    }

    public void refreshPage() {
        getBrowserTab().refreshPage();
    }

    public void navigateToTestCaseEditAndView(TestCaseInfo testCaseInfo) {
        navigateTo("#tm/editTC/" + testCaseInfo.getTestCaseId());
        waitForTestCaseEditPage();
        navigateTo("#tm/viewTC/" + testCaseInfo.getTestCaseId());
    }

    @TestStep(id = "editTestPlan", description = "Edit Test Plan")
    public CreateEditTestCampaignResult editTestPlan(TestCampaignInfo testCampaignInfo) {
        waitForTestCampaignDetailsPage();
        QuickActionBarViewTPViewModel viewTPBarViewModel = getBrowserTab().getView(QuickActionBarViewTPViewModel.class);

        viewTPBarViewModel.getEditLink().click();
        TestCampaignEditRegionViewModel testPlanEditViewModel = waitForTestCampaignEditPage();

        populateTestCampaignFormFields(testPlanEditViewModel, testCampaignInfo, true);
        populateTestCampaignTestCases(testPlanEditViewModel, testCampaignInfo);

        QuickActionBarEditTPViewModel editTPBarViewModel = getBrowserTab().getView(QuickActionBarEditTPViewModel.class);
        editTPBarViewModel.getSaveActionLink().click();

        TestCampaignInfo createdTestCampaignInfo;

        try {
            TestCampaignDetailsRegionViewModel editedTestPlanRegionViewModel = waitForTestCampaignDetailsPageOrException();
            createdTestCampaignInfo = getSavedTestPlanDetails(editedTestPlanRegionViewModel);
        } catch (WaitTimedOutException e) {
            String errorMessage = "Test Plan Details page was not shown after save action.";
            createScreenshot("editTestPlan", errorMessage);
            return CreateEditTestCampaignResult.error(errorMessage);
        }

        return CreateEditTestCampaignResult.success(createdTestCampaignInfo);
    }

    @TestStep(id = "copyTestPlan", description = "Makes copy of Test Plan")
    public CreateEditTestCampaignResult copyTestPlan(TestCampaignInfo testCampaignInfo) {
        TestCampaignDetailsRegionViewModel testCampaignDetailsPage = waitForTestCampaignDetailsPage();
        QuickActionBarViewTPViewModel viewTPBarViewModel = getBrowserTab().getView(QuickActionBarViewTPViewModel.class);
        viewTPBarViewModel.getCopyLink().click();
        waitForTestCampaignEditPage();
        QuickActionBarCopyTPViewModel copyTPBarCreateModel = getBrowserTab().getView(QuickActionBarCopyTPViewModel.class);
        copyTPBarCreateModel.getCreateActionLink().click();

        try {
            waitForTestCampaignDetailsPageOrException();
        } catch (WaitTimedOutException e) {
            String errorMessage = "Test Plan Details page was not shown after copy action.";
            createScreenshot("copyTestPlan", errorMessage);
            return CreateEditTestCampaignResult.error(errorMessage);
        }
        TestCampaignInfo copiedTestCampaign = getSavedTestPlanDetails(testCampaignDetailsPage);
        return CreateEditTestCampaignResult.success(copiedTestCampaign);
    }

    @TestStep(id = "deleteTestPlan", description = "Deletes Test Plan")
    public CreateEditTestCampaignResult deleteTestPlan(TestCampaignInfo testCampaignInfo) {
        waitForTestCampaignDetailsPage();

        QuickActionBarViewTPViewModel viewTPBarViewModel = getBrowserTab().getView(QuickActionBarViewTPViewModel.class);
        viewTPBarViewModel.getRemoveLink().click();

        DialogViewModel dialogScreenView = getBrowserTab().getView(DialogViewModel.class);
        getBrowserTab().waitUntilComponentIsDisplayed(dialogScreenView.getDialog(), WAIT_TIMEOUT);
        dialogScreenView.getRemoveButton().click();

        TestCampaignsRegionViewModel testCampaignsRegionViewModel = getBrowserTab().getView(TestCampaignsRegionViewModel.class);
        try {
            getBrowserTab().waitUntilComponentIsDisplayed(testCampaignsRegionViewModel.getTestCampaignsRegion(), WAIT_TIMEOUT);
        } catch (WaitTimedOutException e) {
            String errorMessage = "Test Plans page was not shown after delete action.";
            createScreenshot("deleteTestPlan", errorMessage);
            return CreateEditTestCampaignResult.error(errorMessage);
        }

        return CreateEditTestCampaignResult.success(testCampaignInfo);
    }

    @TestStep(id = "navigateToTestPlanExecution", description = "Checks navigation to Test Plan Execution page")
    public NavigationResult navigateToTestPlanExecution(TestCampaignInfo testCampaignInfo) {
        waitForTestCampaignDetailsPage();
        sleep(1);
        QuickActionBarViewTPViewModel viewTPBarViewModel = getBrowserTab().getView(QuickActionBarViewTPViewModel.class);

        viewTPBarViewModel.getExecutionLink().click();
        try {
            waitForTeCampaignExecutionPage();
            sleep(1);
            return NavigationResult.success();
        } catch (WaitTimedOutException e) {
            String errorMessage = "Test Plans Page was not opened on time.";
            createScreenshot("navigateToTestPlanExecution", errorMessage);
            return NavigationResult.error(errorMessage);
        }
    }

    @TestStep(id = "viewTestPlanExecution", description = "View Test Plan Execution")
    public TestCampaignExecutionResult viewTestPlanExecution(TestCampaignInfo testCampaignInfo) {
        TestCampaignExecutionRegionViewModel testPlanExecutionViewModel = getBrowserTab().getView(TestCampaignExecutionRegionViewModel.class);
        TestCampaignExecutionsInfo testPlanExecutionInfo = gatherTestPlanData(testPlanExecutionViewModel);
        return TestCampaignExecutionResult.success(testPlanExecutionInfo);
    }

    @TestStep(id = "searchForTestExecutions", description = "Search for Test Executions")
    public Result<List<TestCampaignItemInfo>> searchForTestExecutions(TestCampaignInfo testCampaignInfo, List<Criterion> criteria) {
        List<TestCampaignItemInfo> assignments = Lists.newArrayList();
        TestCampaignExecutionRegionViewModel testPlanExecutionViewModel =
                getBrowserTab().getView(TestCampaignExecutionRegionViewModel.class);
        testPlanExecutionViewModel.getFilterButton().click();


        waitForAdvancedSearchBlock(TestCampaignExecutionRegionViewModel.TMS_TE_FILTER_CONTENT_CLASSNAME + " .eaTM-Search");
        sleep(1); // Wait for animation

        clearAllCriterions(testPlanExecutionViewModel.getFilterPanel());
        prepopulateDataForAdvancedSearchFromDescendants(testPlanExecutionViewModel.getFilterPanel(), criteria);

        try {
            testPlanExecutionViewModel.getFilterPanel().getDescendantsBySelector(AdvancedSearchBlockViewModel.SEARCH_BUTTON).get(0).click();
            waitForTableUpdate(testPlanExecutionViewModel, TestCampaignExecutionRegionViewModel.TMS_TE_TEST_CASES_TABLE_ID);
        } catch (Exception e) {
            String errorMessage = "Could not execute advanced search for Test Executions";
            createScreenshot("searchForTestExecutions", errorMessage);
            return Result.error(e.getMessage());
        }

        for (UiComponent tableRow : testPlanExecutionViewModel.getTestCasesRows()) {
            TestCampaignItemInfo assignment = gatherTestCaseData(tableRow);
            assignments.add(assignment);
        }
        testPlanExecutionViewModel.getHideSearchButton().click();

        return Result.success(assignments);
    }

    @TestStep(id = "createTestExecution", description = "Creates Test Execution")
    public TestExecutionResult createTestExecution(TestCampaignInfo testCampaignInfo, TestCampaignItemInfo testCaseInfo, TestExecutionInfo testExecutionInfo) {
        return createTestExecution(testCaseInfo, testExecutionInfo, true);
    }

    @TestStep(id = "createPassedTestExecution", description = "Creates passed Test Execution")
    public TestExecutionResult createPassedTestExecution(TestCampaignInfo testCampaignInfo, TestCampaignItemInfo testCaseInfo, TestExecutionInfo testExecutionInfo) {
        return createTestExecution(testCaseInfo, testExecutionInfo, false);
    }

    @TestStep(id = "updateTestExecutionInfo", description = "Updates Test Execution Info")
    public TestExecutionResult updateTestExecutionInfo(TestCampaignInfo testCampaignInfo, TestCampaignItemInfo assignmentInfo, TestExecutionInfo testExecutionInfo) {
        return updateTestExecutionInfo(assignmentInfo, testExecutionInfo);
    }

    @TestStep(id = "updateTestExecutionInfo", description = "Navigates to Test Execution page and gather TestExecutionInfo prepopulated data")
    public TestExecutionResult getPrepopulatedTestExecutionInfo(TestCampaignInfo testCampaignInfo, TestCampaignItemInfo assignmentInfo) {
        String testCaseId = assignmentInfo.getTestCase().getTestCaseId();

        getBrowserTab().open(navigation.getTestExecutionUrl(testCampaignInfo.getId().toString(), testCaseId));
        TestExecutionRegionViewModel testExecutionRegionViewModel = waitForTestExecutionPage();

        TestExecutionInfo prepopulatedTestExecutionInfo = gatherTestExecutionInfo(testExecutionRegionViewModel);
        return TestExecutionResult.success(prepopulatedTestExecutionInfo);
    }

    @TestStep(id = "getNotifications", description = "Checks that new Notifications are shown")
    public Result<List<NotificationInfo>> getNotifications() {
        NotificationRegionViewModel notificationRegionViewModel = getBrowserTab().getView(NotificationRegionViewModel.class);
        waitForComponent(notificationRegionViewModel.getHolder(), WAIT_TIMEOUT, "getNotifications", "Notification list holder did not appear");

        List<UiComponent> notificationWidgets = notificationRegionViewModel.getHolder().getChildren();
        List<NotificationInfo> notifications = Lists.newArrayList();
        for (UiComponent widget : notificationWidgets) {
            NotificationInfo info = new NotificationInfo();
            info.setType(getNotificationType(widget.getDescendantsBySelector(".ebNotification-icon .ebIcon").get(0)));
            info.setText(widget.getDescendantsBySelector(".ebNotification-label").get(0).getText());
            notifications.add(info);
        }
        return Result.success(notifications);
    }

    @TestStep(id = "getCurrentUserAssignments", description = "Get current logged in user assignments")
    public Result<Paginated<TestCampaignItemInfo>> getCurrentUserAssignments(List<FilterInfo> filters) {
        waitForTestCasesTableResults();

        TopBarViewModel topBarViewModel = getBrowserTab().getView(TopBarViewModel.class);
        navigateToUserInboxPage(topBarViewModel);

        AssignmentsRegionViewModel assignmentsModel = getBrowserTab().getView(AssignmentsRegionViewModel.class);
        waitForTableUpdate(assignmentsModel, AssignmentsRegionViewModel.TMS_ASSIGNMENTS_TABLE_ID);

        clearTableFilters(assignmentsModel, AssignmentsRegionViewModel.TMS_ASSIGNMENTS_HOLDER_ID);
        applyTableFilters(assignmentsModel, AssignmentsRegionViewModel.TMS_ASSIGNMENTS_HOLDER_ID, filters);

        waitForTableUpdate(assignmentsModel, AssignmentsRegionViewModel.TMS_ASSIGNMENTS_TABLE_ID);

        Paginated<TestCampaignItemInfo> assignments = getAssignmentsFromTable(assignmentsModel);
        return Result.success(assignments);
    }

    @TestStep(id = "getNewTestCaseGroups", description = "Get groups for current user selected project")
    public Result<List<ReferenceGroup>> getNewTestCaseGroups(ProductInfo productInfo) {
        TestCaseSearchViewModel testCaseSearchViewModel = waitForTestCaseSearchView();
        TestCaseFormRegionViewModel testCaseFormViewModel = navigateToCreateTestCasePage(testCaseSearchViewModel);
        selectProductInfoFromDialog(testCaseFormViewModel, productInfo);

        ReferenceGroup groupsReferenceGroup = new ReferenceGroup();
        groupsReferenceGroup.setId("group");

        testCaseFormViewModel.getGroupSelect().click();
        List<UiComponent> groupSelectItems = testCaseFormViewModel.getGroupSelectItems();
        for (UiComponent item : groupSelectItems) {
            groupsReferenceGroup.addItem(item.getProperty(UiProperties.VALUE), item.getText());
        }

        List<ReferenceGroup> referenceGroups = Lists.newArrayList();
        referenceGroups.add(groupsReferenceGroup);

        return Result.success(referenceGroups);
    }

    @TestStep(id = "getExpectedResult", description = "Get the expected Test Case data matching the copied Test Case")
    public TestCaseInfo getExpectedResult(CreateEditTestCaseResult copyResult) {
        return copyResult.getExpectedTestCaseInfo();
    }

    @TestStep(id = "selectTestExecutionResults", description = "Add Test Execution to multiple test cases")
    public List<TestCaseInfo> selectTestExecutionResults(List<TestCaseInfo> testCaseInfos, String result) {

        List<TestCaseInfo> testCaseInfoRows = Lists.newArrayList();
        TestCampaignExecutionRegionViewModel testCampaignExecutionRegionViewModel = getBrowserTab().getView(TestCampaignExecutionRegionViewModel.class);
        testCampaignExecutionRegionViewModel.getAddResults().click();

        FlyoutViewModel flyoutViewModel = getBrowserTab().getView(FlyoutViewModel.class);
        getBrowserTab().waitUntilComponentIsDisplayed(flyoutViewModel.getFlyoutContents(), WAIT_TIMEOUT);
        UiComponent resultSelect = testCampaignExecutionRegionViewModel.getResultSelect();
        getBrowserTab().waitUntilComponentIsDisplayed(resultSelect, WAIT_TIMEOUT);

        setSelectBoxValue(resultSelect, result);

        waitForTableUpdate(testCampaignExecutionRegionViewModel, TestCampaignExecutionRegionViewModel.TMS_TE_MULTISELECT_TABLE_ID);
        for (TestCaseInfo testCaseInfo : testCaseInfos) {
            UiComponent row = findTableRow(testCampaignExecutionRegionViewModel.getTestCasesMultiSelectTable(), testCaseInfo.getTestCaseId());
            if (row != null) {
                UiComponent checkbox = row.getDescendantsBySelector(".ebCheckbox").get(0);
                checkbox.click();
            }
        }
        testCampaignExecutionRegionViewModel.getCreateButton().click();
        waitForTableUpdate(testCampaignExecutionRegionViewModel, TestCampaignExecutionRegionViewModel.TMS_TE_MULTISELECT_TABLE_ID);
        for (TestCaseInfo testCaseInfo : testCaseInfos) {
            UiComponent row = findTableRow(testCampaignExecutionRegionViewModel.getTestCasesMultiSelectTable(), testCaseInfo.getTestCaseId());
            if (row != null) {
                List<UiComponent> rowInfo = row.getDescendantsBySelector("td");
                TestCaseInfo testInfo = new TestCaseInfo();
                String title = "title";
                testInfo.setTestCaseId(rowInfo.get(2).getProperty(title));
                testInfo.setTitle(rowInfo.get(3).getText());
                testInfo.setExecutionType(TestExecutionResultType.getEnum(rowInfo.get(4).getProperty(title)));
                testCaseInfoRows.add(testInfo);
            }
        }

        flyoutViewModel.getFlyoutCloseIcon().click();
        getBrowserTab().waitUntilComponentIsHidden(flyoutViewModel.getFlyout(), WAIT_TIMEOUT);

        return testCaseInfoRows;
    }

    public void filterTestCampaigns(TestCampaignInfo filterOptions) {
        selectProductDropFeatureAndComponents(
                TestCampaignsRegionViewModel.TMS_TEST_CAMPAIGNS_LIST_FILTER_HOLDER_ID, filterOptions);
    }

    public void setContext(TestCampaignInfo context) {
        selectProductDropFeatureAndComponents(
                TestCampaignEditRegionViewModel.TMS_TEST_CAMPAIGN_EDIT_FILTER_HOLDER_ID, context);
    }

    @TestStep(id = "copyTestCampaignsBetweenDrops", description = "Copy Test Campaigns between Drops")
    public Paginated<TestCampaignInfo> copyTestCampaignsBetweenDrops(TestCampaignInfo filterOptions, DropInfo copyToDrop) {
        TestCampaignsRegionViewModel testPlansModel = waitForTestCampaignsPage();
        filterTestCampaigns(filterOptions);
        waitForTableUpdate(testPlansModel, TestCampaignsRegionViewModel.TMS_TEST_PLANS_TABLE_ID);

        testPlansModel.getCopyLink().click();
        CopyTestCampaignsDialogModel dialog = getBrowserTab().getView(CopyTestCampaignsDialogModel.class);
        Select dropSelect = dialog.getDropSelect();
        setSelectBoxValue(dropSelect, copyToDrop.getName());
        dialog.getCopyButton().click();

        filterOptions.setDrop(copyToDrop);
        filterTestCampaigns(filterOptions);

        testPlansModel = waitForTestCampaignsPage();
        waitForTableUpdate(testPlansModel, TestCampaignsRegionViewModel.TMS_TEST_PLANS_TABLE_ID);

        Paginated<TestCampaignInfo> copies = getTestPlansFromTable(testPlansModel);

        // necessary for view and delete
        copies.getItems().forEach(c -> {
            c.setProduct(filterOptions.getProduct());
            c.setDrop(filterOptions.getDrop());
            c.setFeatures(filterOptions.getFeatures());
        });

        return copies;
    }

    @TestStep(id = "navigateToCreateTestCampaignPage", description = "Navigate to test campaign page")
    public void navigateToCreateTestCampaignPage() {
        TestCampaignsRegionViewModel testPlansModel = waitForTestCampaignsPage();
        testPlansModel.getCreateLink().click();
    }

    @TestStep(id = "setTestCaseAutoCompleteText", description = "Set autocomplete text on test campaign create / edit page")
    public AutoCompleteResult setTestCaseAutoCompleteText(String text) {
        TestCampaignEditRegionViewModel testPlanCreateViewModel = waitForTestCampaignEditPage();
        TextBox testCaseInput = testPlanCreateViewModel.getTestCaseInput();
        testCaseInput.click();
        testCaseInput.setText(text);
        UiComponent testCaseList = getBrowserTab().waitUntilComponentIsDisplayed(".ebComponentList", WAIT_LONG_TIMEOUT);
        List<String> listItems = testCaseList.getDescendantsBySelector(".ebComponentList-item").stream()
                .map(i -> i.getText())
                .collect(Collectors.toList());

        return AutoCompleteResult.success(listItems);
    }

    private void assignTestCasesToUser(Set<TestCampaignItemInfo> items, String userName) {
        TestCampaignEditRegionViewModel testCampaignEditViewModel = waitForTestCampaignEditPage();

        selectTestCases(items, testCampaignEditViewModel.getTableRows());
        UiComponent actionButton = testCampaignEditViewModel.getAssignUpdateButtons().get(0);
        actionButton.getDescendantsBySelector(TestCampaignEditRegionViewModel.TMS_ACTION_LINK).get(0).click();
        UiComponent dialogBox = testCampaignEditViewModel.getUserDialogBox();
        TextBox selectUserBox = dialogBox.getDescendantsBySelector(".ebInput").get(0).as(TextBox.class);
        selectUserBox.setText(userName);

        UiComponent multiselectList = testCampaignEditViewModel.getMultiselectList();
        getBrowserTab().waitUntilComponentIsDisplayed(multiselectList, WAIT_TIMEOUT);
        multiselectList.getDescendantsBySelector(".ebComponentList-item").get(0).click();
        getBrowserTab().waitUntilComponentIsHidden(multiselectList, WAIT_TIMEOUT);
        Button assignButton = dialogBox.getDescendantsBySelector(".ebBtn").get(0).as(Button.class);
        assignButton.click();
    }

    private void selectTestCases(Set<TestCampaignItemInfo> items, List<UiComponent> tableRows) {
        items.forEach(item -> {
            tableRows.forEach(row -> {
                String testCaseIdText = row.getChildren().get(1).getText();
                if (item.getTestCase().getTestCaseId().equals(testCaseIdText)) {
                    row.getChildren().get(0).click();
                }
            });
        });
    }

    private TestCampaignInfo getSavedTestPlanDetails(TestCampaignDetailsRegionViewModel viewModel) {
        TestCampaignInfo testCampaignInfo = new TestCampaignInfo();
        testCampaignInfo.setName(viewModel.getNameText());
        testCampaignInfo.setProduct(viewModel.getProduct());
        testCampaignInfo.setDrop(viewModel.getDrop());
        testCampaignInfo.setFeatures(viewModel.getFeatures());
        testCampaignInfo.setComponents(viewModel.getComponents());
        testCampaignInfo.setDescription(viewModel.getDescriptionText());
        testCampaignInfo.setEnvironment(viewModel.getEnvironmentText());

        Set<TestCampaignItemInfo> assignments = getTestPlanAssignmentData(viewModel.getTestCasesTable(), true);
        testCampaignInfo.setTestCampaignItems(assignments);

        return testCampaignInfo;
    }

    private ReferenceDataItem getNotificationType(UiComponent icon) {
        Iterable<String> classList = getClassList(icon);
        String iconClass = Iterables.find(classList, new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return input.startsWith("ebIcon_");
            }
        });
        String typeName = iconClass != null ? iconClass.substring(7) : "";
        return NotificationType.valueOf(typeName.toUpperCase()).getReferenceDataItem();

    }

    private TestExecutionResult createTestExecution(TestCampaignItemInfo testCaseInfo, TestExecutionInfo testExecutionInfo, boolean defectsAllowed) {
        TestCampaignExecutionRegionViewModel testPlanExecutionViewModel = getBrowserTab().getView(TestCampaignExecutionRegionViewModel.class);

        String testCaseId = testCaseInfo.getTestCase().getTestCaseId();

        UiComponent testCaseRow = findTableRow(testPlanExecutionViewModel.getTestCasesTable(), testCaseId);
        if (testCaseRow == null) {
            createScreenshot("createTestExecution", "Test case was not found by title!");
            return TestExecutionResult.error("Test case was not found by title!");
        }

        Button addResults = testCaseRow.getDescendantsBySelector(TestCampaignExecutionRegionViewModel.TMS_TE_TEST_CASE_BUTTON).get(0).as(Button.class);
        testCaseRow.click();
        addResults.click();

        TestExecutionRegionViewModel testExecutionViewModel = getBrowserTab().getView(TestExecutionRegionViewModel.class);
        sleep(1);
        try {
            testExecutionViewModel.getCommentBox().setText(testExecutionInfo.getComment());
        } catch (UiComponentNotFoundException e) {
            addResults.click();  //re- click
            testExecutionViewModel.getCommentBox().setText(testExecutionInfo.getComment());
        }
        testExecutionViewModel.getExecutionTime().setText(testExecutionInfo.getExecutionTime());
        setSelectBoxValue(testExecutionViewModel.getResultSelect(), testExecutionInfo.getResult());

        if (defectsAllowed) {
            createFailExecution(testExecutionInfo, testExecutionViewModel);
        } else {
            for (UiComponent passIcon : testExecutionViewModel.getPassIcons()) {
                if (!isSelected(passIcon)) {
                    passIcon.click();
                }
            }
            try {
                getBrowserTab().waitUntilComponentIsHidden(testExecutionViewModel.getDefectIdTextarea(), WAIT_TIMEOUT);
            } catch (WaitTimedOutException e) {
                String errorMessage = "Test Execution defect field was not hidden after result set to PASS";
                createScreenshot("createTestExecution", errorMessage);
                return TestExecutionResult.error(errorMessage);
            }
        }

        QuickActionBarCreateTEViewModel createTEViewModel = getBrowserTab().getView(QuickActionBarCreateTEViewModel.class);
        createTEViewModel.getSaveActionLink().click();

        TestCampaignExecutionRegionViewModel testPlanDetailsRegionViewModel = waitForTeCampaignExecutionPage();
        try {
            getBrowserTab().waitUntilComponentIsDisplayed(testPlanDetailsRegionViewModel.getTestCampaignExecutionRegion(), WAIT_TIMEOUT);
        } catch (WaitTimedOutException e) {
            String errorMessage = "Test Plan Execution page was not shown after save action.";
            createScreenshot("createTestExecution", errorMessage);
            return TestExecutionResult.error(errorMessage);
        }

        return TestExecutionResult.success(testExecutionInfo);
    }

    private void createFailExecution(TestExecutionInfo testExecutionInfo, TestExecutionRegionViewModel testExecutionViewModel) {
        for (UiComponent failIcon : testExecutionViewModel.getFailIcons()) {
            if (!isSelected(failIcon)) {
                failIcon.click();
            }
        }
        Set<String> defectIds = testExecutionInfo.getDefectIds();
        for (String defectId : defectIds) {
            testExecutionViewModel.getDefectIdTextarea().sendKeys(defectId);
        }
        Set<String> requirementIds = testExecutionInfo.getRequirementIds();
        for (String requirementId : requirementIds) {
            testExecutionViewModel.getRequirementIdTextarea().sendKeys(requirementId);
        }
    }

    private boolean isSelected(UiComponent testStepResultIcon) {
        String selected = testStepResultIcon.getProperty("selected");
        return selected != null && Boolean.parseBoolean(selected);
    }

    private TestExecutionResult updateTestExecutionInfo(TestCampaignItemInfo assignmentInfo, TestExecutionInfo testExecutionInfo) {
        TestCampaignExecutionRegionViewModel testPlanExecutionViewModel = getBrowserTab().getView(TestCampaignExecutionRegionViewModel.class);

        String testCaseId = assignmentInfo.getTestCase().getTestCaseId();
        UiComponent testCaseRow = findTableRow(testPlanExecutionViewModel.getTestCasesTable(), testCaseId);
        if (testCaseRow == null) {
            createScreenshot("createTestExecution", "Test case was not found by title!");
            return TestExecutionResult.error("Test case was not found by title!");
        }

        Button addResult = testCaseRow.getDescendantsBySelector("button.eaTM-ButtonCell-button").get(0).as(Button.class);
        addResult.click();

        TestExecutionRegionViewModel testExecutionViewModel = waitForTestExecutionPage();

        testExecutionViewModel.getCommentBox().setText(testExecutionInfo.getComment());
        setSelectBoxValue(testExecutionViewModel.getResultSelect(), testExecutionInfo.getResult());

        UiComponent testStepBlock = testExecutionViewModel.getTestStepsHolder();
        List<UiComponent> testSteps =
                testStepBlock.getDescendantsBySelector(TestExecutionRegionViewModel.TEST_STEP_SELECTOR);

        for (int i = 0; i < testSteps.size(); i++) {
            UiComponent testStep = testSteps.get(i);
            final int testStepOrderNr = i + 1;

            Optional<TestStepExecutionInfo> testStepExecutionOptional =
                    getTestStepExecutionOptional(testExecutionInfo, testStepOrderNr);
            applyTestStepResult(testStep, testStepExecutionOptional);

            List<UiComponent> verifySteps =
                    testStep.getDescendantsBySelector(TestExecutionRegionViewModel.VERIFY_STEP_SELECTOR);
            applyVerifyStepsActualResults(testStepOrderNr, verifySteps, testExecutionInfo.getVerifyStepExecutions());
        }

        QuickActionBarCreateTEViewModel createTEViewModel = getBrowserTab().getView(QuickActionBarCreateTEViewModel.class);
        createTEViewModel.getSaveActionLink().click();

        TestCampaignExecutionRegionViewModel testPlanDetailsRegionViewModel = waitForTeCampaignExecutionPage();
        try {
            getBrowserTab().waitUntilComponentIsDisplayed(testPlanDetailsRegionViewModel.getTestCampaignExecutionRegion(), WAIT_TIMEOUT);
        } catch (WaitTimedOutException e) {
            String errorMessage = "Test Plan Execution page was not shown after save action.";
            createScreenshot("createTestExecution", errorMessage);
            return TestExecutionResult.error(errorMessage);
        }

        return TestExecutionResult.success(testExecutionInfo);
    }

    private Optional<TestStepExecutionInfo> getTestStepExecutionOptional(TestExecutionInfo testExecutionInfo, final int testStepOrderNr) {
        return Iterables.tryFind(testExecutionInfo.getTestStepExecutions(), new Predicate<TestStepExecutionInfo>() {
            @Override
            public boolean apply(TestStepExecutionInfo testStepExecutionInfo) {
                return testStepExecutionInfo.getTestStepOrderNr() == testStepOrderNr;
            }
        });
    }

    private void applyTestStepResult(UiComponent testStep, Optional<TestStepExecutionInfo> testStepExecutionInfoOptional) {
        deselectTestStepResult(testStep);
        if (!testStepExecutionInfoOptional.isPresent()) {
            return;
        }
        TestStepExecutionInfo testStepExecution = testStepExecutionInfoOptional.get();
        ReferenceDataItem testStepResult = testStepExecution.getResult();
        if (testStepResult == null) {
            return;
        }

        String selector = TestCaseFormRegionViewModel.TEST_STEP_PASS_RESULT;
        if (TestStepResultType.FAIL.getTitle().equals(testStepResult.getTitle())) {
            selector = TestCaseFormRegionViewModel.TEST_STEP_FAIL_RESULT;
        }
        UiComponent testStepResultIcons = testStep.getDescendantsBySelector(selector).get(0);
        testStepResultIcons.click();
    }

    private void deselectTestStepResult(UiComponent testStep) {
        List<UiComponent> selectedPassResult = testStep.getDescendantsBySelector(TestCaseFormRegionViewModel.TEST_STEP_PASS_RESULT_SELECTED);
        if (!selectedPassResult.isEmpty()) {
            selectedPassResult.iterator().next().click();
        }
        List<UiComponent> selectedFailResult = testStep.getDescendantsBySelector(TestCaseFormRegionViewModel.TEST_STEP_FAIL_RESULT_SELECTED);
        if (!selectedFailResult.isEmpty()) {
            selectedFailResult.iterator().next().click();
        }
    }

    private void applyVerifyStepsActualResults(final int testStepOrderNr, List<UiComponent> verifySteps, List<VerifyStepExecutionInfo> verifyStepExecutions) {
        if (verifyStepExecutions.isEmpty()) {
            return;
        }

        for (int i = 0; i < verifySteps.size(); i++) {
            UiComponent verifyStep = verifySteps.get(i);
            final int verifyStepOrderNr = i + 1;

            Optional<VerifyStepExecutionInfo> verifyStepExecutionOptional = getVerifyStepExecutionOptional(verifyStepExecutions, testStepOrderNr, verifyStepOrderNr);
            applyVerifyStepActualResult(verifyStep, verifyStepExecutionOptional);
        }
    }

    private void applyVerifyStepActualResult(UiComponent verifyStep, Optional<VerifyStepExecutionInfo> verifyStepExecutionInfoOptional) {
        deselectVerifyStepActualResult(verifyStep);
        if (!verifyStepExecutionInfoOptional.isPresent()) {
            return;
        }
        VerifyStepExecutionInfo verifyStepExecution = verifyStepExecutionInfoOptional.get();
        String actualResult = verifyStepExecution.getActualResult();
        if (actualResult == null) {
            return;
        }

        List<UiComponent> addLinks = verifyStep.getDescendantsBySelector(TestCaseFormRegionViewModel.VERIFY_STEP_ADD_ACTUAL_RESULT_LINK);
        addLinks.iterator().next().click();

        List<UiComponent> actualResultTexts = verifyStep.getDescendantsBySelector(TestCaseFormRegionViewModel.VERIFY_STEP_ACTUAL_RESULT_TEXT_BOX);
        TextBox actualResultTextBox = actualResultTexts.iterator().next().as(TextBox.class);
        actualResultTextBox.setText(actualResult);
    }

    private void deselectVerifyStepActualResult(UiComponent verifyStep) {
        List<UiComponent> actualResultDeleteIcons = verifyStep.getDescendantsBySelector(TestCaseFormRegionViewModel.VERIFY_STEP_DELETE_ACTUAL_RESULT_ICON);
        if (actualResultDeleteIcons.isEmpty()) {
            return;
        }
        UiComponent deleteIcon = actualResultDeleteIcons.iterator().next();
        if (deleteIcon.isDisplayed()) {
            deleteIcon.click();
        }
    }

    private Optional<VerifyStepExecutionInfo> getVerifyStepExecutionOptional(List<VerifyStepExecutionInfo> verifyStepExecutions, final int testStepOrderNr, final int verifyStepOrderNr) {
        return Iterables.tryFind(verifyStepExecutions, new Predicate<VerifyStepExecutionInfo>() {
            @Override
            public boolean apply(VerifyStepExecutionInfo verifyStepExecutionInfo) {
                return verifyStepExecutionInfo.getTestStep() == testStepOrderNr && verifyStepExecutionInfo.getVerifyStep() == verifyStepOrderNr;
            }
        });
    }

    private TestExecutionInfo gatherTestExecutionInfo(TestExecutionRegionViewModel testExecutionViewModel) {
        TestExecutionInfo testExecutionInfo = new TestExecutionInfo();
        testExecutionInfo.setComment(testExecutionViewModel.getCommentBox().getText());

        // Get TestExecutionResult
        String resultValue = testExecutionViewModel.getResultSelectValue().getText();
        testExecutionInfo.setResult(TestExecutionResultType.getEnum(resultValue));

        // Get defect IDs
        List<UiComponent> defectIds = testExecutionViewModel.getDefectIdMultiSelect().getDescendantsBySelector(TestExecutionRegionViewModel.MULTI_SELECT_ITEM);
        for (UiComponent defectIdField : defectIds) {
            testExecutionInfo.getDefectIds().add(defectIdField.getText());
        }

        gatherTestStepsResults(testExecutionViewModel, testExecutionInfo);
        return testExecutionInfo;
    }

    private void gatherTestStepsResults(
            TestExecutionRegionViewModel testExecutionViewModel,
            TestExecutionInfo testExecutionInfo) {
        UiComponent testStepBlock = testExecutionViewModel.getTestStepsHolder();
        List<UiComponent> testSteps =
                testStepBlock.getDescendantsBySelector(TestExecutionRegionViewModel.TEST_STEP_SELECTOR);

        for (int i = 0; i < testSteps.size(); i++) {
            UiComponent testStep = testSteps.get(i);
            final int testStepOrderNr = i + 1;

            populateTestStepResult(testStep, testStepOrderNr, testExecutionInfo);

            List<UiComponent> verifySteps =
                    testStep.getDescendantsBySelector(TestExecutionRegionViewModel.VERIFY_STEP_SELECTOR);
            populateVerifyStepsActualResults(testStepOrderNr, verifySteps, testExecutionInfo);
        }
    }

    private void populateTestStepResult(UiComponent testStep, int testStepOrderNr, TestExecutionInfo testExecutionInfo) {
        List<UiComponent> selectedPassResult =
                testStep.getDescendantsBySelector(TestExecutionRegionViewModel.TEST_STEP_PASS_RESULT_SELECTED);
        if (!selectedPassResult.isEmpty()) {
            TestStepExecutionInfo testStepExecutionInfo = new TestStepExecutionInfo();
            testStepExecutionInfo.setResult(TestStepResultType.getEnum(TestStepResultType.PASS.getTitle()));
            testStepExecutionInfo.setTestStep((long) testStepOrderNr);

            testExecutionInfo.getTestStepExecutions().add(testStepExecutionInfo);
            return;
        }

        List<UiComponent> selectedFailResult =
                testStep.getDescendantsBySelector(TestExecutionRegionViewModel.TEST_STEP_FAIL_RESULT_SELECTED);
        if (!selectedFailResult.isEmpty()) {
            TestStepExecutionInfo testStepExecutionInfo = new TestStepExecutionInfo();
            testStepExecutionInfo.setResult(TestStepResultType.getEnum(TestStepResultType.FAIL.getTitle()));
            testStepExecutionInfo.setTestStep((long) testStepOrderNr);

            testExecutionInfo.getTestStepExecutions().add(testStepExecutionInfo);
        }
    }

    private void populateVerifyStepsActualResults(int testStepOrderNr, List<UiComponent> verifySteps, TestExecutionInfo testExecutionInfo) {
        if (verifySteps.isEmpty()) {
            return;
        }

        for (int i = 0; i < verifySteps.size(); i++) {
            UiComponent verifyStep = verifySteps.get(i);
            final int verifyStepOrderNr = i + 1;

            populateVerifyStepResult(verifyStep, testStepOrderNr, verifyStepOrderNr, testExecutionInfo);
        }
    }

    private void populateVerifyStepResult(UiComponent verifyStep, int testStepOrderNr, int verifyStepOrderNr, TestExecutionInfo testExecutionInfo) {
        List<UiComponent> actualResultTextBoxes = verifyStep.getDescendantsBySelector(TestCaseFormRegionViewModel.VERIFY_STEP_ACTUAL_RESULT_TEXT_BOX);
        if (actualResultTextBoxes.isEmpty()) {
            return;
        }

        TextBox actualResultTextBox = actualResultTextBoxes.iterator().next().as(TextBox.class);
        if (actualResultTextBox.isDisplayed()) {
            VerifyStepExecutionInfo verifyStepExecution = new VerifyStepExecutionInfo();
            verifyStepExecution.setTestStep((long) testStepOrderNr);
            verifyStepExecution.setVerifyStep((long) verifyStepOrderNr);
            verifyStepExecution.setActualResult(actualResultTextBox.getText());

            testExecutionInfo.getVerifyStepExecutions().add(verifyStepExecution);
        }
    }

    private void selectProductDropFeatureAndComponents(String selector, TestCampaignInfo testCampaignInfo) {
        UiComponent widgetHolder = getBrowserTab().waitUntilComponentIsDisplayed(selector);

        UiComponent productSelectBox = widgetHolder.getDescendantsBySelector(ProductSelectorViewModel.PRODUCT_SELECTOR).get(0);
        productSelectBox.click();
        selectItemFromDropdown(productSelectBox, testCampaignInfo.getProduct().getName());

        sleep(1); //required to ensure that correct drop options are present after selecting product
        UiComponent dropSelectBox = widgetHolder.getDescendantsBySelector(ProductSelectorViewModel.DROP_SELECTOR).get(0);
        if (dropSelectBox.isDisplayed()) {
            waitForComponentEnabled(dropSelectBox);
            dropSelectBox.click();
            selectItemFromDropdown(dropSelectBox, testCampaignInfo.getDrop().getName());
        }

        sleep(1); //required to ensure that correct feature options are present after selecting product &/drop
        UiComponent featureSelectBox = widgetHolder.getDescendantsBySelector(ProductSelectorViewModel.FEATURE_SELECTOR).get(0);
        Set<String> references = testCampaignInfo.getFeatures().stream().map(item -> item.getName()).collect(Collectors.toSet());
        setMultiSelectBox(featureSelectBox, references);

        UiComponent componentSelectBox = widgetHolder.getDescendantsBySelector(ProductSelectorViewModel.COMPONENT_SELECTOR).get(0);
        if (componentSelectBox.isDisplayed() && !testCampaignInfo.getComponents().isEmpty()) {
            componentSelectBox.click();
            for (TechnicalComponentInfo technicalComponent : testCampaignInfo.getComponents()) {
                selectItemFromDropdown(componentSelectBox, technicalComponent.getName());
            }
        }

    }

    private void selectItemFromDropdown(UiComponent dropDown, String value) {
        UiComponent selectBoxList;
        try {
            selectBoxList = getBrowserTab().waitUntilComponentIsDisplayed(ProductSelectorViewModel.SELECT_BOX_LIST_SELECTOR, WAIT_LONG_TIMEOUT);
        } catch (WaitTimedOutException e) {
            dropDown.focus();
            dropDown.click();
            selectBoxList = getBrowserTab().waitUntilComponentIsDisplayed(ProductSelectorViewModel.SELECT_BOX_LIST_SELECTOR, WAIT_LONG_TIMEOUT);
        }

        List<UiComponent> listItems = selectBoxList.getDescendantsBySelector(ProductSelectorViewModel.SELECT_BOX_LIST_ITEMS_SELECTOR);
        for (UiComponent item : listItems) {
            if (!item.isDisplayed()) {
                dropDown.click();
            }
            String dropdownItemTitle = item.getText();
            if (dropdownItemTitle.equals(value)) {
                item.focus();
                item.click();
                break;
            }
        }
    }

    private TestCampaignExecutionsInfo gatherTestPlanData(TestCampaignExecutionRegionViewModel testPlanExecutionViewModel) {
        TestCampaignInfo testCampaignInfo = new TestCampaignInfo();
        testCampaignInfo.setName(testPlanExecutionViewModel.getTestCampaignName().getText());

        testCampaignInfo.setDescription(testPlanExecutionViewModel.getTestCampaignDescription().getText());
        testCampaignInfo.setEnvironment(testPlanExecutionViewModel.getTestCampaignEnvironment().getText());

        List<TestCaseExecutions> testCaseExecutions = new ArrayList();
        List<UiComponent> testCasesRows = testPlanExecutionViewModel.getTestCasesRows();
        for (UiComponent testCasesRow : testCasesRows) {
            TestCampaignItemInfo assignmentInfo = gatherTestCaseData(testCasesRow);

            testCasesRow.click();

            try {
                getBrowserTab().waitUntilComponentIsDisplayed(testPlanExecutionViewModel.getProgressBlock(), WAIT_ULTRA_SHORT_TIMEOUT);
            } catch (Exception e) {
                createScreenshot("gatherTestPlanExecutionData_getProgressBlock", "ProgressBar probably was hidden very fast.");
            }
            getBrowserTab().waitUntilComponentIsHidden(testPlanExecutionViewModel.getProgressBlock(), WAIT_TIMEOUT);

            testCampaignInfo.getTestCampaignItems().add(assignmentInfo);
            gatherTestExecutionData(testPlanExecutionViewModel.getTestExecutions());
            TestCaseExecutions currentTestCaseExecutions = new TestCaseExecutions(
                    assignmentInfo.getTestCase().getTestCaseId(),
                    gatherTestExecutionData(testPlanExecutionViewModel.getTestExecutions()));

            testCaseExecutions.add(currentTestCaseExecutions);
        }

        return new TestCampaignExecutionsInfo(testCampaignInfo, testCaseExecutions);
    }

    private List<TestExecutionInfo> gatherTestExecutionData(List<UiComponent> testExecutions) {
        List<TestExecutionInfo> testExecutionInfoList = Lists.newArrayList();
        if (testExecutions.isEmpty()) {
            return testExecutionInfoList;
        }

        for (UiComponent testExecution : testExecutions) {
            TestExecutionInfo testExecutionInfo = new TestExecutionInfo();
            testExecutionInfo.setAuthor(testExecution.getDescendantsBySelector(TestCampaignExecutionRegionViewModel.TMS_TE_USERNAME_ID).get(0).getText());
            String dateString = testExecution.getDescendantsBySelector(TestCampaignExecutionRegionViewModel.TMS_TE_DATE_ID).get(0).getText();
            testExecutionInfo.setCreatedAt(parseDateString(dateString));
            testExecutionInfo.setComment(testExecution.getDescendantsBySelector(TestCampaignExecutionRegionViewModel.TMS_TE_COMMENT_BLOCK_ID).get(0).getText());

            String testExecutionResultText = testExecution.getDescendantsBySelector(TestCampaignExecutionRegionViewModel.TMS_TE_RESULT_TEXT_ID).get(0).getText();
            testExecutionInfo.setResult(TestExecutionResultType.getEnum(testExecutionResultText));

            List<UiComponent> defectLinks = testExecution.getDescendantsBySelector(TestCampaignExecutionRegionViewModel.TMS_TE_DEFECT_LINK);
            for (UiComponent defectLink : defectLinks) {
                testExecutionInfo.getDefectIds().add(defectLink.getText());
            }

            List<UiComponent> requirmentLinks = testExecution.getDescendantsBySelector(TestCampaignExecutionRegionViewModel.TMS_TE_REQUIREMENT_LINK);
            for (UiComponent requirementLink : requirmentLinks) {
                testExecutionInfo.getRequirementIds().add(requirementLink.getText());
            }

            testExecutionInfoList.add(testExecutionInfo);
        }

        return testExecutionInfoList;
    }

    private Date parseDateString(String dateString) {
        Date date = new Date();
        try {
            DateFormat dateFormat = new SimpleDateFormat("MM/d/yyyy, h:mm:ss");
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            logger.error("Could not parse Test Execution Date " + e.getMessage());
        }
        return date;
    }

    private Set<TestCampaignItemInfo> getTestPlanAssignmentData(UiComponent table, boolean isTestPlanDetailsPage) {
        Set<TestCampaignItemInfo> assignments = new LinkedHashSet<>();
        List<UiComponent> tableRows = table.getDescendantsBySelector(TableHelper.TABLE_ROWS);

        tableRows.forEach(row -> assignments.add(getTestCaseData(row, isTestPlanDetailsPage)));

        return assignments;
    }

    private TestCampaignItemInfo getTestCaseData(UiComponent testCasesRow, boolean isTestPlanDetailsPage) {
        int startIndex = isTestPlanDetailsPage ? 0 : 1;
        TestCampaignItemInfo assignmentInfo = new TestCampaignItemInfo();
        List<UiComponent> tableCells = testCasesRow.getDescendantsBySelector("td");

        TestCaseInfo testCaseInfo = new TestCaseInfo();
        testCaseInfo.setTestCaseId(tableCells.get(startIndex).getProperty("title"));
        testCaseInfo.setTitle(tableCells.get(startIndex + 1).getProperty("title"));

        FeatureInfo featureInfo = new FeatureInfo();
        featureInfo.setName(tableCells.get(startIndex + 2).getProperty("title"));
        assignmentInfo.setFeature(featureInfo);

        String version = tableCells.get(startIndex + 3).getProperty("title");
        testCaseInfo.setVersion(version);

        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(tableCells.get(startIndex + 4).getProperty("title"));
        assignmentInfo.setUser(userInfo);
        assignmentInfo.setTestCase(testCaseInfo);


        ReferenceDataItem executionType = new ReferenceDataItem();
        executionType.setTitle(tableCells.get(startIndex + 5).getProperty("title"));
        testCaseInfo.setExecutionType(executionType);

        return assignmentInfo;
    }

    private TestCampaignItemInfo gatherTestCaseData(UiComponent testCasesRow) {
        TestCampaignItemInfo assignmentInfo = new TestCampaignItemInfo();
        List<UiComponent> tableCells = testCasesRow.getDescendantsBySelector("td");

        String resultTitle = tableCells.get(0).getProperty("title");
        assignmentInfo.setResult(TestExecutionResultType.getEnum(resultTitle));

        TestCaseInfo testCaseInfo = new TestCaseInfo();
        testCaseInfo.setTestCaseId(tableCells.get(1).getProperty("title"));
        testCaseInfo.setTitle(tableCells.get(2).getProperty("title"));
        testCaseInfo.setComment(tableCells.get(3).getProperty("title"));

        assignmentInfo.setTestCase(testCaseInfo);

        String defectIds = tableCells.get(4).getProperty("title");

        if (!Strings.isNullOrEmpty(defectIds)) {

            String[] defectTitles = defectIds.split(", ");

            Set<DefectInfo> defects = new HashSet<>();
            for (String defect : defectTitles) {
                DefectInfo defectinfo = new DefectInfo();
                defectinfo.setExternalId(defect);
                defectinfo.setExternalTitle(defect);
                defects.add(defectinfo);
            }
            assignmentInfo.setDefects(defects);
        }

        return assignmentInfo;
    }

    private int getPageCount(ViewModel viewModel) {
        String lastPageLinkSelector = TestCampaignsRegionViewModel.TMS_TEST_PLANS_ID + " .ebPagination-pages li:last-child a";
        Link lastPageLink = viewModel.getLink(lastPageLinkSelector);
        if (lastPageLink.exists()) {
            String linkText = lastPageLink.getText();
            return Integer.parseInt(linkText);
        }
        return 1;
    }

    private void openPage(ViewModel viewModel, int pageNumber, int pageCount) {
        if (pageCount == 1) {
            return;
        }
        String page = String.valueOf(pageNumber);

        String currentPageSelector = TestCampaignsRegionViewModel.TMS_TEST_PLANS_ID + " .ebPagination-pages li a.ebPagination-entryAnchor_current";
        List<Link> pagesLinks = viewModel.getViewComponents(currentPageSelector, Link.class);
        if (pagesLinks.size() != 1) {
            throw new RuntimeException("There are more than one page selected as current page. (UI SDK bug)");
        }
        if (page.equals(pagesLinks.iterator().next().getText())) {
            return;
        }

        String pageLinkSelector = TestCampaignsRegionViewModel.TMS_TEST_PLANS_ID + " .ebPagination-pages li a:not(.ebPagination-entryAnchor_current)";
        List<Link> links = viewModel.getViewComponents(pageLinkSelector, Link.class);
        for (Link link : links) {
            String text = link.getText();
            if (page.equals(text)) {
                link.click();
                return;
            }
        }
        throw new RuntimeException("Cannot find UI SDK page " + pageNumber + " on screen");
    }

    private UiComponent findTableRow(UiComponent table, String rowId) {
        List<UiComponent> rowList = table.getDescendantsBySelector("[data-id=\"" + rowId + "\"]");
        if (rowList.isEmpty() || rowList.size() > 1) {
            return null;
        }
        return rowList.get(0);
    }

    private Paginated<TestCaseInfo> getTestCasesFromDetailsBlock(DetailsBlockModel detailsBlock) {
        List<TestCaseInfo> testInfoList = Lists.newArrayList();

        for (UiComponent testCaseLine : detailsBlock.getTestCasesLines()) {
            UiComponent testCaseLink = testCaseLine.getDescendantsBySelector(DetailsBlockModel.TEST_CASE_TITLE_SELECTOR).get(0);

            String[] splitLinkText = testCaseLink.getText().split(": ");

            TestCaseInfo testInfo = new TestCaseInfo();
            testInfo.setTestCaseId(splitLinkText[0]);
            testInfo.setTitle(splitLinkText[1]);

            testInfoList.add(testInfo);
        }

        Paginated<TestCaseInfo> testCases = new Paginated<>();
        testCases.setItems(testInfoList);
        testCases.setTotalCount(Integer.toString(testInfoList.size()));
        return testCases;
    }

    private Paginated<TestCaseInfo> getTestCases(TableResultsViewModel tableResultsViewModel) {
        Paginated<TestCaseInfo> testCases;
        try {
            testCases = getTestCasesFromTable(tableResultsViewModel);
        } catch (Exception e) {
            createScreenshot("getTestCases_getTestCasesFromTable", "TableWidget probably redrawn: trying to locate again...");
            testCases = getTestCasesFromTable(tableResultsViewModel);
        }
        return testCases;
    }

    private void clearTableFilters(final ViewModel viewModel, String selectorPrefix) {
        String selector = selectorPrefix + " .ebTable thead tr";
        List<UiComponent> viewComponents = viewModel.getViewComponents(selector, UiComponent.class);
        if (viewComponents.size() < 2) {
            return;
        }

        UiComponent filtersRow = viewComponents.get(1);
        List<UiComponent> filterInputs = filtersRow.getDescendantsBySelector(".eaTM-StringFilter-input");
        if (filterInputs.isEmpty()) {
            return;
        }

        for (UiComponent filterInput : filterInputs) {
            filterInput.as(TextBox.class).setText("");
        }
    }

    private void prepopulateDataForAdvancedSearchFromDescendants(UiComponent content, List<Criterion> criterions) {
        int criterionsSize = content.getDescendantsBySelector(AdvancedSearchBlockViewModel.CRITERION_LIST).size();

        for (Criterion criterion : criterions) {
            UiComponent criterionListBlock = content.getDescendantsBySelector(AdvancedSearchBlockViewModel.CRITERION_LIST_BLOCK).get(0);
            content.getDescendantsBySelector(AdvancedSearchBlockViewModel.CRITERION_ADD_BLOCK).get(0).click();

            getBrowserTab().waitUntil(content.getDescendantsBySelector(AdvancedSearchBlockViewModel.CRITERION_LIST_BLOCK).get(0),
                    UiComponentPredicates.CHILD_ADDED.withCurrentChildrenCount(criterionsSize), WAIT_SHORT_TIMEOUT);

            UiComponent fieldSelectBox = criterionListBlock.getDescendantsBySelector(AdvancedSearchBlockViewModel.FIELD_SELECT_BOX + criterionsSize + " > button").get(0);
            UiComponent conditionSelectBox = criterionListBlock.getDescendantsBySelector(AdvancedSearchBlockViewModel.CONDITION_SELECT_BOX + criterionsSize + " > button").get(0);

            setSelectBoxValue(fieldSelectBox, criterion.getField().getTitle());
            setSelectBoxValue(conditionSelectBox, criterion.getCondition().getTitle());

            List<UiComponent> inputs = content.getDescendantsBySelector(AdvancedSearchBlockViewModel.CRITERION_INPUT);
            UiComponent inputField = inputs.get(criterionsSize);
            inputField.focus();
            inputField.sendKeys(criterion.getValue());

            criterionsSize++;
        }
    }

    private void clearAllCriterions(UiComponent searchPanel) {
        List<UiComponent> criterionHolders = searchPanel.getDescendantsBySelector(AdvancedSearchBlockViewModel.CRITERION_LIST);
        for (UiComponent criterionHolder : criterionHolders) {
            UiComponent actionsBlock = criterionHolder.getChildren().get(3);
            actionsBlock.getChildren().get(0).click();
        }
    }

    private AdvancedSearchBlockViewModel waitForAdvancedSearchBlock(String selectorName) {
        AdvancedSearchBlockViewModel advancedSearchBlockViewModel = getBrowserTab().getView(AdvancedSearchBlockViewModel.class);
        if (Strings.isNullOrEmpty(selectorName)) {
            selectorName = advancedSearchBlockViewModel.getSearchPanel().getComponentSelector().getSelectorExpression();
        }

        try {
            getBrowserTab().waitUntilComponentIsDisplayed(selectorName, WAIT_SHORT_TIMEOUT);
        } catch (WaitTimedOutException e) {
            createScreenshot("waitForAdvancedSearchBlock", e.getMessage());
        }
        return advancedSearchBlockViewModel;
    }

    private void checkCriterionsWithUiFields(AdvancedSearchBlockViewModel searchViewModel, List<Criterion> criterions) {
        UiComponent contentRegion = searchViewModel.getSearchPanel();
        List<UiComponent> criterionList = contentRegion.getDescendantsBySelector(AdvancedSearchBlockViewModel.CRITERION_LIST);

        if (criterionList.size() != criterions.size()) {
            throw new IllegalArgumentException("Criterions count from UI is not equal to defined criterions. (" + criterionList.size() + " != " + criterions.size() + ").");
        }

        UiComponent criterionListBlock = contentRegion.getDescendantsBySelector(AdvancedSearchBlockViewModel.CRITERION_LIST_BLOCK).get(0);
        List<UiComponent> searchValueList = contentRegion.getDescendantsBySelector(AdvancedSearchBlockViewModel.CRITERION_INPUT);

        int criterionIndex = 0;
        for (Criterion criterion : criterions) {
            String prefix = "Criterion with index " + criterionIndex;
            if (!criterion.getField().getTitle().equals(criterionListBlock.getDescendantsBySelector(AdvancedSearchBlockViewModel.FIELD_SELECT_BOX + criterionIndex).get(0).getText())) {
                throw new IllegalArgumentException(prefix + " doesn't have same item on UI for field selectBox.");
            }
            if (!criterion.getCondition().getTitle().equals(criterionListBlock.getDescendantsBySelector(AdvancedSearchBlockViewModel.CONDITION_SELECT_BOX + criterionIndex).get(0).getText())) {
                throw new IllegalArgumentException(prefix + " doesn't have same item on UI for condition selectBox.");
            }
            if (!criterion.getValue().equals(searchValueList.get(criterionIndex).getText())) {
                throw new IllegalArgumentException(prefix + " doesn't have same item on UI for value in the input.");
            }
            criterionIndex++;
        }
    }

    private void checkUrlParams(List<Criterion> criterions) {
        String currentUrl = getBrowserTab().getCurrentUrl();
        String[] splitUrl = currentUrl.split("#");
        String[] splitHash = splitUrl[1].split("\\?");
        if (splitHash.length == 1) {
            throw new IllegalStateException("Url didn't have query params!");
        }
        String[] splitQueryParams = splitHash[1].split("&");

        if (criterions.isEmpty() && splitQueryParams.length != 0) {
            throw new IllegalArgumentException("Url has query params which are not defined in criterions!");
        }
        if (criterions.size() != splitQueryParams.length) {
            throw new IllegalArgumentException("Query params for search is not equals with defined criterions!");
        }

        Boolean isFoundAllCriterions = validateCriterions(criterions, splitQueryParams);
        if (!isFoundAllCriterions) {
            throw new IllegalArgumentException("Some criterions were not found in the query string!");
        }
    }

    private Boolean validateCriterions(List<Criterion> criterions, String[] splitQueryParams) {
        Boolean isFound = Boolean.FALSE;
        for (Criterion criterion : criterions) {
            isFound = Boolean.FALSE;
            for (String splitQueryParam : splitQueryParams) {
                if (criterion.toString().equals(splitQueryParam)) {
                    isFound = Boolean.TRUE;
                    break;
                }
            }
        }
        return isFound;
    }

    private Paginated<TestCampaignItemInfo> getAssignmentsFromTable(AssignmentsRegionViewModel assignmentsModel) {
        Paginated<TestCampaignItemInfo> assignmentPage = new Paginated<>();
        List<TestCampaignItemInfo> assignmentInfoList = Lists.newArrayList();

        for (UiComponent tableRow : assignmentsModel.getTableRows()) {
            TestCampaignItemInfo assignmentInfo = getAssignmentInfoFromTableRow(tableRow);
            assignmentInfoList.add(assignmentInfo);
        }
        assignmentPage.setItems(assignmentInfoList);
        return assignmentPage;
    }

    private TestCampaignItemInfo getAssignmentInfoFromTableRow(UiComponent tableRow) {
        TestCampaignItemInfo assignmentInfo = new TestCampaignItemInfo();
        List<UiComponent> cellList = tableRow.getChildren();

        assignmentInfo.setId(Long.parseLong(cellList.get(0).getText()));
        assignmentInfo.setTestCampaign(new ReferenceDataItem("testPlan", cellList.get(1).getText()));

        TestCaseInfo testCaseInfo = new TestCaseInfo();
        testCaseInfo.setTestCaseId(cellList.get(2).getText());
        testCaseInfo.setTitle(cellList.get(3).getText());
        testCaseInfo.setVersion(cellList.get(4).getText());
        assignmentInfo.setTestCase(testCaseInfo);
        return assignmentInfo;
    }

    private Paginated<TestCampaignInfo> getTestPlansFromTable(TestCampaignsRegionViewModel testPlansViewModel) {
        Paginated<TestCampaignInfo> testPlans = new Paginated<>();
        List<TestCampaignInfo> testCampaignInfoList = Lists.newArrayList();

        for (UiComponent tableRow : testPlansViewModel.getTableRows()) {
            TestCampaignInfo testCampaignInfo = getTestPlanInfoFromTableRow(tableRow);
            testCampaignInfoList.add(testCampaignInfo);
        }
        testPlans.setItems(testCampaignInfoList);
        return testPlans;
    }

    private Paginated<TestCaseInfo> getTestCasesFromTable(TableResultsViewModel tableResultsViewModel) {
        Paginated<TestCaseInfo> testCases = new Paginated<>();
        List<TestCaseInfo> testInfoList = Lists.newArrayList();
        for (UiComponent tableRow : tableResultsViewModel.getTableRows()) {
            TestCaseInfo testInfo = getTestCaseInfoFromTableRow(tableRow);
            testInfoList.add(testInfo);
        }
        testCases.setItems(testInfoList);
        return testCases;
    }

    private TestCampaignInfo getTestPlanInfoFromTableRow(UiComponent tableRow) {
        TestCampaignInfo testCampaignInfo = new TestCampaignInfo();
        List<UiComponent> cellList = tableRow.getChildren();

        testCampaignInfo.setName(cellList.get(0).getText());
        testCampaignInfo.setDescription(cellList.get(1).getText());
        testCampaignInfo.setEnvironment(cellList.get(2).getText());

        return testCampaignInfo;
    }

    private TestCaseInfo getTestCaseInfoFromTableRow(UiComponent tableRow) {
        TestCaseInfo testInfo = new TestCaseInfo();
        List<UiComponent> cellList = tableRow.getChildren();

        String requirementsString = cellList.get(2).getText();
        String[] requirementIds = requirementsString.split(", ");
        LinkedHashSet<String> requirementIdList = new LinkedHashSet();

        for (String requirmentId : requirementIds) {
            requirementIdList.add(requirmentId);
        }

        testInfo.setTestCaseId(cellList.get(1).getText());
        testInfo.setRequirementIds(requirementIdList);
        testInfo.setTitle(cellList.get(3).getText());
        testInfo.setDescription(cellList.get(4).getText());

        return testInfo;
    }

    private void applyTableFilters(final ViewModel viewModel, final String selectorPrefix, final List<FilterInfo> filters) {
        String selector = selectorPrefix + " .ebTable thead tr";
        List<UiComponent> viewComponents = viewModel.getViewComponents(selector, UiComponent.class);
        if (viewComponents.isEmpty() || viewComponents.size() == 1) {
            return;
        }

        UiComponent filtersRow = viewComponents.get(1);
        List<UiComponent> filterRowCells = filtersRow.getDescendantsBySelector("th");
        if (filterRowCells.isEmpty()) {
            return;
        }

        UiComponent columnsRow = viewComponents.get(0);
        List<UiComponent> columnRowCells = columnsRow.getDescendantsBySelector("th");
        for (int i = 0; i < columnRowCells.size(); i++) {
            UiComponent columnRowCell = columnRowCells.get(i);
            List<UiComponent> headerTitles = columnRowCell.getDescendantsBySelector(".ebTable-headerText");

            if (!headerTitles.isEmpty()) {
                UiComponent headerText = headerTitles.iterator().next();
                FilterInfo filterInfo = findFilterByField(filters, headerText.getText());
                if (filterInfo != null) {
                    applyTableCellFilter(filterRowCells.get(i), filterInfo);
                }
            }
        }
    }

    private void applyTableCellFilter(UiComponent filterRowCell, FilterInfo filterInfo) {
        UiComponent optionsHolder = filterRowCell.getDescendantsBySelector(".eaTM-StringFilter-options > .eaTM-FilterOptions").get(0);
        setSelectBoxValue(optionsHolder, filterInfo.getCondition().getValue());

        TextBox inputField = filterRowCell.getDescendantsBySelector(".eaTM-StringFilter-input").get(0).as(TextBox.class);
        inputField.setText(filterInfo.getValue());
    }

    private FilterInfo findFilterByField(List<FilterInfo> filters, String text) {
        for (FilterInfo filterInfo : filters) {
            if (text.equals(filterInfo.getTableColumn().getTitle())) {
                return filterInfo;
            }
        }
        return null;
    }

    private TestCaseInfo setTestCaseId(InputStream json, String testCaseId) {
        TestCaseInfo info = JsonHelper.toTestCaseInfo(json);
        if (info.getTestCaseId() != null) {
            info.setTestCaseId(testCaseId);
        }
        return info;
    }

    private Map<String, String> getQueryParams() {
        String url = getBrowserTab().getCurrentUrl();
        logger.info("URL before wait: " + url);

        final AtomicReference<String> currentUrl = new AtomicReference<>();
        GenericPredicate predicate = new GenericPredicate() {
            @Override
            public boolean apply() {
                String url = getBrowserTab().getCurrentUrl();
                currentUrl.set(url);
                return url.contains("?");
            }
        };
        try {
            getBrowserTab().waitUntil(predicate, WAIT_SHORT_TIMEOUT);
        } catch (Exception e) {
            return Maps.newHashMap();
        } finally {
            url = currentUrl.get();
            logger.info("URL after wait: " + url);
        }
        String[] splitUrl = url.split("#");
        String[] splitHash = splitUrl[1].split("\\?");
        if (splitHash.length == 1) {
            throw new IllegalStateException("Url didn't have query params: " + currentUrl);
        }
        String[] splitQueryParams = splitHash[1].split("&");

        Map<String, String> queryParams = Maps.newHashMap();
        for (String queryString : splitQueryParams) {
            String[] nameValuePair = queryString.split("=");
            queryParams.put(nameValuePair[0], nameValuePair[1]);
        }
        return queryParams;
    }

    private DetailsBlockModel selectRequirementFromTree(String requirementId, TreeViewModel treeView) {
        UiComponent requirementToClick = expandTreesAndFindRequirement(requirementId.toUpperCase(), treeView.getTreeWidget());
        if (requirementToClick == null) {
            throw new IllegalStateException("Requirement item \"" + requirementId + "\" not found in TreeWidget.");
        } else {
            DetailsBlockModel detailsBlock = getBrowserTab().getView(DetailsBlockModel.class);
            requirementToClick.click();
            getBrowserTab().waitUntilComponentIsHidden(detailsBlock.getProgressBlock(), WAIT_TIMEOUT);
            return detailsBlock;
        }
    }

    // Recursive method
    private UiComponent expandTreesAndFindRequirement(final String requirementId, UiComponent treeWidget) {
        if (treeWidget.getChildren().isEmpty()) {
            return null;
        }
        return traverseRequirementsTree(treeWidget, new TreeVisitor<UiComponent, UiComponent>() {
            @Override
            public UiComponent visitNode(UiComponent treeNode) {
                String title = treeNode.getChildren().get(2).getText().toUpperCase();
                if (title.startsWith(requirementId)) {
                    return treeNode;
                } else {
                    return null;
                }
            }
        });
    }

    private <T> T traverseRequirementsTree(UiComponent treeWidget, TreeVisitor<UiComponent, T> visitor) throws IllegalStateException {
        visitor.preVisitChildren();
        for (UiComponent treeItem : treeWidget.getChildren()) {
            visitor.preVisitNode();
            T result = visitNode(treeItem, visitor);
            if (result != null) {
                return result;
            }
            visitor.postVisitNode();
        }
        visitor.postVisitChildren();
        return null;
    }

    private <T> T visitNode(UiComponent treeItem, TreeVisitor<UiComponent, T> visitor) {
        List<UiComponent> treeItemContent = treeItem.getChildren();

        boolean isExpandable = treeItem.getProperty("class").equals(TreeViewModel.TREE_WIDGET_EXPANDABLE_CLASS);
        UiComponent expandableIcon = treeItem.getDescendantsBySelector(TreeViewModel.EXPANDABLE_ICON_SELECTOR).get(0);
        boolean isExpanded = expandableIcon.getProperty("class").contains(TreeViewModel.EXPANDED_ICON_CLASS);

        UiComponent itemContent = treeItemContent.get(0);
        List<UiComponent> contentItems = itemContent.getChildren();
        if (treeItemContent.size() > 1) {
                /* TreeItem content is not loaded */
            if (contentItems.size() == 3) {
                T result = visitor.visitNode(itemContent);
                if (result != null) {
                    return result;
                }

                if (isExpandable && !isExpanded) {
                    contentItems.get(0).click();
                    getBrowserTab().waitUntil(treeItem, UiComponentPredicates.CHILD_ADDED.withCurrentChildrenCount(1), WAIT_TIMEOUT);
                    treeItemContent = treeItem.getChildren();
                }
            } else {
                throw new IllegalStateException("TreeItem content should have three children!");
            }

            if (treeItemContent.size() == 2 && isExpandable) {
                    /* TreeItem content loaded */
                T result = traverseRequirementsTree(treeItemContent.get(1), visitor);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    private FlyoutPanelViewModel waitForFlyoutPanel() {
        FlyoutPanelViewModel flyoutPanelViewModel = getBrowserTab().getView(FlyoutPanelViewModel.class);
        getBrowserTab().waitUntilComponentIsDisplayed(flyoutPanelViewModel.getFlyoutPanel(), WAIT_TIMEOUT);
        return flyoutPanelViewModel;
    }

    private TestCaseDetailsRegionViewModel waitForTestCasePage() {
        TestCaseDetailsRegionViewModel testCaseDetailsViewModel = getBrowserTab().getView(TestCaseDetailsRegionViewModel.class);
        getBrowserTab().waitUntilComponentIsDisplayed(testCaseDetailsViewModel.getTestCaseDetailsRegion(), WAIT_LONG_TIMEOUT);
        return testCaseDetailsViewModel;
    }

    private TestCaseFormRegionViewModel waitForTestCaseEditPage() {
        TestCaseFormRegionViewModel testCaseFormRegionViewModel = getBrowserTab().getView(TestCaseFormRegionViewModel.class);
        waitForComponent(testCaseFormRegionViewModel.getTestCaseFormRegion(), WAIT_LONG_TIMEOUT, "waitForTestCaseEditPage", "Test Case Edit Page was not opened on time");
        return testCaseFormRegionViewModel;
    }

    private CreateDefectViewModel waitForDefectDialog() {
        CreateDefectViewModel createDefectViewModel = getBrowserTab().getView(CreateDefectViewModel.class);
        waitForComponent(createDefectViewModel.getCreateDefectContent(), WAIT_LONG_TIMEOUT, "waitForDefectDialog", "Create Defect dialog did not open");
        return createDefectViewModel;
    }

    private TestCampaignsRegionViewModel waitForTestCampaignsPage() {
        TestCampaignsRegionViewModel testPlansViewModel = getBrowserTab().getView(TestCampaignsRegionViewModel.class);
        waitForComponent(testPlansViewModel.getTestCampaignsRegion(), WAIT_LONG_TIMEOUT, "waitForTestCampaignsPage", "Test Campaigns Page was not opened on time.");
        return testPlansViewModel;
    }

    private TestCampaignDetailsRegionViewModel waitForTestCampaignDetailsPageOrException() {
        TestCampaignDetailsRegionViewModel testPlanDetailsViewModel = getBrowserTab().getView(TestCampaignDetailsRegionViewModel.class);
        getBrowserTab().waitUntilComponentIsDisplayed(testPlanDetailsViewModel.getTestCampaignDetailsRegion(), WAIT_LONG_TIMEOUT);
        return testPlanDetailsViewModel;
    }

    private TestCampaignDetailsRegionViewModel waitForTestCampaignDetailsPage() {
        TestCampaignDetailsRegionViewModel testPlanDetailsViewModel = getBrowserTab().getView(TestCampaignDetailsRegionViewModel.class);
        waitForComponent(testPlanDetailsViewModel.getTestCampaignDetailsRegion(), WAIT_LONG_TIMEOUT, "waitForTestCampaignDetailsPage", "Test Campaign Details Page was not opened on time.");
        return testPlanDetailsViewModel;
    }

    private TestCampaignEditRegionViewModel waitForTestCampaignEditPage() {
        TestCampaignEditRegionViewModel testPlanEditViewModel = getBrowserTab().getView(TestCampaignEditRegionViewModel.class);
        waitForComponent(testPlanEditViewModel.getTestCampaignEditRegion(), WAIT_LONG_TIMEOUT, "waitForTestCampaignEditPage", "Test Campaign Edit Page was not opened on time.");
        return testPlanEditViewModel;
    }

    private TestCampaignExecutionRegionViewModel waitForTeCampaignExecutionPage() {
        TestCampaignExecutionRegionViewModel testPlanExecutionViewModel = getBrowserTab().getView(TestCampaignExecutionRegionViewModel.class);
        waitForComponent(testPlanExecutionViewModel.getTestCampaignExecutionRegion(), WAIT_LONG_TIMEOUT, "waitForTestCampaignExecutionPage", "Test Plan Execution Page was not opened on time.");
        return testPlanExecutionViewModel;
    }

    private TestExecutionRegionViewModel waitForTestExecutionPage() {
        TestExecutionRegionViewModel testExecutionViewModel = getBrowserTab().getView(TestExecutionRegionViewModel.class);
        waitForComponent(testExecutionViewModel.getTestExecutionRegion(), WAIT_LONG_TIMEOUT, "waitForTestExecutionPage", "Test Execution Page was not opened on time.");
        return testExecutionViewModel;
    }

    private TableResultsViewModel waitForTestCasesTableResults() {
        TableResultsViewModel tableResultsViewModel = getBrowserTab().getView(TableResultsViewModel.class);
        getBrowserTab().waitUntilComponentIsDisplayed(tableResultsViewModel.getTableBody(), WAIT_LONG_TIMEOUT);
        return tableResultsViewModel;
    }

    private TreeViewModel waitForTreeViewWidget() {
        TreeViewModel treeView = getBrowserTab().getView(TreeViewModel.class);
        getBrowserTab().waitUntilComponentIsDisplayed(treeView.getTreeWidget(), WAIT_TIMEOUT);
        return treeView;
    }

    private void waitForComponent(UiComponent component) {
        getBrowserTab().waitUntilComponentIsDisplayed(component, WAIT_TIMEOUT);
    }

    public TestCaseFormRegionViewModel clickOnEditTestCaseLink() {
        QuickActionBarViewTCViewModel quickActionBarViewTCViewModel = getBrowserTab().getView(QuickActionBarViewTCViewModel.class);
        Link editLink = quickActionBarViewTCViewModel.getEditLink();
        getBrowserTab().waitUntilComponentIsDisplayed(editLink, WAIT_INT_TIMEOUT);
        editLink.focus();
        editLink.click();

        TestCaseFormRegionViewModel testCaseFormViewModel = getBrowserTab().getView(TestCaseFormRegionViewModel.class);
        getBrowserTab().waitUntilComponentIsDisplayed(testCaseFormViewModel.getTestCaseFormRegion(), WAIT_TIMEOUT);
        return testCaseFormViewModel;
    }

    private RequirementsRegionViewModel navigateToRequirementsTree(TestCaseSearchViewModel testCaseSearchViewModel) {
        setSelectBoxValue(testCaseSearchViewModel.getContextMenuButton(), "Requirements");
        RequirementsRegionViewModel requirementsRegion = getBrowserTab().getView(RequirementsRegionViewModel.class);
        getBrowserTab().waitUntilComponentIsDisplayed(requirementsRegion.getRequirementsRegion(), WAIT_TIMEOUT);
        return requirementsRegion;
    }

    private TestCaseFormRegionViewModel navigateToCreateTestCasePage(QuickActionBarViewTCViewModel quickActionBar) {
        quickActionBar.getCreateLink().click();
        TestCaseFormRegionViewModel testCaseFormViewModel = getBrowserTab().getView(TestCaseFormRegionViewModel.class);
        getBrowserTab().waitUntilComponentIsDisplayed(testCaseFormViewModel.getTestCaseFormRegion(), WAIT_TIMEOUT);
        return testCaseFormViewModel;
    }

    private TestCaseFormRegionViewModel navigateToCreateTestCasePage(TestCaseSearchViewModel testCaseSearchViewModel) {
        setSelectBoxValue(testCaseSearchViewModel.getContextMenuButton(), "Create Test Case"); // not a selectbox but works the same

        TestCaseFormRegionViewModel testCaseFormViewModel = getBrowserTab().getView(TestCaseFormRegionViewModel.class);
        getBrowserTab().waitUntilComponentIsDisplayed(testCaseFormViewModel.getTestCaseFormRegion(), WAIT_TIMEOUT);
        return testCaseFormViewModel;
    }

    private TestCampaignsRegionViewModel navigateToTestCampaignsPage(TestCaseSearchViewModel testCaseSearchViewModel) {
        setSelectBoxValue(testCaseSearchViewModel.getContextMenuButton(), "Test Campaigns");
        TestCampaignsRegionViewModel testPlansViewModel = getBrowserTab().getView(TestCampaignsRegionViewModel.class);
        getBrowserTab().waitUntilComponentIsDisplayed(testPlansViewModel.getTestCampaignsRegion(), WAIT_LONG_TIMEOUT);
        return testPlansViewModel;
    }

    private TestCaseSearchViewModel navigateToTestCasesPage(TestCampaignsRegionViewModel testCampaignListRegionViewModel) {
        testCampaignListRegionViewModel.getSearch().click();
        TestCaseSearchViewModel testCaseSearchViewModel = getBrowserTab().getView(TestCaseSearchViewModel.class);
        getBrowserTab().waitUntilComponentIsDisplayed(testCaseSearchViewModel.getResultsTable(), WAIT_LONG_TIMEOUT);
        return testCaseSearchViewModel;
    }

    private UserInboxViewModel navigateToUserInboxPage(TopBarViewModel topBarViewModel) {
        getBrowserTab().waitUntilComponentIsDisplayed(topBarViewModel.getUserLink(), WAIT_TIMEOUT);
        topBarViewModel.getUserLink().click();

        try {
            getBrowserTab().waitUntilComponentIsDisplayed(topBarViewModel.getToolTip(), WAIT_TIMEOUT);
            getBrowserTab().waitUntilComponentIsHidden(topBarViewModel.getToolTip(), WAIT_TIMEOUT);
        } catch (WaitTimedOutException e) {
            //didn't appear continue
        }

        getBrowserTab().waitUntilComponentIsDisplayed(topBarViewModel.getInboxLink(), WAIT_TIMEOUT);
        topBarViewModel.getInboxLink().click();

        UserInboxViewModel userInboxViewModel = getBrowserTab().getView(UserInboxViewModel.class);
        getBrowserTab().waitUntilComponentIsDisplayed(userInboxViewModel.getUserInboxRegion(), WAIT_LONG_TIMEOUT);
        return userInboxViewModel;
    }

    private void populateTestCampaignTestCases(TestCampaignEditRegionViewModel testPlanEditViewModel, TestCampaignInfo testCampaignInfo) {
        Set<String> testCaseIds = Sets.newHashSet();
        clearTestCasesFromTestCampaign(0, testPlanEditViewModel, testCaseIds);
        for (TestCampaignItemInfo testCaseInfo : testCampaignInfo.getTestCampaignItems()) {
            testCaseIds.add(testCaseInfo.getTestCase().getTestCaseId());
        }
        addTestCasesToTestCampaign(testPlanEditViewModel, testCaseIds);
    }

    private void populateMultipleTestCampaignTestCases(UiComponent component, List<Criterion> criterions) {
        if (!criterions.isEmpty()) {
            waitForAdvancedSearchBlock(AdvancedSearchBlockViewModel.TEST_CASES_SEARCH_FULL_BLOCK);

            UiComponent searchPanel = component.getDescendantsBySelector(AdvancedSearchBlockViewModel.TEST_CASES_SEARCH_FULL_BLOCK).get(0);
            clearAllCriterions(searchPanel);
            prepopulateDataForAdvancedSearchFromDescendants(searchPanel, criterions);
        }
        component.getDescendantsBySelector(AdvancedSearchBlockViewModel.SEARCH_BUTTON).get(0).click();

        TestCaseSearchWidgetViewModel testCaseSearchWidgetViewModel = getBrowserTab().getView(TestCaseSearchWidgetViewModel.class);
        waitForTableUpdate(testCaseSearchWidgetViewModel, TestCaseSearchWidgetViewModel.SEARCH_RESULTS_TABLE);
        selectTestCampaignTableAllCheckBox(testCaseSearchWidgetViewModel);
    }

    // Recursive method
    private void clearTestCasesFromTestCampaign(int startIndex, TestCampaignEditRegionViewModel testPlanEditViewModel, Set<String> testCaseIds) {
        List<UiComponent> testCaseIdCells = testPlanEditViewModel.getTestCaseIdCells();
        int testCaseIdsSize = testCaseIdCells.size();
        for (int i = startIndex; i < testCaseIdsSize; i++) {
            String testCaseId = testCaseIdCells.get(i).getText();

            if (!testCaseIds.contains(testCaseId)) {
                List<UiComponent> testCaseRemoveIcons = testPlanEditViewModel.getTestCaseRemoveCells();
                testCaseRemoveIcons.get(i).click();

                waitForTableUpdate(testPlanEditViewModel, TestCampaignEditRegionViewModel.TMS_TP_TEST_CASE_TABLE_ID);
                clearTestCasesFromTestCampaign(i, testPlanEditViewModel, testCaseIds);

                break;
            } else {
                testCaseIds.remove(testCaseId);
            }
        }
    }

    private void addTestCasesToTestCampaign(TestCampaignEditRegionViewModel testPlanEditViewModel, Set<String> testCaseIds) {
        if (testCaseIds.isEmpty()) {
            return;
        }

        for (String testCaseId : testCaseIds) {
            testPlanEditViewModel.getTestCaseInput().setText(testCaseId);
            UI.pause(500);
            testPlanEditViewModel.getTestCaseAddButton().click();

            waitForTableUpdate(testPlanEditViewModel, TestCampaignEditRegionViewModel.TMS_TP_TEST_CASE_TABLE_ID);
        }
    }

    private void selectTestCampaignTableAllCheckBox(TestCaseSearchWidgetViewModel testCaseSearchWidgetViewModel) {
        UiComponent table = testCaseSearchWidgetViewModel.getTestCaseTable();
        UiComponent selectAllCheckbox = table.getDescendantsBySelector(TestCaseSearchWidgetViewModel.CHECK_BOX).get(0);
        selectAllCheckbox.focus();
        if (selectAllCheckbox.hasFocus()) {
            selectAllCheckbox.click();
        } else {
            selectAllCheckbox.focus();
            selectAllCheckbox.click();
        }
        testCaseSearchWidgetViewModel.getAddSelectedButton().click();
    }

    private void populateTestCampaignFormFields(TestCampaignEditRegionViewModel testPlanEditViewModel, TestCampaignInfo testCaseInfo, boolean isEdit) {
        if (!isEdit) {
            setNotNullValue(testPlanEditViewModel.getNameInput(), testCaseInfo.getName());
        }
        setNotNullValue(testPlanEditViewModel.getDescriptionTextarea(), testCaseInfo.getDescription());
        setNotNullValue(testPlanEditViewModel.getEnvironmentName(), testCaseInfo.getEnvironment());
    }

    private void populateTestCaseFormFields(TestCaseFormRegionViewModel testCaseFormRegionViewModel, TestCaseInfo testCaseInfo) {
        Set<String> requirementIds = testCaseInfo.getRequirementIds();
        for (String requirementId : requirementIds) {
            testCaseFormRegionViewModel.getRequirementIdTextarea().sendKeys(requirementId);
        }

        setNotNullValue(testCaseFormRegionViewModel.getTestCaseId(), testCaseInfo.getTestCaseId());
        setNotNullValue(testCaseFormRegionViewModel.getTitle(), testCaseInfo.getTitle());
        setNotNullValue(testCaseFormRegionViewModel.getDescription(), testCaseInfo.getDescription());
        setNotNullValue(testCaseFormRegionViewModel.getPreCondition(), testCaseInfo.getPrecondition());
    }

    private void setNotNullValue(TextBox textBox, String value) {
        if (value != null) {
            textBox.setText(value);
        }
    }

    private void populateTestCaseSelectBoxes(TestCaseFormRegionViewModel testCaseFormRegionViewModel, TestCaseInfo testCaseInfo) {
        setSelectBoxValue(testCaseFormRegionViewModel.getProductSelect(), testCaseInfo.getFeature().getProduct().getName());

        getBrowserTab().waitUntil(testCaseFormRegionViewModel.getFeatureSelect(), (d) -> d.isEnabled());
        setSelectBoxValue(testCaseFormRegionViewModel.getFeatureSelect(), testCaseInfo.getFeature().getName());

        getBrowserTab().waitUntil(testCaseFormRegionViewModel.getComponentSelect(), (d) -> d.isEnabled());
        sleep(2); //required to make sure technical component options are available after selecting feature
        setMultiSelectBoxValue(testCaseFormRegionViewModel.getComponentSelect(), testCaseInfo.getTechnicalComponents());

        setSelectBoxValue(testCaseFormRegionViewModel.getTypeSelect(), testCaseInfo.getType());
        setSelectBoxValue(testCaseFormRegionViewModel.getPrioritySelect(), testCaseInfo.getPriority());

        setMultiSelectBoxValue(testCaseFormRegionViewModel.getGroupSelect(), testCaseInfo.getGroups());
        setMultiSelectBoxValue(testCaseFormRegionViewModel.getContextSelect(), testCaseInfo.getContexts());

        setSelectBoxValue(testCaseFormRegionViewModel.getExecutionTypeSelect(), testCaseInfo.getExecutionType());
    }

    private void setAutomationCandidacy(TestCaseFormRegionViewModel testCaseFormRegionViewModel, TestCaseInfo testCaseInfo) {
        if (testCaseFormRegionViewModel.getAutomationCandidateBlock().isDisplayed()) {
            ReferenceDataItem automationCandidate = testCaseInfo.getAutomationCandidate();
            if (automationCandidate.getTitle().equals(automationCandidateYes.getTitle())) {
                testCaseFormRegionViewModel.getAutomationCandidateSwitcher().click();
            }
        }
    }

    private void setIntrusive(TestCaseFormRegionViewModel testCaseFormRegionViewModel, TestCaseInfo testCaseInfo) {
        if (testCaseInfo.isIntrusive()) {
            testCaseFormRegionViewModel.getIntrusiveSwitcher().click();
        }
        Optional<String> optionalIntrusive = Optional.fromNullable(testCaseInfo.getIntrusiveComment());
        if (optionalIntrusive.isPresent()) {
            testCaseFormRegionViewModel.getIntrusiveComment().setText(optionalIntrusive.get());
        }
    }

    private void setSelectBoxValue(UiComponent selectComponent, ReferenceDataItem reference) {
        if (reference == null) {
            return;
        }
        String name = reference.getTitle();
        setSelectBoxValue(selectComponent, name);
    }

    private void setSelectBoxValue(UiComponent selectComponent, String name) {
        getBrowserTab().waitUntil(new GenericPredicate() {
            @Override
            public boolean apply() {
                return selectComponent.isEnabled();
            }
        }, WAIT_LONG_TIMEOUT);
        selectComponent.focus();
        selectComponent.click();

        ViewModel genericView = getBrowserTab().getGenericView();
        UiComponent itemsBox = genericView.getViewComponent(".ebComponentList");
        if (!itemsBox.isDisplayed()) {
            selectComponent.focus();
            selectComponent.click();
            getBrowserTab().waitUntilComponentIsDisplayed(itemsBox, WAIT_LONG_TIMEOUT);
        }
        List<UiComponent> componentItems = genericView.getViewComponents(".ebComponentList .ebComponentList-item", UiComponent.class);
        for (UiComponent componentItem : componentItems) {
            if (componentItem.getText().equals(name)) {
                componentItem.focus();
                componentItem.click();
                break;
            }
        }
    }

    private List<String> getSelectBoxValues(UiComponent selectComponent) {
        // Click on SelectBox to open DropDown list
        selectComponent.click();
        List<String> values = Lists.newArrayList();

        ViewModel genericView = getBrowserTab().getGenericView();
        UiComponent itemsBox = genericView.getViewComponent(".ebComponentList");
        getBrowserTab().waitUntilComponentIsDisplayed(itemsBox, WAIT_TIMEOUT);

        List<UiComponent> componentItems = genericView.getViewComponents(".ebComponentList > div > .ebComponentList-item", UiComponent.class);
        for (UiComponent componentItem : componentItems) {
            values.add(componentItem.getText());
        }
        // Click on SelectBox to close DropDown list
        selectComponent.click();
        return values;
    }

    private void setMultiSelectBoxValue(UiComponent multiSelectComponent, Set<? extends ReferenceDataItem> references) {
        multiSelectComponent.click();
        ComponentModel componentModel = getBrowserTab().getView(ComponentModel.class);
        UiComponent itemsBox = componentModel.getComponentList();
        getBrowserTab().waitUntilComponentIsDisplayed(itemsBox, WAIT_LONG_TIMEOUT);

        List<UiComponent> componentItems = componentModel.getComponentItems();

        componentItems.stream().forEach(componentItem -> {
            if (!itemsBox.isDisplayed()) {
                multiSelectComponent.click();
                getBrowserTab().waitUntilComponentIsDisplayed(itemsBox, WAIT_LONG_TIMEOUT);
            }
            UiComponent itemCheckBox = componentItem.getDescendantsBySelector(".ebCheckbox").get(0);
            UiComponent itemTitle = componentItem.getDescendantsBySelector(".ebCheckbox-label").get(0);

            String itemTitleText = itemTitle.getText();
            boolean isSelected = itemCheckBox.isSelected();
            for (ReferenceDataItem reference : references) {
                if (itemTitleText.equals(reference.getTitle()) && !isSelected) {
                    itemCheckBox.click();
                } else if (!itemTitleText.equals(reference.getTitle()) && isSelected) {
                    itemCheckBox.click();
                }
            }
        });
        // Close multi-select
        multiSelectComponent.click();
    }

    private void setMultiSelectBox(UiComponent multiSelectComponent, Set<String> references) {
        getBrowserTab().waitUntilComponentIsDisplayed(multiSelectComponent, WAIT_TIMEOUT);
        multiSelectComponent.click();
        ViewModel genericView = getBrowserTab().getGenericView();
        UiComponent itemsBox = genericView.getViewComponent(".ebComponentList");
        getBrowserTab().waitUntilComponentIsDisplayed(itemsBox, WAIT_LONG_TIMEOUT);

        List<UiComponent> componentItems = genericView.getViewComponents(".ebComponentList .ebComponentList-item", UiComponent.class);

        for (UiComponent typeSelectItem : componentItems) {
            if (!itemsBox.isDisplayed()) {
                multiSelectComponent.click();
                getBrowserTab().waitUntilComponentIsDisplayed(itemsBox, WAIT_LONG_TIMEOUT);
            }
            CheckBox itemCheckBox = typeSelectItem.getDescendantsBySelector(".ebCheckbox").get(0).as(CheckBox.class);
            UiComponent itemTitle = typeSelectItem.getDescendantsBySelector(".ebCheckbox-label").get(0);

            if (itemCheckBox.isSelected()) {
                itemCheckBox.click();
            }

            String itemTitleText = itemTitle.getText();
            for (String reference : references) {
                if (itemTitleText.equals(reference)) {
                    itemCheckBox.click();
                }
            }
        }
        // Close multi-select
        multiSelectComponent.click();
    }

    private void populateTestStepsAndVerifies(TestCaseFormRegionViewModel testCaseFormRegionViewModel, TestCaseInfo testCaseInfo) {
        UiComponent testStepsHolder = testCaseFormRegionViewModel.getTestStepsHolder();
        List<UiComponent> testSteps = testCaseFormRegionViewModel.getTestSteps();
        int testStepsCount = testSteps.size();
        int index = 0;
        for (UiComponent testStep : testSteps) {
            modifyTestStepInfo(testStep, testCaseInfo.getTestSteps().get(index));
            index++;
        }
        // remove update fields
        List<TestStepInfo> testCases = new LinkedList(testCaseInfo.getTestSteps());
        for (int i = 0; i <= index - 1; i++) {
            testCases.remove(i);
        }
        // add new fields
        for (TestStepInfo testStepInfo : testCases) {
            testCaseFormRegionViewModel.getAddTestStepLink().click();
            getBrowserTab().waitUntil(testStepsHolder, UiComponentPredicates.CHILD_ADDED.withCurrentChildrenCount(testStepsCount++), WAIT_TIMEOUT);
            UiComponent testStep = testStepsHolder.getChildren().get(testStepsCount - 1);
            modifyTestStepInfo(testStep, testStepInfo);
        }
    }

    private void modifyTestStepInfo(UiComponent testStep, TestStepInfo testStepInfo) {
        TextBox titleInput = testStep.getDescendantsBySelector(TestCaseFormRegionViewModel.TEST_STEP_EDIT_TITLE).get(0).as(TextBox.class);
        Link addVerifyLink = testStep.getDescendantsBySelector(TestCaseFormRegionViewModel.TEST_STEP_EDIT_ADDLINK).get(0).as(Link.class);
        UiComponent verifiesHolder = testStep.getDescendantsBySelector(TestCaseFormRegionViewModel.TEST_STEP_EDIT_VERIFIES_LIST).get(0);

        TextBox testStepData = testStep.getDescendantsBySelector(TestCaseFormRegionViewModel.TEST_STEP_DATA_TEXTBOX).get(0).as(TextBox.class);

        setNotNullValue(titleInput, testStepInfo.getName());
        titleInput.sendKeys(Keys.RETURN);
        setNotNullValue(testStepData, testStepInfo.getData());

        populateVerifiesInfo(verifiesHolder, addVerifyLink, testStepInfo.getVerifies());
    }

    private void populateVerifiesInfo(UiComponent verifiesHolder, Link addVerifyLink, List<VerifyStepInfo> verifies) {
        List<UiComponent> oldVerifies = verifiesHolder.getDescendantsBySelector(TestCaseFormRegionViewModel.TEST_STEP_EDIT_VERIFY_EDIT_TITLE);
        int verifiesCount = verifiesHolder.getChildren().size();

        int index = 0;
        for (UiComponent verify : oldVerifies) {
            modifyVerifyInfo(verify, verifies.get(index));
            index++;
        }

        List<VerifyStepInfo> verifyStepInfos = new LinkedList(verifies);
        for (int i = 0; i <= index - 1; i++) {
            verifyStepInfos.remove(i);
        }

        for (VerifyStepInfo verify : verifyStepInfos) {
            addVerifyLink.click();
            getBrowserTab().waitUntil(verifiesHolder, UiComponentPredicates.CHILD_ADDED.withCurrentChildrenCount(verifiesCount++), WAIT_TIMEOUT);

            UiComponent verifyTitle = verifiesHolder.getDescendantsBySelector(TestCaseFormRegionViewModel.TEST_STEP_EDIT_VERIFY_EDIT_TITLE).get(verifiesCount - 1);
            modifyVerifyInfo(verifyTitle, verify);
        }
    }

    private void modifyVerifyInfo(UiComponent verifyTitle, VerifyStepInfo verify) {
        TextBox verifyTitleBox = verifyTitle.as(TextBox.class);
        verifyTitleBox.setText(verify.getName());
        verifyTitleBox.sendKeys(Keys.RETURN);
    }

    private void submitCreateTestCaseForm() {
        QuickActionBarCreateTCViewModel quickActionBarCreateTCViewModel = getBrowserTab().getView(QuickActionBarCreateTCViewModel.class);
        quickActionBarCreateTCViewModel.getCreateActionLink().click();
    }

    private void submitEditTestCaseForm() {
        QuickActionBarEditTCViewModel quickActionBarEditTCViewModel = getBrowserTab().getView(QuickActionBarEditTCViewModel.class);
        quickActionBarEditTCViewModel.getSaveActionLink().click();
    }

    private void clickTestCaseIdInResultsTable(TestCaseInfo testCaseInfo) {
        TableResultsViewModel tableResultsViewModel = getBrowserTab().getView(TableResultsViewModel.class);
        getBrowserTab().waitUntilComponentIsDisplayed(tableResultsViewModel.getTable(), WAIT_TIMEOUT);
        // Cannot click link through normal methods
        List<UiComponent> cells = tableResultsViewModel.getCells();
        for (UiComponent cell : cells) {
            String text = cell.getText();
            if (text.equals(testCaseInfo.getTestCaseId())) {
                cell.getDescendantsBySelector("a").get(0).click();
                break;
            }
        }
    }

    private void deleteSpecificTestCases(TestCampaignInfo testCampaignInfo, UiComponent table) {
        List<String> testCaseIds = testCampaignInfo.getTestCampaignItems()
                .stream()
                .map(item -> item.getTestCase().getTestCaseId())
                .collect(Collectors.toList());

        for (int i = 0; i < testCaseIds.size(); i++) {
            List<UiComponent> tableRows = table.getDescendantsBySelector(TABLE_ROW);
            for (UiComponent row : tableRows) {
                List<UiComponent> cells = row.getDescendantsBySelector("td");
                String testCaseIdFromTable = cells.get(1).getProperty("title");
                if (testCaseIds.contains(testCaseIdFromTable)) {
                    row.getDescendantsBySelector("i").get(1).click();
                }
                break;
            }
        }
    }

    private static boolean isEditAndRemoveBtnHidden(QuickActionBarViewTCViewModel viewModel) {
        return !viewModel.getEditLink().isDisplayed()
                && !viewModel.getRemoveLink().isDisplayed();
    }

    private void populateCreateBugWidget(Map<String, String> bugInfo) {
        CreateDefectViewModel createDefectViewModel = waitForDefectDialog();
        UiComponent content = createDefectViewModel.getCreateDefectContent();

        for (Map.Entry<String, String> info : bugInfo.entrySet()) {
            UiComponent field;
            boolean isInputField = false;

            if ("foundInSprint".equals(info.getKey()) || "deliveredInSprint".equals(info.getKey())) {
                field = content.getDescendantsBySelector(TestCampaignExecutionRegionViewModel.TMS_TE_CREATE_DEFECT
                        + info.getKey() + " .ebInput").get(0);
                isInputField = true;
            } else if ("environment".equals(info.getKey()) || "summary".equals(info.getKey())) {
                field = content.getDescendantsBySelector(TestCampaignExecutionRegionViewModel.TMS_TE_CREATE_DEFECT
                        + info.getKey()).get(0);
            } else {
                field = content.getDescendantsBySelector(TestCampaignExecutionRegionViewModel.TMS_TE_CREATE_DEFECT
                        + info.getKey() + " .ebTextArea").get(0);
            }

            waitForComponent(field);
            field.focus();
            field.sendKeys(info.getValue());
            if (isInputField) {
                field.click();
            } else {
                field.sendKeys(Keys.ENTER);
            }
        }

    }

    private void waitForComponentEnabled(UiComponent uiComponent) {
        getBrowserTab().waitUntil(new GenericPredicate() {
            @Override
            public boolean apply() {
                return uiComponent.isEnabled();
            }
        });
    }

}
