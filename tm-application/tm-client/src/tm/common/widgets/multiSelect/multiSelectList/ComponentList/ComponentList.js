define([
    'jscore/core',
    'widgets/WidgetCore',
    './ComponentListView',
    'jscore/ext/utils/base/underscore',
    'widgets/utils/domUtils'
], function (core, WidgetCore, View, _, dom) {
    'use strict';

    /**
     * The ComponentList class uses the Ericsson brand assets.<br>
     * The ComponentList can be instantiated using the constructor ComponentList.
     *
     * <strong>Constructor:</strong>
     *   <ul>
     *     <li>ComponentList(Object options)</li>
     *   </ul>
     *
     * <strong>Events:</strong>
     *   <ul>
     *     <li>itemSelected: this event is triggered when value is selected in the ComponentList</li>
     *   </ul>
     *
     * <strong>Options:</strong>
     *   <ul>
     *     <li>items: an array used as a list of available items in the ComponentList.</li>
     *   </ul>
     *
     * @class ComponentList
     * @extends Widget
     * @private
     */

    var FIXED_OFFSET = 6;

    return WidgetCore.extend({
        /*jshint validthis:true */

        view: function () {
            return new View(this.options);
        },

        /**
         * The init method is automatically called by the constructor when using the "new" operator. If an object with
         * key/value pairs was passed into the constructor then the options variable will have those key/value pairs.
         *
         * @method init
         * @param {Object} options
         * @private
         */
        init: function (options) {
            // user offsets are for positioning purposes
            this._userOffsets = {top: 0, left: 0};
            this._currentFocusedItem = undefined;

            // flatten and grab focusable items
            this.focusableItems = this.getFocusableItems(options.items);
            this.checkForIcons();
        },

        getFocusableItems: function (items) {
            var _items = [];
            if (items) {
                items.forEach(function (item) {
                    if (item.items) {
                        item.items.forEach(function (item) {
                            if (item.type !== 'separator' && !item.disabled) {
                                _items.push(item);
                            }
                        });
                    } else {
                        if (item.type !== 'separator' && !item.disabled) {
                            _items.push(item);
                        }
                    }
                });
            }
            return _items;
        },

        checkForIcons: function () {
            var showIcons = false;
            if (this.options.items) {
                this.options.items.forEach(function (item) {
                    if (item.items && item.items.length > 0) {
                        item.items.forEach(function (subItem) {
                            if (subItem.icon !== undefined) {
                                showIcons = true;
                            }
                        });
                    } else if (item.icon !== undefined) {
                        showIcons = true;
                    }
                }.bind(this));
                this.options.items.forEach(function (item) {
                    if (showIcons && item.items && item.items.length > 0) {
                        item.items.forEach(function (subItem) {
                            subItem.showIcon = true;
                        });
                    } else if (showIcons) {
                        item.showIcon = true;
                    }
                }.bind(this));
            }
        },

        /**
         * Overrides method from widget.
         * Executes every time, when added back to the screen.
         *
         * @method onViewReady
         * @private
         */
        onViewReady: function () {

            // Prevents losing focus when clicking on an item in the list (causes annoying flicker).
            this.getElement().addEventHandler('mousedown', function (e) {
                e.preventDefault();
            });

            this.view.getSelectableItems().forEach(function (el, index) {

                el._indexInList = index;

                // Add the mouse move focus handlers.
                el.addEventHandler('mousemove', function (evt) {
                    if (evt.originalEvent.pageX !== this.mousePageX || evt.originalEvent.pageY !== this.mousePageY) {
                        this.mousePageX = evt.originalEvent.pageX;
                        this.mousePageY = evt.originalEvent.pageY;
                        this.resetCurrentItemFocus();
                        this.setFocusedItem(index, true);
                    }
                }.bind(this));

                // Add the click handler
                el.addEventHandler('click', function (e) {
                    if (e && e.stopPropagation) {
                        e.stopPropagation();
                    }
                    this.onListItemClicked(index);
                }.bind(this));

            }.bind(this));

            if (this.options.selectDeselectAll === true) {
                this.view.getSelectAllLink().addEventHandler('click', function (e) {
                    e.originalEvent.stopPropagation();
                    this.view.getSelectableItems().forEach(function (item) {
                        var checkbox = item.find('.ebCheckbox');
                        if (checkbox && !checkbox.getProperty('checked')) {
                            checkbox.setProperty('checked', true);
                        }
                    }, this);
                    this.onListItemClicked();
                }, this);
                this.view.getDeSelectAllLink().addEventHandler('click', function (e) {
                    e.originalEvent.stopPropagation();
                    this.view.getSelectableItems().forEach(function (item) {
                        var checkbox = item.find('.ebCheckbox');
                        if (checkbox && checkbox.getProperty('checked')) {
                            checkbox.setProperty('checked', false);
                        }
                    }, this);
                    this.onListItemClicked();
                }, this);
            }

            if (this.options.animate) {
                this.getElement().setModifier('animated', undefined, 'elWidgets-ComponentList');
            }
        },

        onDestroy: function () {
            this.doHide();
        },

        /**
         * Gets values from the ComponentList
         *
         * @method getItems
         * @return {Array} items
         */
        getItems: function () {
            return this.options.items;
        },

        /**
         * Calculates the position of where the select box should be located relative to the screen.<br>
         * Sets an interval to check if the position changes, and if it does, it's hidden.<br>
         * A body event handler is added to ensure that clicking on the parent element doesn't hide
         * the component list immediately. This can be complex task for developers, so making it easier
         * by just putting it in this method.
         *
         * @method show
         */
        show: function () {
            this.deleteTransitionEvent();
            this.render();
            this.isShowing = true;

            // Create the interval. This interval will check frequently what the current position is suppose to be.
            // If the position changes at all, then the list will disappear.
            if (!this.posInterval) {
                this.posInterval = setInterval(function () {
                    var curPosition = calculatePosition.call(this);
                    if (this.position.top !== curPosition.top || this.position.left !== curPosition.left) {
                        this.hide();
                    }
                }.bind(this), 1000 / 24);
            }

            // Add an event handler to the html element.
            // This handler will check to see if the click target is a child of the parent or the parent itself.
            // If not, this component list is hidden.
            // StopPropagation won't work, because when you click on a different selectbox, you'd want the other
            // one to hide itself automatically. So this approach has to be taken.
            requestAnimationFrame(function () {
                if (!this.options.persistent) {
                    var body = core.Element.wrap(document.documentElement);
                    if (!this.bodyEventId) {
                        this.bodyEventId = body.addEventHandler('click', function (e) {
                            if (!this.getElement().contains(core.Element.wrap(e.originalEvent.target))) {
                                body.removeEventHandler(this.bodyEventId);
                                delete this.bodyEventId;
                                this.hide();
                            }
                        }.bind(this));
                    }
                }
            }.bind(this));

            if (this.options.animate) {
                this.getElement().setModifier('show', undefined, 'elWidgets-ComponentList');
            }
        },

        /**
         * Adds the offsets to the position of the list.
         *
         * @method setPositionOffsets
         * @param {Object} offsets (top/left)
         */
        setPositionOffsets: function (offsets) {
            this._userOffsets = offsets;
            this.render();
        },

        /**
         * Renders the component list to the screen, and performs some calculations to ensure that the list is not cut
         * off by the viewport.
         *
         * @method render
         */
        render: function () {
            this.getElement().detach();

            // Set the width of the context menu to the width of the parent, unless specified otherwise
            var width = this.options.width || (this.options.parent.getProperty('offsetWidth') + 'px');
            var height = this.options.height || '250px';

            if (this.options.minWidth) {
                this.getElement().setStyle('min-width', this.options.minWidth);
            }

            // Calculate the position. We need to keep track of the position for delta purposes
            var position = calculatePosition.call(this);
            this.position = position;

            var normalTop = position.top + this.options.parent.getProperty('offsetHeight') + FIXED_OFFSET;

            // Set the style so that we can get the offsets
            this.getElement().setStyle({
                position: 'fixed',
                display: 'block',
                top: normalTop + 'px',
                left: position.left + 'px',
                width: width,
                'max-height': height
            });

            core.Element.wrap(document.body).append(this.getElement());

            // We need to see if the list goes outside screen boundaries. These variables will help.
            var offsetHeight = this.getElement().getProperty('offsetHeight');
            var offsetWidth = this.getElement().getProperty('offsetWidth');
            var screenHeight = window.innerHeight;
            var screenWidth = window.innerWidth;
            var parentHeight = this.options.parent.getProperty('offsetHeight');
            var parentWidth = this.options.parent.getProperty('offsetWidth');

            // If the list goes past the bottom of the screen, flip it to appear above the parent instead of below
            if (position.top + offsetHeight + parentHeight + FIXED_OFFSET > screenHeight) {
                var newTop = position.top - offsetHeight - FIXED_OFFSET;
                this.getElement().setStyle({
                    top: newTop + 'px'
                });

                if (newTop < 0) {
                    // Figure out which direction to appear in again
                    var upwardsHeight = position.top - FIXED_OFFSET * 2;
                    var downwardsHeight = screenHeight - normalTop - FIXED_OFFSET * 3;
                    var finalHeight = upwardsHeight > downwardsHeight ? upwardsHeight : downwardsHeight;
                    var finalTop = upwardsHeight > downwardsHeight ? FIXED_OFFSET : normalTop;

                    this.getElement().setStyle({
                        top: finalTop + 'px',
                        maxHeight: finalHeight + 'px'
                    });
                }
            }

            // If the list bypasses the right of the viewport, right align it to the parent instead
            if (position.left + offsetWidth > screenWidth) {
                this.getElement().setStyle({
                    left: position.left - (offsetWidth - parentWidth) + 'px'
                });
            }
        },

        /**
         * Shows/Hides the list.
         *
         * @method toggle
         */
        toggle: function () {
            this.isShowing = !this.isShowing;
            if (this.isShowing) {
                this.show();
            } else {
                this.hide();
            }
        },

        deleteTransitionEvent: function () {
            if (this.transitionEvent) {
                this.transitionEvent.destroy();
                delete this.transitionEvent;
            }
        },

        /**
         * Clears in the interval and detaches the component list from the DOM.
         *
         * @method hide
         */
        hide: function () {
            this.isShowing = false;
            if (this.options.animate) {
                this.getElement().removeModifier('show', 'elWidgets-ComponentList');
                this.deleteTransitionEvent();
                this.transitionEvent = this.getElement().addEventHandler(dom.transitionEventName, function () {
                    this.doHide();
                    this.deleteTransitionEvent();
                }.bind(this));
            } else {
                this.doHide();
            }
        },

        /**
         * Returns current visibility status of the list
         *
         * @method isVisible
         * @return {boolean}
         */
        isVisible: function () {
            return this.isShowing;
        },

        doHide: function () {
            clearInterval(this.posInterval);
            delete this.posInterval;
            this.getElement().detach();
            if (this.bodyEventId) {
                this.bodyEventId.destroy();
                delete this.bodyEventId;
            }
            this.trigger('hide');
        },

        //-----------------------------------------------------------------
        //-----------------------------------------------------------------
        /**
         * Show the message info.
         *
         * @method showMessageInfo
         */
        showMessageInfo: function (message) {
            this.view.getMessageInfo().setText(message);
            this.getElement().setModifier('info', '', 'ebComponentList');
        },

        /**
         * Hide the message info
         *
         * @method hideMessageInfo
         */
        hideMessageInfo: function () {
            this.getElement().removeModifier('info', 'ebComponentList');
        },

        //-----------------------------------------------------------------
        //-----------------------------------------------------------------
        /**
         * Set focus selection modifier on the item at that index
         *
         * @method setFocusedItem
         */
        setFocusedItem: function (index, noScroll) {
            // negative index means we don't want to focus on anything at all.
            // This is required for parents like ComboBox where we want to move up to the input field
            if (index < 0) {
                return;
            }

            index = (index % this.focusableItems.length);
            this._currentFocusedItem = this.view.getSelectableItems()[index];

            if (this._currentFocusedItem) {
                this._currentFocusedItem.setModifier('focused', '', 'ebComponentList-item');

                if (!noScroll) {
                    dom.scrollIntoView(this._currentFocusedItem, this.getElement());
                }
            }
        },

        /**
         * Remove focus selection modifier on the current focused item
         *
         * @method resetCurrentItemFocus
         * @return {int} index of current focused item
         */
        resetCurrentItemFocus: function () {
            var index = -1;
            if (this._currentFocusedItem !== undefined) {
                this._currentFocusedItem.removeModifier('focused', 'ebComponentList-item');
                index = this._currentFocusedItem._indexInList;
                delete this._currentFocusedItem;
            }
            return index;
        },
        //-----------------------------------------------------------------
        //-----------------------------------------------------------------

        /**
         * An event which is executed when on the list item is clicked
         *
         * @method onListItemClicked
         * @private
         */
        onListItemClicked: function (index) {
            // Test to see if anything is focused on
            if (index !== undefined || this._currentFocusedItem !== undefined) {
                index = index !== undefined ? index : this._currentFocusedItem._indexInList;
                this.resetCurrentItemFocus();
                this.setFocusedItem(index);

                if (!this.options.checkboxes) {
                    this.trigger('itemSelected', this.focusableItems[index]);
                    this.hide();
                } else {
                    var checkedItems = [];
                    this.view.getSelectableItems().forEach(function (el) {
                        if (el.find('.ebCheckbox').getProperty('checked')) {
                            checkedItems.push(this.focusableItems[el._indexInList]);
                        }
                    }.bind(this));
                    this.trigger('itemSelected', checkedItems);
                }
            }
        }

    });

    //-----------------------------------------------------------------
    //-----------------------------------------------------------------
    /**
     * Calculates where the element should appear using the bounding box of the parent element and applying user offsets.
     *
     * TODO: Reimplement this function using property accessors for parent.
     *
     * @method calculatePosition
     * @private
     * @return {Object} top, left
     */
    function calculatePosition () {
        /*jshint validthis:true*/
        var container = this.options.container || this.options.parent;
        // TODO: Change this to proper DOM function
        var clientRect = container._getHTMLElement().getBoundingClientRect();

        var userOffsets = this._userOffsets || {};
        userOffsets.top = userOffsets.top || 0;
        userOffsets.left = userOffsets.left || 0;

        return {
            top: clientRect.top + userOffsets.top,
            left: clientRect.left + userOffsets.left
        };
    }

});
