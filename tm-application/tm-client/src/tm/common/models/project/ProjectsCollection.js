/*global define*/
define([
    '../../../ext/mvpCollection',
    './ProjectModel'
], function (Collection, ProjectModel) {
    'use strict';

    return Collection.extend({

        Model: ProjectModel,

        url: '/tm-server/api/projects?perPage=300',

        parse: function (data) {
            return data.items;
        }

    });

});
