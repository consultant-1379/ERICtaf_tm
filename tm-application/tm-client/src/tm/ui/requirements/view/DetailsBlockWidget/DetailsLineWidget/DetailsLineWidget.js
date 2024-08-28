/*global define*/
define([
    'jscore/core',
    './DetailsLineWidgetView',
    '../../../../../common/Animation',
    '../../../../../common/Constants',
    '../../../../../common/Navigation',
    '../../../../../common/widgets/actionIcon/ActionIcon'
], function (core, View, Animation, Constants, Navigation, ActionIcon) {
    'use strict';

    return core.Widget.extend({

        View: View,

        init: function () {
            this.model = this.options.model;
        },

        onViewReady: function () {
            this.view.afterRender();

            var title = this.view.getTitle();
            title.setText(this.model.getTestCaseId() + ': ' + this.model.getTitle());
            title.setAttribute('href', '#' + Navigation.getTestCaseDetailsUrl(this.model.getId()));

            this.view.getType().setText((this.model.getType() !== '' ? '(' + this.model.getType() + ')' : ''));

            var viewIcon = new ActionIcon({
                iconKey: 'circleArrowRight',
                interactive: true
            });
            viewIcon.attachTo(this.view.getActions());
            viewIcon.addEventHandler('click', this.onViewIconClick, this);
        },

        onViewIconClick: function () {
            Navigation.navigateTo(Navigation.getTestCaseDetailsUrl(this.model.getId()));
        }

    });

});
