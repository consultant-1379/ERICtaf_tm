/*global define*/
define([
    'jscore/core',
    'template!./_stackedProgressBar.html',
    'styles!./_stackedProgressBar.less'
], function (core, template, style) {
    'use strict';

    var StackedProgressBarView = core.View.extend({

        iconModifier: null,

        getTemplate: function () {
            return template(this.options);
        },

        getStyle: function () {
            return style;
        },

        getIconParent: function () {
            return this.getElement().find('.' + StackedProgressBarView.EL_ICON_PARENT);
        },

        getIcon: function () {
            var parentElem = this.getIconParent();
            return parentElem.find('.' + StackedProgressBarView.EL_ICON);
        },

        getLabel: function () {
            return this.getElement().find('.' + StackedProgressBarView.EL_LABEL);
        },

        getHeader: function () {
            return this.getElement().find('.' + StackedProgressBarView.EL_HEADER);
        },

        getBar: function () {
            return this.getElement().find('.' + StackedProgressBarView.EL_BAR);
        },

        getProgressValue: function () {
            return this.getElement().find('.' + StackedProgressBarView.EL_VALUE);
        },

        setLabel: function (label) {
            var labelElem = this.getLabel();
            labelElem.setText(label);
            this.showHeader();
        },

        setIcon: function (icon) {
            var iconElem = this.getIcon();

            if (this.iconModifier) {
                iconElem.removeModifier(this.iconModifier);
            }

            this.iconModifier = icon;
            iconElem.setModifier(icon);

            if (iconElem.getStyle('display') === 'none') {
                iconElem.setStyle('display', 'inline-block');
            }

            this.showHeader();
        },

        showHeader: function () {
            var headerElem = this.getHeader();
            if (headerElem.getStyle('display') === 'none') {
                this.toggleElement(headerElem);
            }

        },

        toggleElement: function (elem) {
            if (elem.getStyle('display') === 'none') {
                elem.setStyle('display', 'block');
            } else {
                elem.setStyle('display', 'none');
            }
        }

    }, {
        EL_HEADER: 'eaTM-StackedProgressBar-header',
        EL_ICON_PARENT: 'eaTM-StackedProgressBar-icon',
        EL_LABEL: 'eaTM-StackedProgressBar-label',
        EL_VALUE: 'eaTM-StackedProgressBar-value',
        EL_BAR: 'eaTM-StackedProgressBar-bar',
        EL_ICON: 'ebIcon'

    });
    return StackedProgressBarView;
});
