/*global define*/
define([
    '../../ext/mvpModel'
], function (Model) {
    'use strict';

    return Model.extend({

        getFileName: function () {
            return this.getAttribute('fileName');
        },

        getType: function () {
            return this.getAttribute('type');
        }
    });

});
