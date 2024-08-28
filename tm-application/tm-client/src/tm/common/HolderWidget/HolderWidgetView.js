/*global define*/
define([
    'jscore/core'
], function (core) {
    'use strict';

    return core.View.extend({

        init: function () {
            this.content = this.options.content || '';
        },

        getTemplate: function () {
            return '<div class="eaTM-Holder">' + this.content + '</div>';
        }

    });

});
