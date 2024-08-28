/*global define, location*/
define([
    'jscore/core',
    'jscore/ext/locationController',
    './LocationHelper',
    '../common/Constants',
    '../common/ContextFilter'
], function (core, LocationController, LocationHelper, Constants, ContextFilter) {
    'use strict';

    var Router = function (pages, eventBus) {
        this.pages = pages;
        this.eventBus = eventBus;

        this.locationController = new LocationController({
            autoUrlDecode: false
        });
    };

    Router.prototype.registerHandler = function (navigate) {

        this.locationController.addLocationListener(function (hash) {
            var testCaseId = '', testPlanId = '';

            var requirementsUrl = Constants.urls.APP_NAME + Constants.urls.REQUIREMENTS_TREE_PAGE,
                editTestCaseUrl = Constants.urls.APP_NAME + Constants.urls.EDIT_TC_PAGE,
                viewTestCaseUrl = Constants.urls.APP_NAME + Constants.urls.VIEW_TC_PAGE,
                testCaseViewPattern = new RegExp(
                    Constants.urls.APP_NAME +
                    Constants.urls.VIEW_TC_PAGE + '(.*)' +
                    Constants.urls.VERSION_PAGE + '(.*)'
                ),
                testCaseCopyPattern = new RegExp(
                    Constants.urls.APP_NAME +
                    Constants.urls.COPY_TC_PAGE + '(.*)' +
                    Constants.urls.VERSION_PAGE + '(.*)'
                ),
                listTestPlansUrl = Constants.urls.APP_NAME + Constants.urls.LIST_TEST_PLANS,
                viewTestPlanUrl = Constants.urls.APP_NAME + Constants.urls.VIEW_TEST_PLAN,
                viewOldTestPlanUrl = Constants.urls.APP_NAME + Constants.urls.VIEW_OLD_TEST_PLAN, // serve the old url
                editTestPlanUrl = Constants.urls.APP_NAME + Constants.urls.EDIT_TEST_PLAN,
                copyTestPlanUrl = Constants.urls.APP_NAME + Constants.urls.COPY_TEST_PLAN,
                testPlanExecutionUrl = Constants.urls.APP_NAME + Constants.urls.TEST_PLAN_EXECUTION;

            // Backwards-compatible escape behavior
            if (hash.indexOf(Constants.urls.CREATE_TC_PAGE) === -1) {
                hash = decodeURIComponent(hash);
            }

            if (hash === '' || hash === Constants.urls.APP_NAME || hash === Constants.urls.APP_NAME + '/') {
                navigate(this.pages[Constants.pages.WELCOME]);
            } else if (hash.indexOf(Constants.urls.TEST_CASES) > -1) {

                ContextFilter.projectIdParam = '';
                ContextFilter.searchQuery = '';
                if (hash.indexOf('/?') > -1) {
                    ContextFilter.searchQuery = hash.substr(hash.indexOf('/?') + 2);
                    ContextFilter.projectIdParam = this.getParameterFromSearchQuery(Constants.params.PROJECT_ID);
                }
                ContextFilter.isAdvancedSearch = hash.indexOf('/search') > -1;

                navigate(this.pages[Constants.pages.TEST_CASE_SEARCH], ContextFilter.projectIdParam);

                if (ContextFilter.isAdvancedSearch) {
                    this.eventBus.publish(Constants.events.SHOW_BAR_FOR_ADVANCED_SEARCH);
                } else {
                    this.eventBus.publish(Constants.events.REPLACE_SEARCH_INPUT,
                        this.getParameterFromSearchQuery(Constants.params.ANY));
                    this.eventBus.publish(Constants.events.HIDE_BAR_FOR_ADVANCED_SEARCH);
                }
            } else if (hash.indexOf(Constants.urls.CREATE_TC_PAGE) > -1) {
                var loc = LocationHelper.parse(hash);

                // Backwards-compatible test case ID population
                var parts = LocationHelper.splitPath(loc.pathname).reverse();
                if (Constants.urls.CREATE_TC_PAGE === ('/' + parts[1])) {
                    testCaseId = parts[0];
                }

                var query = LocationHelper.parseQuery(loc.search);
                navigate(this.pages[Constants.pages.TEST_CASE_CREATE], testCaseId, {
                    query: query
                });
            } else if (hash.indexOf(editTestCaseUrl) > -1) {
                testCaseId = hash.replace(editTestCaseUrl, '');
                navigate(this.pages[Constants.pages.TEST_CASE_EDIT], testCaseId);
            } else if (testCaseCopyPattern.test(hash)) {
                var copyPatternResult = testCaseCopyPattern.exec(hash);
                testCaseId = copyPatternResult[1];
                navigate(this.pages[Constants.pages.TEST_CASE_COPY], testCaseId, {
                    version: copyPatternResult[2] != null ? copyPatternResult[2] : null,
                    testCaseId: testCaseId != null ? testCaseId : null
                });
            } else if (testCaseViewPattern.test(hash)) {
                var viewPatternResult = testCaseViewPattern.exec(hash);
                testCaseId = viewPatternResult[1];
                navigate(this.pages[Constants.pages.TEST_CASE_DETAILS], testCaseId, {
                    version: viewPatternResult[2] != null ? viewPatternResult[2] : null,
                    testCaseId: testCaseId != null ? testCaseId : null
                });
            } else if (hash.indexOf(viewTestCaseUrl) > -1) {
                testCaseId = hash.replace(viewTestCaseUrl, '');
                navigate(this.pages[Constants.pages.TEST_CASE_DETAILS], testCaseId);
            } else if (hash.indexOf(listTestPlansUrl) > -1) {
                ContextFilter.projectIdParam = '';
                if (hash.indexOf('/?') > -1) {
                    ContextFilter.projectIdParam = this.getParameterFromHash(Constants.params.PROJECT_ID);
                }
                navigate(this.pages[Constants.pages.TEST_PLAN_LIST], ContextFilter.projectIdParam);
            } else if (hash.indexOf(Constants.urls.VIEW_TEST_PLAN) > -1) {
                testPlanId = hash.replace(viewTestPlanUrl, '');
                navigate(this.pages[Constants.pages.TEST_PLAN_DETAILS], testPlanId);
            } else if (hash.indexOf(Constants.urls.VIEW_OLD_TEST_PLAN) > -1) {
                this.redirect(hash.replace(viewOldTestPlanUrl, viewTestPlanUrl));
            }else if (hash.indexOf(Constants.urls.CREATE_TEST_PLAN) > -1) {
                navigate(this.pages[Constants.pages.TEST_PLAN_CREATE]);
            } else if (hash.indexOf(Constants.urls.COPY_TEST_PLAN) > -1) {
                testPlanId = hash.replace(copyTestPlanUrl, '');
                navigate(this.pages[Constants.pages.TEST_PLAN_COPY], testPlanId);
            } else if (hash.indexOf(Constants.urls.EDIT_TEST_PLAN) > -1) {
                testPlanId = hash.replace(editTestPlanUrl, '');
                navigate(this.pages[Constants.pages.TEST_PLAN_EDIT], testPlanId);
            } else if (hash.indexOf(Constants.urls.TEST_PLAN_EXECUTION) > -1) {
                testPlanId = hash.replace(testPlanExecutionUrl, '');
                var whereToNavigate = this.pages[Constants.pages.TEST_PLAN_EXECUTION];
                if (testPlanId.indexOf(Constants.urls.TEST_CASE_PARAM) > -1) {
                    testPlanId = testPlanId.replace(Constants.urls.TEST_CASE_PARAM, ':');
                    whereToNavigate = this.pages[Constants.pages.CREATE_TEST_EXECUTION];
                }
                navigate(whereToNavigate, testPlanId);
            } else if (hash.indexOf(requirementsUrl) > -1) {
                ContextFilter.projectIdParam = '';
                ContextFilter.requirementIdParam = '';
                if (hash.indexOf('/?') > -1) {
                    ContextFilter.projectIdParam = this.getParameterFromHash(Constants.params.PROJECT_ID);
                    ContextFilter.requirementIdParam = this.getParameterFromHash(Constants.params.REQUIREMENT_ID);
                }
                navigate(this.pages[Constants.pages.REQUIREMENTS_TREE]);
            } else if (hash.indexOf(Constants.urls.USER_INBOX) > -1) {
                navigate(this.pages[Constants.pages.USER_INBOX]);
            } else if (hash.indexOf(Constants.urls.REVIEW_INBOX) > -1) {
                navigate(this.pages[Constants.pages.REVIEW_INBOX]);
            } else if (hash.indexOf(Constants.urls.IMPORT) > -1) {
                navigate(this.pages[Constants.pages.IMPORT]);
            } else if (hash.indexOf(Constants.urls.ADMIN) > -1) {
                navigate(this.pages[Constants.pages.ADMIN]);
            } else if (hash.indexOf(Constants.urls.GROUP_TEST_CAMPAIGN) > -1) {
                if (hash.indexOf('/?') > -1) {
                    var product = this.getParameterFromHash(Constants.params.PRODUCT);
                    var name = this.getParameterFromHash(Constants.params.NAME);
                    ContextFilter.productIdParam = product;
                    if (name.length > 0) {
                        ContextFilter.searchGroupQuery = 'name~' + name;
                    }
                }
                navigate(this.pages[Constants.pages.TEST_CAMPAIGN_GROUP]);
            }
        }, this);

        this.locationController.start();
    };

    Router.prototype.getParameterFromSearchQuery = function (name) {
        name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
        var regex = new RegExp('[\\?&]{0,1}' + name + '[=~]([^&#]*)');
        var results = regex.exec(ContextFilter.searchQuery);
        return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
    };

    Router.prototype.getParameterFromHash = function (name) {
        name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
        var regex = new RegExp('[\\?&]{0,1}' + name + '[=~]([^&#]*)');
        var results = regex.exec(location.hash);
        return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
    };

    Router.prototype.getParameterFromValue = function (name, searchQuery) {
        name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
        var regex = new RegExp('[\\?&]{0,1}' + name + '[=~]([^&#]*)');
        var results = regex.exec(searchQuery);
        return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
    };

    Router.prototype.encodeParameters = function (query) {
        var result = [];
        var a = (query[0] === '?' ? query.substr(1) : query).split('&');
        for (var i = 0; i < a.length; i++) {
            if (a[i].indexOf('=') > 0) {
                var b = a[i].split('=');
                result.push(b[0] + '=' + encodeURIComponent(b[1] || ''));
            } else if (a[i].indexOf('~') > 0) {
                var c = a[i].split('~');
                result.push(c[0] + '~' + encodeURIComponent(c[1] || ''));
            }
        }
        return result;
    };

    Router.prototype.stop = function () {
        this.locationController.stop();
    };

    Router.prototype.redirect = function (url) {
        location.hash = url;
    };

    return Router;

});
