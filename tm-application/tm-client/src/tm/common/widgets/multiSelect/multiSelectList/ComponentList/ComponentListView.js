define([
    'jscore/core',
    'template!./_componentList.html',
    'styles!./_componentList.less',
    'widgets/utils/domUtils',
    'template!./_componentListPartial.html',
    'jscore/ext/utils/base/underscore'
], function (core, template, style, domUtils, componentListPartial, _) {
    'use strict';

    return core.View.extend({

        getTemplate: function () {
            var data = _.clone(this.options);
            if (data.items) {
                if (data.items && data.items[0] && data.items[0].items !== undefined) {
                    data.items.forEach(function (group) {
                        this.prepareItemsData(group.items);
                    }.bind(this));
                } else {
                    this.prepareItemsData(data.items);
                }
            }

            return template(data, {
                partials: {
                    item: componentListPartial
                }
            });
        },

        getStyle: function () {
            return style;
        },

        prepareItemsData: function (items) {
            items.forEach(function (item) {
                if (item.type === 'separator') {
                    item._separator = true;
                } else if (!item.link) {
                    item._action = true;
                }

                if (this.options.checkboxes && item.type !== 'separator') {
                    item._checkbox = true;
                }
            }.bind(this));

            // remove invalid separators
            for (var i = 0; i < items.length; i++) {
                var item = items[i];
                if (item.type === 'separator') {
                    if (!items[i - 1] || !items[i + 1]) {
                        item._separator = false;
                        items.splice(i, 1);
                        i--;
                    }
                }
            }
        },

        hbeach: function () {

        },

        getItems: function () {
            if (!this._allItems) {
                this._allItems = domUtils.findAll('.ebComponentList-item, .ebComponentList-separator',
                    this.getElement());
            }
            return this._allItems;
        },

        getSelectableItems: function () {
            if (!this.__selectableItems) {
                this.__selectableItems = domUtils.findAll('.ebComponentList-item:not(.ebComponentList-item_disabled)',
                    this.getElement());
            }
            return this.__selectableItems;
        },

        getMessageInfo: function () {
            return this.getElement().find('.ebComponentList-info');
        },
        getSelectAllLink: function () {
            return domUtils.findAll('.eaTM-MultiSelectBox-selectAll', this.getElement())[0];
        },
        getDeSelectAllLink: function () {
            return domUtils.findAll('.eaTM-MultiSelectBox-deselectAll', this.getElement())[0];
        },
        getSelectDeSelectDiv: function () {
            return domUtils.findAll('.eaTM-MultiSelectBox-selectDeselectAllWrap', this.getElement())[0];
        }
    });

});
