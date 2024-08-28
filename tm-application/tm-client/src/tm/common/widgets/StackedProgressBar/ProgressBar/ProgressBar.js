define([
    'widgets/WidgetCore',
    './ProgressBarView',
    'widgets/Tooltip'
], function (WidgetCore, View, Tooltip) {
    'use strict';
    return WidgetCore.extend({

        view: function () {
            return new View(this.options);
        },

        init: function (options) {
            this.options = options || {};
        },

        onViewReady: function () {
            this.setColor(this.options.color || 'purple');
            this.setValue(this.options.value !== undefined ? this.options.value : 5);

            if (this.options.title != null) {
                var tooltip = new Tooltip({
                    parent: this.getElement()
                });
                tooltip.setContentText(this.options.title);
            }
        },

        /**
         * set color for progress bar
         *
         * @method setColor
         * @param  {string} color Ex. paleBlue, green, darkGreen, red, orange (or hex value such as #aaa)
         */
        setColor: function (color) {
            if (color.indexOf('#') === 0) {
                this.getElement().setStyle('background-color', color);
            } else {
                this.getElement().setModifier('color', color);
            }
        },

        /**
         * Updated the value of progress bar.
         *
         * @method setValue
         * @param  {number} value accepts all positive
         */
        setValue: function (value) {

            if (value !== undefined && typeof value === 'number') {
                this.view.setValue(value);

            } else {
                throw new Error('You must specify value and it should be number.');
            }
        }
    });
});
