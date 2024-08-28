/*global define*/
define([
    'widgets/ItemsControl',
    './MenuView'
], function (ItemsControl, View) {
    'use strict';

    return ItemsControl.extend({

        View: View,

        onControlReady: function () {
            this.setWidth('30rem');
        },

        onItemSelected: function (selectedValue) {
            if (selectedValue.action) {
                selectedValue.action();
            }
        }
    });

});
