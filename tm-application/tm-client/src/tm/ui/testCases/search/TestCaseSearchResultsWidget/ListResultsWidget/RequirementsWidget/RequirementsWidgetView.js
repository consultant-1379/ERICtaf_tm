/*global define*/
define([
    'jscore/core',
    'template!./RequirementsWidget.html',
    'styles!cssBlockers/widgets/requirements/_requirements.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        getTemplate: function () {
            return template({template: this.options.template});
        },

        getStyle: function () {
            return styles;
        }

    });

});
