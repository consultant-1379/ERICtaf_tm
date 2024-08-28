define([
    'jscore/ext/utils/base/underscore',
    '../../ext/mvpCollection'
], function (_, Collection) {
    'use strict';

    return Collection.extend({

        init: function () {
            this.totalCount = 0;
            this.perPage = 20;
            this.page = 1;
            this.isPaginated = true;
            this.sortData = {};
            this.filtersData = {};
            this.searchQuery = '';

            this.onInit();
        },

        onInit: function () {},

        getPagingParams: function () {
            var params = [];
            if (this.isPaginated) {
                params.push('perPage=' + this.getPerPage());
                params.push('page=' + this.getPage());
            }

            if (!_.isEmpty(this.getSortData())) {
                params.push('orderMode=' + this.getSortData().sortMode);
                params.push('orderBy=' + this.getSortData().sortAttr);
            }

            var searchQueries = [];
            if (!_.isEmpty(this.searchQuery)) {
                searchQueries.push(this.searchQuery);
            }
            if (!_.isEmpty(this.getFiltersData())) {
                _.map(this.getFiltersData(), function (filterObj, key) {
                    searchQueries.push(key + filterObj.comparator + filterObj.value);
                });
            }
            if (!_.isEmpty(searchQueries)) {
                params.push('q=' + this.escape(searchQueries.join('&')));
            }
            return params;
        },

        getPerPage: function () {
            return this.perPage;
        },

        getPage: function () {
            return this.page;
        },

        getTotalCount: function () {
            return this.totalCount;
        },

        getSortData: function () {
            return this.sortData;
        },

        getFiltersData: function () {
            return this.filtersData;
        },

        getSearchQuery: function () {
            return this.searchQuery;
        },

        setPerPage: function (perPage) {
            this.perPage = perPage;
        },

        setPage: function (page) {
            this.page = page;
        },

        setTotalCount: function (totalCount) {
            this.totalCount = totalCount;
        },

        setSortData: function (sortData) {
            this.sortData = sortData;
        },

        setFiltersData: function (filtersData) {
            this.filtersData = filtersData;
        },

        setSearchQuery: function (searchQuery) {
            this.searchQuery = searchQuery;
        },

        resetPage: function () {
            this.setTotalCount(0);
            this.setPage(1);
        }

    });

});
