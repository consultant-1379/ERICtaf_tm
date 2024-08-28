define([
    'widgets/table/Cell',
    '../../ObjectHelper'
], function (Cell, ObjectHelper) {
    'use strict';

    return function () {

        return Cell.extend({

            setValue: function () {
                var attribute = this.options.column.attribute,
                    data = this.options.row.getData();
                this.value = ObjectHelper.findValue(data, attribute);
                this.view.getElement().setText(this.value);
            },

            getValue: function () {
                return this.value;
            },

            setTooltip: function () {
                this.view.getElement().setAttribute('title', this.value);
            }

        });

    };

});
