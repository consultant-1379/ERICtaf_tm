define([
    '../../../../common/ActionBarRegion/ActionBarRegion',
    './TestCaseImportBarRegionView',
    '../../../../common/Navigation'
], function (ActionBarRegion, View, Navigation) {
    'use strict';

    return ActionBarRegion.extend({
        /*jshint validthis:true */

        View: View,

        onStart: function () {
            this.view.afterRender();

            this.eventBus = this.getContext().eventBus;

            this.view.getBackButton().addEventHandler('click', function () {
                Navigation.goToPreviousPage();
            }, this);
        }
    });

});
