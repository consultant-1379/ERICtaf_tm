define([
    'jscore/ext/mvp'
], function (mvp) {
    'use strict';

    return mvp.Model.extend({

        url: '/tm-server/api/statistics/test-cases',

        getName: function () {
            return this.getAttribute('name');
        },

        getValue: function () {
            return this.getAttribute('value');
        },

        setValue: function (value) {
            this.setAttribute('value', value);
        },

        setName: function (name) {
            this.setAttribute('name', name);
        }

    });

});
