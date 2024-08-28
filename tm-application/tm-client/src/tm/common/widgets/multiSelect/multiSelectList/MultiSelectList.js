/*global define*/
define([
    'jscore/core',
    './ComponentList/ComponentList',
    './children/ComponentItem',
    './children/ComponentSeparator',
    './children/NoResults',
    './children/ProgressBar'
], function (core, ComponentList, ComponentItem, ComponentSeparator, NoResults, ProgressBar) {
    'use strict';

    var View = core.View.extend({

        getTemplate: function () {
            return '<div class="ebComponentList eb_scrollbar"></div>';
        },

        getItemsHolder: function () {
            return this.getElement();
        }

    });

    /**
     * The MultiSelectList class uses the Ericsson brand assets.<br>
     * The MultiSelectList can be instantiated using the constructor MultiSelectList.
     *
     * <strong>Constructor:</strong>
     *   <ul>
     *     <li>MultiSelectList(Object options)</li>
     *   </ul>
     *
     * <strong>Events:</strong>
     *   <ul>
     *     <li>itemChanged: this event is triggered when value is selected in the MultiSelectList</li>
     *   </ul>
     *
     * <strong>Options:</strong>
     *   <ul>
     *       <li>items: an array used as list of available items in the MultiSelectList</li>
     *   </ul>
     *
     * @private
     * @class MultiSelectList
     * @extends ComponentList
     */
    var MultiSelectList = ComponentList.extend({
        /*jshint validthis:true*/

        View: View,

        /**
         * Overrides method from widget.
         * Executes every time, when added back to the screen.
         *
         * @method onViewReady
         * @private
         */
        onViewReady: function () {
            this.selectedWidget = null;
            this.selectedItem = null;
            this._widgets = [];

            this.noResultsWidget = new NoResults();
            this.progressBarWidget = new ProgressBar();

            this.setItems(this.options.items || []);

            this.itemActions = {
                moveDown: moveItemDown,
                moveUp: moveItemUp,
                enter: clickOnItem
            };

            this.addEventHandler('action', this.onItemAction, this);
            this.getElement().addEventHandler('click', this.onComponentListClick, this);
        },

        onComponentListClick: function () {
            this.trigger(MultiSelectList.COMPONENT_LIST_CLICKED);
        },

        /**
         * Sets items to the MultiSelectList.
         *
         * @param {Array} items
         */
        setItems: function (items) {
            // clean up previous items
            this.clear();

            this.items = items;

            var countItems = function (item) {
                return item && item.type !== 'separator';
            };
            var notEmpty = (items.length > 0) ? items.some(countItems) : false;

            if (notEmpty) {
                var widget;
                items.forEach(function (itemObj, index) {
                    if (itemObj.type && itemObj.type === 'separator') {
                        widget = new ComponentSeparator();
                    } else {
                        widget = new ComponentItem({
                            title: itemObj.title,
                            name: itemObj.name,
                            index: index
                        });
                        widget.addEventHandler(ComponentItem.ITEM_CLICKED, this.onListItemClicked, this);
                    }
                    widget.attachTo(this.view.getItemsHolder());
                    this._widgets.push(widget);
                }.bind(this));
            } else {
                this.noResultsWidget.attachTo(this.view.getItemsHolder());
            }
        },

        attachProgressBar: function () {
            this.progressBarWidget.attachTo(this.view.getItemsHolder());
        },

        /**
         * Highlights a selected item in the MultiSelectList.
         *
         * @method setSelectedItem
         * @param index
         */
        setSelectedItem: function (index) {
            var itemObj = this.items[index],
                widgetObj = this._widgets[index];

            if (this.selectedWidget !== null) {
                this.selectedWidget.setSelected(false);
            }
            if (itemObj !== undefined && itemObj.type === undefined) {
                widgetObj.setSelected(true);
                this.selectedItem = itemObj;
                this.selectedWidget = widgetObj;
            }
        },

        /**
         * Returns selected item from the MultiSelectList.
         *
         * @method getSelectedItem
         * @returns {null|*|itemObj}
         */
        getSelectedItem: function () {
            return this.selectedItem;
        },

        onItemAction: function (actionName) {
            if (this.itemActions[actionName] !== undefined) {
                this.itemActions[actionName].call(this);
            }
        },

        /**
         * An event which is executed when on the list item is clicked
         *
         * @method onListItemClicked
         * @param {int} index
         * @private
         */
        onListItemClicked: function (index) {
            this.selectedItem = this.items[index];
            if (this.selectedItem !== undefined) {
                this.trigger(MultiSelectList.ITEM_IS_SELECTED);
                this.hide();
            } else {
                this.selectedItem = null;
                this.selectedWidget = null;
            }
        },

        /**
         * Returns current selected item
         *
         * @method getSelectedValue
         * @return {Object}
         */
        getSelectedValue: function () {
            return this.selectedItem || {};
        },

        clear: function () {
            this._widgets.forEach(function (widget) {
                widget.detach();
                widget.destroy();
            });
            this._widgets = [];

            this.selectedWidget = null;
            this.selectedItem = null;

            this.progressBarWidget.detach();
            this.noResultsWidget.detach();
        }

    }, {
        ITEM_IS_SELECTED: 'itemIsSelected',
        ITEM_IS_NOT_SELECTED: 'itemIsNotSelected',
        COMPONENT_LIST_CLICKED: 'componentListClicked'
    });

    return MultiSelectList;

    function getNextItemIndex (currentIndex, step, itemsCount, iterator) {
        var nextIndex = currentIndex + step;
        if (nextIndex === itemsCount) {
            nextIndex = 0;
        }
        if (nextIndex === -1) {
            nextIndex = itemsCount - 1;
        }
        if (iterator > itemsCount) {
            return null;
        }

        var nextItem = this.items[nextIndex];
        if (nextItem !== undefined && nextItem.type === undefined) {
            return nextIndex;
        }
        iterator++;
        return getNextItemIndex.call(this, nextIndex, step, itemsCount, iterator);
    }

    function moveItemDown () {
        var itemsCount = this.items.length;
        if (this.selectedItem !== null && itemsCount > 1) {
            var selectedIndex = this.selectedWidget.getIndex(),
                nextItemIndex = getNextItemIndex.call(this, selectedIndex, 1, itemsCount, 0);

            this.selectedWidget.setSelected(false);
            if (nextItemIndex !== null) {
                this.selectedWidget = this._widgets[nextItemIndex];
                this.selectedItem = this.items[nextItemIndex];

                this.selectedWidget.setSelected(true);
            }
        }
    }

    function moveItemUp () {
        var itemsCount = this.items.length;
        if (this.selectedItem !== null && itemsCount > 1) {
            var selectedIndex = this.selectedWidget.getIndex(),
                previousItemIndex = getNextItemIndex.call(this, selectedIndex, -1, itemsCount);

            this.selectedWidget.setSelected(false);
            if (previousItemIndex !== null) {
                this.selectedWidget = this._widgets[previousItemIndex];
                this.selectedItem = this.items[previousItemIndex];

                this.selectedWidget.setSelected(true);
            }
        }
    }

    function clickOnItem () {
        var eventName = this.selectedItem !== null ? MultiSelectList.ITEM_IS_SELECTED : MultiSelectList.ITEM_IS_NOT_SELECTED;
        this.trigger(eventName);
        this.hide();
    }

});
