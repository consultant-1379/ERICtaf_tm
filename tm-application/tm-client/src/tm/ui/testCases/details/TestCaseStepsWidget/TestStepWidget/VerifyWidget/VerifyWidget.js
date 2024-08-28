define([
    'jscore/core',
    'jscore/ext/utils/base/underscore',
    './VerifyWidgetView',
    '../../../../../../ext/domUtils',
    '../../../../../../common/widgets/actionIcon/ActionIcon'
], function (core, _, View, domUtils, ActionIcon) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true */

        View: View,

        init: function (options) {
            this.verifyObj = options.verifyObj;
            this.showActualResult = options.showActualResult;
            this.deleted = true;
        },

        onViewReady: function () {
            this.view.afterRender();

            this.view.getOrderNr().setText(this.options.stepIndex + '.' + this.options.itemIndex);
            this.view.getTitle().setText(this.verifyObj.name);

            if (this.showActualResult) {
                initDeleteIcon.call(this);

                this.view.showActualResultBlock();
                this.view.getActualResultBox().addEventHandler('input', this.onActualResultInput, this);
                this.view.getAddLink().addEventHandler('click', this.onAddLinkClick, this);

                this.resizeBox();
            }
        },

        onDeleteIconClick: function (e) {
            e.preventDefault();
            deleteActualResult.call(this);
        },

        onAddLinkClick: function (e) {
            e.preventDefault();
            showActualResult.call(this, '');
        },

        onActualResultInput: function () {
            this.resizeBox();
        },

        getVerifyStepId: function () {
            return this.verifyObj.id;
        },

        getVerifyStepTitle: function () {
            return this.verifyObj.name;
        },

        getActualResult: function () {
            if (!this.showActualResult || this.deleted) {
                return null;
            }
            return this.view.getActualResultBox().getValue();
        },

        applyActualResult: function (actualResult) {
            if (!this.showActualResult) {
                return;
            }
            showActualResult.call(this, actualResult);
        },

        resizeBox: function () {
            _.defer(function () {
                domUtils.textareaAutoAdjust(this.view.getActualResultBox());
            }.bind(this));
        },

        clearCompare: function () {
            this.view.getTitleCompare().setText('');
        }

    });

    function showActualResult (actualResult) {
        this.deleted = false;
        this.view.getActualResultBox().setValue(actualResult);
        this.view.showActualResultFieldset();
        this.resizeBox();
    }

    function deleteActualResult () {
        this.deleted = true;
        this.view.getActualResultBox().setValue('');
        this.view.hideActualResultFieldset();
        this.resizeBox();
    }

    function initDeleteIcon () {
        this.deleteIcon = new ActionIcon({
            iconKey: 'delete',
            title: 'Delete Actual Result',
            interactive: true
        });
        this.deleteIcon.attachTo(this.view.getIconBlock());
        this.deleteIcon.addEventHandler('click', this.onDeleteIconClick, this);
    }

});
