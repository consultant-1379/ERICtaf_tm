define([
    '../../../common/collection/PaginatedCollection',
    './AssignmentModel'
], function (Collection, AssignmentModel) {
    'use strict';

    return Collection.extend({

        Model: AssignmentModel,

        url: function () {
            var params = this.getPagingParams();

            params.push('view=detailed');

            return '/tm-server/api/assignments?' + params.join('&');
        },

        parse: function (data) {
            this.totalCount = data.totalCount;
            return data.items;
        }

    });
});
