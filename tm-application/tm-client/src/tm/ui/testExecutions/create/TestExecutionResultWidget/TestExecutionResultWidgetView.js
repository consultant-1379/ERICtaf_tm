/*global define*/
define([
    'jscore/core',
    'text!./TestExecutionResultWidget.html',
    'styles!./TestExecutionResultWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.resultSelect = element.find('.eaTM-TestExecutionResult-resultSelect');
            this.isoBlock = element.find('.eaTM-TestExecutionResult-isoBlock');
            this.isoSelect = element.find('.eaTM-TestExecutionResult-isoSelect');
            this.comment = element.find('.eaTM-TestExecutionResult-comment');
            this.defectIdMultiSelect = element.find('.eaTM-TestExecutionResult-defectIdMultiSelect');
            this.reuirementIdMultiSelect = element.find('.eaTM-TestExecutionResult-requirementIdMultiSelect');
            this.defectIdsBlock = element.find('.eaTM-TestExecutionResult-defectIdBlock');
            this.requirementIdsBlock = element.find('.eaTM-TestExecutionResult-requirementIdBlock');
            this.executionTime = element.find('.eaTM-TestExecutionResult-executionTime');
            this.filesBlock = element.find('.eaTM-TestExecutionResult-filesBlock');
            this.kpiMeasurement = element.find('.eaTM-TestExecutionResult-kpiMeasurement');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getResultSelect: function () {
            return this.resultSelect;
        },

        getIsoBlock: function () {
            return this.isoBlock;
        },

        getIsoSelect: function () {
            return this.isoSelect;
        },

        getComment: function () {
            return this.comment;
        },

        getDefectIdMultiSelect: function () {
            return this.defectIdMultiSelect;
        },

        getDefectIdsBlock: function () {
            return this.defectIdsBlock;
        },

        getRequirementsIdsBlock: function () {
            return this.requirementIdsBlock;
        },

        getRequirementIdMultiSelect: function () {
            return this.reuirementIdMultiSelect;
        },

        getDefectButtonBlock: function () {
            return this.defectButtonBlock;
        },

        getExecutionTime: function () {
            return this.executionTime;
        },

        getFilesBlock: function () {
            return this.filesBlock;
        },

        showIsoBlock: function () {
            this.getIsoBlock().removeModifier('hidden', 'eaTM-TestExecutionResult-isoBlock');
        },

        hideIsoBlock: function () {
            this.getIsoBlock().setModifier('hidden', undefined, 'eaTM-TestExecutionResult-isoBlock');
        },

        getKpiMeasurement: function () {
            return this.kpiMeasurement;
        }

    });

});
