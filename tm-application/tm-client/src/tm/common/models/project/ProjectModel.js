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

        getName: function () {
            return this.getAttribute('name');
        }

    });

});
