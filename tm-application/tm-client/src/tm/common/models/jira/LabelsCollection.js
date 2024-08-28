/*global define*/
define([
    '../../../ext/mvpCollection',
    './LabelModel'
], function (Collection, LabelModel) {
    'use strict';

    return Collection.extend({

        Model: LabelModel,

        init: function () {
            this.responseSearchValue = '';
            this.searchValue = '';
        },

        url: function () {
            return '/tm-server/api/jira/labels?query=' + this.escape(this.searchValue);
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
        }

    });

});
