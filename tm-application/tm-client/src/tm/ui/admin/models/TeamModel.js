define([
    '../../../ext/mvpModel'
], function (Model) {
    'use strict';

    return Model.extend({

        url: function () {
            return '/tm-server/api/team/';
        },

        getName: function () {
            return this.getAttribute('name');
        },

        getFeature: function () {
            return this.getAttribute('feature');
        },

        setName: function (name) {
            return this.setAttribute('name', name);
        },

        setFeature: function (feature) {
            return this.setAttribute('feature', feature);
        }

    });

});
