/*global define*/
define([
    '../../../../ext/mvpCollection',
    './RequirementIdModel'
], function (Collection, RequirementIdModel) {
    'use strict';

    return Collection.extend({

        Model: RequirementIdModel,

        init: function () {
            this.responseSearchValue = '';
            this.searchValue = '';
            this.limit = 20;
            this.types  = [];
        },

        url: function () {
            var queries = [];

            queries.push('search=' + this.escape(this.searchValue));
            if (this.limit && this.limit > 0) {
                queries.push('limit=' + this.limit);
            }

            if (this.types.length > 0) {
                this.types.forEach(function (type) {
                    queries.push('type=' + type);
                });
            }

            var queryStr = queries.join('&');
            if (queryStr !== '') {
                queryStr = '?' + queryStr;
            }

            return '/tm-server/api/requirements/completion' + queryStr;
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
        },

        setTypes: function (types) {
            this.types = types;
        }

    });

});
