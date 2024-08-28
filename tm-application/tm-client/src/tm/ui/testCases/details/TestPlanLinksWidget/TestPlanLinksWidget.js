define([
    'jscore/core',
    './TestPlanLinksWidgetView',
    '../../../../common/Constants',
    '../../../../common/Navigation',
    'widgets/ExpandableList'
], function (core, View, Constants, Navigation, ExpandableList) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true */

        onViewReady: function () {
            this.list = new ExpandableList({
                items: this.options.testPlans,
                showAll: true
            });
            this.list.attachTo(this.getElement());
            this.list.addEventHandler('itemClick', this.onSelected, this);
        },

        onSelected: function (model) {
            this.clearHighlights();
            var id = model.get('id');
            Navigation.navigateTo(Navigation.getTestPlanDetailsUrl(id));
            this.list.itemWidgets.forEach(function (obj) {
                if (obj.options.model.id === id) {
                    obj.setModifier('highlight');
                }
            });
        },

        clearHighlights: function () {
            this.list.itemWidgets.forEach(function (obj) {
                obj.removeModifier('highlight');

            });
        },

        view: function () {
            return new View({
                testPlans: this.options.testPlans
            });
        }

    });

});
