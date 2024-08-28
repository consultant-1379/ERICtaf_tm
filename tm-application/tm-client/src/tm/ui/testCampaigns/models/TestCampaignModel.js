define([
    '../../../ext/mvpModel',
    'jscore/ext/utils/base/underscore'
], function (Model, _) {
    'use strict';

    return Model.extend({

        url: '/tm-server/api/test-campaigns',

        getId: function () {
            return this.getAttribute('id');
        },

        setId: function (id) {
            return this.setAttribute('id', id);
        },

        getName: function () {
            return this.getAttribute('name');
        },

        setName: function (name) {
            return this.setAttribute('name', name);
        },

        getProduct: function () {
            return this.getAttribute('product');
        },

        setProduct: function (product) {
            this.setAttribute('product', product);
        },

        getDrop: function () {
            return this.getAttribute('drop');
        },

        setDrop: function (drop) {
            this.setAttribute('drop', drop);
        },

        getProductId: function () {
            if (this.getProductFeature() !== null) {
                return this.getProductFeature()[0].product.id;
            }
            return null;
        },

        getProductFeature: function () {
            return this.getAttribute('features');
        },

        setProductFeature: function (feature) {
            this.setAttribute('features', feature);
        },

        getComponents: function () {
            return this.getAttribute('components');
        },

        setComponents: function (components) {
            this.setAttribute('components', components);
        },

        getTestPlanItems: function () {
            return this.getAttribute('testCampaignItems');
        },

        setTestPlanItems: function (assignments) {
            return this.setAttribute('testCampaignItems', assignments);
        },

        getLocked: function () {
            return this.getAttribute('locked');
        },

        setLocked: function (closed) {
            return this.setAttribute('locked', closed);
        },

        getProject: function () {
            return this.getAttribute('project');
        },

        setProject: function (project) {
            return this.setAttribute('project', project);
        },

        getStartDate: function () {
            return this.getAttribute('startDate');
        },

        getEndDate: function () {
            return this.getAttribute('endDate');
        },

        getProductName: function () {
            return this.getAttribute('product') ? this.getAttribute('product').name : '';
        },

        getDropName: function () {
            return this.getAttribute('drop') ? this.getAttribute('drop').name : '';
        },

        getDropId: function () {
            return this.getAttribute('drop') ? this.getAttribute('drop').id : '';
        },

        getFeatureNames: function () {
            if (this.getAttribute('features')) {
                return _.map(this.getAttribute('features'), function (featureObj) {
                    return featureObj.name;
                });
            }
            return '';
        },

        getComponentNames: function () {
            if (this.getAttribute('components')) {
                return _.map(this.getAttribute('components'), function (componentObj) {
                    return componentObj.name;
                });
            }
            return '';
        },

        setHostname: function (hostname) {
            return this.setAttribute('hostname', hostname);
        },

        // Remove model id to mimic "new" model.
        clearIds: function () {
            this.setId(null);
            this.getTestPlanItems().forEach(function (testPlanItem) {
                testPlanItem.id = null;
            });
        },

        getAutoCreate: function () {
            return this.getAttribute('autoCreate');
        },

        setAutoCreate: function (value) {
            this.setAttribute('autoCreate', value);
        },

        getGroups: function () {
            return this.getAttribute('groups');
        },

        setGroups: function (groups) {
            this.setAttribute('groups', groups);
        },

        getAuthor: function () {
            return this.getAttribute('author');
        }

    });

});
