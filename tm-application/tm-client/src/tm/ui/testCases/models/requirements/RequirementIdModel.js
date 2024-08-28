/*global define*/
define([
    '../../../../ext/mvpModel'
], function (Model) {
    'use strict';

    return Model.extend({

        getValue: function () {
            return this.getAttribute('value');
        },

        getName: function () {
            return this.getAttribute('name');
        }

    });

});
