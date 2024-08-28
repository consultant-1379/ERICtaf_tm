define([
    'jscore/core',
    'jscore/ext/utils/base/underscore',
    '../Constants',
    './ActionBarRegion',
    '../../ui/testCases/search/TestCaseSearchBarRegion/TestCaseSearchBarRegion',
    '../../ui/requirements/view/RequirementsBarRegion/RequirementsBarRegion',
    '../../ui/testCases/details/TestCaseDetailsBarRegion/TestCaseDetailsBarRegion',
    '../../ui/testCases/edit/TestCaseCreateBarRegion/TestCaseCreateBarRegion',
    '../../ui/testCases/edit/TestCaseEditBarRegion/TestCaseEditBarRegion',
    '../../ui/testCases/edit/TestCaseCopyBarRegion/TestCaseCopyBarRegion',
    '../../ui/testCampaigns/list/TestCampaignListBarRegion/TestCampaignListBarRegion',
    '../../ui/testCampaigns/details/TestCampaignDetailsBarRegion/TestCampaignDetailsBarRegion',
    '../../ui/testCampaigns/edit/TestCampaignCreateBarRegion/TestCampaignCreateBarRegion',
    '../../ui/testCampaigns/edit/TestCampaignCopyBarRegion/TestCampaignCopyBarRegion',
    '../../ui/testCampaigns/edit/TestCampaignEditBarRegion/TestCampaignEditBarRegion',
    '../../ui/testExecutions/view/TestCampaignExecutionBarRegion/TestCampaignExecutionBarRegion',
    '../../ui/testExecutions/create/CreateTestExecutionBarRegion/CreateTestExecutionBarRegion',
    '../../ui/userInbox/view/UserInboxBarRegion/UserInboxBarRegion',
    '../../ui/testCases/import/TestCaseImportBarRegion/TestCaseImportBarRegion',
    '../../ui/admin/AdminBarRegion/AdminBarRegion',
    '../../ui/reviewInbox/view/ReviewInboxBarRegion/ReviewInboxBarRegion',
    '../../ui/testCampaigns/group/TestCampaignGroupBarRegion/TestCampaignGroupBarRegion'
], function (core, _, Constants, ActionBar, TestCaseSearchBar, RequirementsBarRegion, TestCaseDetailsBar,
             TestCaseCreateBar, TestCaseEditBar, TestCaseCopyBar, TestPlanListBar, TestPlanDetailsBar, TestPlanCreateBar,
             TestPlanCopyBar, TestPlanEditBar, TestPlanExecutionBar, CreateTestExecutionBar, UserInboxBarRegion,
             TestCaseImportBarRegion, AdminBarRegion, ReviewInboxBarRegion, TestCampaignGroupBarRegion) {
    'use strict';

    var ActionBarRegionFactory = function (context) {
        this.context = context;
    };

    var actionBars = {};
    actionBars[Constants.pages.TEST_CASE_SEARCH] = TestCaseSearchBar;
    actionBars[Constants.pages.REQUIREMENTS_TREE] = RequirementsBarRegion;
    actionBars[Constants.pages.TEST_CASE_DETAILS] = TestCaseDetailsBar;
    actionBars[Constants.pages.TEST_CASE_CREATE] = TestCaseCreateBar;
    actionBars[Constants.pages.TEST_CASE_EDIT] = TestCaseEditBar;
    actionBars[Constants.pages.TEST_CASE_COPY] = TestCaseCopyBar;
    actionBars[Constants.pages.TEST_PLAN_LIST] = TestPlanListBar;
    actionBars[Constants.pages.TEST_PLAN_DETAILS] = TestPlanDetailsBar;
    actionBars[Constants.pages.TEST_PLAN_CREATE] = TestPlanCreateBar;
    actionBars[Constants.pages.TEST_PLAN_COPY] = TestPlanCopyBar;
    actionBars[Constants.pages.TEST_PLAN_EDIT] = TestPlanEditBar;
    actionBars[Constants.pages.TEST_PLAN_EXECUTION] = TestPlanExecutionBar;
    actionBars[Constants.pages.CREATE_TEST_EXECUTION] = CreateTestExecutionBar;
    actionBars[Constants.pages.USER_INBOX] = UserInboxBarRegion;
    actionBars[Constants.pages.IMPORT] = TestCaseImportBarRegion;
    actionBars[Constants.pages.ADMIN] = AdminBarRegion;
    actionBars[Constants.pages.REVIEW_INBOX] = ReviewInboxBarRegion;
    actionBars[Constants.pages.TEST_CAMPAIGN_GROUP] = TestCampaignGroupBarRegion;

    ActionBarRegionFactory.prototype.create = function (pageId, opts) {
        var ActionBar = actionBars[pageId];
        if (ActionBar != null) {
            return new ActionBar(_.extend({
                context: this.context
            }, opts));
        }
        return null;
    };

    return ActionBarRegionFactory;

});
