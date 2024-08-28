define([
    'jscore/core',
    './AddTestCaseTestCampaignWidgetView',
    'widgets/SelectBox',
    '../../../../common/models/completion/CompletionsCollection',
    '../../../../common/ModelHelper',
    '../../../../common/models/domain/DomainDataCollection',
    'widgets/Combobox'
], function (core, View, SelectBox, CompletionsCollection, ModelHelper, DomainDataCollection, Combobox) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true */

        View: View,

        init: function (options) {
            this.profileProduct = options.profileProduct;
            this.productName = this.profileProduct.externalId;
            this.dropName = null;
            this.featureName = null;
        },

        onViewReady: function () {
            this.featureSelect = new SelectBox({
                modifiers: [
                    {name: 'width', value: 'fullBlock'}
                ]
            });
            this.testCampaignSelect =  new Combobox({
                autoComplete: {
                    message: {notFound: 'Not Found'},
                    caseInsensitive: true
                },
                modifiers: [
                    {name: 'width', value: 'full'}
                ]
            });

            if (this.profileProduct.dropCapable) {
                this.dropSelect = new SelectBox({
                    modifiers: [
                        {name: 'width', value: 'fullBlock'}
                    ]
                });

                this.dropSelect.attachTo(this.view.getDropSelectHolder());
                this.dropSelect.addEventHandler('change', function () {
                    this.dropName = this.dropSelect.getValue().name;
                    fetchTestCampaigns.call(this, this.concatenateParams());
                }.bind(this));
                fetchDrops.call(this, this.profileProduct.id);
            } else {
                this.view.getDropSelectHolder().setStyle('display', 'none');
            }

            this.featureSelect.attachTo(this.view.getFeatureSelectHolder());
            this.testCampaignSelect.attachTo(this.view.getTestCampaignSelectHolder());

            this.testCampaignSelect.view.getInput().setAttribute('id', 'TMS_TestCaseSearch_AddToTestCampaign');

            this.featureSelect.addEventHandler('change', function () {
                this.featureName = this.featureSelect.getValue().name;
                fetchTestCampaigns.call(this, this.concatenateParams());
            }.bind(this));

            fetchFeatures.call(this, this.profileProduct.id);
            fetchTestCampaigns.call(this, this.concatenateParams());
        },

        concatenateParams: function () {
            this.params = [];
            if (this.productName) {
                this.params.push('productName=' + this.productName);
            }

            if (this.dropName) {
                this.params.push('dropName=' + this.dropName);
            }

            if (this.featureName) {
                this.params.push('featureName=' + this.featureName);
            }

            return this.params.join('&');
        },

        getSelectedTestCampaign: function () {
            return this.testCampaignSelect.getValue();
        }
    });

    function fetchTestCampaigns (search) {
        this.testPlansCompletionCollection = new CompletionsCollection();
        this.testPlansCompletionCollection.setResource('test-campaigns');
        this.testPlansCompletionCollection.setSearch(search);
        this.testPlansCompletionCollection.setLimit(6000);
        this.testPlansCompletionCollection.fetch({
            reset: true,
            statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                200: function () {
                    var referenceItems = [];
                    this.testPlansCompletionCollection.each(function (testPlanCompletionModel) {
                        referenceItems.push({
                            value: testPlanCompletionModel.getValue(),
                            name: testPlanCompletionModel.getName()
                        });
                    });
                    this.testCampaignSelect.setItems(referenceItems);
                }.bind(this)
            })
        });
    }

    function fetchDrops (productId) {
        this.dropCollection = new DomainDataCollection([], {
            type: 'drop'
        });
        this.dropCollection.setProductId(productId);
        this.dropCollection.fetch({
            success: function (data) {
                var items = data.toJSON();
                this.dropSelect.setItems(items);
            }.bind(this)
        });
    }

    function fetchFeatures (productId) {
        this.featureCollection = new DomainDataCollection([], {
            type: 'feature'
        });
        this.featureCollection.setProductId(productId);
        this.featureCollection.fetch({
            success: function (data) {
                var items = data.toJSON();
                this.featureSelect.setItems(items);
            }.bind(this)
        });
    }

});
