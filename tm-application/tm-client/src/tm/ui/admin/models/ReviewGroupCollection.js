/*global define*/
define([
    '../../../common/collection/PaginatedCollection',
    './ReviewGroupModel'
], function (Collection, GroupModel) {
    'use strict';

    return Collection.extend({

        Model: GroupModel,

        url: function () {
            var search = [];

            this.setSearchQuery(search.join('&'));
            var params = this.getPagingParams();
            return '/tm-server/api/review-group?' + params.join('&');
        },

        parse: function (data) {
            this.setTotalCount(data.totalCount);
            this.criterions = data.meta.query.criteria;
            return data.items;
        }
    });

});
