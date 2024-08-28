/*global define*/
define([
    'widgets/WidgetCore',
    './SelectedItemView'
], function (WidgetCore, View) {
    'use strict';

    var SelectedItem = WidgetCore.extend({

        View: View,

        init: function () {
            this.item = {};
            this.index = 0;
            this.selected = false;
        },

        onViewReady: function () {
            this.item = this.options.item;

            this.setIndex(this.options.index);

            this.view.getTitle().setText(this.item.name);

            this.view.getActionIcon().addEventHandler('click', this.onActionIconClick, this);
            this.addEventHandler(SelectedItem.SELECT_ITEM, this.onItemSelect, this);
            this.addEventHandler(SelectedItem.DESELECT_ITEM, this.onItemDeselect, this);
        },

        setIndex: function (index) {
            this.index = index;
            this.getElement().setAttribute('data-index', this.index);
        },

        getIndex: function () {
            return this.index;
        },

        isSelected: function () {
            return this.selected;
        },

        onActionIconClick: function (event) {
            this.trigger(SelectedItem.DELETE_ITEM, this.index);
            event.preventDefault();
        },

        onItemSelect: function () {
            this.view.getRoot().setModifier('active');
            this.selected = true;
        },

        onItemDeselect: function () {
            this.view.getRoot().removeModifier('active');
            this.selected = false;
        }

    }, {
        SELECT_ITEM: 'selectitem',
        DESELECT_ITEM: 'deselectitem',
        DELETE_ITEM: 'deleteitem'
    });

    return SelectedItem;

});
