/*global setTimeout*/
define([
    'jscore/core',
    '../../../../../../ext/domUtils',
    './VerifyEditWidgetView',
    '../../../../../../common/widgets/actionIcon/ActionIcon'
], function (core, domUtils, View, ActionIcon) {
    'use strict';

    // These static variables are needed for correct DnD work
    var itemDragIndex = null,
        parentIndex = null;

    return core.Widget.extend({
        /*jshint validthis:true */

        View: View,

        init: function (options) {
            this.mouseOverTitle = false;
            this.mouseOverRootElement = false;
            this.isDeleted = false;

            this.draggableAreaAdded = false;

            this.isNew = options.isNew;
            this.verifyObj = options.verifyObj;
            this.stepIndex = options.stepIndex;
            this.itemIndex = options.itemIndex;
            this.itemsCount = options.itemsCount;
        },

        onViewReady: function () {
            this.view.afterRender();

            createIcons.call(this);

            updateVerifyStepIndex.call(this);

            var title = this.view.getTitle(),
                holder = this.view.getHolder(),
                rootElement = this.view.getElement(),
                draggableArea = this.view.getDraggableArea();

            title.setValue(this.verifyObj.name);
            setTimeout(function () {
                domUtils.textareaAutoAdjust(title);
            });

            holder.addEventHandler('dragstart', this.onDragStart, this);
            holder.addEventHandler('dragover', this.onDragOver, this);
            holder.addEventHandler('dragend', this.onDragEnd, this);
            holder.addEventHandler('drop', this.onDrop, this);
            draggableArea.addEventHandler('dragenter', this.onDragEnter, this);
            draggableArea.addEventHandler('dragleave', this.onDragLeave, this);

            title.addEventHandler('input', this.onTitleInput, this);
            title.addEventHandler('keydown', this.onTitleKeyDown, this);
            title.addEventHandler('click touch', this.onTitleClick, this);
            title.addEventHandler('blur focusout', this.onTitleBlur, this);

            title.addEventHandler('mouseover', this.onTitleMouseOver, this);
            title.addEventHandler('mouseout', this.onTitleMouseOut, this);
            title.addEventHandler('dblclick', this.onTitleDoubleClick, this);

            rootElement.addEventHandler('click', this.onRootElementClick, this);
            rootElement.addEventHandler('mouseover', this.onRootElementMouseOver, this);
            rootElement.addEventHandler('mouseout', this.onRootElementMouseOut, this);

            if (this.isNew) {
                setTimeout(function () {
                    this.view.getTitle().trigger('click');
                }.bind(this), 10);
            }
        },

        onDragStart: function (e) {
            if (e.stopPropagation) {
                e.stopPropagation();
            }
            parentIndex = this.stepIndex;
            itemDragIndex = this.itemIndex;
            this.view.getHolder().setModifier('dragStarted');

            e.originalEvent.dataTransfer.effectAllowed = 'move';
            e.originalEvent.dataTransfer.setData('text/html', this.view.getElement()._getHTMLElement().innerHTML);
        },

        onDragEnd: function (e) {
            if (e.stopPropagation) {
                e.stopPropagation();
            }
            parentIndex = null;
            itemDragIndex = null;
            this.view.getHolder().removeModifier('dragStarted');
            this.trigger('action', 'dragEnd', this);
        },

        onDragEnter: function (e) {
            if (e.stopPropagation) {
                e.stopPropagation();
            }
            if (e.preventDefault) {
                e.preventDefault();
            }
        },

        onDragLeave: function (e) {
            this.view.getHolder().removeModifier('dragOver');
            this.view.getDraggableArea().removeModifier('active');
            this.draggableAreaAdded = false;

            if (e.stopPropagation) {
                e.stopPropagation();
            }
            if (e.preventDefault) {
                e.preventDefault();
            }
        },

        onDrop: function (e) {
            if (e.stopPropagation) {
                e.stopPropagation();
            }
            this.trigger('action', 'itemDrop', this, itemDragIndex);
            return false;
        },

        onDragOver: function (e) {
            if (parentIndex !== this.stepIndex) {
                return true;
            }
            if (!this.draggableAreaAdded) {
                this.view.getHolder().setModifier('dragOver');
                this.view.getDraggableArea().setModifier('active');
                this.draggableAreaAdded = true;
            }
            if (e.stopPropagation) {
                e.stopPropagation();
            }
            if (e.preventDefault) {
                e.preventDefault();
            }
            e.originalEvent.dataTransfer.dropEffect = 'move';
            return false;
        },

        onTitleInput: function () {
            if (!this.isFocused) {
                this.isFocused = true;
                enableButtonsForEdit.call(this);
            }
            var title = this.view.getTitle();
            domUtils.textareaAutoAdjust(title);
        },

        onTitleKeyDown: function (e) {
            var keyCode = e.originalEvent.keyCode || e.originalEvent.which;
            if (keyCode === 13) { // Enter
                this.mouseOverTitle = this.mouseOverRootElement = false;
                this.view.getTitle().trigger('blur');
            } else if (keyCode === 27) { // Esc
                this.cancelIcon.trigger('click');
                e.preventDefault();
            } else if (keyCode === 9) {
                this.mouseOverTitle = this.mouseOverRootElement = false;
            }
        },

        onTitleClick: function (e) {
            e.preventDefault();
            if (this.isDeleted) {
                return;
            }

            if (!this.isFocused) {
                this.isFocused = true;
                this.mouseOverTitle = this.mouseOverRootElement = true;

                enableButtonsForEdit.call(this);
            }
        },

        onTitleBlur: function (event) {
            if (this.mouseOverTitle || this.mouseOverRootElement) {
                return;
            }

            if (this.isNew) {
                this.trigger('action', 'addToList', this);
                this.isNew = false;
            }
            this.isFocused = false;

            var newValue = this.view.getTitle().getValue();
            this.verifyObj.name = newValue;

            enableButtonsForView.call(this);

            event.preventDefault();
        },

        onTitleMouseOver: function () {
            this.view.getHolder().setAttribute('draggable', 'false');
            this.mouseOverTitle = true;
        },

        onTitleMouseOut: function () {
            this.view.getHolder().setAttribute('draggable', 'true');
            this.mouseOverTitle = false;
        },

        onRootElementClick: function () {
            if (!this.mouseOverTitle) {
                this.mouseOverRootElement = false;
                this.view.getTitle().trigger('blur');
            }
        },

        onRootElementMouseOver: function () {
            this.mouseOverRootElement = true;
        },

        onRootElementMouseOut: function () {
            this.mouseOverRootElement = false;
        },

        getValue: function () {
            return this.verifyObj;
        },

        getIndex: function () {
            return this.itemIndex;
        },

        updateStepIndex: function (stepIndex) {
            this.stepIndex = stepIndex;
            updateVerifyStepIndex.call(this);
        },

        updateOrder: function (index, itemsCount) {
            this.itemIndex = index;
            this.itemsCount = itemsCount;

            updateVerifyStepIndex.call(this);
            enableButtonsForView.call(this);
        },

        disableVerifyDnd: function () {
            this.view.getHolder().removeModifier('dragOver');
            this.view.getDraggableArea().removeModifier('active');
            this.draggableAreaAdded = false;
        },

        onDeleteIconClick: function () {
            var titleEl = this.view.getTitle();
            titleEl.setModifier('disabled');
            titleEl.setAttribute('disabled', 'disabled');
            this.verifyObj.isDeleted = this.isDeleted = true;
            enableButtonsForView.call(this);
        },

        onRestoreIconClick: function () {
            var titleEl = this.view.getTitle();
            titleEl.removeModifier('disabled');
            titleEl.removeAttribute('disabled');
            this.verifyObj.isDeleted = this.isDeleted = false;
            enableButtonsForView.call(this);
        },

        onCancelIconClick: function () {
            if (this.isNew) {
                this.trigger('action', 'destroy', this);
                return;
            }

            this.view.getTitle().setValue(this.verifyObj.name);

            this.isFocused = false;
            this.mouseOverTitle = this.mouseOverRootElement = false;
            this.view.getTitle().trigger('blur');
        },

        onTitleDoubleClick: function () {
            this.view.getTitle()._getHTMLElement().select();
        }
    });

    /*************** PRIVATE FUNCTIONS ******************/

    function enableButtonsForEdit () {
        this.deleteIcon.setHidden(true);
        this.restoreIcon.setHidden(true);
        this.cancelIcon.setHidden(false);
    }

    function enableButtonsForView () {
        this.deleteIcon.setHidden(this.isDeleted);
        this.restoreIcon.setHidden(!this.isDeleted);
        this.cancelIcon.setHidden(true);
    }

    function updateVerifyStepIndex () {
        this.view.getOrderNr().setText((this.stepIndex + 1) + '.' + (this.itemIndex + 1));
    }

    function createIcons () {
        this.icons = [];

        this.deleteIcon = new ActionIcon({iconKey: 'delete', interactive: true});
        this.restoreIcon = new ActionIcon({iconKey: 'undo', interactive: true, hide: true});
        this.cancelIcon = new ActionIcon({iconKey: 'close', interactive: true, hide: true});

        this.deleteIcon.addEventHandler('click', this.onDeleteIconClick, this);
        this.restoreIcon.addEventHandler('click', this.onRestoreIconClick, this);
        this.cancelIcon.addEventHandler('click', this.onCancelIconClick, this);

        this.icons.push(this.deleteIcon);
        this.icons.push(this.restoreIcon);
        this.icons.push(this.cancelIcon);

        if (this.isNew) {
            enableButtonsForEdit.call(this);
        }

        this.icons.forEach(function (iconWidget) {
            iconWidget.attachTo(this.view.getIconsBlock());
        }.bind(this));
    }

});
