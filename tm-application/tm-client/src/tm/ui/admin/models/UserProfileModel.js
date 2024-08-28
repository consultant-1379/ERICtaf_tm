define([
    '../../../ext/mvpModel'
], function (Model) {
    'use strict';

    return Model.extend({

        url: function () {
            return '/tm-server/api/users/';
        },

        getName: function () {
            return this.getAttribute('userName');
        },

        getSignum: function () {
            return this.getAttribute('userId');
        },

        getAdministrator: function () {
            return this.getAttribute('administrator');
        }

    });

});
