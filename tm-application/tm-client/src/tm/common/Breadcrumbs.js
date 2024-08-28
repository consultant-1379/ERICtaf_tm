define([
    './Constants',
    './Navigation',
    'widgets/Breadcrumb',
    'jscore/ext/utils/base/underscore'
], function (Constants, Navigation, Breadcrumb, _) {
    'use strict';

    return {

        getEditBreadcrumbs: function (pageToShowId, itemId, pagesObj) {

            var initBreadCrumbs = pagesObj.defaultBreadcrumbs.slice(0);
            var breadcrumbItems = [];

            switch (pageToShowId) {
                case Constants.pages.TEST_CASE_EDIT :

                    breadcrumbItems = [
                        {name: Constants.breadcrumbs.TEST_CASE_DETAILS, url: '#' + Navigation.getTestCaseDetailsUrl(itemId)},
                        {name: Constants.breadcrumbs.TEST_CASE_EDIT, url: '#' + Navigation.getTestCaseEditUrl(itemId)}
                    ];

                    return createBreadCrumbs.call(this, initBreadCrumbs, breadcrumbItems);

                case Constants.pages.TEST_PLAN_EDIT :

                    breadcrumbItems = [
                        {name: Constants.breadcrumbs.TEST_PLANS, url: '#' + Navigation.getTestPlansListUrl()},
                        {name: Constants.breadcrumbs.TEST_PLAN_DETAILS, url: '#' + Navigation.getTestPlanDetailsUrl(itemId)},
                        {name: Constants.breadcrumbs.TEST_PLAN_EDIT, url: '#' + Navigation.getTestPlanEditUrl(itemId)}
                    ];

                    return createBreadCrumbs.call(this, initBreadCrumbs, breadcrumbItems);

                case Constants.pages.TEST_PLAN_EXECUTION :

                    breadcrumbItems = [
                        {name: Constants.breadcrumbs.TEST_PLANS, url: '#' + Navigation.getTestPlansListUrl()},
                        {name: Constants.breadcrumbs.TEST_PLAN_DETAILS, url: '#' + Navigation.getTestPlanDetailsUrl(itemId)},
                        {name: Constants.breadcrumbs.TEST_PLAN_EXECUTION, url: '#' + Navigation.getTestPlanExecutionUrl(itemId)}
                    ];

                    return createBreadCrumbs.call(this, initBreadCrumbs, breadcrumbItems);

                case Constants.pages.CREATE_TEST_EXECUTION :

                    var navId = getSplitValues(itemId)[0],
                        testCaseId = getSplitValues(itemId)[1];

                    breadcrumbItems = [
                        {name: Constants.breadcrumbs.TEST_PLANS, url: '#' + Navigation.getTestPlansListUrl()},
                        {name: Constants.breadcrumbs.TEST_PLAN_DETAILS, url: '#' + Navigation.getTestPlanDetailsUrl(navId)},
                        {name: Constants.breadcrumbs.TEST_PLAN_EXECUTION, url: '#' + Navigation.getTestPlanExecutionUrl(navId)},
                        {name: Constants.breadcrumbs.TEST_EXECUTION, url: '#' + Navigation.getTestExecutionCreateUrl(navId, testCaseId)}
                    ];

                    return createBreadCrumbs.call(this, initBreadCrumbs, breadcrumbItems);

                default :
                    return null;

            }

        }

    };

    function createBreadCrumbs (newBreadCrumb, breadCrumbList) {
        _.each(breadCrumbList, function (item) {
            newBreadCrumb.push(item);
        });
        return new Breadcrumb({
            data: newBreadCrumb
        });
    }

    function getSplitValues (value) {
        return value.split(':');
    }

});
