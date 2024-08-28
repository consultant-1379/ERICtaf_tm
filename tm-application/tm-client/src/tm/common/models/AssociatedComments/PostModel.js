define([
    '../../../ext/mvpModel'
], function (Model) {
    'use strict';

    return Model.extend({

        url: function () {
            if (!this.resourceRoot) {
                throw new Error('Resource Root should be defined for PostModel!');
            }
            if (!this.getObjectId()) {
                throw new Error('Object ID should be defined for PostModel!');
            }
            return '/tm-server/api/' + this.resourceRoot + '/' + this.getObjectId() + '/comments/';
        },

        getUser: function () {
            return this.getAttribute('user');
        },

        setUser: function (user) {
            return this.setAttribute('user', user);
        },

        getIsDeleted: function () {
            return this.getAttribute('isDeleted');
        },

        getCreatedAt: function () {
            return this.getAttribute('createdAt');
        },

        getMessage: function () {
            return this.getAttribute('message');
        },

        setMessage: function (message) {
            return this.setAttribute('message', message);
        },

        getResourceRoot: function () {
            return this.resourceRoot;
        },

        setResourceRoot: function (resourceRoot) {
            this.resourceRoot = resourceRoot;
        },

        getObjectId: function () {
            return this.getAttribute('objectId');
        },

        setObjectId: function (objectId) {
            return this.setAttribute('objectId', objectId);
        },

        getId: function () {
            return this.getAttribute('id');
        },

        setId: function (id) {
            return this.setAttribute('id', id);
        }

    });

});
