/*global define*/
define([
    '../../../common/collection/PaginatedCollection',
    './TestCampaignGroupModel'
], function (Collection, TestCampaignGroupModel) {
    'use strict';

    return Collection.extend({

        Model: TestCampaignGroupModel,

        url: function () {
            var search = [];
            if (this.product) {
                search.push('product=' + this.product);
            }

            this.setSearchQuery(search.join('&'));
            var params = this.getPagingParams();
            return '/tm-server/api/testCampaignGroup?' + params.join('&');
        },

        setProduct: function (product) {
            this.product = product;
        },

        parse: function (data) {
            this.setTotalCount(data.totalCount);
            this.criterions = data.meta.query.criteria;
            return data.items;
        }
    });

});
