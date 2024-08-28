/*global define*/
define([
    '../../../common/collection/PaginatedCollection',
    './DropModel'
], function (Collection, DropModel) {
    'use strict';

    return Collection.extend({

        Model: DropModel,

        url: function () {
            var search = [];
            if (this.product) {
                search.push('product=' + this.product);
            }

            this.setSearchQuery(search.join('&'));
            var params = this.getPagingParams();
            return '/tm-server/api/drops?' + params.join('&');
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
