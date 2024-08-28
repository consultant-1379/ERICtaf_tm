/*global define*/
define([
    '../../../common/collection/PaginatedCollection',
    './ProductModel'
], function (Collection, ProductModel) {
    'use strict';

    return Collection.extend({

        Model: ProductModel,

        url: function () {
            var search = [];
            this.setSearchQuery(search.join('&'));
            var params = this.getPagingParams();
            return '/tm-server/api/products?' + params.join('&');
        }
    });

});
