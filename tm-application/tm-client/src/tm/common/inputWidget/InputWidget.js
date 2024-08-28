define([
    'jscore/core',
    './InputWidgetView'
], function (core, View) {
    /*jshint validthis:true*/
    'use strict';

    var InputWidget = core.Widget.extend({

        View: View,

        init: function (options) {
            this.label = options.label;
            this.id = options.id;
        },

        onViewReady: function () {
            this.view.afterRender();
            this.getElement().find('span').setText(this.label);
            this.view.getInput().setProperty('id', 'TMS_Input_eaTM-Input-field-' + this.id.replace(/\s+/g, '_'));
        },

        getValue: function () {
            return this.view.getInput().getValue();
        },

        setValue: function (value) {
            this.view.getInput().setValue(value);
        },

        highlightTextField: function () {
            this.view.getInput().setModifier('highlight');
        },

        removeHighlightTextField: function () {
            this.view.getInput().removeModifier('highlight');
        }

    });
    return InputWidget;

});
