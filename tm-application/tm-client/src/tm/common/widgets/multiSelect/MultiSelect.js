/*global define*/
define([
    'jscore/core',
    'jscore/ext/mvp',
    'jscore/ext/utils/base/underscore',
    'widgets/WidgetCore',
    'widgets/utils/domUtils',
    './MultiSelectView',
    './selectedItemsList/SelectedItemsList',
    './multiSelectList/MultiSelectList',
    '../../../ext/widgetsDomUtils',
    '../../../ext/stringUtils'
], function (core, mvp, _, WidgetCore, domUtils, View, SelectedItemsList, MultiSelectList, widgetsDomUtils,
             stringUtils) {
    'use strict';

    /**
     * The MultiSelect class uses the Ericsson brand assets.<br>
     * The MultiSelect can be instantiated using the constructor MultiSelect.
     *
     * <strong>Constructor:</strong>
     *   <ul>
     *     <li>MultiSelect(Object options)</li>
     *   </ul>
     *
     * <strong>Events:</strong>
     *   <ul>
     *     <li>change: this event is triggered when value is changed in the MultiSelect</li>
     *     <li>focus: this event is triggered when the MultiSelect is focused</li>
     *     <li>click: this event is triggered when user clicks on the MultiSelect</li>
     *   </ul>
     *
     * <strong>Options:</strong>
     *   <ul>
     *       <li>selectedItems: an array used as a selected items of the MultiSelect</li>
     *       <li>items: an array or Collection used as a data of available items in the MultiSelect</li>
     *       <li>enabled: boolean indicating whether the MultiSelect should be enabled. Default is true.</li>
     *       <li>modifiers: an array used to define modifiers for the MultiSelect.
     *          <a href="#modifierAvailableList">see available modifiers</a>
     *          <br>E.g: modifiers:[{name: 'foo'}, {name: 'bar', value: 'barVal'}, {name: 'wMargin', prefix: 'eb'}]
     *       </li>
     *   </ul>
     *
     * <a name="modifierAvailableList"></a>
     * <strong>Modifiers:</strong>
     *  <ul>
     *      <li>disabled: disabled (Asset Library)</li>
     *      <li>width: [mini, small, long, xLong] (Asset Library)</li>
     *  </ul>
     *
     * @class MultiSelect
     * @extends WidgetCore
     * @beta
     */
    var MultiSelect = WidgetCore.extend({
        /*jshint validthis:true*/

        View: View,

        init: function () {
            this._events = {};
            this.items = [];
            this.selectedItems = [];
            this.keyCodeActions = {
                8: backspacePressed,
                9: tabPressed,
                13: enterPressed,
                46: deletePressed,
                27: escapePressed,
                37: leftArrowPressed,
                38: upArrowPressed,
                39: rightArrowPressed,
                40: downArrowPressed
            };
            this.isLocalSearch = true;
            this.isNewLine = true;
            this.textPaddingLeft = 0;
            this.textareaHeight = 0;
        },

        /**
         * Overrides method from widget.<br>
         * Executes every time, when added back to the screen.
         *
         * @method onViewReady
         * @private
         */
        onViewReady: function () {
            if (this.options.enabled === false) {
                this.disable();
            } else {
                this.enable();
            }

            this.setModifiers(this.options.modifiers || []);

            this.getElement().addEventHandler('change', function () {
                this.trigger('change');
            }, this);

            this.getElement().addEventHandler('focus', function () {
                this.trigger('focus');
            }, this);

            this.getElement().addEventHandler('click', function (event) {
                this.trigger('click', event);
            }, this);

            this.minTextWidth = this.options.minTextWidth || 50;

            this.componentList = new MultiSelectList({
                parent: this.getElement(),
                persistent: this.options.persistent,
                width: this.options.width,
                height: this.options.height
            });
            this.componentList.addEventHandler(MultiSelectList.COMPONENT_LIST_CLICKED, this.onComponentListClick, this);
            this.componentList.addEventHandler(MultiSelectList.ITEM_IS_SELECTED, this.onComponentListItemClick, this);
            this.componentList.addEventHandler(MultiSelectList.ITEM_IS_NOT_SELECTED, this.onTypedItemResolve, this);

            this.setItems(this.options.items || []);

            this.selectedItemsList = new SelectedItemsList();
            this.selectedItemsList.attachTo(this.view.getListHolder());
            this.selectedItemsList.addEventHandler(SelectedItemsList.ITEM_REMOVED, this.onSelectedItemRemoved, this);

            this.setSelectedItems(this.options.selectedItems || []);

            this.getElement().addEventHandler('keydown', this.onElementKeyDown, this);
            this.view.getTextarea().addEventHandler('click', this.onTextareaClick, this);
            this.view.getTextarea().addEventHandler('blur', this.onTextareaBlur, this);
            this.view.getTextarea().addEventHandler('input', this.onTextareaInput, this);
            this.view.getButtonHelper().addEventHandler('click', this.onButtonHelperClick, this);

            domUtils.delegate(this.view.getListHolder(), '.ebMultiSelectList-item', 'click', function (event) {
                var itemIndex = event.currentTarget.getAttribute('data-index');
                this.selectedItemsList.setSelectedItem(itemIndex);
                this.view.getTextarea().trigger('focus');
                event.preventDefault();
            }, this);

            core.Window.addEventHandler('resize', function () {
                this.resize();
            }.bind(this));
        },

        /**
         * Resizes the MultiSelect
         *
         * @method resize
         * @private
         */
        resize: function () {
            this.updateTextareaStyles();
        },

        /**
         * Overrides method from widget.<br>
         * Executes every time, when the widget attached to the DOM.
         *
         * @method resize
         * @private
         */
        onDOMAttach: function () {
            this.updateTextareaStyles();
        },

        /**
         * Sets items for the MultiSelect.<br>
         * If items array is empty then field is disabled.<br>
         * If collection is without url then field is disabled.
         *
         * @method setItems
         * @param {Array|Collection} items
         */
        setItems: function (items) {
            this.clear();
            items = items || [];

            this._data = items instanceof mvp.Collection ? items : new mvp.Collection(items);

            this._events.reset = this._data.addEventHandler('reset', function () {
                this.updateMultiSelectList();
                if (this.componentList.isShowing) {
                    this.componentList.render();
                }
                this.componentList.setSelectedItem(0);
            }.bind(this));

            this.isLocalSearch = !this._data.url || typeof this._data.url !== 'function';

            if (this.isLocalSearch) {
                var collectionItems = this._data.toJSON();

                var countItems = function (item) {
                    return item && item.type !== 'separator';
                };
                var notEmpty = (collectionItems.length > 0) ? collectionItems.some(countItems) : false;

                if (notEmpty) {
                    this.enable();
                } else {
                    this.getElement().setModifier('disabled');
                }
            } else {
                this._data.fetch({reset: false});
            }
        },

        /**
         * Filters MultiSelectList results according to selected items.
         *
         * @method updateMultiSelectList
         */
        updateMultiSelectList: function () {
            this.filterListResults();
        },

        /**
         * Clears events of old widgets.
         *
         * @method clear
         */
        clear: function () {
            for (var event in this._events) {
                if (this._events.hasOwnProperty(event)) {
                    this._data.removeEventHandler(event, this._events[event]);
                }
            }
        },

        /**
         * Enables the ItemsControl.
         *
         * @method enable
         */
        enable: function () {
            this.enabled = true;
            this.getElement().removeModifier('disabled');
            this.getElement().addEventHandler('click', this.onParentClick, this);

            this.view.getRoot().removeModifier('disabled');
            this.view.getButtonHelper().setProperty('disabled', false);
            this.view.getTextarea().setProperty('disabled', false);
        },

        /**
         * Disables the ItemsControl.
         *
         * @method disable
         */
        disable: function () {
            this.enabled = false;
            this.getElement().setModifier('disabled');

            this.view.getRoot().setModifier('disabled');
            this.view.getButtonHelper().setProperty('disabled', true);
            this.view.getTextarea().setProperty('disabled', true);
        },

        /**
         * Sets the width of the list. Accepts a CSS style.
         *
         * @method setWidth
         * @param {String} widthCSS
         */
        setWidth: function (widthCSS) {
            this.options.width = widthCSS;

            if (this.componentList) {
                this.componentList.options.width = widthCSS;
            }
        },

        /**
         * Sets the max height of the list. Accepts a CSS style.
         *
         * @method setHeight
         * @param {String} heightCSS
         */
        setHeight: function (heightCSS) {
            this.options.height = heightCSS;

            if (this.componentList) {
                this.componentList.options.height = heightCSS;
            }
        },

        /**
         * Sets selected items for the MultiSelect.
         *
         * @method setSelectedItems
         * @param {Array} items
         */
        setSelectedItems: function (items) {
            this.selectedItemsList.setItems(items);
            this.updateTextareaStyles();
        },

        /**
         * Returns selected items from the MultiSelect.
         *
         * @method getSelectedItems
         * @return {Object} value
         */
        getSelectedItems: function () {
            return this.selectedItemsList.getItems();
        },

        /**
         * An event which is executed when clicked on the ItemsControl
         *
         * @method onItemsControlClick
         * @private
         */
        onParentClick: function () {
            this.getElement().trigger('focus');
        },

        /**
         * An event which is executed when a click made on the ComponentList
         *
         * @method onComponentListClick
         * @private
         */
        onComponentListClick: function () {
            this.view.getTextarea().trigger('focus');
        },

        /**
         * An event which is executed when a value is selected from the ComponentList
         *
         * @method onComponentListItemClick
         * @private
         */
        onComponentListItemClick: function () {
            var selectedVal = this.componentList.selectedItem;
            if (selectedVal) {
                this.onItemSelected(selectedVal);
            }
        },

        /**
         * An event which is executed when Enter or Tab is clicked but value is not selected in the ComponentList
         *
         * @method onTypedItemResolve
         * @public
         */
        onTypedItemResolve: function () {
            var textareaVal = stringUtils.trim(this.view.getTextarea().getValue());
            if (textareaVal !== null && textareaVal.length > 0) {
                var valObj = {name: textareaVal, value: textareaVal};
                this.onItemSelected(valObj);
                this.componentList.hide();
            }
        },

        /**
         * A method which is called when an item is selected.
         *
         * @method onItemSelected
         * @protected
         *
         * @param {Object} selectedVal
         */
        onItemSelected: function (selectedVal) {
            this.selectedItemsList.addItem(selectedVal);
            this.updateTextareaStyles();

            var previousSearch = this.view.getTextarea().getValue();
            this.view.getTextarea().setValue('');

            if (!this.isLocalSearch && previousSearch !== '') {
                this.trigger(MultiSelect.SEARCH_CHANGED, '');
            }
            this.getElement().trigger('change');

            this.view.getTextarea().trigger('focus');
        },

        /**
         * Sets the width for the box (not the list).
         *
         * @method setBoxSize
         * @param {string} wModifier Can be selected from available sizes: ['mini', 'small', 'long', 'xLong', 'fullBlock']<br>
         *     null to reset to default<br>
         */
        setBoxSize: function (wModifier) {
            // check if the modifier is a size modifier
            var possible = ['mini', 'small', 'long', 'xLong', 'fullBlock'];

            if (possible.indexOf(wModifier) !== -1) {
                this.view.getRoot().setModifier('width', wModifier);
                this.currentInputSize = wModifier;
            } else if (this.currentInputSize) {
                this.view.getRoot().removeModifier('width');
                this.currentInputSize = '';
            }
        },

        /**
         * Returns textarea width.
         *
         * @method getTextareaSize
         * @returns {number}
         * @private
         */
        getTextareaSize: function () {
            var textareaDimensions = domUtils.getElementDimensions(this.view.getTextarea());
            return textareaDimensions.width - this.textPaddingLeft - 30;
        },

        /**
         * Updates textarea styles according it's content.
         *
         * @method updateTextareaStyles
         * @private
         */
        updateTextareaStyles: function () {
            var listHolderDimensions = domUtils.getElementDimensions(this.view.getListHolder()),
                widgets = this.selectedItemsList._widgets;

            this.textareaHeight = listHolderDimensions.height || 22;
            this.textPaddingLeft = 0;
            this.isNewLine = false;

            if (widgets.length > 0) {
                var lastItem = widgets[widgets.length - 1],
                    positionObj = lastItem.getElement().getPosition(),
                    listHolderPositionObj = this.view.getListHolder().getPosition();

                this.textPaddingLeft = listHolderDimensions.width - (listHolderPositionObj.right - positionObj.right) + 10;
            }

            if (this.getTextareaSize() < this.minTextWidth || isTextOverflowed.call(this)) {
                this.textareaHeight += 22;
                this.textPaddingLeft = 0;
                this.isNewLine = true;
            }

            setTextareaStyles.call(this);

            if (this.componentList.isShowing) {
                this.componentList.render();
                this.showComponentList();
            }
        },

        onTextareaInput: function () {
            if (!this.isNewLine && isTextOverflowed.call(this)) {
                // check if needed to go to new line
                this.textareaHeight += 22;
                this.textPaddingLeft = 0;
                setTextareaStyles.call(this);
                this.isNewLine = true;
            }

            if (this.isLocalSearch) {
                this.filterListResults();
                this.showComponentList();
            } else {
                this.componentList.attachProgressBar();
                this.trigger(MultiSelect.SEARCH_CHANGED, this.view.getTextarea().getValue());
            }
        },

        prepareComponentList: function () {
            this.componentList.attachProgressBar();
            this.showComponentList();
        },

        showComponentList: function () {
            this.componentList.show();
            this.componentList.setSelectedItem(0);

            this.view.getTextarea().trigger('focus');
        },

        filterListResults: function () {
            var filteredItems;
            if (this.isLocalSearch) {
                var searchValue = this.view.getTextarea().getValue(),
                    foundCollection = searchValue !== '' ? this._data.searchMap(searchValue, ['name']) : this._data;
                filteredItems = filterItems.call(this, foundCollection);
            } else {
                filteredItems = filterItems.call(this, this._data);
            }
            this.componentList.setItems(filteredItems);
        },

        onButtonHelperClick: function () {
            if (this.componentList && this.enabled) {
                this.componentList.toggle();

                if (this.componentList.isShowing) {
                    this.componentList.setSelectedItem(0);
                }
            }
            this.filterListResults();
            this.showComponentList();
        },

        onElementKeyDown: function (event) {
            if (!this.componentList || !this.enabled) {
                return;
            }

            var keyCode = event.originalEvent.keyCode || event.originalEvent.which;
            if (this.keyCodeActions[keyCode] !== undefined) {
                this.keyCodeActions[keyCode].call(this, event);
            }
        },

        onTextareaClick: function () {
            this.selectedItemsList.setSelectedItem(null);
        },

        onTextareaBlur: function () {
            this.selectedItemsList.setSelectedItem(null);
        },

        onSelectedItemRemoved: function () {
            this.filterListResults();
            this.updateTextareaStyles();
            this.view.getTextarea().trigger('focus');
        }

    }, {
        SEARCH_CHANGED: 'searchchanged'
    });

    return MultiSelect;

    function backspacePressed (event) {
        if (isSelectedItemActionAllowed.call(this)) {
            this.selectedItemsList.trigger('action', 'backspaceDelete');
            this.updateTextareaStyles();
            event.preventDefault();
        }
    }

    function tabPressed (event) {
        var textareaValue = stringUtils.trim(this.view.getTextarea().getValue());
        if (textareaValue !== null && textareaValue.length > 0) {
            this.onTypedItemResolve();
            event.preventDefault();
        }
    }

    function deletePressed (event) {
        if (this.selectedItemsList.hasSelectedItem()) {
            this.selectedItemsList.trigger('action', 'normalDelete');
            this.updateTextareaStyles();
            event.preventDefault();
        }
    }

    function enterPressed (event) {
        if (this.componentList.isShowing) {
            this.componentList.trigger('action', 'enter');
        }
        event.preventDefault();
    }

    function escapePressed (event) {
        if (this.componentList.isShowing) {
            this.componentList.hide();
        }
        event.preventDefault();
    }

    function leftArrowPressed (event) {
        if (isSelectedItemActionAllowed.call(this)) {
            this.selectedItemsList.trigger('action', 'goToLeft');
            event.preventDefault();
        }
    }

    function isSelectedItemActionAllowed () {
        var caretPosition = widgetsDomUtils.getCaretPosition(this.view.getTextarea());
        return this.selectedItemsList.hasSelectedItem() || caretPosition.start === 0 && caretPosition.end === 0;
    }

    function upArrowPressed (event) {
        if (this.componentList.isShowing) {
            this.componentList.trigger('action', 'moveUp');
        }
        event.preventDefault();
    }

    function rightArrowPressed (event) {
        if (this.selectedItemsList.hasSelectedItem()) {
            this.selectedItemsList.trigger('action', 'goToRight');
            event.preventDefault();
        }
    }

    function downArrowPressed (event) {
        if (!this.componentList.isShowing) {
            this.filterListResults();
            this.showComponentList();
        } else {
            this.componentList.trigger('action', 'moveDown');
        }
        event.preventDefault();
    }

    function setTextareaStyles () {
        var styles = [];
        styles.push('height:' + (this.textareaHeight + 4) + 'px');
        styles.push('padding-top:' + (this.textareaHeight - 18) + 'px');

        if (this.textPaddingLeft > 0) {
            styles.push('padding-left:' + this.textPaddingLeft + 'px');
        }

        this.view.getTextarea().setAttribute('style', styles.join(';'));
    }

    function isTextOverflowed () {
        var textareaFontStyle = this.view.getTextarea().getStyle('font'),
            textareaText = this.view.getTextarea().getValue();

        return widgetsDomUtils.getTextWidth(textareaText, textareaFontStyle).width > this.getTextareaSize();
    }

    function filterItems (collection) {
        var items = collection.toJSON(),
            selectedValues = [];

        this.selectedItemsList.getItems().forEach(function (selectedItem) {
            selectedValues.push(selectedItem.value);
        });

        if (selectedValues.length === 0) {
            return items;
        }

        items = _.filter(items, function (itemObj) {
            return selectedValues.indexOf(itemObj.value) === -1;
        }.bind(this));

        return items;
    }

});
