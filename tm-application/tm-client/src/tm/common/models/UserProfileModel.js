/*global define*/
define([
    '../../ext/mvpModel'
], function (Model) {
    'use strict';

    return Model.extend({

        url: '/tm-server/api/users',

        init: function () {
            this.setAttribute('id', 'me');
        },

        getUserId: function () {
            return this.getAttribute('userId');
        },

        getUserName: function () {
            return this.getAttribute('userName');
        },

        getProject: function () {
            return this.getAttribute('project');
        },

        getProduct: function () {
            return this.getAttribute('product');
        },

        setProduct: function (product) {
            this.setAttribute('product', product);
        },

        getSavedSearch: function () {
            return this.getAttribute('savedSearch');
        },

        setSavedSearch: function (searches) {
            this.setAttribute('savedSearch', searches);
        },

        addSavedSearch: function (item) {
            this.getAttribute('savedSearch').push(item);
        },

        getProjectExternalId: function () {
            var project =  this.getAttribute('project');
            if (project == null) {
                return null;
            }
            return project.externalId;
        },

        getProductExternalId: function () {
            var product =  this.getAttribute('product');
            if (product == null) {
                return null;
            }
            return product.externalId;
        },

        setProject: function (project) {
            this.setAttribute('project', project);
        }

    });

});
