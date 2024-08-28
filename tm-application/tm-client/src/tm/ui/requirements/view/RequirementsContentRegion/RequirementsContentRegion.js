/*global define*/
define([
    'jscore/core',
    'jscore/ext/utils/base/underscore',
    '../../../../ext/stringUtils',
    '../../../../ext/domUtils',
    './RequirementsContentRegionView',
    '../RequirementsTreeWidget/RequirementsTreeWidget',
    '../DetailsBlockWidget/DetailsBlockWidget',
    '../../../requirements/models/RequirementsCollection',
    '../../../requirements/models/TestCasesByRequirementCollection',
    '../../../../common/Constants',
    '../../../../common/Animation',
    '../../../../common/Navigation',
    '../../../../common/ContextFilter',
    '../../../../common/ModelHelper'
], function (core, _, stringUtils, domUtils, View, RequirementsTreeWidget, DetailsBlockWidget,
             RequirementsCollection, TestCasesByRequirementCollection, Constants, Animation,
             Navigation, ContextFilter, ModelHelper) {
    /* jshint validthis: true */
    'use strict';

    return core.Region.extend({

        View: View,

        init: function () {
            this.projectId = '';
            this.requirementId = '';
            this.firstTime = true;

            this.eventBus = this.getContext().eventBus;
            this.requirementsCollection = new RequirementsCollection();
            this.filteredRequirementsCollection = new RequirementsCollection();
            this.testCasesCollection = new TestCasesByRequirementCollection();

            this.refreshRequirements = _.debounce(this.refreshRequirements, 100);
        },

        onViewReady: function () {
            this.view.afterRender();

            this.requirementsTreeWidget = new RequirementsTreeWidget({
                requirementsCollection: this.filteredRequirementsCollection,
                region: this
            });
            this.requirementsTreeWidget.attachTo(this.view.getTreeHolder());

            this.detailsBlock = new DetailsBlockWidget({
                testCasesCollection: this.testCasesCollection,
                region: this
            });
            this.detailsBlock.attachTo(this.view.getDetailsBlock());

            this.animation = new Animation(this.getElement(), this.eventBus);
            this.animation.showOn(Constants.events.SHOW_REQUIREMENTS_CONTENT, this.refreshCurrentPage.bind(this));
            this.animation.hideOn(Constants.events.HIDE_REQUIREMENTS_CONTENT);
            this.animation.markCurrentOn(Constants.events.MARK_REQUIREMENTS_CONTENT_CURRENT,
                this.refreshCurrentPage.bind(this));
        },

        refreshCurrentPage: function () {
            if (this.projectId !== '' && this.projectId === ContextFilter.projectIdParam && this.requirementId !== ContextFilter.requirementIdParam) {
                return;
            }
            if (this.firstTime) {
                this.updateTreeRequirements(ContextFilter.requirementIdParam, ContextFilter.projectIdParam);
                this.firstTime = false;
            }
            if (this.projectId !== ContextFilter.getActiveProjectId()) {
                this.detailsBlock.setDetails(null);
                this.requirementsTreeWidget.clearFilter();
            }
            this.projectId = ContextFilter.getActiveProjectId();
            this.requirementId = ContextFilter.requirementIdParam;
            this.requirementsTreeWidget.setSearchInput(this.requirementId);
        },

        refreshRequirements: function (searchValue) {
            searchValue = stringUtils.trim(searchValue);

            if (searchValue === '') {
                this.filteredRequirementsCollection.reset([], {reset: true});
                return;
            }

            this.updateTreeRequirements(searchValue, ContextFilter.projectIdParam);
        },

        filterRequirements: function (searchValue) {
            var requirements = domUtils.deepCopy([], this.requirementsCollection.toJSON());
            var requirementsCollection = filterRequirementsCollection.call(this, requirements,
                searchValue.toLowerCase().trim());
            this.filteredRequirementsCollection.reset(requirementsCollection, {reset: true});
        },

        unfilterRequirements: function () {
            var requirements = this.requirementsCollection.toJSON();
            this.filteredRequirementsCollection.reset(requirements, {reset: true});
        },

        setDetailsContent: function (treeItem) {
            this.detailsBlock.setDetails(treeItem);
        },

        refreshPageHash: function (requirementId) {
            var url = Navigation.getRequirementsTreeUrlWithParams(ContextFilter.projectIdParam, requirementId);
            Navigation.navigateTo(url);
        },

        updateTreeRequirements: function (requirementId, projectId) {
            if (!requirementId) {
                this.requirementsTreeWidget.clearContent();
                this.requirementsTreeWidget.setMessage('Type in requirement number and press ENTER');
                return;
            }

            this.requirementsCollection.setRequirementId(requirementId.toUpperCase());
            this.requirementsCollection.setProjectId(projectId);
            this.requirementsCollection.fetch({
                reset: true,
                data: {
                    view: 'reverseTree'
                },
                statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                    200: function () {
                        this.requirementsTreeWidget.requestTreeRedraw();
                        this.filteredRequirementsCollection.reset(this.requirementsCollection.toJSON(), {reset: true});
                    }.bind(this),
                    404: function () {
                        this.filteredRequirementsCollection.reset([], {reset: true});
                        this.requirementsTreeWidget.setMessage('Requirement not found');
                    }.bind(this)
                })
            });
        },

        fetchTestCasesCollection: function (requirementId) {
            this.testCasesCollection.setRequirementId(requirementId);
            this.testCasesCollection.fetch({
                reset: true,
                data: {
                    view: 'detailed'
                },
                statusCode: ModelHelper.authenticationHandler(this.eventBus)
            });
        }

    });

    // recursive function
    function filterRequirementsCollection (requirementsTree, searchValue) {
        var filteredRequirements = [];
        requirementsTree.forEach(function (requirementObj) {
            var children = requirementObj.children;
            if (requirementObj.externalId.toLowerCase().indexOf(searchValue) > -1) {
                requirementObj.matching = true;
                filteredRequirements.push(requirementObj);
            } else {
                requirementObj.matching = false;
                var filteredChildren = filterRequirementsCollection(children, searchValue);
                if (filteredChildren.length > 0) {
                    requirementObj.children = filteredChildren;
                    filteredRequirements.push(requirementObj);
                }
            }
        });
        return filteredRequirements;
    }
});
