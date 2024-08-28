/*global define*/
define([
    '../../../ext/mvpModel'
], function (Model) {
    'use strict';

    return Model.extend({

        getId: function () {
            return this.getAttribute('id');
        },

        getExternalId: function () {
            return this.getAttribute('externalId');
        },

        getType: function () {
            return this.getAttribute('type');
        },

        getTitle: function () {
            return this.getAttribute('title');
        },

        getSummary: function () {
            return this.getAttribute('summary');
        },

        getProject: function () {
            return this.getAttribute('project');
        },

        getTestCasesCount: function () {
            return this.getAttribute('testCaseCount');
        },

        getChildren: function () {
            return this.getAttribute('children');
        },

        getMatching: function () {
            return this.getAttribute('matching');
        },

        getExternalStatusName: function () {
            return this.getAttribute('externalStatusName');
        },

        getDelivered: function () {
            return this.getAttribute('deliveredIn');
        }

    });

});
