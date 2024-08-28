define([
    'jscore/core',
    'jscore/ext/binding',
    './TestCampaignDetailsWidgetView'
], function (core, binding, View) {
    'use strict';

    return core.Widget.extend({

        View: View,

        onViewReady: function () {
            this.model = this.options.model;
            binding.bindModel(this.model, 'name', this.view.getName(), 'text');
            binding.bindModel(this.model, 'description', this.view.getDescription(), 'text');
            binding.bindModel(this.model, 'environment', this.view.getEnvironment(), 'text');
        },

        updateUnboundParams: function () {
            this.view.getProduct().setText(this.model.getProductName());
            this.view.getFeature().setText(this.model.getFeatureNames().join(', '));
            if (this.model.getProduct() && this.model.getProduct().dropCapable) {
                this.view.getDrop().setText(this.model.getDropName());
                this.view.showDropBlock();
                this.view.getDrop().setText(this.model.getDropName());
            } else {
                this.view.hideDropBlock();
            }
        }

    });
});
