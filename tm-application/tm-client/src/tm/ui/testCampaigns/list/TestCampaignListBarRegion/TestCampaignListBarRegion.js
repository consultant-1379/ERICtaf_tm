/*global define*/
define([
    'jscore/core',
    'jscore/base/jquery',
    'jscore/ext/utils/base/underscore',
    './TestCampaignListBarRegionView',
    '../../../../common/Navigation',
    '../../../../common/Constants',
    '../../../../common/ActionBarRegion/ActionBarRegion',
    '../../../../common/widgets/actionLink/ActionLink',
    '../../../../common/widgets/SelectBoxFilter/SelectBoxFilter',
    '../../../../common/models/domain/DomainDataCollection',
    'widgets/Dialog',
    'widgets/SelectBox',
    '../../models/TestCampaignsCollection',
    '../../../../common/ModelHelper',
    '../../../../common/notifications/NotificationRegion/NotificationRegion',
    '../../../../common/ContextFilter'
], function (core, $, _, View, Navigation, Constants, ActionBarRegion, ActionLink, SelectBoxFilter, DomainDataCollection,
             Dialog, SelectBox, TestCampaignsCollection, ModelHelper, Notifications, ContextFilter) {

    'use strict';

    return ActionBarRegion.extend({
        /*jshint validthis:true */

        View: View,

        onStart: function () {
            this.view.getSearchButton().addEventHandler('click', function () {
                Navigation.navigateTo(Navigation.getTestCaseListUrlWithParams(ContextFilter.isAdvancedSearch, ContextFilter.searchQuery));
            });

            this.selectBoxFilter = new SelectBoxFilter({
                eventBus: this.getEventBus(),
                events: {
                    productChangedEvent: Constants.events.PRODUCT_FILTER_CHANGED,
                    cascadeFinishedEvent: Constants.events.SEARCH_TEST_PLANS
                }
            });
            this.selectBoxFilter.attachTo(this.view.getFilterHolder());

            createDropSelectBox.call(this);

            this.dropCollection = new DomainDataCollection([], {
                type: 'drop'
            });

            initCreateActionLink.call(this);
            initCopyActionLink.call(this);
            createDialogWithDropSelect.call(this);
            initGroupActionLink.call(this);

            this.getEventBus().subscribe(Constants.events.PRODUCT_FILTER_CHANGED, this.showOrHideCopyButton, this);
            this.getEventBus().subscribe(Constants.events.TEST_CAMPAIGNS_FILTER_CHANGED, this.setSearchQuery, this);

            this.collection = new TestCampaignsCollection();
        },

        show: function () {
            ActionBarRegion.prototype.show.call(this);
        },

        fetchDrops: function () {
            var productId = this.selectBoxFilter.getSelection().product.id;
            this.dropCollection.setProductId(productId);

            var currentDrop = this.selectBoxFilter.getSelection().drop;

            if (currentDrop != null) {
                this.dropCollection.fetch({
                    success: function (data) {
                        var items = data.toJSON();
                        items = _.without(items, _.findWhere(items, {value: currentDrop.id}));
                        this.dropSelect.setItems(items);
                        this.dropSelect.setValue(items[1]);
                        this.dialog.show();
                    }.bind(this)
                });
            } else {
                var options = Notifications.NOTIFICATION_TYPES.warning;
                options.canDismiss = true;
                options.canClose = true;
                this.getEventBus().publish(Constants.events.NOTIFICATION, 'Please select a drop', options);
            }
        },

        showOrHideCopyButton: function (product) {
            if (product.dropCapable) {
                this.view.showCopyTestCampaignsButton();
            } else {
                this.view.hideCopyTestCampaignsButton();
            }
        },

        setSearchQuery: function (filters) {
            var searchQueries = [];
            if (!_.isEmpty(filters)) {
                _.map(filters, function (filterObj, key) {
                    searchQueries.push(key + filterObj.comparator + filterObj.value);
                });
            }
            if (!_.isEmpty(searchQueries)) {
                this.query = 'q=' + searchQueries.join('&');
            }
        }

    });

    function initCreateActionLink () {
        this.createActionLink = new ActionLink({
            icon: {iconKey: 'plus', interactive: true, title: 'Create Test Campaign'},
            link: {text: 'Create Test Campaign'},
            url: Navigation.getTestPlanCreateUrl()
        });
        this.createActionLink.attachTo(this.view.getCreateLinkHolder());
    }

    function initCopyActionLink () {
        this.copyActionLink = new ActionLink({
            icon: {iconKey: 'copy', interactive: true, title: 'Copy Test Campaigns'},
            link: {text: 'Copy Test Campaigns'},
            action: this.fetchDrops.bind(this)
        });
        this.copyActionLink.attachTo(this.view.getCopyLinkHolder());
    }

    function initGroupActionLink () {
        this.copyGroupLink = new ActionLink({
            icon: {iconKey: 'topology', interactive: true, title: 'Test Campaign Groups'},
            link: {text: 'Test Campaign Groups'},
            url: Navigation.getTestCampaignGroupsUrlWithParams(
                ContextFilter.profileProduct ? 'product=' + ContextFilter.profileProduct.externalId : null)
        });
        this.copyGroupLink.attachTo(this.view.getGroupHolder());
    }

    function createDialogWithDropSelect () {
        this.dialog = new Dialog({
            header: 'Test Campaigns matching your filters will be copied to the Drop selected below:',
            optionalContent: this.dropSelect,
            content: 'Select a Drop to which to copy the Test Campaigns',
            buttons: [
                {
                    caption: 'Copy',
                    color: 'green',
                    action: function () {
                        var selectedDrop = this.dropSelect.getValue();
                        if (_.isEmpty(selectedDrop)) {
                            return;
                        }
                        copyTestCampaigns.call(this, selectedDrop);
                        this.dialog.hide();
                    }.bind(this)
                },
                {
                    caption: 'Cancel',
                    action: function () {
                        this.dialog.hide();
                    }.bind(this)
                }
            ]
        });
    }

    function createDropSelectBox () {
        this.dropSelect = new SelectBox({
            modifiers: [
                {name: 'width', value: 'fullBlock'}
            ]
        });

        this.dropSelect
            .view
            .getButton()
            .setAttribute('id', 'TMS_TestCampaignList_ViewTestCampaignsBar-dropSelect');
    }

    function copyTestCampaigns (toDrop) {
        var filterSelection = this.selectBoxFilter.getSelection();
        var requestData = {
            query: this.query,
            productId: filterSelection.product.id,
            fromDropId: filterSelection.drop.id,
            featureIds:  _.map(filterSelection.feature, function (feature) {
                return feature.id;
            }),
            componentIds: _.map(filterSelection.components, function (component) {
                return component.id;
            }),
            copyToDropId: toDrop.itemObj.id
        };
        $.ajax({
            type: 'post',
            url: '/tm-server/api/test-campaigns/copy',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(requestData),
            statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                201: function (response) {
                    var total = response.length;
                    var productName = response[0].drop.productName;
                    var dropName = response[0].drop.name;
                    sendNotification.call(this, 'success', total + ' Test Campaigns were successfully copied to ' + productName + ' : ' + dropName);
                }.bind(this),

                400: function (response) {
                    sendNotification.call(this, 'error', getErrorMsg.call(this, response));
                }.bind(this)
            })
        });
    }

    function sendNotification (type, message) {
        var options = Notifications.NOTIFICATION_TYPES[type];
        options.canDismiss = true;
        options.canClose = true;
        this.getEventBus().publish(Constants.events.NOTIFICATION, message, options);
    }

    function getErrorMsg (response) {
        return response.responseText.split(':')[1].split(',')[0];
    }
});
