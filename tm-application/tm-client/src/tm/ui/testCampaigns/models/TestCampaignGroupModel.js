define([
    '../../../ext/mvpModel'
], function (Model) {
    'use strict';

    return Model.extend({

        url: '/tm-server/api/testCampaignGroup',

        getId: function () {
            return this.getAttribute('id');
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

        getTestCampaigns: function () {
            return this.getAttribute('testCampaigns');
        }
    });

});
