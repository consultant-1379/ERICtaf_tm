/*global define*/
define([
    'jscore/core',
    'jscore/ext/binding',
    'tablelib/Table',
    './TestCaseDetailsWidgetView',
    '../../../../common/cells/CellFactory',
    '../../../../common/DateHelper',
    '../../../../common/requirement/link/item/RequirementLinkWidget',
    'container/api',
    '../../../../common/widgets/actionIcon/ActionIcon'
], function (core, binding, Table, View, CellFactory, DateHelper, RequirementLinkWidget, containerApi,
             ActionIcon) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true */

        View: View,

        SHOW_AUTOMATION_CANDIDATE_BLOCK: 'Manual',

        onViewReady: function () {
            this.view.afterRender();

            this.currentModel = this.options.model;
            this.region = this.options.region;

            bindModel.call(this);

            this.revisionTable = new Table({
                tooltips: true,
                modifiers: [
                    {name: 'striped'}
                ],
                columns: [
                    {
                        title: 'Username',
                        attribute: 'username',
                        width: '110px'
                    },
                    {
                        title: 'Timestamp',
                        attribute: 'timestamp',
                        cellType: CellFactory.format({
                            mapper: function (timeString) {
                                return DateHelper.formatStringToDatetime(timeString);
                            }
                        })
                    },
                    {
                        title: 'Version',
                        attribute: 'version'
                    },
                    {
                        title: 'Status',
                        attribute: 'status'
                    }
                ]
            });

            this.actionIcon = new ActionIcon({
                iconKey: 'clock',
                interactive: true
            });

            this.actionIcon.attachTo(this.view.getRevisionsBlock());
            this.actionIcon.addEventHandler('click', this.onOpenHistory, this);
        },

        updateFields: function () {
            updateUnboundParams.call(this);
            updateRevisionsPopup.call(this);
        },

        onOpenHistory: function () {
            containerApi.getEventBus().publish('flyout:show', {
                header: 'History',
                content: this.revisionTable
            });
        }

    });

    /*************** PRIVATE FUNCTIONS ******************/

    function updateRevisionsPopup () {
        var modifications = this.currentModel.getModifications();
        this.view.getVersionTitle().removeModifier('show');
        this.view.getLastAuthor().setText('');

        if (modifications) {
            var modCount = modifications.length;
            if (modCount > 0) {
                this.view.getVersionTitle().setModifier('show');
                this.view.getLastAuthor().setText(modifications[0].username);
                this.revisionTable.setData(modifications);
            }
        }
    }

    function updateUnboundParams () {
        var mainModel = this.currentModel,
            requirements = mainModel.getRequirements();

        this.view.getRequirementsBlock().setText('');

        if (requirements && requirements.length > 0) {
            requirements.forEach(function (requirementObj) {
                var requirementWidgetCell = new RequirementLinkWidget({
                    externalId: requirementObj.externalId,
                    title: requirementObj.summary,
                    statusName: requirementObj.externalStatusName,
                    label: requirementObj.summary,
                    deliveredIn: requirementObj.deliveredIn
                });
                requirementWidgetCell.attachTo(this.view.getRequirementsBlock());
            }.bind(this));
        }

        updateFields.call(this);
    }

    function toggleAutomationCandidate () {
        var executionTypeObj = this.currentModel.getExecutionTypeObj();
        if (executionTypeObj && executionTypeObj.title === this.SHOW_AUTOMATION_CANDIDATE_BLOCK) {
            this.view.showAutomationCandidateBlock();
        } else {
            this.view.hideAutomationCandidateBlock();
        }
    }

    function initIntrusive () {
        var intrusive = this.currentModel.isIntrusive();
        if (intrusive) {
            this.view.getIntrusive().setText('Yes');
        } else {
            this.view.getIntrusive().setText('No');
        }
    }

    function bindModel () {
        var mainModel = this.currentModel;
        // binding updateInfo fields
        binding.bindModel(mainModel, 'createdAt', this.view.getCreatedAt(), 'text');
        binding.bindModel(mainModel, 'updatedAt', this.view.getUpdatedAt(), 'text');

        // binding testCase fields - text fields
        binding.bindModel(mainModel, 'testCaseId', this.view.getTestCaseId(), 'text');
        binding.bindModel(mainModel, 'title', this.view.getTitle(), 'text');
        binding.bindModel(mainModel, 'description', this.view.getDescription(), 'text');
        binding.bindModel(mainModel, 'precondition', this.view.getPreCondition(), 'text');
        binding.bindModel(mainModel, 'intrusiveComment', this.view.getIntrusiveComment(), 'text');
    }

    function updateFields () {
        // fields with reference data
        this.view.getComponent().setText(this.currentModel.getComponentTitles());
        this.view.getFeature().setText(this.currentModel.getFeatureTitle());
        this.view.getType().setText(this.currentModel.getType());
        this.view.getExecutionType().setText(this.currentModel.getExecutionType() || '');
        this.view.getAutomationCandidate().setText(this.currentModel.getAutomationCandidateTitle() || '');
        toggleAutomationCandidate.call(this);
        this.view.getTestCaseStatus().setText(this.currentModel.getTestCaseStatus() || '');
        this.view.getTestCaseReviewer().setText(this.currentModel.getTestCaseReviewer() || '');
        this.view.getPriority().setText(this.currentModel.getPriority());
        this.view.getTeam().setText(this.currentModel.getTeamName() || '');
        this.view.getSuite().setText(this.currentModel.getSuiteName() || '');

        // fields with reference data array
        this.view.getGroup().setText(this.currentModel.getGroups() || '');
        this.view.getContext().setText(this.currentModel.getContexts() || '');
        initIntrusive.call(this);
    }
});
