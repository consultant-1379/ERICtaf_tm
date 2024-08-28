/*global define*/
define([
    'jscore/core',
    'jscore/base/jquery',
    './SelectBoxFilterView',
    'widgets/SelectBox',
    '../../../common/models/domain/DomainDataCollection',
    'jscore/ext/utils/base/underscore',
    'widgets/MultiSelectBox',
    '../../../common/Constants',
    '../../../common/ContextFilter'
], function (core, $, View, SelectBox, DomainDataCollection, _, MultiSelectBox, Constants, ContextFilter) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true */

        View: View,

        init: function (options) {
            this.events = options.events;
            this.eventBus = options.eventBus;
            this.labelPosition = options.labelPosition;

            this.selection = {
                product: null,
                drop: null,
                feature: [],
                components: []
            };
        },

        onViewReady: function () {
            initSelectBoxes.call(this);
            if (this.labelPosition === 'top') {
                this.view.setLabelsOnTop();
            }
            this.eventBus.subscribe(Constants.events.PRODUCT_CHANGED, loadInitProductValue, this);
        },

        getSelection: function () {
            return this.selection;
        },

        setProduct: function (product) {
            this.productSelect.setValue(mapToReference.call(this, product));
            this.selection.product = product;
            this.selection.drop = null;
            this.selection.feature = null;
            this.selection.components = null;
        },

        setDrop: function (drop, productId) {
            var deferredDrops = fetchDrops.call(this, productId);
            deferredDrops.done(function () {
                this.dropSelect.setValue(mapToReference.call(this, drop));
                this.selection.drop = this.dropSelect.getValue().itemObj;
            }.bind(this));
            return deferredDrops;
        },

        setFeature: function (features, productId) {
            var deferredFeatures = fetchFeatures.call(this, productId);
            deferredFeatures.done(function () {
                var featureReferences = [];
                features.forEach(function (feature) {
                    featureReferences.push(mapToReference.call(this, feature));
                }, this);
                this.featureSelect.setValue(featureReferences);
                this.selection.feature = features;
            }.bind(this));
            return deferredFeatures;
        },

        setComponents: function (components, deferredFeatures) {
            deferredFeatures.done(function () {
                var selectedFeatures = this.featureSelect.getSelectedItems();
                var featureIds = getIds.call(this, selectedFeatures);
                var deferredComponents = fetchComponents.call(this, featureIds);
                deferredComponents.done(function () {
                    var componentReferences = [];
                    components.forEach(function (component) {
                        componentReferences.push(mapToReference.call(this, component));
                    }, this);
                    if (componentReferences.length > 0) {
                        this.componentSelect.setValue(componentReferences);
                    }
                    this.selection.components = components;
                    cascadeFinished.call(this);
                }.bind(this));
                return deferredComponents;
            }.bind(this));

        },

        hideOrShowDropsAndComponents: function (product) {
            if (!product.dropCapable) {
                this.view.hideDropBlock();
                this.view.hideComponentBlock();
                this.selection.drop = null;
                this.selection.components = [];
            } else {
                this.view.showDropBlock();
                this.view.showComponentBlock();
            }
        }
    });

    function mapToReference (model) {
        return {name: model.name, value: model.id, itemObj: model};
    }

    function initSelectBoxes () {
        this.productSelect = new SelectBox();
        this.dropSelect = new SelectBox();
        this.featureSelect = new MultiSelectBox({
            selectDeselectAll: true
        });
        this.componentSelect = new MultiSelectBox({
            selectDeselectAll: true,
            width: 'long'
        });

        initProducts.call(this);
    }

    function initProducts () {
        return $.ajax({
                url: '/tm-server/api/products/'
            })
            .then(function (products) {
                initProductSelect.call(this, parseItems(products));
                initDropSelect.call(this, products[0].id);
                initFeaturesAndComponents.call(this, products);
            }.bind(this));
    }

    function initFeaturesAndComponents (products) {
        return $.ajax({
                url: '/tm-server/api/products/' + products[0].id + '/features'
            })
            .then(function (features) {
                initFeatureSelect.call(this, parseItems(features));
                initComponentSelect.call(this);
            }.bind(this));
    }

    function parseItems (items) {
        return _.map(items, function (item) {
            return {
                value: item.id,
                name: item.name,
                itemObj: item
            };
        });
    }

    function initProductSelect (products) {
        this.productSelect.setItems(products);
        loadInitProductValue.call(this);
        this.productSelect.addEventHandler('change', function () {
            this.selection.product = this.productSelect.getValue().itemObj;
            var productId = this.selection.product.id;
            if (this.events.productChangedEvent) {
                this.eventBus.publish(this.events.productChangedEvent, this.selection.product);
            }
            this.hideOrShowDropsAndComponents(this.selection.product);
            this.dropSelect.setValue('');
            this.selection.drop = null;
            this.featureSelect.setItems([]);
            this.selection.feature = [];
            this.componentSelect.setItems([]);
            this.selection.components = [];
            fetchDrops.call(this, productId);
            fetchFeatures.call(this, productId);
            this.eventBus.publish(Constants.events.UPDATE_USER_PROFILE_WITH_PROJECT,
                null, this.productSelect.getValue());
        }, this);

        this.productSelect.attachTo(this.view.getProductSelectBoxHolder());
    }

    function initDropSelect (productId) {
        this.dropSelect.attachTo(this.view.getDropSelectBoxHolder());
        this.dropSelect.addEventHandler('change', function () {
            this.selection.drop = this.dropSelect.getValue().itemObj;
            cascadeFinished.call(this);
        }, this);
        fetchDrops.call(this, productId);
    }

    function initFeatureSelect (features) {
        this.featureSelect.setItems(features);
        this.featureSelect.attachTo(this.view.getFeatureSelectBoxHolder());
        this.featureSelect.addEventHandler('change', function () {
            var selectedFeatures = this.featureSelect.getSelectedItems();
            var featureIds = getIds.call(this, selectedFeatures);
            this.selection.feature = _.map(selectedFeatures, function (componentOption) {
                return componentOption.itemObj;
            });
            fetchComponents.call(this, featureIds);
        }, this);
    }

    function initComponentSelect () {
        this.componentSelect.addEventHandler('change', function () {
            var selectedComponents = this.componentSelect.getSelectedItems();
            this.selection.components = _.map(selectedComponents, function (componentOption) {
                return componentOption.itemObj;
            });
            cascadeFinished.call(this);
        }, this);
        this.componentSelect.attachTo(this.view.getComponentSelectBoxHolder());
    }

    function fetchDrops (productId) {
        var df = $.Deferred();
        this.dropCollection = new DomainDataCollection([], {
            type: 'drop'
        });
        this.dropCollection.setProductId(productId);
        this.dropCollection.fetch({
            success: function (data) {
                var items = data.toJSON();
                if (!_.isEmpty(items)) {
                    this.dropSelect.setItems(items);
                } else {
                    this.dropSelect.setItems([]);
                    this.selection.drop = null;
                }
                this.dropSelect.setValue('');
                df.resolve();
            }.bind(this)
        });
        return df.promise();
    }

    function fetchFeatures (productId) {
        var df = $.Deferred();
        this.featureCollection = new DomainDataCollection([], {
            type: 'feature'
        });
        this.featureCollection.setProductId(productId);
        this.featureCollection.fetch({
            success: function (data) {
                var items = data.toJSON();
                if (!_.isEmpty(items)) {
                    this.featureSelect.setItems(items);
                } else {
                    this.featureSelect.setItems([]);
                }
                this.componentSelect.setItems([]);
                df.resolve();
            }.bind(this)
        });
        return df.promise();
    }

    function fetchComponents (featureIds) {
        var df = $.Deferred();
        this.componentCollection = new DomainDataCollection([], {
            type: 'component'
        });
        this.componentCollection.setFeatureId(featureIds);
        this.componentCollection.fetch({
            success: function (data) {
                var items = data.toJSON();
                if (!_.isEmpty(items)) {
                    this.componentSelect.setItems(items);
                } else {
                    this.componentSelect.setItems([]);
                }
                this.selection.components = [];
                cascadeFinished.call(this);
                df.resolve();
            }.bind(this)
        });
        return df.promise();
    }

    function cascadeFinished () {
        if (this.events.cascadeFinishedEvent) {
            var deferred = _.debounce(function () {
                this.eventBus.publish(this.events.cascadeFinishedEvent, this.selection);
            }, 50);
            deferred.call(this);
        }
    }

    function loadInitProductValue () {
        if (ContextFilter.profileProduct != null) {
            _.defer(function () {
                this.setProduct(ContextFilter.profileProduct);
                var productId = ContextFilter.profileProduct.id;
                if (ContextFilter.profileProduct.dropCapable) {
                    fetchDrops.call(this, productId);
                }
                fetchFeatures.call(this, productId);
                this.hideOrShowDropsAndComponents(this.selection.product);
                cascadeFinished.call(this);
            }.bind(this));
        }
    }

    function getIds (entities) {
        var ids = [];
        entities.forEach(function (entity) {
            ids.push(entity.value);
        });

        return ids;
    }
});
