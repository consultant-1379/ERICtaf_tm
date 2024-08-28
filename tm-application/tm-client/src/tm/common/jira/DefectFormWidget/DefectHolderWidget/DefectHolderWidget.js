define([
    'jscore/core',
    './DefectHolderWidgetView'
], function (core, View) {
    /*jshint validthis:true*/
    'use strict';

    var ComboDefectWidget = core.Widget.extend({

        View: View,

        init: function (options) {
            this.title = options.title;
            this.widget = options.widget;
            this.label = options.label;
        },

        onViewReady: function () {
            this.view.afterRender();
            this.view.getLabelText().setText(this.label);
            var parsedTitle = this.title.replace(/\s+/g, '_');
            this.view.getInput().setProperty('id', 'TMS_TestExecution_CreateDefect-' + parsedTitle);
            this.view.getInput().setAttribute('class', 'eaTM-CreateDefect-' + parsedTitle + '-input');
            this.widget.attachTo(this.view.getInput());
        }

    });
    return ComboDefectWidget;

});
