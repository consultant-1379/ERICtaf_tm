define([
    'jscore/core',
    'widgets/WidgetCore',
    './selectedItem/SelectedItem',
    '../../../../common/Logger'
], function (core, WidgetCore, SelectedItem, Logger) {
    'use strict';

    var logger = new Logger('SelectedItemsList');

    var View = core.View.extend({

        getTemplate: function () {
            return '<ul class="ebMultiSelectList"></ul>';
        },

        getRoot: function () {
            return this.getElement();
        }

    });

    var SelectedItemsList = WidgetCore.extend({
        /*jshint validthis:true*/

        View: View,

        init: function () {
            this.items = [];
            this._widgets = [];
            this.selectedItem = null;

            this.itemActions = {
                goToLeft: goToPreviousItem,
                goToRight: goToNextItem,
                backspaceDelete: backspaceDelete,
                normalDelete: normalDelete,
                deselect: deselectItem
            };
        },

        onViewReady: function () {
            this.setItems(this.items);
            this.addEventHandler('action', this.onWidgetAction, this);
        },

        setSelectedItem: function (index) {
            this._widgets.forEach(function (widget) {
                widget.trigger(SelectedItem.DESELECT_ITEM);
            });
            this.selectedItem = null;
            if (index !== null) {
                this._widgets[index].trigger(SelectedItem.SELECT_ITEM);
                this.selectedItem = this._widgets[index];
            }
        },

        hasSelectedItem: function () {
            return this.selectedItem !== null;
        },

        setItems: function (items) {
            clear.call(this);

            this.items = items || [];
            this._widgets = [];

            var selectedItem, index = 0;
            items.forEach(function (itemObj) {
                selectedItem = new SelectedItem({
                    item: itemObj,
                    index: index
                });
                selectedItem.addEventHandler(SelectedItem.DELETE_ITEM, this.onItemDelete, this);

                this._widgets.push(selectedItem);
                selectedItem.attachTo(this.view.getRoot());
                index++;
            }.bind(this));
        },

        getItems: function () {
            return this.items;
        },

        addItem: function (itemObj) {
            var index = this._widgets.length,
                selectedItem = new SelectedItem({
                    item: itemObj,
                    index: index
                });
            selectedItem.addEventHandler(SelectedItem.DELETE_ITEM, this.onItemDelete, this);

            this.items.push(itemObj);
            this._widgets.push(selectedItem);
            selectedItem.attachTo(this.view.getRoot());
        },

        onWidgetAction: function (action) {
            if (this.itemActions[action] !== undefined) {
                this.itemActions[action].call(this);
            }
        },

        onItemDelete: function (itemIndex) {
            var widgetObj = this._widgets[itemIndex];
            if (!widgetObj) {
                return;
            }

            deleteItem.call(this, widgetObj);
            updateItemsIndexes.call(this);
            this.selectedItem = null;
        }

    }, {
        ITEM_REMOVED: 'itemremoved'
    });

    return SelectedItemsList;

    function goToPreviousItem () {
        if (!this.hasSelectedItem()) {
            var itemsCount = this._widgets.length;
            if (itemsCount === 0) {
                return;
            }
            this.selectedItem = this._widgets[itemsCount - 1];
            this.selectedItem.trigger(SelectedItem.SELECT_ITEM);
            return;
        }
        if (this.selectedItem.getIndex() === 0) {
            return;
        }

        var previousIndex = this.selectedItem.getIndex() - 1;
        this.selectedItem.trigger(SelectedItem.DESELECT_ITEM);

        this.selectedItem = this._widgets[previousIndex];
        this.selectedItem.trigger(SelectedItem.SELECT_ITEM);
    }

    function goToNextItem () {
        if (!this.hasSelectedItem()) {
            return;
        }
        if (this.selectedItem.getIndex() === this._widgets.length - 1) {
            this.selectedItem.trigger(SelectedItem.DESELECT_ITEM);
            this.selectedItem = null;
            return;
        }

        var nextIndex = this.selectedItem.getIndex() + 1;
        this.selectedItem.trigger(SelectedItem.DESELECT_ITEM);

        this.selectedItem = this._widgets[nextIndex];
        this.selectedItem.trigger(SelectedItem.SELECT_ITEM);
    }

    function backspaceDelete () {
        if (!this.hasSelectedItem()) {
            var itemsCount = this._widgets.length;
            if (itemsCount === 0) {
                return;
            }
            this.selectedItem = this._widgets[itemsCount - 1];
            this.selectedItem.trigger(SelectedItem.SELECT_ITEM);
            return;
        }
        var previousIndex = this.selectedItem.getIndex() - 1;
        if (previousIndex < 0) {
            previousIndex = null;
        }

        deleteItem.call(this, this.selectedItem);
        updateItemsIndexes.call(this);
        this.selectedItem = null;

        if (previousIndex !== null) {
            this.selectedItem = this._widgets[previousIndex];
            this.selectedItem.trigger(SelectedItem.SELECT_ITEM);
        }
    }

    function normalDelete () {
        if (!this.hasSelectedItem()) {
            return;
        }
        var nextIndex = this.selectedItem.getIndex() + 1;
        if (nextIndex === this._widgets.length) {
            nextIndex = null;
        }

        deleteItem.call(this, this.selectedItem);
        updateItemsIndexes.call(this);
        this.selectedItem = null;

        if (nextIndex !== null) {
            this.selectedItem = this._widgets[nextIndex - 1];
            this.selectedItem.trigger(SelectedItem.SELECT_ITEM);
        }
    }

    function deselectItem () {
        logger.info('Item deselected');
        // TODO: ...
    }

    function deleteItem (itemWidget) {
        var itemIndex = itemWidget.getIndex(),
            itemToRemove = this.items[itemIndex];

        // destroy widget
        this._widgets[itemIndex].destroy();

        // remove from arrays
        this._widgets.splice(itemIndex, 1);
        this.items.splice(itemIndex, 1);

        this.trigger(SelectedItemsList.ITEM_REMOVED, itemToRemove);
    }

    function updateItemsIndexes () {
        this._widgets.forEach(function (widget, index) {
            widget.setIndex(index);
        });
    }

    function clear () {
        this._widgets.forEach(function (widget) {
            widget.removeEventHandler(SelectedItem.DELETE_ITEM);
            widget.detach();
            widget.destroy();
        });

        this.widgets = [];
        this.items = [];

        this.trigger(SelectedItemsList.ITEM_REMOVED);
    }

});
