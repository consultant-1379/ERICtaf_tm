define([
    'jscore/ext/utils/base/underscore',
    'widgets/WidgetCore',
    './AutocompleteInputView',
    '../../common/ModelHelper',
    '../widgets/multiSelect/multiSelectList/MultiSelectList'
], function (_, WidgetCore, View, ModelHelper, MultiSelectList) {
    'use strict';

    var AutocompleteInput = WidgetCore.extend({
        /*jshint validthis: true */

        View: View,

        init: function (options) {
            this.refresh = options.refresh || function () {
                };

            this.refreshCompletions = _.debounce(this.refreshCompletions, 300);
        },

        onViewReady: function () {
            this.completionWidget = new MultiSelectList({
                parent: this.getElement(),
                persistent: this.options.persistent,
                width: this.options.width,
                height: this.options.height
            });
            this.completionWidget.addEventHandler(MultiSelectList.ITEM_IS_SELECTED, onItemSelected, this);
            this.completionWidget.addEventHandler(MultiSelectList.ITEM_IS_NOT_SELECTED, onItemNotSelected, this);

            this.getElement().addEventHandler('keydown', onInputKeyDown, this);

            var input = this.view.getInput();
            input.setAttribute('placeholder', this.options.placeholder || '');
            input.addEventHandler('input', onTextInput, this);
        },

        getValueObj: function () {
            return this.completionWidget.getSelectedValue();
        },

        getValue: function () {
            return this.view.getInput().getValue();
        },

        setValue: function (value) {
            this.view.getInput().setValue(value);
        },

        refreshCompletions: function (search) {
            this.refresh.call(null, search, function (items) {
                this.completionWidget.setItems(items);
                this.completionWidget.show();
                this.completionWidget.setSelectedItem(0);
                if (this.userObject) {
                    this.userObject.call(this.completionWidget.getSelectedItem());
                }
            }.bind(this));
        }

    });

    function onItemSelected () {
        var selected = this.completionWidget.getSelectedItem();
        if (selected != null) {
            this.setValue(selected.value);
        }
        if (this.completionWidget.isShowing) {
            this.completionWidget.hide();
        } else {
            this.trigger('submit');
        }
    }

    function onItemNotSelected () {
        this.completionWidget.hide();
    }

    var keyCodeHandlers = {
        13: function () { // enter
            onItemSelected.call(this);
        },

        38: function (event) { // up
            event.preventDefault();
            if (this.completionWidget.isShowing) {
                this.completionWidget.trigger('action', 'moveUp');
            }
        },

        40: function (event) { // down
            event.preventDefault();
            if (this.completionWidget.isShowing) {
                this.completionWidget.trigger('action', 'moveDown');
            }
        }
    };

    function onInputKeyDown (event) {
        var handler = keyCodeHandlers[event.originalEvent.keyCode];
        if (handler != null) {
            handler.call(this, event);
        }
    }

    function onTextInput () {
        var search = this.view.getInput().getValue();
        this.completionWidget.attachProgressBar();
        this.refreshCompletions(search);
    }

    return AutocompleteInput;

});
