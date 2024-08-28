/*global define, setTimeout*/
define([
    'jscore/core',
    '../../../../../ext/domUtils',
    './TestStepEditWidgetView',
    '../../../../../common/widgets/actionIcon/ActionIcon',
    './VerifyEditWidget/VerifyEditWidget'
], function (core, domUtils, View, ActionIcon, VerifyEditWidget) {
    'use strict';

    // This static variable is needed for correct DnD work
    var itemDragIndex = null;

    return core.Widget.extend({
        /*jshint validthis:true */

        View: View,

        init: function (options) {
            this._verifyWidgets = [];
            this.verifyActions = {
                dragEnd: dragEnd,
                itemDrop: itemDrop,
                addToList: addItemToList,
                destroy: destroyWidget
            };

            this.mouseOverTitle = false;
            this.mouseOverTitleHolder = false;
            this.isDeleted = false;
            this.isFocused = false;
            this.draggableAreaAdded = false;

            this.isNew = options.isNew;
            this.testStepObj = options.testStepObj;
            this.verifies = options.testStepObj.verifies;
            this.itemIndex = this.options.itemIndex;
            this.itemsCount = this.options.itemsCount;
        },

        onViewReady: function () {
            this.view.afterRender();

            createIcons.call(this);
            createVerifyWidgets.call(this);

            this.view.getOrderNr().setText(this.itemIndex + 1);

            var titleHolder = this.view.getTitleHolder(),
                title = this.view.getTitle(),
                rootElement = this.view.getElement(),
                draggableArea = this.view.getDraggableArea(),
                dataField = this.view.getDataField(),
                verifiesList = this.view.getVerifiesList();

            title.setValue(this.testStepObj.name);
            dataField.setValue(this.testStepObj.data);

            setTimeout(function () {
                domUtils.textareaAutoAdjust(title);
                domUtils.textareaAutoAdjust(dataField);
            });

            rootElement.addEventHandler('dragstart', this.onDragStart, this);
            rootElement.addEventHandler('dragover', this.onDragOver, this);
            rootElement.addEventHandler('dragend', this.onDragEnd, this);
            rootElement.addEventHandler('drop', this.onDrop, this);
            draggableArea.addEventHandler('dragenter', this.onDragEnter, this);
            draggableArea.addEventHandler('dragleave', this.onDragLeave, this);

            title.addEventHandler('input', this.onTitleInput, this);
            title.addEventHandler('keydown', this.onTitleKeyDown, this);
            title.addEventHandler('click touch', this.onTitleClick, this);
            title.addEventHandler('blur focusOut', this.onTitleBlur, this);
            title.addEventHandler('mouseover', this.onTitleMouseOver, this);
            title.addEventHandler('mouseout', this.onTitleMouseOut, this);
            title.addEventHandler('dblclick', this.onTitleDoubleClick, this);

            titleHolder.addEventHandler('click', this.onTitleHolderClick, this);
            titleHolder.addEventHandler('mouseover', this.onTitleHolderMouseOver, this);
            titleHolder.addEventHandler('mouseout', this.onTitleHolderMouseOut, this);

            dataField.addEventHandler('input', this.onDataFieldEntry, this);
            dataField.addEventHandler('mouseout', this.onDataFieldMouseOut, this);
            dataField.addEventHandler('mouseover', this.disableDrag, this);
            dataField.addEventHandler('dblclick', this.onDataFieldDoubleClick, this);

            verifiesList.addEventHandler('mouseover', this.disableDrag, this);
            verifiesList.addEventHandler('mouseout', this.onDataFieldMouseOut, this);

            this.view.getAddLink().addEventHandler('click', this.onAddVerifyClick, this);

            if (this.isNew) {
                setTimeout(function () {
                    this.view.getTitle().trigger('click');
                }.bind(this), 10);
            }
        },

        onDragStart: function (e) {
            itemDragIndex = this.itemIndex;
            this.view.getElement().setModifier('dragStarted');

            e.originalEvent.dataTransfer.effectAllowed = 'move';
            e.originalEvent.dataTransfer.setData('text/html', this.view.getElement()._getHTMLElement().innerHTML);
        },

        onDragEnd: function () {
            itemDragIndex = null;
            this.view.getElement().removeModifier('dragStarted');
            this.trigger('action', 'dragEnd', this);
        },

        onDragEnter: function (e) {
            if (e.preventDefault) {
                e.preventDefault();
            }
        },

        onDragLeave: function (e) {
            this.view.getDraggableArea().removeModifier('active');
            this.draggableAreaAdded = false;

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
            if (itemDragIndex === null) {
                return true;
            }
            if (!this.draggableAreaAdded) {
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

        onAddVerifyClick: function (e) {
            var index = this._verifyWidgets.length;

            var verifyEditWidget = createVerifyEditWidget(this.itemIndex, index, index + 1, {name: ''}, true);
            verifyEditWidget.addEventHandler('action', this.onVerifyAction, this);
            verifyEditWidget.attachTo(this.view.getVerifiesList());

            this._verifyWidgets.push(verifyEditWidget);
            e.preventDefault();
        },

        onVerifyAction: function (actionName) {
            if (this.verifyActions[actionName] !== undefined) {
                var args = Array.prototype.slice.call(arguments, 1);
                this.verifyActions[actionName].apply(this, args);
            }
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
            this.view.removeHighlight();
            var keyCode = e.originalEvent.keyCode || e.originalEvent.which;
            if (keyCode === 13) { // Enter
                this.mouseOverTitle = this.mouseOverTitleHolder = false;
                this.view.getTitle().trigger('blur');
            } else if (keyCode === 27) { // Esc
                this.cancelIcon.trigger('click');
                e.preventDefault();
            } else if (keyCode === 9) {
                this.mouseOverTitle = this.mouseOverTitleHolder = false;
            }
        },

        onTitleClick: function (e) {
            e.preventDefault();
            if (this.isDeleted) {
                return;
            }

            if (!this.isFocused) {
                this.isFocused = true;
                this.mouseOverTitle = this.mouseOverTitleHolder = true;

                enableButtonsForEdit.call(this);
            }
        },

        onTitleBlur: function (event) {
            if (this.mouseOverTitle || this.mouseOverTitleHolder) {
                return;
            }

            if (this.isNew) {
                this.trigger('action', 'addToList', this);
                this.isNew = false;
            }
            this.isFocused = false;

            var newValue = this.view.getTitle().getValue();
            this.view.getTitle().setValue(newValue);
            this.testStepObj.name = newValue;

            enableButtonsForView.call(this);

            event.preventDefault();
        },

        onTitleMouseOver: function () {
            this.mouseOverTitle = true;
            this.view.getElement().setAttribute('draggable', 'false');
        },

        onTitleMouseOut: function () {
            this.view.getElement().setAttribute('draggable', 'true');
            this.mouseOverTitle = false;
        },

        onTitleHolderClick: function () {
            if (!this.mouseOverTitle) {
                this.mouseOverTitleHolder = false;
                this.view.getTitle().trigger('blur');
            }
        },

        onTitleHolderMouseOver: function () {
            this.mouseOverTitleHolder = true;
        },

        onTitleHolderMouseOut: function () {
            this.mouseOverTitleHolder = false;
        },

        getValue: function () {
            var verifies = [];
            this._verifyWidgets.forEach(function (verifyObj) {
                var verify = verifyObj.getValue();
                if (!verify.isDeleted) {
                    verifies.push(verify);
                }
            });
            this.testStepObj.verifies = verifies;
            this.testStepObj.data = this.view.getDataField().getValue();
            return this.testStepObj;
        },

        getIndex: function () {
            return this.itemIndex;
        },

        setIndex: function (index) {
            this.itemIndex = index;
        },

        updateOrder: function (index, itemsCount) {
            this.itemIndex = index;
            this.itemsCount = itemsCount;

            this.view.getOrderNr().setText(this.itemIndex + 1);
            this.updateStepIndex(this.itemIndex);
            enableButtonsForView.call(this);
        },

        updateStepIndex: function (stepIndex) {
            this.itemIndex = stepIndex;

            this._verifyWidgets.forEach(function (verifyWidget) {
                verifyWidget.updateStepIndex(stepIndex);
            });
        },

        disableTestStepDragAndDrop: function () {
            this.view.getDraggableArea().removeModifier('active');
            this.draggableAreaAdded = false;
        },

        onDeleteIconClick: function () {
            var titleEl = this.view.getTitle();
            titleEl.setModifier('disabled');
            titleEl.setAttribute('disabled', 'disabled');
            this.testStepObj.isDeleted = this.isDeleted = true;
            enableButtonsForView.call(this);
        },

        onCopyIconClick: function () {
            this.trigger('action', 'copy', this.getStepInfo());
        },

        getStepInfo: function () {
            var stepInfo = {};
            stepInfo.itemIndex = this.itemIndex;
            stepInfo.name = this.view.getTitleValue();
            stepInfo.data = this.view.getDataFieldValue();
            stepInfo.verifies = this.view.getVerifiesData();
            return stepInfo;
        },

        onRestoreIconClick: function () {
            var titleEl = this.view.getTitle();
            titleEl.removeModifier('disabled');
            titleEl.removeAttribute('disabled');
            this.view.getTitle().removeModifier('deleted');
            this.testStepObj.isDeleted = this.isDeleted = false;
            enableButtonsForView.call(this);
        },

        onCancelIconClick: function () {
            if (this.isNew) {
                this.trigger('action', 'destroy', this);
                return;
            }

            this.view.getTitle().setValue(this.testStepObj.name);

            this.isFocused = false;
            this.mouseOverTitle = this.mouseOverTitleHolder = false;
            this.view.getTitle().trigger('blur');
            this.view.getElement().trigger('focus');
        },

        onDataFieldEntry: function () {
            domUtils.textareaAutoAdjust(this.view.getDataField());
        },

        onDataFieldMouseOut: function () {
            this.view.getElement().setAttribute('draggable', 'true');
        },

        disableDrag: function () {
            this.view.getElement().setAttribute('draggable', 'false');
        },

        onDataFieldDoubleClick: function () {
            this.view.getDataField()._getHTMLElement().select();
        },

        onTitleDoubleClick: function () {
            this.view.getTitle()._getHTMLElement().select();
        },

        markAsCopy: function () {
            this.view.markAsCopy();
        }

    }, {
        TITLE_CLICKED: 'titleClicked'
    });

    /*************** PRIVATE FUNCTIONS ******************/

    function enableButtonsForEdit () {
        this.deleteIcon.setHidden(true);
        this.copyIcon.setHidden(true);
        this.restoreIcon.setHidden(true);
        this.cancelIcon.setHidden(false);
    }

    function enableButtonsForView () {
        this.deleteIcon.setHidden(this.isDeleted);
        this.copyIcon.setHidden(this.isDeleted);
        this.restoreIcon.setHidden(!this.isDeleted);
        this.cancelIcon.setHidden(true);
    }

    function createVerifyWidgets () {
        if (this.verifies && this.verifies.length > 0) {
            var itemsCount = this.verifies.length;
            this.verifies.forEach(function (verifyObj, index) {
                var verifyEditWidget = createVerifyEditWidget(this.itemIndex, index, itemsCount, verifyObj);
                verifyEditWidget.addEventHandler('action', this.onVerifyAction, this);
                verifyEditWidget.attachTo(this.view.getVerifiesList());
                this._verifyWidgets.push(verifyEditWidget);
            }.bind(this));
        }
    }

    function createVerifyEditWidget (stepIndex, itemIndex, itemsCount, verifyObj, isNew) {
        return new VerifyEditWidget({
            isNew: isNew,
            verifyObj: verifyObj,
            stepIndex: stepIndex,
            itemIndex: itemIndex,
            itemsCount: itemsCount
        });
    }

    function createIcons () {
        this.icons = [];

        this.copyIcon = new ActionIcon({iconKey: 'copy', interactive: true});
        this.deleteIcon = new ActionIcon({iconKey: 'delete', interactive: true});
        this.restoreIcon = new ActionIcon({iconKey: 'undo', interactive: true, hide: true});
        this.cancelIcon = new ActionIcon({iconKey: 'close', interactive: true, hide: true});

        this.copyIcon.addEventHandler('click', this.onCopyIconClick, this);
        this.deleteIcon.addEventHandler('click', this.onDeleteIconClick, this);
        this.restoreIcon.addEventHandler('click', this.onRestoreIconClick, this);
        this.cancelIcon.addEventHandler('click', this.onCancelIconClick, this);

        this.icons.push(this.copyIcon);
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

    function dragEnd () {
        this._verifyWidgets.forEach(function (verifyWidget) {
            verifyWidget.disableVerifyDnd();
        });
    }

    function itemDrop (verifyStepObj, itemDragIndex) {
        var itemDropIndex = verifyStepObj.itemIndex,
            itemToDrop = this._verifyWidgets[itemDragIndex];

        if (itemDragIndex === null || itemDragIndex === itemDropIndex) {
            return;
        } else {
            var minItemIndex = itemDragIndex > itemDropIndex ? itemDropIndex : itemDragIndex + 1,
                maxItemIndex = itemDragIndex < itemDropIndex ? itemDropIndex : itemDragIndex - 1,
                increment = itemDragIndex > itemDropIndex ? 1 : -1,
                widgetsToChange = this._verifyWidgets.slice(minItemIndex, maxItemIndex + 1);

            this._verifyWidgets[itemDropIndex] = itemToDrop;
            for (var i = minItemIndex, j = 0; i <= maxItemIndex; i++, j++) {
                this._verifyWidgets[i + increment] = widgetsToChange[j];
            }
        }
        redrawVerifySteps.call(this);
    }

    function redrawVerifySteps () {
        var itemsCount = this._verifyWidgets.length;

        this._verifyWidgets.forEach(function (verifyWidget) {
            verifyWidget.detach();
        });
        this._verifyWidgets.forEach(function (verifyWidget, currIndex) {
            verifyWidget.updateOrder(currIndex, itemsCount);
        });
        this._verifyWidgets.forEach(function (verifyWidget) {
            verifyWidget.attach();
        });
    }

    function addItemToList () {
        var itemsCount = this._verifyWidgets.length;

        this._verifyWidgets.forEach(function (verifyWidget, currIndex) {
            verifyWidget.updateOrder(currIndex, itemsCount);
        });
    }

    function destroyWidget (verifyObj) {
        var index = verifyObj.getIndex();

        // destroy widgets
        this._verifyWidgets[index].destroy();

        // remove from arrays
        this._verifyWidgets.splice(index, 1);
    }

});
