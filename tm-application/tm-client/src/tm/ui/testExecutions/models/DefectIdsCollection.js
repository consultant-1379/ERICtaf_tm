/*global define*/
define([
    '../../../ext/mvpCollection',
    './DefectIdModel'
], function (Collection, DefectIdModel) {
    'use strict';

    return Collection.extend({

        Model: DefectIdModel,

        init: function () {
            this.responseSearchValue = '';
            this.searchValue = '';
            this.limit = 20;
        },

        url: function () {
            var queries = [];

            queries.push('search=' + this.escape(this.searchValue));
            if (this.limit && this.limit > 0) {
                queries.push('limit=' + this.limit);
            }

            var queryStr = queries.join('&');
            if (queryStr !== '') {
                queryStr = '?' + queryStr;
            }

            return '/tm-server/api/defects/completion' + queryStr;
        },

        parse: function (data) {
            this.responseSearchValue = data.search;
            return data.items;
        },

        getResponseSearchValue: function () {
            return this.responseSearchValue;
        },

        setSearchValue: function (searchValue) {
            this.searchValue = searchValue;
        },

        setLimit: function (limit) {
            this.limit = limit;
        }

    });

});
