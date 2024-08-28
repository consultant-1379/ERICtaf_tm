define(function () {
    'use strict';

    var Constants = {};

    Constants.urls = {
        APP_NAME: 'tm',
        DEFAULT_PAGE: '',
        TEST_CASES: '/testCases',
        REQUIREMENTS_TREE_PAGE: '/requirements',
        CREATE_TC_PAGE: '/createTC',
        VIEW_TC_PAGE: '/viewTC/',
        VERSION_PAGE: '/version/',
        EDIT_TC_PAGE: '/editTC/',
        COPY_TC_PAGE: '/copyTC/',
        LIST_TEST_PLANS: '/listTestCampaigns',
        VIEW_TEST_PLAN: '/viewTestCampaign/',
        GROUP_TEST_CAMPAIGN: '/testCampaignGroups',
        VIEW_OLD_TEST_PLAN: '/viewTestPlan/',
        CREATE_TEST_PLAN: '/createTestCampaign',
        COPY_TEST_PLAN: '/copyTestCampaign.xml/',
        EDIT_TEST_PLAN: '/editTestCampaign/',
        TEST_PLAN_EXECUTION: '/testCampaignExecution/',
        ID_PARAM: ':id',
        VERSION_PARAM: ':version',
        TEST_CASE_PARAM: '/testCase/',
        JIRA_BROWSE_LINK: 'http://eteamproject.internal.ericsson.com/browse/',
        USER_INBOX: '/inbox',
        IMPORT: '/import',
        ADMIN: '/admin',
        REVIEW_INBOX: '/review',
        CHANGE_LOG: 'http://confluence-nam.lmera.ericsson.se/display/TAF/Changelog'
    };

    Constants.params = {
        REQUIREMENT_ID: 'requirementId',
        PROJECT_ID: 'projectId',
        PRODUCT_NAME: 'productName',
        PRODUCT: 'product',
        FEATURE_NAME: 'featureName',
        COMPONENT_NAME: 'component',
        NAME: 'name',
        ANY: 'any'
    };

    Constants.pages = {
        TEST_CASE_SEARCH: 'testCaseSearch',
        REQUIREMENTS_TREE: 'requirementsTree',
        TEST_CASE_DETAILS: 'testCaseDetails',
        TEST_CASE_EDIT: 'testCaseEdit',
        TEST_CASE_COPY: 'testCaseCopy',
        TEST_CASE_CREATE: 'testCaseCreate',
        TEST_PLAN_LIST: 'testCampaignList',
        TEST_CAMPAIGN_GROUP: 'testCampaignGroup',
        TEST_PLAN_DETAILS: 'testCampaignDetails',
        TEST_PLAN_CREATE: 'testCampaignCreate',
        TEST_PLAN_COPY: 'testCampaignCopy',
        TEST_PLAN_EDIT: 'testCampaignEdit',
        TEST_PLAN_EXECUTION: 'testCampaignExecution',
        CREATE_TEST_EXECUTION: 'createTestExecution',
        USER_INBOX: 'userInbox',
        REVIEW_INBOX: 'reviewInbox',
        IMPORT: 'import',
        ADMIN: 'admin',
        WELCOME: 'welcome'
    };

    Constants.events = {

        /* Test TestCampaigns - pages*/
        SHOW_TEST_PLAN_LIST: 'showListTestCampaigns',
        HIDE_TEST_PLAN_LIST: 'hideListTestCampaigns',
        MARK_CURRENT_TEST_PLAN_LIST: 'markListTestCampaignsCurrent',

        SHOW_TEST_PLAN_DETAILS: 'showViewTestCampaign',
        HIDE_TEST_PLAN_DETAILS: 'hideViewTestCampaign',
        MARK_CURRENT_TEST_PLAN_DETAILS: 'markViewTestCampaignCurrent',

        SHOW_TEST_PLAN_FORM: 'showEditTestForm',
        HIDE_TEST_PLAN_FORM: 'hideEditTestForm',
        MARK_CURRENT_TEST_PLAN_FORM: 'markEditTestFormCurrent',

        /* Test Plans - actions */
        CREATE_TEST_PLAN: 'createTestCampaign',
        DELETE_TEST_PLAN: 'deleteTestCampaign',
        SAVE_TEST_PLAN: 'saveTestCampaign',
        COPY_TEST_PLAN: 'copyTestCampaign.xml',
        LOCK_TEST_PLAN: 'lockTestCampaign',
        SEARCH_TEST_PLANS: 'searchTestCampaigns',
        SEARCH_TEST_CAMPAIGN_GROUP: 'searchTestCampaignGroups',

        TEST_PLAN_LOCKED: 'testCampaignLocked',

        /* Test Execution - pages */
        SHOW_TEST_PLAN_EXECUTION: 'showTestCampaignExecution',
        HIDE_TEST_PLAN_EXECUTION: 'hideTestCampaignExecution',
        MARK_CURRENT_TEST_PLAN_EXECUTION: 'markTestCampaignExecutionCurrent',

        SHOW_CREATE_TEST_EXECUTION: 'showCreateTestExecution',
        HIDE_CREATE_TEST_EXECUTION: 'hideCreateTestExecution',
        MARK_CURRENT_CREATE_TEST_EXECUTION: 'markCreateTestExecutionCurrent',

        /* Test Execution - actions */
        SAVE_TEST_EXECUTION_RESULT: 'saveTestExecutionResult',
        REFRESH_TEST_EXECUTIONS: 'refreshTestExecutions',

        /* Requirements Tree - pages */
        SHOW_REQUIREMENTS_CONTENT: 'showRequirementsContent',
        HIDE_REQUIREMENTS_CONTENT: 'hideRequirementsContent',
        MARK_REQUIREMENTS_CONTENT_CURRENT: 'markRequirementsContentCurrent',

        /* Test Cases - pages */
        SHOW_CONTENT: 'showContent',
        HIDE_CONTENT: 'hideContent',
        MARK_CONTENT_CURRENT: 'markContentCurrent',

        SHOW_VIEW_TEST_CASE: 'showViewTestCase',
        HIDE_VIEW_TEST_CASE: 'hideViewTestCase',
        MARK_VIEW_TEST_CASE_CURRENT: 'markViewTestCaseCurrent',

        SHOW_VIEW_TEST_CASE_IMPORT: 'showViewTestCaseImport',
        HIDE_VIEW_TEST_CASE_IMPORT: 'hideViewTestCaseImport',
        MARK_VIEW_TEST_CASE_IMPORT_CURRENT: 'markViewTestCaseImportCurrent',

        SHOW_TEST_CASE_FORM: 'showTestCaseForm',
        HIDE_TEST_CASE_FORM: 'hideTestCaseForm',
        MARK_TEST_CASE_FORM_CURRENT: 'markTestCaseFormCurrent',

        /* Test Cases - actions */
        TEST_CASE_SAVE_REQUEST: 'saveTestCase',
        TEST_CASE_CREATE_REQUEST: 'createTestCase',
        TEST_CASE_COPY_REQUEST: 'copyTestCase',
        TEST_CASE_REMOVE_REQUEST: 'removeTestCase',
        SHOW_ASSOCIATED_TEST_PLANS_REQUEST: 'showAssociatedTestCampaigns',
        SHOW_ASSOCIATED_COMMENTS_REQUEST: 'showAssociatedComments',
        TEST_CASE_ATTACH_TO_TEST_CAMPAIGN_REQUEST: 'attachTestCaseToTestCampaign',
        TEST_CASE_UPDATE_COMMENTS_REQUEST: 'updateTestCaseComments',
        SEND_FOR_REVIEW_REQUEST: 'sendForReview',
        CANCEL_REVIEW_REQUEST: 'cancelReviewRequest',
        TEST_CASE_APPROVE_REQUEST: 'testCaseApproveRequest',
        TEST_CASE_UPDATE_VERSIONS: 'testCaseUpdateVersions',

        CLEAR_SEARCH_INPUT: 'clearSearchInput',
        REPLACE_SEARCH_INPUT: 'replaceSearchInput',
        SHOW_SEARCH: 'showSearch',
        HIDE_SEARCH: 'hideSearch',
        SHOW_BAR_FOR_ADVANCED_SEARCH: 'showBarForAdvancedSearch',
        HIDE_BAR_FOR_ADVANCED_SEARCH: 'hideBarForAdvancedSearch',

        TEST_CASE_COMMENTS_ON_ID_FETCHED: 'testCaseCommentsOnIdFetched',
        TEST_CASE_STATUS: 'testCaseStatus',
        TEST_CASE_REVIEW_GROUP_USERS: 'testCaseReviewGroupUsers',

        SUBSCRIBE_TO_TEST_CASE: 'subscribeToTestCase',
        UNSUBSCRIBE_FROM_TEST_CASE: 'unsubscribeFromTestCase',
        USER_IS_SUBSCRIBED_TO_TEST_CASE: 'userIsSubscribedToTestCase',
        USER_IS_NOT_SUBSCRIBED_TO_TEST_CASE: 'userIsNotSubscribedToTestCase',

        /* User inbox */
        SHOW_USER_INBOX: 'showUserInbox',
        HIDE_USER_INBOX: 'hideUserInbox',
        MARK_CURRENT_USER_INBOX: 'markCurrentUserInbox',

        /* User inbox */
        SHOW_REVIEW_INBOX: 'showReviewInbox',
        HIDE_REVIEW_INBOX: 'hideReviewInbox',
        MARK_CURRENT_REVIEW_INBOX: 'markCurrentReviewInbox',

        SHOW_WELCOME: 'showWelcome',
        HIDE_WELCOME: 'hideWelcome',
        MARK_CURRENT_WELCOME: 'markCurrentWelcome',

        /* Other */
        REFERENCES_RECEIVED: 'referencesReceived',

        AUTHENTICATION_REQUIRED: 'authenticationRequired',

        NOTIFICATION: 'notification',

        CHANGE_HEADING_TITLE: 'changeHeadingTitle',
        SEARCH_BY_VALUE: 'searchByValue',

        SHOW_ERROR_BLOCK: 'showErrorBlock',
        HIDE_ERROR_BLOCK: 'hideErrorBlock',

        WINDOW_RESIZE: 'windowResize',

        UPDATE_URL_WITH_SEARCH: 'updateUrlWithSearchQuery',
        UPDATE_SELECTED_PROJECT: 'updateSelectedProject',
        UPDATE_SELECTED_PRODUCT: 'updateSelectedProduct',
        UPDATE_USER_PROFILE_WITH_PROJECT: 'updateUserProfileWithProject',

        PROJECT_CHANGED: 'projectChanged',
        PRODUCT_CHANGED: 'productChanged',

        JIRA_DEFECT_CREATED: 'jiraDefectCreated',

        CONTEXT_MENU_CHANGE: 'contextMenuChange',

        PRODUCT_FILTER_CHANGED: 'productFilterChanged',

        TEST_CAMPAIGNS_FILTER_CHANGED: 'testCampaignsFilterChanged',
        TEST_CASE_SEARCH_FILTER_CHANGED: 'testCaseSearchFilterChanged',

        SHOW_VIEW_ADMIN: 'showViewAdmin',
        HIDE_VIEW_ADMIN: 'hideViewAdmin',
        MARK_VIEW_ADMIN_CURRENT: 'markViewAdminCurrent',

        TEST_CAMPAIGN_FORM_FILTER_LOADED: 'testCampaignFormFilterLoaded',
        TEST_CAMPAIGN_GROUP_CREATE_FOLDER: 'testCampaignGroupCreateFolder',
        TEST_CAMPAIGN_GROUP_CHANGE_PRODUCT: 'testCampaignGroupChangeProduct',

        SAVE_SAVED_SEARCH: 'saveTestCaseSearch',
        UPDATE_SAVED_SEARCH: 'updateSavedSearch',
        RELOAD_USER_PROFILE: 'reloadUserProfile',

        SHOW_GROUP_TEST_CAMPAIGN: 'showTestCampaignGroup',
        HIDE_GROUP_TEST_CAMPAIGN: 'hideTestCampaignGroup',
        MARK_CURRENT_GROUP_TEST_CAMPAIGN: 'markCurrentTestCampaignGroup'
    };

    Constants.breadcrumbs = {
        TEST_PLANS: 'Test Campaigns',
        TEST_PLAN_DETAILS: 'Test Campaign Details',
        TEST_PLAN_EDIT: 'Test Campaign Edit',
        TEST_PLAN_EXECUTION: 'Test Campaign Execution',
        TEST_CASE_EXECUTION: 'Test Case Execution',
        TEST_EXECUTION: 'Test Execution',
        TEST_CASE_EDIT: 'Edit Test Case',
        TEST_CASE_DETAILS: 'Test Case Details'
    };

    Constants.color = {
        GREEN_40: '#d0e3a2',
        GREEN: '#89ba17',
        WHITE: '#ffffff'

    };

    Constants.executionStatusMap = {
        '': {color: '#adadad', icon: 'cellGray', title: 'Not started'}, // Not started for NULL
        1: {color: '#adadad', icon: 'cellGray'},      // Not started
        2: {color: '#89ba17', icon: 'tick'},          // Pass
        3: {color: '#f08a00', icon: 'warningOrange'}, // Passed with exception
        4: {color: '#e32119', icon: 'invalid'},       // Fail
        5: {color: '#00a9d4', icon: 'clock'},         // WIP
        6: {color: '#000000', icon: 'cellRed'},        // Blocked
        7: {color: '#0066b3', icon: 'eyeLine'},        // N/A
        8: {color: '#7b0663', icon: 'obsolete'}        // Not executed
    };

    Constants.report = {
        TestExecution: 'TestExecution',
        TestCase: 'TestCase'
    };

    Constants.executionLabels = {
        notApplicable: 'N/A',
        notExecuted: 'Not executed',
        blocked: 'Blocked',
        workInProgress: 'Work in Progress',
        notStarted: 'Not Started',
        passed: 'Passed',
        passedWithException: 'Passed with Exception',
        failed: 'Failed'
    };

    return Constants;

});
