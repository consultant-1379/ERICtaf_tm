define([
    'jscore/core',
    './TestCampaignListContentRegionView',
    '../../../../common/Constants',
    '../../../../common/Animation',
    '../../../../common/Navigation',
    '../../../../common/ModelHelper',
    '../../../../common/DeleteDialog/DeleteDialog',
    '../../../../common/ContextFilter',
    '../../models/TestCampaignModel',
    '../TestCampaignListWidget/TestCampaignListWidget',
    '../../models/TestCampaignsCollection',
    'jscore/ext/utils/base/underscore'
], function (core, View, Constants, Animation, Navigation, ModelHelper, DeleteDialog, ContextFilter, TestPlanModel,
             TestPlansWidget, TestCampaignsCollection, _) {
    /* jshint validthis: true */
    'use strict';

    return core.Region.extend({

        View: View,

        searchCriteria: {},

        init: function () {
            this.testPlansCollection = new TestCampaignsCollection();
            this.eventBus = this.getContext().eventBus;
            this.eventBus.subscribe(Constants.events.SEARCH_TEST_PLANS, function (searchCriteria) {
                this.searchCriteria = searchCriteria;
                this.refreshTestPlans();
            }, this);
        },

        onViewReady: function () {
            this.animation = new Animation(this.getElement(), this.eventBus);
            this.animation.showOn(Constants.events.SHOW_TEST_PLAN_LIST, this.refreshTestPlans.bind(this));
            this.animation.hideOn(Constants.events.HIDE_TEST_PLAN_LIST);
            this.animation.markCurrentOn(Constants.events.MARK_CURRENT_TEST_PLAN_LIST, this.updateCurrentPage.bind(this));

            this.testPlansWidget = new TestPlansWidget({
                region: this,
                collection: this.testPlansCollection
            });
            this.testPlansWidget.attachTo(this.view.getTestPlans());

            this.activeModel = null;
            this.deleteDialog = new DeleteDialog({
                item: 'Test Campaign',
                action: function () {
                    this.deleteTestPlan(this.activeModel);
                    this.deleteDialog.hide();
                }.bind(this)
            });
        },

        updateCurrentPage: function (paramsObj) {
            this.refreshTestPlans();
            var itemId = '';
            if (paramsObj) {
                itemId = paramsObj.itemId;
            }
            this.eventBus.publish(Constants.events.UPDATE_SELECTED_PROJECT, itemId);
        },

        openTestPlanExecution: function (testPlanId) {
            Navigation.navigateTo(Navigation.getTestPlanExecutionUrl(testPlanId));
        },

        deleteTestPlanDialog: function (model) {
            this.activeModel = model;
            this.deleteDialog.show();
        },

        deleteTestPlan: function (testPlanId) {
            var modelToRemove = this.testPlansCollection.getModel(testPlanId);
            modelToRemove.destroy({
                success: this.refreshTestPlans.bind(this)
            });
        },

        refreshTestPlans: function () {
            this.testPlansWidget.refreshTableInfo();
            if (!_.isEmpty(this.searchCriteria)) {
                this.testPlansCollection.setSearchCriteria(this.searchCriteria);
                this.testPlansCollection.fetch({
                    data: {
                        view: 'detailed'
                    },
                    reset: true,
                    statusCode: ModelHelper.statusCodeHandler(this.eventBus)
                });
            }
        }

    });

});
