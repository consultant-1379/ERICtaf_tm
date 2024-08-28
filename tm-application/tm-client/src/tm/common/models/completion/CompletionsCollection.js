/*global define*/
define([
    '../../../ext/mvpCollection',
    './CompletionModel'
], function (Collection, CompletionModel) {
    'use strict';

    return Collection.extend({

        Model: CompletionModel,

        init: function () {
            this.resource = null;
            this.productId = null;
            this.featureIds = [];
            this.componentIds = [];
            this.search = '';
            this.limit = 20;
        },

        url: function () {
            var queries = [];

            queries.push('search=' + this.escape(this.search));

            if (this.productId) {
                queries.push('product=' + this.productId);
            }
            if (this.featureIds.length > 0) {
                this.featureIds.forEach(function (id) {
                    queries.push('feature=' + id);
                });
            }
            if (this.componentIds.length > 0) {
                this.componentIds.forEach(function (id) {
                    queries.push('component=' + id);
                });
            }

            if (this.limit && this.limit > 0) {
                queries.push('limit=' + this.limit);
            }

            var queryStr = queries.join('&');
            if (queryStr !== '') {
                queryStr = '?' + queryStr;
            }

            if (this.resource != null) {
                return '/tm-server/api/' + this.resource + '/completion' + queryStr;
            } else {
                throw new Error('Completion resource not set');
            }
        },

        parse: function (data) {
            return data.items;
        },

        setResource: function (resource) {
            this.resource = resource;
        },

        setProductId: function (productId) {
            this.productId = productId;
        },

        setFeatureIds: function (featureIds) {
            this.featureIds = featureIds;
        },

        setComponentIds: function (ids) {
            this.componentIds = ids;
        },

        setSearch: function (search) {
            this.search = search;
        },

        setLimit: function (limit) {
            this.limit = limit;
        }

    });
});
