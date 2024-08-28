/*global define*/
define([
    'jscore/core'
], function (core) {
    'use strict';

    var View = core.View.extend({

        getTemplate: function () {
            return '<div class="ebLoader">' +
                '<div class="ebLoader-Holder">' +
                '<span class="ebLoader-Dots"></span>' +
                '</div>' +
                '</div>';
        }

    });

    return core.Widget.extend({

        View: View

    });

});
