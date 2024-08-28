define([
    'jscore/core',
    './TestCaseReportsWidgetView',
    'widgets/ExpandableList'
], function (core, View, ExpandableList) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true */

        view: function () {
            return View;
        },

        onViewReady: function () {
            this.region = this.options.region;

            this.list = new ExpandableList({
                items: this.options.items,
                showAll: true
            });
            this.list.attachTo(this.getElement());
            this.list.addEventHandler('itemClick', this.onSelected, this);
        },

        onSelected: function (model) {
            this.clearHighlights();
            var item = model.toJSON();
            item.action();
        },

        clearHighlights: function () {
            this.list.itemWidgets.forEach(function (obj) {
                obj.removeModifier('highlight');
            });
        }

    });

});
