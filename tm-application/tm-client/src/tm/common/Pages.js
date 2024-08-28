define([
    './Constants',
    'widgets/Breadcrumb',
    '../ui/testCases/search/TestCaseSearchContentRegion/TestCaseSearchContentRegion',
    '../ui/requirements/view/RequirementsContentRegion/RequirementsContentRegion',
    '../ui/testCases/details/TestCaseDetailsContentRegion/TestCaseDetailsContentRegion',
    '../ui/testCases/edit/TestCaseEditContentRegion/TestCaseEditContentRegion',
    '../ui/testCampaigns/list/TestCampaignListContentRegion/TestCampaignListContentRegion',
    '../ui/testCampaigns/details/TestCampaignDetailsContentRegion/TestCampaignDetailsContentRegion',
    '../ui/testCampaigns/edit/TestCampaignEditContentRegion/TestCampaignEditContentRegion',
    '../ui/testExecutions/view/TestCampaignExecutionContentRegion/TestCampaignExecutionContentRegion',
    '../ui/testExecutions/create/CreateTestExecutionContentRegion/CreateTestExecutionContentRegion',
    '../ui/userInbox/view/UserInboxContentRegion/UserInboxContentRegion',
    '../ui/welcome/view/WelcomeContentRegion/WelcomeContentRegion',
    '../ui/testCases/import/TestCaseImportContentRegion/TestCaseImportContentRegion',
    '../ui/admin/AdminContentRegion/AdminContentRegion',
    '../ui/reviewInbox/view/ReviewInboxContentRegion/ReviewInboxContentRegion',
    '../ui/testCampaigns/group/TestCampaignGroupContentRegion/TestCampaignGroupContentRegion'
], function (Constants, Breadcrumb, TestCaseSearchContentRegion, RequirementsContentRegion,
             TestCaseDetailsContentRegion, TestCaseEditContentRegion, TestPlanListContentRegion,
             TestPlanDetailsContentRegion, TestPlanFormContentRegion, TestPlanExecutionContentRegion,
             CreateTestExecutionContentRegion, UserInboxContentRegion, WelcomeContentRegion,
             TestCaseImportContentRegion, AdminContentRegion, ReviewInboxContentRegion, TestCampaignGroupContentRegion) {
    /*jshint validthis:true*/
    'use strict';

    var testCaseSearchUrl = Constants.urls.APP_NAME + Constants.urls.TEST_CASES,
        requirementsTreeUrl = Constants.urls.APP_NAME + Constants.urls.REQUIREMENTS_TREE_PAGE,
        viewTestCaseUrl = Constants.urls.APP_NAME + Constants.urls.VIEW_TC_PAGE + Constants.urls.ID_PARAM,
        createTestCaseUrl = Constants.urls.APP_NAME + Constants.urls.CREATE_TC_PAGE,
        editTestCaseUrl = Constants.urls.APP_NAME + Constants.urls.EDIT_TC_PAGE + Constants.urls.ID_PARAM,
        copyTestCaseUrl = Constants.urls.APP_NAME + Constants.urls.COPY_TC_PAGE + Constants.urls.ID_PARAM,
        listTestPlansUrl = Constants.urls.APP_NAME + Constants.urls.LIST_TEST_PLANS,
        viewTestPlanUrl = Constants.urls.APP_NAME + Constants.urls.VIEW_TEST_PLAN + Constants.urls.ID_PARAM,
        createTestPlanUrl = Constants.urls.APP_NAME + Constants.urls.CREATE_TEST_PLAN,
        copyTestPlanUrl = Constants.urls.APP_NAME + Constants.urls.COPY_TEST_PLAN + Constants.urls.ID_PARAM,
        editTestPlanUrl = Constants.urls.APP_NAME + Constants.urls.EDIT_TEST_PLAN + Constants.urls.ID_PARAM,
        testPlanExecutionUrl = Constants.urls.APP_NAME + Constants.urls.TEST_PLAN_EXECUTION + Constants.urls.ID_PARAM,
        createTestExecutionUrl = Constants.urls.APP_NAME + Constants.urls.TEST_PLAN_EXECUTION + Constants.urls.ID_PARAM,
        userInboxUrl = Constants.urls.APP_NAME + Constants.urls.USER_INBOX,
        importUrl = Constants.urls.APP_NAME + Constants.urls.IMPORT,
        adminUrl = Constants.urls.APP_NAME + Constants.urls.ADMIN,
        reviewInboxUrl = Constants.urls.APP_NAME + Constants.urls.REVIEW_INBOX,
        testCampaignGroupUrl = Constants.urls.APP_NAME + Constants.urls.GROUP_TEST_CAMPAIGN;

    return function (appContext, references) {
        this.appContext = appContext;
        this.references = references;

        this.testCaseId = getTestCaseId();
        this.testPlanId = getTestPlanId();

        this.pages = {};

        this.defaultBreadcrumbs = [
            {
                name: 'Test Management System',
                url: '#' + Constants.urls.APP_NAME
            }
        ];

        prepareBreadCrumbs.call(this);
        prepareAdmin.call(this);
        prepareWelcome.call(this);
        prepareTestCampaign.call(this);
        prepareTestExecutions.call(this);
        prepareTestCases.call(this);
        prepareInbox.call(this);
        prepareImport.call(this);
        prepareRequirements.call(this);
        prepareReview.call(this);
        prepareGroupTestCampaigns.call(this);
    };

    function getTestCaseId () {
        var locationHash = location.hash,
            viewTestCaseUrl = '#' + Constants.urls.APP_NAME + Constants.urls.VIEW_TC_PAGE,
            editTestCaseUrl = '#' + Constants.urls.APP_NAME + Constants.urls.EDIT_TC_PAGE,
            testPlanExecutionUrl = '#' + Constants.urls.APP_NAME + Constants.urls.TEST_PLAN_EXECUTION;

        if (locationHash.indexOf(viewTestCaseUrl) > -1) {
            return locationHash.replace(viewTestCaseUrl, '');
        }
        if (locationHash.indexOf(editTestCaseUrl) > -1) {
            return locationHash.replace(editTestCaseUrl, '');
        }
        if (locationHash.indexOf(testPlanExecutionUrl) > -1 && locationHash.indexOf(
                Constants.urls.TEST_CASE_PARAM) > -1) {
            var idsString = locationHash.replace(testPlanExecutionUrl, '');
            return idsString.split(Constants.urls.TEST_CASE_PARAM)[1];
        }
        return '';
    }

    function getTestPlanId () {
        var locationHash = location.hash,
            viewTestPlanUrl = '#' + Constants.urls.APP_NAME + Constants.urls.VIEW_TEST_PLAN,
            editTestPlanUrl = '#' + Constants.urls.APP_NAME + Constants.urls.EDIT_TEST_PLAN,
            testPlanExecutionUrl = '#' + Constants.urls.APP_NAME + Constants.urls.TEST_PLAN_EXECUTION;

        if (locationHash.indexOf(viewTestPlanUrl) > -1) {
            return locationHash.replace(viewTestPlanUrl, '');
        }
        if (locationHash.indexOf(editTestPlanUrl) > -1) {
            return locationHash.replace(editTestPlanUrl, '');
        }
        if (locationHash.indexOf(testPlanExecutionUrl) > -1) {
            var potentialId = locationHash.replace(testPlanExecutionUrl, '');
            if (potentialId.indexOf(Constants.urls.TEST_CASE_PARAM) > -1) {
                return potentialId.split(Constants.urls.TEST_CASE_PARAM)[0];
            }
            return potentialId;
        }
        return '';
    }

    function prepareBreadCrumbs () {
        this.testCaseSearchBreadcrumbs = this.defaultBreadcrumbs.slice(0);
        this.testCaseSearchBreadcrumbs.push({name: 'Test Cases', url: '#' + testCaseSearchUrl});

        this.requirementsBreadcrumbs = this.defaultBreadcrumbs.slice(0);
        this.requirementsBreadcrumbs.push({name: 'Requirements', url: '#' + requirementsTreeUrl});

        this.viewTestCaseBreadcrumbs = this.defaultBreadcrumbs.slice(0);
        this.viewTestCaseBreadcrumbs.push(
            {name: 'Test Case Details', url: '#' + viewTestCaseUrl.replace(Constants.urls.ID_PARAM, this.testCaseId)});

        this.createTestCaseBreadcrumbs = this.defaultBreadcrumbs.slice(0);
        this.createTestCaseBreadcrumbs.push({name: 'Create Test Case', url: '#' + createTestCaseUrl});

        this.listTestPlansBreadcrumbs = this.defaultBreadcrumbs.slice(0);
        this.listTestPlansBreadcrumbs.push({name: 'Test Campaigns', url: '#' + listTestPlansUrl});

        this.editTestCaseBreadcrumbs = this.viewTestCaseBreadcrumbs.slice(0);
        this.editTestCaseBreadcrumbs.push(
            {name: 'Edit Test Case', url: '#' + editTestCaseUrl.replace(Constants.urls.ID_PARAM, this.testCaseId)});

        this.copyTestCaseBreadcrumbs = this.viewTestCaseBreadcrumbs.slice(0);
        this.copyTestCaseBreadcrumbs.push(
            {name: 'Copy Test Case', url: '#' + copyTestCaseUrl.replace(Constants.urls.ID_PARAM, this.testCaseId)});

        this.viewTestPlanBreadcrumbs = this.listTestPlansBreadcrumbs.slice(0);
        this.viewTestPlanBreadcrumbs.push({
            name: 'Test Campaign Details',
            url: '#' + viewTestPlanUrl.replace(Constants.urls.ID_PARAM, this.testPlanId)
        });

        this.createTestPlanBreadcrumbs = this.listTestPlansBreadcrumbs.slice(0);
        this.createTestPlanBreadcrumbs.push({name: 'Test Campaign Create', url: '#' + createTestPlanUrl});

        this.copyTestPlanBreadcrumbs = this.viewTestPlanBreadcrumbs.slice(0);
        this.copyTestPlanBreadcrumbs.push({name: 'Test Campaign Copy', url: '#' + copyTestPlanUrl});

        this.editTestPlanBreadcrumbs = this.viewTestPlanBreadcrumbs.slice(0);
        this.editTestPlanBreadcrumbs.push(
            {name: 'Test Campaign Edit', url: '#' + editTestPlanUrl.replace(Constants.urls.ID_PARAM, this.testPlanId)});

        this.testPlanExecutionBreadcrumbs = this.viewTestPlanBreadcrumbs.slice(0);
        this.testPlanExecutionBreadcrumbs.push({
            name: 'Test Campaign Execution',
            url: '#' + testPlanExecutionUrl.replace(Constants.urls.ID_PARAM, this.testPlanId)
        });

        this.createTestExecutionBreadcrumbs = this.testPlanExecutionBreadcrumbs.slice(0);
        this.createTestExecutionBreadcrumbs.push({
            name: 'Test Execution',
            url: '#' + createTestExecutionUrl.replace(Constants.urls.ID_PARAM,
                this.testPlanId) + Constants.urls.TEST_CASE_PARAM + this.testCaseId
        });

        this.userInboxBreadcrumbs = this.defaultBreadcrumbs.slice(0);
        this.userInboxBreadcrumbs.push({name: 'Inbox', url: '#' + userInboxUrl});

        this.reviewInboxBreadcrumbs = this.defaultBreadcrumbs.slice(0);
        this.reviewInboxBreadcrumbs.push({name: 'Review', url: '#' + reviewInboxUrl});

        this.importBreadcrumbs = this.defaultBreadcrumbs.slice(0);
        this.importBreadcrumbs.push({name: 'Import', url: '#' + importUrl});

        this.welcomeBreadcrumbs = this.defaultBreadcrumbs.slice(0);

        this.adminBreadcrumbs = this.defaultBreadcrumbs.slice(0);
        this.adminBreadcrumbs.push({name: 'Admin', url: '#' + adminUrl});

        this.groupTestCampaignBreadcrumbs = this.defaultBreadcrumbs.slice(0);
        this.groupTestCampaignBreadcrumbs.push({name: 'Test Campaign Groups', url: '#' + testCampaignGroupUrl});
    }

    function prepareTestCampaign () {
        var testPlanFormRegion = new TestPlanFormContentRegion({
            context: this.appContext,
            references: this.references
        });

        this.pages[Constants.pages.TEST_PLAN_LIST] = {
            id: Constants.pages.TEST_PLAN_LIST,
            isAdded: false,
            actionBar: null,
            title: 'Test Campaigns',
            url: listTestPlansUrl,
            events: {
                show: Constants.events.SHOW_TEST_PLAN_LIST,
                hide: Constants.events.HIDE_TEST_PLAN_LIST,
                current: Constants.events.MARK_CURRENT_TEST_PLAN_LIST
            },
            breadcrumb: new Breadcrumb({
                data: this.listTestPlansBreadcrumbs
            }),
            content: new TestPlanListContentRegion({
                context: this.appContext
            })
        };

        this.pages[Constants.pages.TEST_PLAN_DETAILS] = {
            id: Constants.pages.TEST_PLAN_DETAILS,
            isAdded: false,
            actionBar: null,
            title: 'Test Campaign',
            url: viewTestPlanUrl,
            events: {
                show: Constants.events.SHOW_TEST_PLAN_DETAILS,
                hide: Constants.events.HIDE_TEST_PLAN_DETAILS,
                current: Constants.events.MARK_CURRENT_TEST_PLAN_DETAILS
            },
            breadcrumb: new Breadcrumb({
                data: this.viewTestPlanBreadcrumbs
            }),
            content: new TestPlanDetailsContentRegion({
                context: this.appContext
            })
        };

        this.pages[Constants.pages.TEST_PLAN_CREATE] = {
            id: Constants.pages.TEST_PLAN_CREATE,
            isAdded: false,
            actionBar: null,
            title: 'Test Campaign Create',
            url: createTestPlanUrl,
            events: {
                show: Constants.events.SHOW_TEST_PLAN_FORM,
                hide: Constants.events.HIDE_TEST_PLAN_FORM,
                current: Constants.events.MARK_CURRENT_TEST_PLAN_FORM
            },
            breadcrumb: new Breadcrumb({
                data: this.createTestPlanBreadcrumbs
            }),
            content: testPlanFormRegion
        };

        this.pages[Constants.pages.TEST_PLAN_COPY] = {
            id: Constants.pages.TEST_PLAN_COPY,
            isAdded: false,
            actionBar: null,
            title: 'Test Campaign Copy',
            url: copyTestPlanUrl,
            events: {
                show: Constants.events.SHOW_TEST_PLAN_FORM,
                hide: Constants.events.HIDE_TEST_PLAN_FORM,
                current: Constants.events.MARK_CURRENT_TEST_PLAN_FORM
            },
            breadcrumb: new Breadcrumb({
                data: this.copyTestPlanBreadcrumbs
            }),
            content: testPlanFormRegion
        };

        this.pages[Constants.pages.TEST_PLAN_EDIT] = {
            id: Constants.pages.TEST_PLAN_EDIT,
            isAdded: false,
            actionBar: null,
            title: 'Test Campaign Edit',
            url: editTestPlanUrl,
            events: {
                show: Constants.events.SHOW_TEST_PLAN_FORM,
                hide: Constants.events.HIDE_TEST_PLAN_FORM,
                current: Constants.events.MARK_CURRENT_TEST_PLAN_FORM
            },
            breadcrumb: new Breadcrumb({
                data: this.editTestPlanBreadcrumbs
            }),
            content: testPlanFormRegion
        };
    }

    function prepareTestExecutions () {
        this.pages[Constants.pages.TEST_PLAN_EXECUTION] = {
            id: Constants.pages.TEST_PLAN_EXECUTION,
            isAdded: false,
            actionBar: null,
            title: 'Test Campaign Execution',
            url: testPlanExecutionUrl,
            events: {
                show: Constants.events.SHOW_TEST_PLAN_EXECUTION,
                hide: Constants.events.HIDE_TEST_PLAN_EXECUTION,
                current: Constants.events.MARK_CURRENT_TEST_PLAN_EXECUTION
            },
            breadcrumb: new Breadcrumb({
                data: this.testPlanExecutionBreadcrumbs
            }),
            content: new TestPlanExecutionContentRegion({
                context: this.appContext,
                references: this.references
            })
        };

        this.pages[Constants.pages.CREATE_TEST_EXECUTION] = {
            id: Constants.pages.CREATE_TEST_EXECUTION,
            isAdded: false,
            actionBar: null,
            title: 'Test Execution',
            url: createTestExecutionUrl,
            events: {
                show: Constants.events.SHOW_CREATE_TEST_EXECUTION,
                hide: Constants.events.HIDE_CREATE_TEST_EXECUTION,
                current: Constants.events.MARK_CURRENT_CREATE_TEST_EXECUTION
            },
            breadcrumb: new Breadcrumb({
                data: this.createTestExecutionBreadcrumbs
            }),
            content: new CreateTestExecutionContentRegion({
                context: this.appContext,
                references: this.references
            })
        };
    }

    function prepareTestCases () {
        var testCaseFormRegion = new TestCaseEditContentRegion({
            context: this.appContext,
            references: this.references
        });

        this.pages[Constants.pages.TEST_CASE_SEARCH] = {
            id: Constants.pages.TEST_CASE_SEARCH,
            isAdded: false,
            actionBar: null,
            title: 'Test Cases',
            url: testCaseSearchUrl,
            events: {
                show: Constants.events.SHOW_CONTENT,
                hide: Constants.events.HIDE_CONTENT,
                current: Constants.events.MARK_CONTENT_CURRENT
            },
            breadcrumb: new Breadcrumb({
                data: this.testCaseSearchBreadcrumbs
            }),
            content: new TestCaseSearchContentRegion({
                context: this.appContext
            })
        };

        this.pages[Constants.pages.TEST_CASE_COPY] = {
            id: Constants.pages.TEST_CASE_COPY,
            isAdded: false,
            actionBar: null,
            title: 'Copy Test Case',
            url: copyTestCaseUrl,
            events: {
                show: Constants.events.SHOW_TEST_CASE_FORM,
                hide: Constants.events.HIDE_TEST_CASE_FORM,
                current: Constants.events.MARK_TEST_CASE_FORM_CURRENT
            },
            breadcrumb: new Breadcrumb({
                data: this.copyTestCaseBreadcrumbs
            }),
            content: testCaseFormRegion
        };

        this.pages[Constants.pages.TEST_CASE_DETAILS] = {
            id: Constants.pages.TEST_CASE_DETAILS,
            isAdded: false,
            actionBar: null,
            title: '\u00a0', // &nbsp;
            url: viewTestCaseUrl,
            events: {
                show: Constants.events.SHOW_VIEW_TEST_CASE,
                hide: Constants.events.HIDE_VIEW_TEST_CASE,
                current: Constants.events.MARK_VIEW_TEST_CASE_CURRENT
            },
            breadcrumb: new Breadcrumb({
                data: this.viewTestCaseBreadcrumbs
            }),
            content: new TestCaseDetailsContentRegion({
                context: this.appContext,
                references: this.references
            })
        };

        this.pages[Constants.pages.TEST_CASE_CREATE] = {
            id: Constants.pages.TEST_CASE_CREATE,
            isAdded: false,
            actionBar: null,
            title: 'Create Test Case',
            url: createTestCaseUrl,
            events: {
                show: Constants.events.SHOW_TEST_CASE_FORM,
                hide: Constants.events.HIDE_TEST_CASE_FORM,
                current: Constants.events.MARK_TEST_CASE_FORM_CURRENT
            },
            breadcrumb: new Breadcrumb({
                data: this.createTestCaseBreadcrumbs
            }),
            content: testCaseFormRegion
        };

        this.pages[Constants.pages.TEST_CASE_EDIT] = {
            id: Constants.pages.TEST_CASE_EDIT,
            isAdded: false,
            actionBar: null,
            title: 'Edit Test Case',
            url: editTestCaseUrl,
            events: {
                show: Constants.events.SHOW_TEST_CASE_FORM,
                hide: Constants.events.HIDE_TEST_CASE_FORM,
                current: Constants.events.MARK_TEST_CASE_FORM_CURRENT
            },
            breadcrumb: new Breadcrumb({
                data: this.editTestCaseBreadcrumbs
            }),
            content: testCaseFormRegion
        };
    }

    function prepareRequirements () {
        this.pages[Constants.pages.REQUIREMENTS_TREE] = {
            id: Constants.pages.REQUIREMENTS_TREE,
            isAdded: false,
            actionBar: null,
            title: 'Requirements',
            url: Constants.urls.APP_NAME + Constants.urls.REQUIREMENTS_TREE_PAGE,
            events: {
                show: Constants.events.SHOW_REQUIREMENTS_CONTENT,
                hide: Constants.events.HIDE_REQUIREMENTS_CONTENT,
                current: Constants.events.MARK_REQUIREMENTS_CONTENT_CURRENT
            },
            breadcrumb: new Breadcrumb({
                data: this.requirementsBreadcrumbs
            }),
            content: new RequirementsContentRegion({
                context: this.appContext
            })
        };
    }

    function prepareInbox () {
        this.pages[Constants.pages.USER_INBOX] = {
            id: Constants.pages.USER_INBOX,
            isAdded: false,
            actionBar: null,
            title: 'Inbox',
            url: userInboxUrl,
            events: {
                show: Constants.events.SHOW_USER_INBOX,
                hide: Constants.events.HIDE_USER_INBOX,
                current: Constants.events.MARK_CURRENT_USER_INBOX
            },
            breadcrumb: new Breadcrumb({
                data: this.userInboxBreadcrumbs
            }),
            content: new UserInboxContentRegion({
                context: this.appContext,
                references: this.references
            })
        };
    }

    function prepareReview () {
        this.pages[Constants.pages.REVIEW_INBOX] = {
            id: Constants.pages.REVIEW_INBOX,
            isAdded: false,
            actionBar: null,
            title: 'Review',
            url: userInboxUrl,
            events: {
                show: Constants.events.SHOW_REVIEW_INBOX,
                hide: Constants.events.HIDE_REVIEW_INBOX,
                current: Constants.events.MARK_CURRENT_REVIEW_INBOX
            },
            breadcrumb: new Breadcrumb({
                data: this.reviewInboxBreadcrumbs
            }),
            content: new ReviewInboxContentRegion({
                context: this.appContext,
                references: this.references
            })
        };
    }

    function prepareImport () {
        this.pages[Constants.pages.IMPORT] = {
            id: Constants.pages.IMPORT,
            isAdded: false,
            actionBar: null,
            title: 'Import Test Cases',
            url: importUrl,
            events: {
                show: Constants.events.SHOW_VIEW_TEST_CASE_IMPORT,
                hide: Constants.events.HIDE_VIEW_TEST_CASE_IMPORT,
                current: Constants.events.MARK_VIEW_TEST_CASE_IMPORT_CURRENT
            },
            breadcrumb: new Breadcrumb({
                data: this.importBreadcrumbs
            }),
            content: new TestCaseImportContentRegion({
                context: this.appContext
            })
        };
    }

    function prepareWelcome () {
        this.pages[Constants.pages.WELCOME] = {
            id: Constants.pages.WELCOME,
            type: 'blank',
            isAdded: false,
            title: 'Welcome',
            url: Constants.urls.APP_NAME + Constants.urls.DEFAULT_PAGE,
            events: {
                show: Constants.events.SHOW_WELCOME,
                hide: Constants.events.HIDE_WELCOME,
                current: Constants.events.MARK_CURRENT_WELCOME
            },
            breadcrumb: new Breadcrumb({
                data: this.welcomeBreadcrumbs
            }),
            content: new WelcomeContentRegion({
                context: this.appContext,
                references: this.references
            })
        };
    }

    function prepareAdmin () {
        this.pages[Constants.pages.ADMIN] = {
            id: Constants.pages.ADMIN,
            type: 'normal',
            isAdded: false,
            actionBar: null,
            title: 'Administration',
            url: Constants.urls.APP_NAME + Constants.urls.ADMIN,
            events: {
                show: Constants.events.SHOW_VIEW_ADMIN,
                hide: Constants.events.HIDE_VIEW_ADMIN,
                current: Constants.events.MARK_VIEW_ADMIN_CURRENT
            },
            breadcrumb: new Breadcrumb({
                data: this.adminBreadcrumbs
            }),
            content: new AdminContentRegion({
                context: this.appContext,
                references: this.references
            })
        };
    }

    function prepareGroupTestCampaigns () {
        this.pages[Constants.pages.TEST_CAMPAIGN_GROUP] = {
            id: Constants.pages.TEST_CAMPAIGN_GROUP,
            isAdded: false,
            actionBar: null,
            title: 'Test Campaign Groups',
            url: userInboxUrl,
            events: {
                show: Constants.events.SHOW_GROUP_TEST_CAMPAIGN,
                hide: Constants.events.HIDE_GROUP_TEST_CAMPAIGN,
                current: Constants.events.MARK_CURRENT_GROUP_TEST_CAMPAIGN
            },
            breadcrumb: new Breadcrumb({
                data: this.groupTestCampaignBreadcrumbs
            }),
            content: new TestCampaignGroupContentRegion({
                context: this.appContext,
                references: this.references
            })
        };
    }

});
