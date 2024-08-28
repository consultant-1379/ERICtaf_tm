define([
    'widgets/table/Cell',
    '../../ObjectHelper'
], function (Cell, ObjectHelper) {
    'use strict';

    return function (options) {

        return Cell.extend({

            setValue: function () {
                var searchAttribute = options.searchAttribute,
                    collectionAttribute = options.collectionAttribute,
                    data = this.options.row.getData();
                var collection = ObjectHelper.findValue(data, collectionAttribute);
                this.value = ObjectHelper.findAll(collection, searchAttribute);
                this.view.getElement().setText(this.value.join(', '));
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
