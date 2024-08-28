
/*global define*/
define([
    'jscore/ext/utils/base/underscore',
    '../../../ext/mvpCollection',
    './MetadataModel'
], function (_, Collection, MetadataModel) {
    'use strict';

    return Collection.extend({

        Model: MetadataModel,

        init: function () {
            this.referenceIds = {};
            this.references = {};
            this.projectId = null;

            this.addEventHandler('reset', function (data) {
                this.references = {};
                data.each(function (reference) {
                    var id = reference.getId();
                    this.references[id] = reference;
                }.bind(this));
            }, this);
        },

        url: function () {
            return '/tm-server/api/jira/defect-metadata?projectId=' + this.projectId;
        },

        setProjectId: function (projectId) {
            this.projectId = projectId;
        },

        getById: function (id) {
            return this.references[id];
        }

    });

});
