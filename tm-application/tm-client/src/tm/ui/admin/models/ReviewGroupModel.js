define([
    '../../../ext/mvpModel'
], function (Model) {
    'use strict';

    return Model.extend({

        url: function () {
            return '/tm-server/api/review-group';
        },

        getName: function () {
            return this.getAttribute('name');
        },

        setName: function (name) {
            this.setAttribute('name', name);
        },

        setUsers: function (users) {
            this.setAttribute('users', users);
        },

        getUsers: function () {
            return this.getAttribute('users');
        }
    });

});
