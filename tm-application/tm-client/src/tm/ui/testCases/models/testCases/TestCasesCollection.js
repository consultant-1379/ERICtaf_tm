define([
    '../../../../common/collection/PaginatedCollection',
    './TestCaseModel'
], function (PaginatedCollection, TestCaseModel) {
    'use strict';

    return PaginatedCollection.extend({

        Model: TestCaseModel,

        url: function () {
            var params = this.getPagingParams();
            return '/tm-server/api/test-cases?' + params.join('&');
        },

        getCriterions: function () {
            return this.criterions;
        },

        setSearchQueryAndResetPage: function (searchQuery) {
            this.setSearchQuery(searchQuery);
            this.resetPage();
        },

        parse: function (data) {
            this.setTotalCount(data.totalCount);
            this.criterions = data.meta.query.criteria;
            return data.items;
        }

    });

});
