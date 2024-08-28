/*global define*/
define([
    '../../../ext/mvpCollection',
    './DomainDataModel'
], function (Collection, DomainDataModel) {
    'use strict';

    return Collection.extend({

        Model: DomainDataModel,

        init: function (options) {
            this.type = options.type;
            this.urlBase = '/tm-server/api/products/';
        },

        url: function () {
            if (this.type === 'product') {
                return this.urlBase;
            } else if (this.type === 'drop') {
                if (this.productId === null) {
                    throw new Error('product cannot be null');
                }
                return this.urlBase + this.productId + '/drops';
            } else if (this.type === 'feature') {
                if (this.productId === null) {
                    throw new Error('product cannot be null');
                }
                return this.urlBase + this.productId + '/features';
            } else if (this.type === 'component') {
                if (this.productId === null || this.featureIds === null) {
                    throw new Error('product or features cannot be null');
                }
                return this.urlBase + 'features/components?' + this.featureIds;
            }
        },

        setProductId: function (productId) {
            this.productId = productId;
        },

        setFeatureId: function (featureIds) {
            var ids = [];
            featureIds.forEach(function (id) {
                ids.push('featureId=' + id);
            }.bind(this));

            this.featureIds = ids.join('&');
        }

    });

});
