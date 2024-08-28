/*global define*/
define([
    '../../../common/collection/PaginatedCollection',
    './SuiteModel'
], function (Collection, SuiteModel) {
    'use strict';

    return Collection.extend({

        Model: SuiteModel,

        url: function () {
            var search = [];
            if (this.product) {
                search.push('product=' + this.product);
            }

            if (this.feature) {
                search.push('feature=' + this.feature);
            }
            this.setSearchQuery(search.join('&'));
            var params = this.getPagingParams();
            return '/tm-server/api/suite?' + params.join('&');
        },

        setProduct: function (product) {
            this.product = product;
        },

        setFeature: function (feature) {
            this.feature = feature;
        },

        parse: function (data) {
            this.setTotalCount(data.totalCount);
            this.criterions = data.meta.query.criteria;
            return data.items;
        }
    });

});
