/*global define*/
define([
    'jscore/ext/utils/base/underscore',
    '../../ext/mvpCollection',
    './ReferenceModel'
], function (_, Collection, ReferenceModel) {
    'use strict';

    return Collection.extend({

        Model: ReferenceModel,

        init: function () {
            this.referenceIds = {};
            this.references = {};
            this.productId = null;
            this.featureId = null;

            this.addEventHandler('reset', function (data) {
                this.references = {};
                data.each(function (reference) {
                    var id = reference.getId();
                    this.references[id] = reference;
                }.bind(this));
            }, this);
        },

        url: function () {
            var queries = _.map(this.referenceIds, function (_, ref) {
                return 'referenceId=' + ref;
            });

            if (this.productId != null) {
                queries.push('productId=' + this.productId);
            }

            if (this.featureId != null) {
                queries.push('featureId=' + this.featureId);
            }

            return '/tm-server/api/references?' + queries.join('&');
        },

        addReference: function (ref) {
            this.referenceIds[ref] = true;
        },

        addReferences: function () {
            _.each(arguments, this.addReference.bind(this));
        },

        setProductId: function (productId) {
            this.productId = productId;
        },

        setFeatureId: function (featureId) {
            this.featureId = featureId;
        },

        getById: function (id) {
            return this.references[id];
        },

        getReferenceItemsByNames: function (refId, names) {
            var reference = this.references[refId];
            if (reference == null || names == null) {
                return null;
            }
            var items = reference.getItems();
            return _.filter(items, function (item) {
                return _.some(names, function (name) {
                    if (name == null || item.name == null) {
                        return false;
                    }
                    return name.toLowerCase() === item.name.toLowerCase();
                });
            });
        }

    });

});
