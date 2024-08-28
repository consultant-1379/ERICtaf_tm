define([
    'jscore/core',
    './ContentBoxView',
    './contentBoxItem/ContentBoxItem'
], function (core, View, ContentBoxItem) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true */

        View: View,

        onViewReady: function (options) {
            this.view.afterRender();
            this.view.getTitle().setText(this.options.title);
            this.createListItems(options.content);
            increaseDisplayIndex.call(this);
            decreaseDisplayIndex.call(this);
        },

        createListItems: function (listItems) {
            listItems.forEach(function (item) {
                var listItem = new ContentBoxItem(item);
                listItem.attachTo(this.view.getList());
            }.bind(this));
        }
    });

    //Method to increase z-index of element to prevent tooltip being hidden behind next content area
    function increaseDisplayIndex () {
        this.view.getList().addEventHandler('mouseover', function () {
            this.view.getElement().setStyle('z-index', '100');
        }.bind(this));
    }

    //Method to decrease z-index of element when no longer selected
    function decreaseDisplayIndex () {
        this.view.getList().addEventHandler('mouseout', function () {
            this.view.getElement().setStyle('z-index', '0');
        }.bind(this));
    }
});
