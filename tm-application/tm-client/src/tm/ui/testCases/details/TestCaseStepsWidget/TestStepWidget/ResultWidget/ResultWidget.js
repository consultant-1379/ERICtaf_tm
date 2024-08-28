define([
    'jscore/core',
    'jscore/ext/utils/base/underscore',
    './ResultWidgetView'
], function (core, _, View) {
    'use strict';

    var ResultWidget = core.Widget.extend({
        /* jshint validthis: true */

        View: View,

        onViewReady: function () {
            this.view.afterRender();

            this.passIcon = this.view.getPassIcon();
            this.failIcon = this.view.getFailIcon();

            addPassIconHandler.call(this);
            addFailIconHandler.call(this);
        },

        getResult: function () {
            return this.result;
        },

        applyResult: function (result) {
            setPreviousResult.call(this, result);
        }

    }, {
        PASS_OPTION: {id: '1', title: 'Pass'},
        FAIL_OPTION: {id: '2', title: 'Fail'}
    });

    return ResultWidget;

    function addPassIconHandler () {
        this.view.getPassIcon().addEventHandler('click', function () {
            if (this.activeIcon === this.passIcon) {
                this.result = null;
                this.activeIcon = null;
                this.view.deselectPassIcon();
            } else {
                this.result = ResultWidget.PASS_OPTION;
                this.activeIcon = this.passIcon;
                this.view.deselectFailIcon();
                this.view.selectPassIcon();
            }
        }, this);
    }

    function addFailIconHandler () {
        this.view.getFailIcon().addEventHandler('click', function () {
            if (this.activeIcon === this.failIcon) {
                this.result = null;
                this.activeIcon = null;
                this.view.deselectFailIcon();
            } else {
                this.result = ResultWidget.FAIL_OPTION;
                this.view.deselectPassIcon();
                this.view.selectFailIcon();
                this.activeIcon = this.failIcon;
            }
        }, this);
    }

    function setPreviousResult (result) {
        this.result = result;
        if (_.isEqual(result, ResultWidget.PASS_OPTION)) {
            this.activeIcon = this.passIcon;
            this.view.selectPassIcon();
        } else if (_.isEqual(result, ResultWidget.FAIL_OPTION)) {
            this.activeIcon = this.failIcon;
            this.view.selectFailIcon();
        }
    }

});
