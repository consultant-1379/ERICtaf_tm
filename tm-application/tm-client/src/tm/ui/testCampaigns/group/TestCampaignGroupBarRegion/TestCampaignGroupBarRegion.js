/*global define*/
define([
    'jscore/core',
    'jscore/base/jquery',
    'jscore/ext/utils/base/underscore',
    './TestCampaignGroupBarRegionView',
    '../../../../common/Navigation',
    '../../../../common/Constants',
    '../../../../common/ActionBarRegion/ActionBarRegion',
    '../../../../common/widgets/actionLink/ActionLink',
    '../../../../common/notifications/NotificationRegion/NotificationRegion',
    '../../../../common/ContextFilter',
    'widgets/SelectBox',
    '../../../../common/ReferenceHelper',
    '../../../../common/models/ReferencesCollection'
], function (core, $, _, View, Navigation, Constants, ActionBarRegion, ActionLink, Notifications, ContextFilter,
             SelectBox, ReferenceHelper, ReferencesCollection) {

    'use strict';

    return ActionBarRegion.extend({
        /*jshint validthis:true */

        View: View,

        onStart: function () {
            this.references = new ReferencesCollection();
            this.eventBus = this.getContext().eventBus;
            this.view.getSearchButton().addEventHandler('click', function () {
                Navigation.navigateTo(
                    Navigation.getTestCaseListUrlWithParams(ContextFilter.isAdvancedSearch, ContextFilter.searchQuery));
            });

            initCreateActionLink.call(this);
            initTestCampaignListActionLink.call(this);
            initAddGroupActionLink.call(this);

            this.referenceHelper = new ReferenceHelper({
                references: this.references
            });

            this.selectBox = new SelectBox();

            this.selectBox.view.getButton().setAttribute('id', 'TMS_TestCampaignGroups_productSelect');
            this.selectBox.attachTo(this.view.getProductSelect());

            this.selectBox.addEventHandler('change', function () {
                var product = this.selectBox.getValue();
                this.eventBus.publish(Constants.events.UPDATE_USER_PROFILE_WITH_PROJECT, null, product);
            }.bind(this));

            this.references.addReferences(
                'product'
            );

            this.references.addEventHandler('reset', function () {
                this.onReferencesReceived();
            }, this);

            fetchReferences.call(this);

            this.eventBus.subscribe(Constants.events.PRODUCT_CHANGED, setProductToSelectBox.bind(this), this);
            this.eventBus.subscribe(Constants.events.TEST_CAMPAIGN_GROUP_CHANGE_PRODUCT, setSelectBox.bind(this), this);
        },

        show: function () {
            ActionBarRegion.prototype.show.call(this);
        },

        onAddGroup: function (e) {
            e.preventDefault();
            this.eventBus.publish(Constants.events.TEST_CAMPAIGN_GROUP_CREATE_FOLDER);
        },

        onReferencesReceived: function () {
            this.selectBox.setItems(this.referenceHelper.getReferenceItems('product'));
            ContextFilter.profileReady
                .then(setProductToSelectBox.bind(this));
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

    function initTestCampaignListActionLink () {
        this.testCampaignListActionLink = new ActionLink({
            icon: {iconKey: 'addToFolder', interactive: true, title: 'Test Campaigns List'},
            link: {text: 'Test Campaigns List'},
            url: Navigation.getTestPlansListUrl()
        });
        this.testCampaignListActionLink.attachTo(this.view.getTestCampaignListHolder());
    }

    function initAddGroupActionLink () {
        this.addGroupActionLink = new ActionLink({
            icon: {iconKey: 'folder', interactive: true, title: 'Add Group'},
            link: {text: 'Add Group'},
            action: this.onAddGroup.bind(this)
        });
        this.addGroupActionLink.attachTo(this.view.getAddGroupHolder());
    }

    function setProductToSelectBox () {
        var product = ContextFilter.profileProduct;
        if (product) {
            var productObj = {
                itemObj: product,
                title: product.externalId,
                name: product.externalId,
                value: product.id
            };
            this.selectBox.setValue(productObj);
        }
    }

    function fetchReferences () {
        this.references.fetch({
            reset: true
        });
    }

    function setSelectBox (value) {
        setTimeout(function () {
            this.selectBox.getItems().forEach(function (item) {
                if (value === item.name) {
                    this.selectBox.setValue(item);
                }
            }.bind(this));
        }.bind(this), 1000);
    }
});
