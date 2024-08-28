/*global define*/
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

        parse: function (data) {
            return {
                itemObj: data,
                name: data.name,
                value: data.id
            };
        }

    });

});
