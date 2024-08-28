define([
    'jscore/core',
    'template!./TestExecutionMultiSelectWidget.html',
    'styles!./TestExecutionMultiSelectWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.tableHolder = element.find('.eaTM-TestExecutionMultiSelectWidget-tableHolder');
            this.resultHolder = element.find('.eaTM-TestExecutionMultiSelectWidget-resultHolder');
            this.createButton = element.find('.eaTM-TestExecutionMultiSelectWidget-createButton');
            this.clearButton = element.find('.eaTM-TestExecutionMultiSelectWidget-clearButton');
            this.notificationHolder = element.find('.eaTM-TestExecutionMultiSelectWidget-notificationHolder');
            this.isoSelectHolder = element.find('.eaTM-TestExecutionMultiSelectWidget-isoHolder');
            this.defectIdSelect = element.find('.eaTM-TestExecutionMultiSelectWidget-defectHolder');
        },

        getTemplate: function () {
            return template(this.options.execution);
        },

        getStyle: function () {
            return styles;
        },

        getTableHolder: function () {
            return this.tableHolder;
        },

        getResultHolder: function () {
            return this.resultHolder;
        },

        getCreateButton: function () {
            return this.createButton;
        },

        getClearButton: function () {
            return this.clearButton;
        },

        getNotificationHolder: function () {
            return this.notificationHolder;
        },

        getIsoSelectHolder: function () {
            return this.isoSelectHolder;
        },

        showIsoSelect: function () {
            this.getIsoSelectHolder().removeModifier('hidden', 'eaTM-TestExecutionMultiSelectWidget-isoHolder');
        },

        hideIsoSelect: function () {
            this.getIsoSelectHolder().setModifier('hidden', undefined, 'eaTM-TestExecutionMultiSelectWidget-isoHolder');
        },

        getDefectIdMultiSelect: function () {
            return this.defectIdSelect;
        },

        showDefectSelect: function () {
            this.defectIdSelect.removeModifier('hidden', 'eaTM-TestExecutionMultiSelectWidget-defectHolder');
        },

        hideDefectSelect: function () {
            this.defectIdSelect.setModifier('hidden', undefined, 'eaTM-TestExecutionMultiSelectWidget-defectHolder');
        }

    });

});
