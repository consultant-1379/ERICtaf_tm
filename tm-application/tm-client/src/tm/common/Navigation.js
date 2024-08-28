define([
    './Constants'
], function (Constants) {
    'use strict';

    var requirementsTreeUrl = Constants.urls.APP_NAME + Constants.urls.REQUIREMENTS_TREE_PAGE,
        testCaseViewUrl = Constants.urls.APP_NAME + Constants.urls.VIEW_TC_PAGE + Constants.urls.ID_PARAM,
        testCaseVersionUrl = Constants.urls.APP_NAME + Constants.urls.VIEW_TC_PAGE + Constants.urls.ID_PARAM + Constants.urls.VERSION_PAGE + Constants.urls.VERSION_PARAM,
        testCaseCreateUrl = Constants.urls.APP_NAME + Constants.urls.CREATE_TC_PAGE,
        testCaseEditUrl = Constants.urls.APP_NAME + Constants.urls.EDIT_TC_PAGE + Constants.urls.ID_PARAM,
        testCaseCopyUrl = Constants.urls.APP_NAME + Constants.urls.COPY_TC_PAGE + Constants.urls.ID_PARAM + Constants.urls.VERSION_PAGE + Constants.urls.VERSION_PARAM,
        testPlansListUrl = Constants.urls.APP_NAME + Constants.urls.LIST_TEST_PLANS,
        testPlanViewUrl = Constants.urls.APP_NAME + Constants.urls.VIEW_TEST_PLAN + Constants.urls.ID_PARAM,
        testPlanCreateUrl = Constants.urls.APP_NAME + Constants.urls.CREATE_TEST_PLAN,
        testPlanCopyUrl = Constants.urls.APP_NAME + Constants.urls.COPY_TEST_PLAN + Constants.urls.ID_PARAM,
        testPlanEditUrl = Constants.urls.APP_NAME + Constants.urls.EDIT_TEST_PLAN + Constants.urls.ID_PARAM,
        testPlanExecutionViewUrl = Constants.urls.APP_NAME + Constants.urls.TEST_PLAN_EXECUTION + Constants.urls.ID_PARAM,
        testExecutionCreateUrl = Constants.urls.APP_NAME + Constants.urls.TEST_PLAN_EXECUTION + Constants.urls.ID_PARAM + Constants.urls.TEST_CASE_PARAM + Constants.urls.ID_PARAM,
        userInboxUrl = Constants.urls.APP_NAME + Constants.urls.USER_INBOX,
        welcomeUrl = Constants.urls.APP_NAME + Constants.urls.DEFAULT_PAGE,
        testCaseSearchUrl = Constants.urls.APP_NAME + Constants.urls.TEST_CASES,
        changeLogUrl = Constants.urls.CHANGE_LOG,
        importUrl = Constants.urls.APP_NAME + Constants.urls.IMPORT,
        testCampaignGroupUrl = Constants.urls.APP_NAME + Constants.urls.GROUP_TEST_CAMPAIGN;

    var Navigation = {

        TMS_LOGIN_LINK: '/tm-server/api/login',
        TEST_CASE_SUBSCRIPTION_LINK: '/tm-server/api/test-cases/subscription',

        // Common urls
        goToPreviousPage: function (e) {
            if (e) {
                e.preventDefault();
            }
            return history.back();
        },

        navigateTo: function (url) {
            location.hash = url;
        },

        replaceUrlWith: function (url) {
            location.replace('#' + url);
        },

        // Requirements Tree
        getRequirementsTreeUrl: function () {
            return requirementsTreeUrl;
        },

        getRequirementsTreeUrlWithParams: function (projectId, requirementId) {
            var queryParams = [],
                pageUrl = Navigation.getRequirementsTreeUrl();
            if (projectId) {
                queryParams.push(Constants.params.PROJECT_ID + '=' + projectId);
            }
            if (requirementId) {
                queryParams.push(Constants.params.REQUIREMENT_ID + '=' + requirementId);
            }
            if (queryParams.length > 0) {
                pageUrl += '/?' + queryParams.join('&');
            }
            return pageUrl;
        },

        // Test Cases
        getTestCasesListUrl: function () {
            return testCaseSearchUrl;
        },

        getTestCaseListUrlWithParams: function (isAdvancedSearch, searchQuery) {
            var pageUrl = Navigation.getTestCasesListUrl();
            if (isAdvancedSearch) {
                pageUrl += '/search';
            }
            if (searchQuery !== '') {
                pageUrl += '/?' + searchQuery;
            }
            return pageUrl;
        },

        getTestCaseDetailsUrl: function (testCaseId) {
            return replaceId(testCaseViewUrl, testCaseId);
        },

        getTestCaseVersionUrl: function (testCaseId, version) {
            return testCaseVersionUrl
                .replace(Constants.urls.ID_PARAM, testCaseId)
                .replace(Constants.urls.VERSION_PARAM, version);
        },

        getTestCaseCreateUrl: function () {
            return testCaseCreateUrl;
        },

        getTestCampaignGroupsUrl: function () {
            return testCampaignGroupUrl;
        },

        getTestCaseCreateWithIdUrl: function (testCaseId) {
            return replaceId(testCaseCreateUrl, testCaseId);
        },

        getTestCaseEditUrl: function (testCaseId) {
            return replaceId(testCaseEditUrl, testCaseId);
        },

        getTestCaseCopyUrl: function (testCaseId, version) {
            return testCaseCopyUrl
                .replace(Constants.urls.ID_PARAM, testCaseId)
                .replace(Constants.urls.VERSION_PARAM, version);
        },

        // Test TestCampaigns
        getTestPlansListUrl: function () {
            return testPlansListUrl;
        },

        getTestPlansListUrlWithParams: function (projectId) {
            var pageUrl = Navigation.getTestPlansListUrl();
            if (projectId) {
                pageUrl += '/?' + Constants.params.PROJECT_ID + '=' + projectId;
            }
            return pageUrl;
        },

        getTestCampaignGroupsUrlWithParams: function (query) {
            var pageUrl = Navigation.getTestCampaignGroupsUrl();

            if (query) {
                pageUrl += '/?' + query;
            }
            return pageUrl;
        },

        getTestPlanDetailsUrl: function (testPlanId) {
            return replaceId(testPlanViewUrl, testPlanId);
        },

        getTestPlanCreateUrl: function () {
            return testPlanCreateUrl;
        },

        getTestPlanCopyUrl: function (testPlanId) {
            return replaceId(testPlanCopyUrl, testPlanId);
        },

        getTestPlanEditUrl: function (testPlanId) {
            return replaceId(testPlanEditUrl, testPlanId);
        },

        // Test Executions
        getTestPlanExecutionUrl: function (testPlanId) {
            return replaceId(testPlanExecutionViewUrl, testPlanId);
        },

        getTestExecutionCreateUrl: function (testPlanId, testCaseId) {
            return replaceId(testExecutionCreateUrl, testPlanId, testCaseId);
        },

        getUserInboxUrl: function () {
            return userInboxUrl;
        },

        getWelcomeUrl: function () {
            return welcomeUrl;
        },

        getChangeLogUrl: function () {
            return changeLogUrl;
        },

        getImportUrl: function () {
            return importUrl;
        }

    };

    return Navigation;

    function replaceId (url, id1, id2) {
        if (id1) {
            url = url.replace(Constants.urls.ID_PARAM, id1);
        }
        if (id2) {
            url = url.replace(Constants.urls.ID_PARAM, id2);
        }
        return url;
    }

});
