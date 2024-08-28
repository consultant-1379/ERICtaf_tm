define([
    'jscore/core',
    'text!./RequirementsTreeWidget.html',
    'styles!./RequirementsTreeWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.filterInput = element.find('.eaTM-RequirementsTreeWidget-filterInput');
            this.filterClearIcon = element.find('.eaTM-RequirementsTreeWidget-filterClearIcon');
            this.filterIcon = element.find('.eaTM-RequirementsTreeWidget-filterIcon');
            this.emptyTreeResult = element.find('.eaTM-RequirementsTreeWidget-emptyTreeResult');
            this.treeHolder = element.find('.eaTM-RequirementsTreeWidget-treeHolder');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getFilterInput: function () {
            return this.filterInput;
        },

        getFilterClearIcon: function () {
            return this.filterClearIcon;
        },

        getFilterIcon: function () {
            return this.filterIcon;
        },

        getEmptyTreeResult: function () {
            return this.emptyTreeResult;
        },

        getTreeHolder: function () {
            return this.treeHolder;
        }

    });

});
