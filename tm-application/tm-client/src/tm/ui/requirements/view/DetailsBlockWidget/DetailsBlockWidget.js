/*global define*/
define([
    'jscore/core',
    '../../../../common/Constants',
    './DetailsBlockWidgetView',
    './DetailsLineWidget/DetailsLineWidget',
    '../../../../common/requirement/link/item/RequirementLinkWidget'

], function (core, Constants, View, DetailsLineWidget, RequirementLinkWidget) {
    'use strict';

    return core.Widget.extend({

        DEFAULT_TITLE: '',
        DEFAULT_MESSAGE: 'Select a requirement to view its test cases.',
        NO_TEST_CASES_MESSAGE: 'No test cases found by selected requirement.',

        View: View,

        init: function (options) {
            this.detailsLines = [];
            this.requirementId = '';

            this.region = options.region;
            this.testCasesCollection = options.testCasesCollection;

            this.requirementLinkWidget = new RequirementLinkWidget({
                item: {
                    externalId: '',
                    requirementTitle: '',
                    statusName: '',
                    deliveredIn: ''
                }
            });

        },

        onViewReady: function () {
            this.view.afterRender();

            this.requirementLinkWidget.attachTo(this.view.getRequirementIdLinkBlock());

            this.testCasesCollection.addEventHandler('reset', this.onCollectionReset, this);
            this.setDetails(null);
        },

        setDetails: function (detailsObject) {
            if (this.pagination) {
                this.pagination.destroy();
            }

            this.initLinkHolderValues(detailsObject);
            this.view.getDetailsInfo().removeModifier('hide');
            this.view.getTestCasesTable().setModifier('hide');
            this.view.getProgressBlock().setModifier('hide');

            if (detailsObject === null) {
                this.view.getDetailsInfo().setText(this.DEFAULT_MESSAGE);
            } else {
                this.region.fetchTestCasesCollection(this.requirementId);
            }
        },

        onCollectionReset: function (collection) {
            this.view.getDetailsInfo().setModifier('hide');
            this.view.getDetails().removeModifier('hide');
            this.view.getProgressBlock().setModifier('hide');

            if (collection.size() === 0) {
                this.view.getDetailsInfo().setText(this.NO_TEST_CASES_MESSAGE);
                this.view.getDetailsInfo().removeModifier('hide');
            } else {
                this.view.getTestCasesTable().removeModifier('hide');
            }

            if (this.detailsLines.length > 0) {
                this.detailsLines.forEach(function (detailsLine) {
                    detailsLine.detach();
                    detailsLine.destroy();
                });
            }

            collection.each(function (model) {
                var detailsLine = new DetailsLineWidget({
                    model: model
                });
                detailsLine.attachTo(this.view.getTestCasesTable());
                this.detailsLines.push(detailsLine);
            }, this);
        },

        initLinkHolderValues: function (detailsObject) {
            if (detailsObject == null) {
                this.requirementId = '';
                this.requirementLinkWidget.clear();
                this.requirementLinkWidget.prepareToView();

                this.view.getRequirementBlock().setModifier('hide');
            } else {
                this.requirementId = detailsObject.id;

                this.requirementLinkWidget.setExternalId(detailsObject.id);
                this.requirementLinkWidget.setTitle(detailsObject.summary);
                this.requirementLinkWidget.setStatusName(detailsObject.statusName);
                this.requirementLinkWidget.setLabel(detailsObject.summary);
                this.requirementLinkWidget.setDeliveredIn(detailsObject.deliveredIn);

                this.requirementLinkWidget.prepareToView();

                this.view.getRequirementBlock().removeModifier('hide');
            }
        }

    });

});
