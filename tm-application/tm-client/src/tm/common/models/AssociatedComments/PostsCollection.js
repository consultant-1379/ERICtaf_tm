define([

    '../../../ext/mvpCollection',
    './PostModel'
], function (Collection, PostModel) {
    'use strict';

    return Collection.extend({

        Model: PostModel,

        url: function () {
            if (!this.resourceRoot) {
                throw new Error('Resource Root should be defined for PostsCollection!');
            }
            if (!this.objectId) {
                throw new Error('Object ID should be defined for PostsCollection!');
            }
            return '/tm-server/api/' + this.resourceRoot + '/' + this.objectId + '/comments/';
        },

        getResourceRoot: function () {
            return this.resourceRoot;
        },

        setResourceRoot: function (resourceRoot) {
            this.resourceRoot = resourceRoot;
        },

        getObjectId: function () {
            return this.objectId;
        },

        setObjectId: function (objectId) {
            this.objectId = objectId;
        },

        getTotalCommentsCount: function () {
            return this.totalCommentsCount;
        },

        parse: function (data) {
            this.totalCommentsCount = data.totalCommentsCount;
            return data.posts || [];
        }

    }, {
        TEST_CASES_ROOT: 'test-cases'
    });

});
