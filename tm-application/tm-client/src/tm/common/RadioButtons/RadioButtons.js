define([
    'jscore/core',
    './RadioButtonsView',
    'jscore/ext/utils/base/underscore',
    'widgets/utils/domUtils'
], function (core, View, _, domUtils) {
    'use strict';

    return core.Widget.extend({
        /* jshint validthis: true */

        View: View,

        init: function () {
            this.radioButtonType = this.options.radioButtonType ? this.options.radioButtonType : 'radio';
            this.radioOptions = this.options.radioOptions ? this.options.radioOptions : [];
            this.groupId = this.options.groupId ? this.options.groupId : 'default';
            this.value = {};

        },

        onViewReady: function () {
            this.view.afterRender();
            createRadioOptions.call(this, this.radioOptions);

            var options = this.view.getRadioOptionsDiv();

            var selection = domUtils.findAll('input[name=' + this.groupId + ']', options);
            _.each(selection, function (selected) {
                selected.addEventHandler('click', function () {
                    _.each(selection, function (selectedOption) {
                        if (selectedOption.getProperty('checked') === true) {
                            this.value = selectedOption.getValue();
                            this.trigger('selectedCheckBox', selectedOption);
                            return;
                        }
                    }.bind(this));

                }.bind(this));
            }.bind(this));

        },

        getGroupId: function () {
            return this.groupId;
        },

        getValue: function () {
            return this.value;
        },

        setValue: function (value) {
            var selectionUpdate = this.view.getRadioOptionsDiv().find('input[value=' + value + ']');
            if (selectionUpdate) {
                selectionUpdate.setAttribute('checked', true);
            }
        },

        reset: function () {
            this.value = [];
            var selection = domUtils.findAll('input[name=' + this.groupId + ']', this.view.getRadioOptionsDiv());
            _.each(selection, function (element) {
                element.setProperty('checked', false);
                element.setAttribute('data-waschecked', false);
            });
        }
    });

    function createRadioOptions (radioOptions) {
        var optionDiv = this.view.getRadioOptionsDiv();
        _.each(radioOptions, function (radioValue) {
            var label = document.createElement('label');
            label.setAttribute('class', 'eaTM-radioLabel');

            var input = document.createElement('input');
            input.setAttribute('class', 'ebRadioBtn');
            input.setAttribute('type', this.radioButtonType);
            input.setAttribute('value', radioValue);
            input.setAttribute('name', this.groupId);
            input.setAttribute('data-waschecked', false);

            var span = document.createElement('span');
            span.setAttribute('class', 'ebRadioBtn-inputStatus');

            var secondSpan = document.createElement('span');
            secondSpan.setAttribute('class', 'ebRadioBtn-label');
            secondSpan.innerHTML = radioValue;

            label.appendChild(input);
            label.appendChild(span);
            label.appendChild(secondSpan);

            var element = core.Element.wrap(label);
            optionDiv.append(element);

        }.bind(this));

    }
});
