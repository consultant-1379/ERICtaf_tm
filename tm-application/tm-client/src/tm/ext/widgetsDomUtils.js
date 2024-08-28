/*global define, document*/
define([

], function () {
    'use strict';

    return {

        /**
         * Uses canvas.measureText to compute and return the width of the given text of given font in pixels.
         *
         * @param {String} text The text to be rendered.
         * @param {String} fontStyle The css font descriptor that text is to be rendered with (e.g. 'bold 14px verdana').
         *
         * @returns {TextMetrics}
         */
        getTextWidth: function (text, fontStyle) {
            if (!fontStyle) {
                fontStyle = 'normal 12px Arial';
            }
            // if given, use cached canvas for better performance
            // else, create new canvas
            var canvas = this.getTextWidth.canvas || (this.getTextWidth.canvas = document.createElement('canvas'));
            var context = canvas.getContext('2d');
            context.font = fontStyle;
            return context.measureText(text);
        },

        /**
         * Returns caret position in the TextBox.
         *
         * @param {core.Element} element
         * @returns {Object}
         */
        getCaretPosition: function (element) {
            return {
                start: element.getProperty('selectionStart'),
                end: element.getProperty('selectionEnd')
            };
        }

    };

});
