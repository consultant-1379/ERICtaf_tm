/*global define*/
define([
    'jscore/core'
], function (core) {
    'use strict';

    var View = core.View.extend({

        getTemplate: function () {
            return '<div class="ebComponentList-separator"></div>';
        }

    });

    return core.Widget.extend({

        View: View

    });

});
