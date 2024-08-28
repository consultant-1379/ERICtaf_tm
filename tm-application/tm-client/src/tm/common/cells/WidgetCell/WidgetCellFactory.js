define([
    'jscore/core',
    'jscore/ext/utils/base/underscore',
    'widgets/table/Cell'
], function (core, _, Cell) {
    'use strict';

    return function (options) {

        return Cell.extend({

            setValue: function (value) {
                var WidgetType = options.widget;
                this.widget = new WidgetType({
                    value: value
                });
                this.widget.attachTo(this.view.getElement());
            },

            getValue: function () {
                return this.value;
            },

            setTooltip: function () {
                if (_.isFunction(this.widget.getTooltip)) {
                    this.view.getElement().setAttribute('title', this.widget.getTooltip());
                }
            }

        });

    };

});
