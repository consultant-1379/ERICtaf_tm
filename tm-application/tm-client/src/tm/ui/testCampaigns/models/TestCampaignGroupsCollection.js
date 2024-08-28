/*global define*/
define([
    '../../../common/collection/PaginatedCollection',
    './TestCampaignGroupModel'
], function (Collection, TestCampaignGroupModel) {
    /*jshint validthis:true */
    'use strict';

    return Collection.extend({

        Model: TestCampaignGroupModel,

        product: null,

        url: function () {
            var url = '/tm-server/api/testCampaignGroup?';
            if (this.product) {
                url = url.concat('product=', this.product, '&');
            }

            var params = this.getPagingParams();
            return url + params.join('&');
        },

        setSearchCriteria: function (searchCriteria) {
            if (searchCriteria) {
                productOperator.call(this, searchCriteria);
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

});
