define([
    'jscore/core',
    'jscore/ext/utils/base/underscore',
    'widgets/table/Cell',
    'widgets/SelectBox',
    '../../ObjectHelper'
], function (core, _, Cell, SelectBox, ObjectHelper) {
    'use strict';

    return function (options) {

        return Cell.extend({

            setValue: function () {
                var attribute = this.options.column.attribute,
                    data = this.options.row.getData();
                var version = ObjectHelper.findValue(data, attribute);

                var items = this.getRow().getData().testCaseVersions;
                items.forEach(function (item) {
                    item.name = item.title;
                });

                this.selectBox = new SelectBox({
                    items: items
                });

                if (version) {
                    this.selectBox.setValue({ name: version, value: version, title: version});
                }
                this.selectBox.attachTo(this.view.getElement());

                this.selectBox.addEventHandler('change', function () {
                    if (options.action) {
                        options.action(data.testCase, this.selectBox.getValue().name);
                    }
                }.bind(this));
            },

            getValue: function () {
                return this.selectBox.getValue();
            },

            setTooltip: function () {
                if (_.isFunction(this.selectBox.getTooltip)) {
                    this.view.getElement().setAttribute('title', this.selectBox.getTooltip());
                }
            }

        });

    };

});
