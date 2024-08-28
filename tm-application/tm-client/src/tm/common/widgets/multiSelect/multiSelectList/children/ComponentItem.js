/*global define*/
define([
    'jscore/core'
], function (core) {
    'use strict';

    var View = core.View.extend({

        getTemplate: function () {
            return '<div class="ebComponentList-item"></div>';
        },

        getRoot: function () {
            return this.getElement();
        }

    }, {
        SELECTED_MODIFIER: 'selected'
    });

    var ComponentItem = core.Widget.extend({

        View: View,

        init: function () {
            this.index = 0;
        },

        onViewReady: function () {
            var title = this.options.title,
                name = this.options.name,
                rootEl = this.view.getRoot();

            this.index = this.options.index;

            if (title) {
                rootEl.setAttribute('title', title);
            }
            rootEl.setText(name);

            rootEl.addEventHandler('click', function (event) {
                this.trigger(ComponentItem.ITEM_CLICKED, this.index);
                event.preventDefault();
            }, this);
        },

        setSelected: function (isSelected) {
            var rootEl = this.view.getRoot();
            if (isSelected) {
                rootEl.setModifier(View.SELECTED_MODIFIER);
            } else {
                rootEl.removeModifier(View.SELECTED_MODIFIER);
            }
        },

        getIndex: function () {
            return this.index;
        }

    }, {
        ITEM_CLICKED: 'itemclicked'
    });

    return ComponentItem;

});
