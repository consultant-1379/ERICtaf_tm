define([
    'jscore/core',
    'text!./ReviewInboxAssignmentsWidget.html'
], function (core, template) {
    'use strict';

    return core.View.extend({

        getTemplate: function () {
            return template;
        }

    });

});
