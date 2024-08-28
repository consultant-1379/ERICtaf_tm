define([
    'widgets/WidgetCore',
    './StackedProgressBarView',
    './ProgressBar/ProgressBar'
], function (WidgetCore, View, ProgressBar) {
    'use strict';

    /**
     * The StackedProgressBar class uses the Ericsson brand assets.<br>
     * The StackedProgressBar can be instantiated using the constructor ProgressBar.
     *
     * @options
     *   {String} label - Optional, a string used as a progress bar caption.
     *     {String} icon - Optional, a string used to define an icon to show next to label. You can use any of the icons from ebIcon. Icons should be declared as follows, icon: 'iconName'. The ebIcon_ prefix is not required in the declaration.
     * @class widgets/StackedProgressBar
     * @extends WidgetCore
     */
    return WidgetCore.extend({

        view: function () {
            return new View(this.options);
        },

        /**
         * The init method is automatically called by the constructor when using the 'new' operator. If an object with
         * key/value pairs was passed into the constructor then the options variable will have those key/value pairs.
         *
         * @method init
         * @param {Object} options
         * @private
         */
        init: function (options) {
            this.options = options || {};
        },

        /**
         * Overrides method from widget.
         * Executes every time, when added back to the screen.
         *
         * @method onViewReady
         * @private
         */
        onViewReady: function () {
            this.barWidgets = [];

            this.view.iconModifier = this.options.icon;

            // if we have label or icon we need to show Header section
            if (this.options.label || this.options.icon) {
                this.view.showHeader();
            }

            if (!this.options.icon) {
                this.view.toggleElement(this.view.getIcon());
            }

            if (this.options.items) {
                this.setItems(this.options.items);
            }

        },

        /**
         * Overrides method from widget.
         * item = {
         *  value: 50,
         *  color: red
         * }
         *
         * @method setItems
         * @param {Object} items list of items
         * @private
         */
        setItems: function (items) {
            this.clearAll();

            if (items) {
                var total = 0;
                var isCorrect = true;
                items.forEach(function (item) {
                    total += item.value;
                    isCorrect = lessThan(total, 100);
                });
                if (isCorrect && items.length !== 0) {
                    this.view.getProgressValue().setText('');
                    items.forEach(function (item) {
                        var progressbar = new ProgressBar({
                            value: item.value,
                            color: item.color,
                            title: item.title
                        });

                        if (item.report != null) {
                            this.view.getProgressValue().setText(item.value.toFixed(1) + '%');
                            this.view.getProgressValue().setStyle('color', item.color);
                        }

                        progressbar.attachTo(this.view.getBar());
                        this.barWidgets.push(progressbar);
                    }.bind(this));
                    if (this.barWidgets.length === 1) {
                        this.barWidgets[0].setModifier('round');
                    } else {
                        this.barWidgets[0].setModifier('first');
                        this.barWidgets[this.barWidgets.length - 1].setModifier('last');
                    }
                } else {
                    console.warn('The data provided is greater than 100%');
                }
            }
        },
        /**
         * Get all Items of the Stacked Progress bar.
         *
         */
        getItems: function () {
            var items = [];
            this.barWidgets.forEach(function (item) {
                items.push(item.options);
            });

            return items;
        },

        /**
         * Set/Update the label for stacked progress bar.
         *
         * @method setLabel
         * @param  {string} label text for progress bar
         */
        setLabel: function (label) {
            if (label && typeof label === 'string') {
                this.view.setLabel(label);
            } else {
                throw new Error('You must enter valid label.');
            }
        },

        /**
         * Set icon for label and stacked progress bar.
         *
         * @method setIcon
         * @param  {string} icon Ex. tick, close, etc.
         */
        setIcon: function (icon) {
            if (icon && typeof icon === 'string') {
                this.view.setIcon(icon);
            } else {
                throw new Error('You must specify valid icon.');
            }
        },

        /**
         * Set the bar width only of the stacked progress bar.
         *
         * @method setWidth
         * @param  {string} can be any width value rem, px, em etc.
         */
        setWidth: function (value) {
            this.view.getBar().setStyle('width', value);
        },

        clearAll: function () {
            this.barWidgets.forEach(function (widget) {
                widget.destroy();
            });

            this.barWidgets = [];
        }

    });

    function lessThan (value, range) {
        if (value > range) {
            return false;
        } else {
            return true;
        }
     }
});
