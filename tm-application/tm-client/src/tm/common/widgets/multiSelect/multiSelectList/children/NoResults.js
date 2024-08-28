/*global define*/
define([
    'jscore/core'
], function (core) {
    'use strict';

    var View = core.View.extend({

        getTemplate: function () {
            return '<div class="ebComponentList-notFound">Items not found.</div>';
        }

    });

    return core.Widget.extend({

        View: View

    });

});
