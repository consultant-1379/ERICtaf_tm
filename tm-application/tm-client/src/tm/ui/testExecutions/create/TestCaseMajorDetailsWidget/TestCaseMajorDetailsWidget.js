/*global define*/
define([
    'jscore/core',
    'jscore/ext/binding',
    './TestCaseMajorDetailsWidgetView',
    '../../../../common/requirement/link/item/RequirementLinkWidget'
], function (core, binding, View, RequirementLinkWidget) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true */

        View: View,

        onViewReady: function () {
            this.view.afterRender();

            this.currentModel = this.options.model;
            this.region = this.options.region;

            bindModel.call(this);
        },

        updateFields: function () {
            updateUnboundParams.call(this);
        }

    });

    /*************** PRIVATE FUNCTIONS ******************/

    function updateUnboundParams () {
        var mainModel = this.currentModel,
            requirements = mainModel.getRequirements();

        this.view.getRequirementsBlock().setText('');

        if (requirements && requirements.length > 0) {
            requirements.forEach(function (requirementObj) {
                var requirementWidgetCell = new RequirementLinkWidget({
                    externalId: requirementObj.externalId,
                    title: requirementObj.summary,
                    label: requirementObj.summary,
                    statusName: requirementObj.externalStatusName
                });
                requirementWidgetCell.attachTo(this.view.getRequirementsBlock());
            }.bind(this));

        }

        // fields with reference data
        this.view.getType().setText(this.currentModel.getType());

        // fields with reference data array
        this.view.getGroup().setText(this.currentModel.getGroups() || '');
        this.view.getContext().setText(this.currentModel.getContexts() || '');
    }

    function bindModel () {
        var mainModel = this.currentModel;

        // binding testCase fields - text fields
        binding.bindModel(mainModel, 'testCaseId', this.view.getTestCaseId(), 'text');
        binding.bindModel(mainModel, 'title', this.view.getTitle(), 'text');
        binding.bindModel(mainModel, 'description', this.view.getDescription(), 'text');
        binding.bindModel(mainModel, 'precondition', this.view.getPreCondition(), 'text');
    }

});
