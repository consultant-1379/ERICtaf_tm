/*global define*/
define([
    'jscore/core',
    'jscore/base/jquery',
    './ContextSearchMenuView',
    'widgets/SelectBox',
    '../../../common/models/domain/DomainDataCollection',
    'jscore/ext/utils/base/underscore',
    '../../../common/Constants',
    '../../../common/ContextFilter',
    'widgets/MultiSelectBox'
], function (core, $, View, SelectBox, DomainDataCollection, _, Constants, ContextFilter, MultiSelectBox) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true */

        View: View,

        init: function (options) {
            this.eventBus = options.eventBus;

            this.selection = {
                product: null,
                feature: null,
                component: null
            };
        },

        onViewReady: function () {
            initSelectBoxes.call(this);
            this.eventBus.subscribe(Constants.events.PRODUCT_CHANGED, loadInitProductValue, this);
        },

        getSelection: function () {
            return this.selection;
        },

        setProduct: function (product) {
            this.productSelect.setValue(mapToReference.call(this, product));
            this.selection.product = product;
            this.featureSelect.setValue('');
            this.selection.feature = null;
            this.componentSelect.setItems([]);
            this.selection.component = null;
        },

        setFeature: function (feature) {
            this.featureSelect.setValue(mapToReference.call(this, feature));
            this.selection.feature = feature;
            this.featureSelect.trigger('change');
        },

        setComponents: function (components) {
            var componentReferences = [];
            components.forEach(function (component) {
                componentReferences.push(mapToReference.call(this, component));
            }, this);
            if (componentReferences.length > 0) {
                this.componentSelect.setValue(componentReferences);
            }
            this.selection.components = components;
        }

    });

    function mapToReference (model) {
        return {name: model.name, value: model.id, itemObj: model};
    }

    function initSelectBoxes () {
        this.productSelect = new SelectBox();
        this.productSelect.view.getButton().setAttribute('id', 'TMS_ContextSearchMenu-productSelect');
        this.featureSelect = new SelectBox();
        this.featureSelect.view.getButton().setAttribute('id', 'TMS_ContextSearchMenu-featureSelect');
        this.componentSelect = new MultiSelectBox({
            selectDeselectAll: true,
            width: 'long'
        });
        this.componentSelect.view.getButton().setAttribute('id', 'TMS_ContextSearchMenu-componentSelect');

        initProducts.call(this);
    }

    function initProducts () {
        return $.ajax({
                url: '/tm-server/api/products/'
            })
            .then(function (products) {
                initProductSelect.call(this, parseItems(products));
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
        if (this.selection.product != null) {
            this.productSelect.setValue(this.selection.product);
            updateSearch.call(this);
        }
        loadInitProductValue.call(this);
        this.productSelect.addEventHandler('change', function () {
            this.selection.product = this.productSelect.getValue().itemObj;
            var productId = this.selection.product.id;
            this.featureSelect.setValue('');
            this.selection.feature = null;
            this.componentSelect.setItems([]);
            this.selection.components = null;
            fetchFeatures.call(this, productId);
            updateSearch.call(this);
            this.eventBus.publish(Constants.events.UPDATE_USER_PROFILE_WITH_PROJECT,
                null, this.productSelect.getValue());
        }, this);

        this.productSelect.attachTo(this.view.getProductSelectBoxHolder());
    }

    function initFeatureSelect (features) {
        this.featureSelect.setItems(features);
        this.featureSelect.attachTo(this.view.getFeatureSelectBoxHolder());
        this.featureSelect.addEventHandler('change', function () {
            this.selection.feature = this.featureSelect.getValue().itemObj;
            var featureId = this.selection.feature.id;
            this.selection.components = null;
            this.componentSelect.setItems([]);
            fetchComponents.call(this, [featureId]);
            updateSearch.call(this);
        }, this);
    }

    function initComponentSelect () {
        this.componentSelect.addEventHandler('change', function () {
            this.selection.components = this.componentSelect.getSelectedItems();
            updateSearch.call(this);
        }, this);
        this.componentSelect.attachTo(this.view.getComponentSelectBoxHolder());
    }

    function fetchFeatures (productId) {
        this.featureCollection = new DomainDataCollection([], {
            type: 'feature'
        });
        this.featureCollection.setProductId(productId);
        this.featureCollection.fetch({
            success: function (data) {
                var items = data.toJSON();
                this.featureSelect.setItems(items);
            }.bind(this)
        });
    }

    function fetchComponents (featureId) {
        this.componentCollection = new DomainDataCollection([], {
            type: 'component'
        });
        this.componentCollection.setFeatureId(featureId);
        this.componentCollection.fetch({
            success: function (data) {
                var items = data.toJSON();
                if (!_.isEmpty(items)) {
                    this.componentSelect.setItems(items);
                    this.componentSelect.setValue([]);
                }
            }.bind(this)
        });
    }

    function updateSearch () {
        this.eventBus.publish(Constants.events.CONTEXT_MENU_CHANGE, this.selection);
    }

    function loadInitProductValue () {
        if (ContextFilter.profileProduct != null) {
            _.defer(function () {
                this.setProduct(ContextFilter.profileProduct);
                var productId = this.selection.product.id;
                fetchFeatures.call(this, productId);
                updateSearch.call(this);
            }.bind(this));
        }
    }
});
