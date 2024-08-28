/*global define*/
define([
    '../../../common/collection/PaginatedCollection',
    './ProjectModel'
], function (Collection, ProjectModel) {
    'use strict';

    return Collection.extend({

        Model: ProjectModel,

        url: function () {
            var search = [];
            if (this.product) {
                search.push('product=' + this.product);
            }

            this.setSearchQuery(search.join('&'));
            var params = this.getPagingParams();
            return '/tm-server/api/projects?' + params.join('&');
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
