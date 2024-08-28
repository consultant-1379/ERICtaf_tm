define([
    'jscore/core',
    './TestCampaignGroupContentRegionView',
    '../../../../common/Constants',
    '../../../../common/Animation',
    '../../../../common/ModelHelper',
    '../../../../common/ContextFilter',
    '../../models/TestCampaignModel',
    '../TestCampaignGroupWidget/TestCampaignGroupWidget',
    '../../models/TestCampaignGroupsCollection',
    'widgets/Dialog',
    '../../../admin/models/TestCampaignGroupModel',
    '../../../../common/inputWidget/InputWidget',
    '../../../../common/notifications/NotificationRegion/NotificationRegion',
    '../../../../common/Navigation',
    './../../../../routing/Router'
], function (core, View, Constants, Animation, ModelHelper, ContextFilter, TestPlanModel,
             TestCampaignGroupWidget, TestCampaignsCollection, Dialog, TestCampaignGroupModel, InputWidget,
             NotificationRegion, Navigation, Router) {
    /* jshint validthis: true */
    'use strict';

    return core.Region.extend({

        View: View,

        searchCriteria: {},

        init: function () {
            this.testCampaignsCollection = new TestCampaignsCollection();
            this.eventBus = this.getContext().eventBus;
            this.currentPage = false;
        },

        onViewReady: function () {
            this.animation = new Animation(this.getElement(), this.eventBus);
            this.animation.showOn(Constants.events.SHOW_GROUP_TEST_CAMPAIGN, this.setCurrentPage.bind(this));
            this.animation.hideOn(Constants.events.HIDE_GROUP_TEST_CAMPAIGN, this.notCurrentPage.bind(this));
            this.animation.markCurrentOn(Constants.events.MARK_CURRENT_GROUP_TEST_CAMPAIGN,
                this.setCurrentPage.bind(this));

            this.testCampaignGroupWidget = new TestCampaignGroupWidget({
                region: this,
                collection: this.testCampaignsCollection
            });
            this.testCampaignGroupWidget.attachTo(this.view.getTestCampaigns());

            this.eventBus.subscribe(Constants.events.TEST_CAMPAIGN_GROUP_CREATE_FOLDER, this.onAddGroup, this);
            this.eventBus.subscribe(Constants.events.PRODUCT_CHANGED, this.onProductChange, this);
        },

        setCurrentPage: function () {
            this.currentPage = true;
        },

        notCurrentPage: function () {
            this.currentPage = false;
        },

        onProductChange: function () {
            this.testCampaignsCollection.setPage(1);
            if (ContextFilter.searchGroupQuery && ContextFilter.productIdParam) {
                var groupName = Router.prototype.getParameterFromValue('name', ContextFilter.searchGroupQuery);
                this.refreshTestCampaignGroups(ContextFilter.searchGroupQuery);
                this.testCampaignGroupWidget.setSearch(groupName);
                this.eventBus.publish(Constants.events.TEST_CAMPAIGN_GROUP_CHANGE_PRODUCT,
                    ContextFilter.productIdParam);
            } else {
                this.refreshTestCampaignGroups();
            }
        },

        refreshTestCampaignGroups: function (searchQuery) {
            var query = getQueryParams.call(this, searchQuery);

            if (query.length > 0) {
                this.testCampaignsCollection.setSearchQuery(decodeURIComponent(query.join('&')));
            }
            this.testCampaignsCollection.fetch({
                reset: true,
                statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                    200: function () {
                        if (this.currentPage) {
                            var encodedQuery = Router.prototype.encodeParameters(query.join('&'));
                            Navigation.navigateTo(
                                Navigation.getTestCampaignGroupsUrlWithParams(encodedQuery.join('&')));
                        }
                        ContextFilter.searchGroupQuery = null;
                    }.bind(this)
                })
            });
        },

        onAddGroup: function () {
            var inputWidget = new InputWidget({
                label: 'Name',
                id: 'TestCampaignGroupName'

            });

            var dialog = new Dialog({
                header: 'Create Test Campaign Group',
                type: 'information',
                content: inputWidget,
                buttons: [
                    {
                        caption: 'Add',
                        color: 'green',
                        action: function () {
                            var name = inputWidget.getValue().trim();
                            createTestCampaign.call(this, name);
                            dialog.hide();
                        }.bind(this)
                    },
                    {
                        caption: 'Cancel',
                        action: function () {
                            dialog.hide();
                        }
                    }
                ]
            });

            dialog.show();
        },

        editGroup: function (model) {
            var inputWidget = new InputWidget({
                label: 'Name',
                id: 'TestCampaignGroupName'

            });

            var dialog = new Dialog({
                header: 'Edit Test Campaign Group',
                type: 'warning',
                content: inputWidget,
                buttons: [
                    {
                        caption: 'Edit',
                        color: 'blue',
                        action: function () {
                            var name = inputWidget.getValue().trim();
                            saveTestCampaignGroup.call(this, name, model);
                            dialog.hide();
                        }.bind(this)
                    },
                    {
                        caption: 'Cancel',
                        action: function () {
                            dialog.hide();
                        }
                    }
                ]
            });

            dialog.show();
        },

        deleteGroup: function (model) {
            var dialog = new Dialog({
                header: 'Delete Test Campaign Group',
                type: 'warning',
                content: 'Are you sure you want to delete?',
                buttons: [
                    {
                        caption: 'Delete',
                        color: 'red',
                        action: function () {
                            deleteTestCampaignGroup.call(this, model);
                            dialog.hide();
                        }.bind(this)
                    },
                    {
                        caption: 'Cancel',
                        action: function () {
                            dialog.hide();
                        }
                    }
                ]
            });

            dialog.show();
        },

        copyUrlGroup: function (model) {
            this.baseURL = window.location.href;
            if (this.baseURL.includes('?')) {
                this.baseURL = window.location.href.split('?')[0];
            }
            var params = [
                'product=' + encodeURIComponent(ContextFilter.productIdParam),
                'name~' + encodeURIComponent(model.label)
            ];
            return this.baseURL + '?' + params.join('&');
        }
    });

    function createTestCampaign (name) {
        var testCampaignGroupModel = new TestCampaignGroupModel();

        testCampaignGroupModel.setName(name);
        testCampaignGroupModel.setProduct(ContextFilter.profileProduct);

        testCampaignGroupModel.save({}, {
            statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                201: function () {
                    var options = NotificationRegion.NOTIFICATION_TYPES.success;
                    options.canDismiss = true;
                    options.canClose = true;
                    this.eventBus.publish(Constants.events.NOTIFICATION, 'Created successfully', options);
                    this.refreshTestCampaignGroups('name~' + name);
                    this.testCampaignGroupWidget.setSearch(name);
                }.bind(this)
            })

        });
    }

    function getQueryParams (searchQuery) {
        var query = [];
        if (ContextFilter.productIdParam) {
            query.push('product=' + ContextFilter.productIdParam);
        } else if (!ContextFilter.productIdParam && ContextFilter.profileProduct) {
            query.push('product=' + ContextFilter.profileProduct.externalId);
        }

        if (searchQuery) {
            query.push(searchQuery);
        }

        return query;
    }

    function saveTestCampaignGroup (name, model) {
        var foundModel = {};
        this.testCampaignsCollection.each(function (modelItem, index) {
            if (modelItem.id === model.id) {
                foundModel = this.testCampaignsCollection.getAtIndex(index);
                return;
            }
        }.bind(this));

        if (foundModel) {
            foundModel.setName(name);

            foundModel.save({}, {
                statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                    200: function () {
                        var options = NotificationRegion.NOTIFICATION_TYPES.success;
                        options.canDismiss = true;
                        options.canClose = true;
                        this.eventBus.publish(Constants.events.NOTIFICATION, 'Updated successfully', options);
                        this.refreshTestCampaignGroups('name~' + name);
                        this.testCampaignGroupWidget.setSearch(name);
                    }.bind(this)
                })

            });
        }
    }

    function deleteTestCampaignGroup (model) {
        var foundModel = {};
        this.testCampaignsCollection.each(function (modelItem, index) {
            if (modelItem.id === model.id) {
                foundModel = this.testCampaignsCollection.getAtIndex(index);
                return;
            }
        }.bind(this));

        if (foundModel) {
            foundModel.destroy({
                wait: true,
                statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                    204: function () {
                        var options = NotificationRegion.NOTIFICATION_TYPES.success;
                        options.canDismiss = true;
                        options.canClose = true;
                        this.eventBus.publish(Constants.events.NOTIFICATION, 'Deleted successfully', options);
                        this.refreshTestCampaignGroups();
                        this.testCampaignGroupWidget.setSearch('');
                    }.bind(this)
                })
            });
        }
    }

});
