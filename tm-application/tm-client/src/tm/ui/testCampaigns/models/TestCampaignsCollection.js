/*global define*/
define([
    '../../../common/collection/PaginatedCollection',
    './TestCampaignModel'
], function (Collection, TestCampaignModel) {
    /*jshint validthis:true */
    'use strict';

    return Collection.extend({

        Model: TestCampaignModel,

        product: null,
        drop: null,
        features: null,
        components: [],

        url: function () {
            var url = '/tm-server/api/test-campaigns?';
            if (this.product) {
                url = url.concat('product=', this.product, '&');
            }
            if (this.features) {
                this.features.forEach(function (feature) {
                    url = url.concat('feature=', feature, '&');
                });
            }
            if (this.drop) {
                url = url.concat('drop=', this.drop, '&');
            }
            if (this.components.length > 0) {
                this.components.forEach(function (component) {
                    url = url.concat('component=', component, '&');
                });
            }
            var params = this.getPagingParams();
            return url + params.join('&');
        },

        setSearchCriteria: function (searchCriteria) {
            if (searchCriteria) {
                productOperator.call(this, searchCriteria);
                if (searchCriteria.drop) {
                    this.drop = searchCriteria.drop.id;
                } else {
                    this.drop = null;
                }
                featureOperator.call(this, searchCriteria);
                componentOperator.call(this, searchCriteria);
            }
            this.resetPage();
        },

        parse: function (data) {
            this.totalCount = data.totalCount;
            return data.items;
        }

    });

    function productOperator (searchCriteria) {
        if (searchCriteria.product) {
            this.product = searchCriteria.product.id;
        } else {
            this.product = null;
        }
    }

    function featureOperator (searchCriteria) {
        if (searchCriteria.feature) {
            this.features = [];
            searchCriteria.feature.forEach(function (featureItem) {
                this.features.push(featureItem.id);
            }, this);
        } else {
            this.features = [];
        }
    }

    function componentOperator (searchCriteria) {
        if (searchCriteria.components) {
            this.components = [];
            searchCriteria.components.forEach(function (component) {
                this.components.push(component.id);
            }, this);
        } else {
            this.components = [];
        }
    }

});
