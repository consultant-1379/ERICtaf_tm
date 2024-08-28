/*global define*/
define([
    '../../../common/collection/PaginatedCollection',
    './GroupModel'
], function (Collection, GroupModel) {
    'use strict';

    return Collection.extend({

        Model: GroupModel,

        url: function () {
            var search = [];
            if (this.product) {
                search.push('product=' + this.product);
            }

            this.setSearchQuery(search.join('&'));
            var params = this.getPagingParams();
            return '/tm-server/api/scopes?' + params.join('&');
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
