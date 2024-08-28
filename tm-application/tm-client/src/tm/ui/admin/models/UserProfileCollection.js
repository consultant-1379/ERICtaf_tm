/*global define*/
define([
    '../../../common/collection/PaginatedCollection',
    './UserProfileModel'
], function (Collection, UserProfileModel) {
    'use strict';

    return Collection.extend({

        Model: UserProfileModel,

        url: function () {
            var search = [];
            if (this.administrator) {
                search.push('administrator=' + this.administrator);
            }

            this.setSearchQuery(search.join('&'));
            var params = this.getPagingParams();
            return '/tm-server/api/users?' + params.join('&');
        },

        getPerPage: function () {
            return this.perPage;
        },

        getPage: function () {
            return this.page;
        },

        setPerPage: function (perPage) {
            this.perPage = perPage;
        },

        setPage: function (page) {
            this.page = page;
        },

        setAdministrator: function (administrator) {
            this.administrator = administrator;
        },

        parse: function (data) {
            this.setTotalCount(data.totalCount);
            this.criterions = data.meta.query.criteria;
            return data.items;
        }
    });

});
