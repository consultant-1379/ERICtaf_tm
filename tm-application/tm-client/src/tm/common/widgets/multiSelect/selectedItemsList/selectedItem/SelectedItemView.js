/*global define*/
define([
    'jscore/core',
    'text!./_selectedItem.html'
], function (core, template) {
    'use strict';

    var ListItemView = core.View.extend({

        // TODO: Should be added to core.View and executed after render()
        afterRender: function () {
            var element = this.getElement();
            this.title = element.find('.' + ListItemView.EL_TITLE);
            this.actionIcon = element.find('.' + ListItemView.EL_ACTION);
            this.icon = element.find('.' + ListItemView.EL_ICON);
        },

        getTemplate: function () {
            return template;
        },

        getRoot: function () {
            return this.getElement();
        },

        getTitle: function () {
            return this.title;
        },

        getActionIcon: function () {
            return this.actionIcon;
        },

        getIcon: function () {
            return this.icon;
        }

    }, {
        EL_ICON: 'ebIcon',
        EL_TITLE: 'ebMultiSelectList-itemTitle',
        EL_ACTION: 'ebMultiSelectList-itemAction'
    });

    return ListItemView;

});
