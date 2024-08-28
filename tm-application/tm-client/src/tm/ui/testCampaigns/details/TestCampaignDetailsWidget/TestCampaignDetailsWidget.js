define([
    'jscore/core',
    'jscore/ext/binding',
    './TestCampaignDetailsWidgetView',
    '../../../../common/DateHelper'
], function (core, binding, View, DateHelper) {
    'use strict';

    return core.Widget.extend({

        View: View,

        onViewReady: function () {
            this.model = this.options.model;
            this.model.addEventHandler('change', this.updateUnboundParams, this);

            binding.bindModel(this.model, 'name', this.view.getName(), 'text');
            binding.bindModel(this.model, 'description', this.view.getDescription(), 'text');
            binding.bindModel(this.model, 'environment', this.view.getEnvironment(), 'text');
            binding.bindModel(this.model, 'psFrom', this.view.getPSFrom(), 'text');
            binding.bindModel(this.model, 'psTo', this.view.getPSTo(), 'text');
            binding.bindModel(this.model, 'guideRevision', this.view.getGuideRevision(), 'text');
            binding.bindModel(this.model, 'sedRevision', this.view.getSedRevision(), 'text');
            binding.bindModel(this.model, 'otherDependentSW', this.view.getOtherDependentSW(), 'text');
            binding.bindModel(this.model, 'nodeTypeVersion', this.view.getNodeTypeVersion(), 'text');
            binding.bindModel(this.model, 'comment', this.view.getComment(), 'text');
            binding.bindModel(this.model, 'sovStatus', this.view.getSovStatus(), 'text');
        },

        updateUnboundParams: function () {
            var author = this.model.getAuthor();
            if (author) {
                this.view.getAuthor().setText(this.model.getAuthor().userName);
            } else {
                this.view.getAuthor().setText('');
            }

            this.view.getProduct().setText(this.model.getProductName());
            if (this.model.getFeatureNames() !== '') {
                this.view.getFeature().setText(this.model.getFeatureNames().join(', '));
            }
            if (this.model.getProduct() && this.model.getProduct().dropCapable) {
                this.view.showDropBlock();
                this.view.showComponentBlock();
                this.view.getDrop().setText(this.model.getDropName());
                var componentNames = this.model.getComponentNames();
                this.view.getComponents().setText(Array.prototype.join.call(componentNames, ', '));
            } else {
                this.view.hideDropBlock();
                this.view.hideComponentBlock();
            }

            this.view.getStartDate().setText(DateHelper.formatStringToDate(this.model.getStartDate()));
            this.view.getEndDate().setText(DateHelper.formatStringToDate(this.model.getEndDate()));
            var groups = convertGroupObjArray(this.model.getGroups());
            this.view.getGroup().setText(groups.join(','));

        }

    });

    function convertGroupObjArray (groups) {
        if (!groups) {
            return [''];
        }
        var result = [];
        groups.forEach(function (item) {
            result.push(item.title);
        });

        return result;
    }
});
