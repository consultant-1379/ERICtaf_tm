/*global define*/
define([
    'jscore/core',
    'styles!./ListResultsWidget.less'
], function (core, styles) {
    'use strict';

    return core.View.extend({

        getTemplate: function () {
            return '<div id="TMS_TestCaseSearch_ListResults" class="eaTM-ListResults"></div>';
        },

        getStyle: function () {
            return styles;
        }

    });

});
