/*global define*/
define([
    'jscore/core',
    'text!./_multiSelect.html'
], function (core, template) {
    'use strict';

    var MultiSelectView = core.View.extend({

        // TODO: Should be added to core.View and executed after render()
        afterRender: function () {
            var element = this.getElement();
            this.textArea = element.find('.' + MultiSelectView.EL_TEXTAREA);
            this.buttonHelper = element.find('.' + MultiSelectView.EL_BUTTON_HELPER);
            this.iconHolder = element.find('.' + MultiSelectView.EL_ICON_HOLDER);
            this.icon = element.find('.' + MultiSelectView.EL_ICON);
            this.listHolder = element.find('.' + MultiSelectView.EL_LIST_HOLDER);
        },

        getTemplate: function () {
            return template;
        },

        getRoot: function () {
            return this.getElement();
        },

        getTextarea: function () {
            return this.textArea;
        },

        getButtonHelper: function () {
            return this.buttonHelper;
        },

        getIconHolder: function () {
            return this.iconHolder;
        },

        getIcon: function () {
            return this.icon;
        },

        getListHolder: function () {
            return this.listHolder;
        }

    }, {
        EL_ICON: 'ebIcon',
        EL_TEXTAREA: 'ebMultiSelect-textarea',
        EL_BUTTON_HELPER: 'ebMultiSelect-helper',
        EL_ICON_HOLDER: 'ebMultiSelect-iconHolder',
        EL_LIST_HOLDER: 'ebMultiSelect-listHolder'
    });

    return MultiSelectView;

});
