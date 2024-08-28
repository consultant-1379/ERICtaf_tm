/*global define*/
define([
    '../../../ext/mvpCollection',
    './RequirementModel'
], function (Collection, RequirementModel) {
    'use strict';

    return Collection.extend({

        Model: RequirementModel,

        init: function () {
            this.searchQuery = '';
        },

        url: function () {
            var url =  '/tm-server/api/requirements/' + this.escape(this.requirementId) + '?view=reverseTree';
            if (this.projectId) {
                url = url + '&projectId=' + this.projectId;
            }
            return url;
        },

        setRequirementId: function (requirementId) {
            this.requirementId = requirementId;
        },

        setProjectId: function (projectId) {
            this.projectId = projectId;
        }

    });

});
