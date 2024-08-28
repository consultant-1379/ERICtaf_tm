define([
    '../../../ext/mvpModel'
], function (Model) {
    'use strict';

    return Model.extend({

        getId: function () {
            return this.getAttribute('id');
        },

        getName: function () {
            return this.getAttribute('name');
        },

        getVersion: function () {
            return this.getAttribute('version');
        },

        parse: function (data) {
            return {
                itemObj: data,
                name: data.version,
                value: data.id
            };
        }

    });

});
