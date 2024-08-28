define([
    'jscore/core',
    'jscore/ext/binding',
    './TestCampaignFormWidgetView',
    'widgets/PopupDatePicker',
    'widgets/SelectBox',
    '../../../../common/ReferenceHelper',
    '../../../../common/widgets/SelectBoxFilter/SelectBoxFilter',
    '../../../../common/Constants',
    '../../../../common/widgets/multiSelect/MultiSelect',
    '../../../../common/models/ReferencesCollection',
    '../../../../common/ContextFilter'
], function (core, binding, View, PopupDatePicker, SelectBox, ReferenceHelper, SelectBoxFilter, Constants, MultiSelect,
             ReferencesCollection, ContextFilter) {
    'use strict';

    var TestPlanFormWidget = core.Widget.extend({
        /*jshint validthis:true*/

        View: View,

        init: function () {
            this.startDate = new PopupDatePicker();
            this.endDate = new PopupDatePicker();
            this.model = this.options.model;
            this.eventBus = this.options.eventBus;

            this.modelReferences = new ReferencesCollection();
            this.modelReferences.addReferences(
                'campaignGroup'
            );

        },

        onViewReady: function () {
            this.view.afterRender();

            this.startDate.attachTo(this.view.getStartDate());
            this.endDate.attachTo(this.view.getEndDate());

            this.selectBoxFilter = new SelectBoxFilter({
                labelPosition: 'top',
                eventBus: this.eventBus,
                events: {
                    cascadeFinishedEvent: Constants.events.TEST_CAMPAIGN_FORM_FILTER_LOADED
                }
            });
            this.selectBoxFilter.attachTo(this.view.getFilterHolder());

            binding.bindModel(this.model, 'name', this.view.getTestPlanName(), 'value');
            binding.bindModel(this.model, 'environment', this.view.getEnvironment(), 'value');
            binding.bindModel(this.model, 'description', this.view.getTestPlanDescription(), 'value');
            binding.bindModel(this.model, 'psFrom', this.view.getPSFrom(), 'value');
            binding.bindModel(this.model, 'psTo', this.view.getPSTo(), 'value');
            binding.bindModel(this.model, 'guideRevision', this.view.getGuideRevision(), 'value');
            binding.bindModel(this.model, 'sedRevision', this.view.getSedRevision(), 'value');
            binding.bindModel(this.model, 'otherDependentSW', this.view.getOtherDependentSW(), 'value');
            binding.bindModel(this.model, 'nodeTypeVersion', this.view.getNodeTypeVersion(), 'value');
            binding.bindModel(this.model, 'comment', this.view.getComment(), 'value');
            binding.bindModel(this.model, 'sovStatus', this.view.getSovStatus(), 'value');

            this.multiSelect = new MultiSelect({
                autoComplete: {
                    message: {notFound: 'Not Found'},
                    caseInsensitive: true
                }
            });

            this.multiSelect.setModifier('width', 'fullBlock');

            this.multiSelect.attachTo(this.view.getGroup());

            var referenceHelper = new ReferenceHelper({
                references: this.modelReferences
            });

            this.modelReferences.addEventHandler('reset', function () {
                this.multiSelect.setItems(referenceHelper.getReferenceItems('campaignGroup'));
            }.bind(this));

            ContextFilter.profileReady
                .then(loadReferences.bind(this))
                .then(this.eventHandlers());
        },

        eventHandlers: function () {
            this.startDate.addEventHandler('dateselect', function () {
                var startDateInput = this.startDate.getValue();
                this.model.setAttribute('startDate', startDateInput);
            }, this);

            this.endDate.addEventHandler('dateselect', function () {
                var endDateInput = this.endDate.getValue();
                this.model.setAttribute('endDate', endDateInput);
            }, this);

            this.startDate.addEventHandler('dateclear', function () {
                var startDateInput = null;
                this.model.setAttribute('startDate', startDateInput);
            }, this);

            this.endDate.addEventHandler('dateclear', function () {
                var endDateInput = null;
                this.model.setAttribute('endDate', endDateInput);
            }, this);

            this.view.getAutoUpdateCheckbox().addEventHandler('click', function () {
                var checked = this.view.getAutoUpdateCheckbox().getProperty('checked');
                this.model.setAutoCreate(checked);
            }.bind(this));

            this.eventBus.subscribe(Constants.events.PRODUCT_CHANGED, function () {
                loadReferences.call(this);
            }.bind(this));
        },

        updateUnboundFields: function () {
            this.clear();
            var mainModel = this.model;
            this.startDate.setValue(mainModel.getStartDate() == null ? '' : new Date(mainModel.getStartDate()));
            this.endDate.setValue(mainModel.getEndDate() == null ? '' : new Date(mainModel.getEndDate()));
            var product = mainModel.getProduct();
            var productId = '';
            if (product != null) {
                productId = product.id;
            }
            var deferredFeature = null;

            if (product) {
                this.selectBoxFilter.setProduct(product);
                this.selectBoxFilter.hideOrShowDropsAndComponents(product);
                this.showAutoUpdateCheckbox(product);
            }
            if (mainModel.getDrop()) {
                this.selectBoxFilter.setDrop(mainModel.getDrop(), productId);
            }
            if (mainModel.getProductFeature()) {
                deferredFeature = this.selectBoxFilter.setFeature(mainModel.getProductFeature(), productId);
            }
            if (mainModel.getComponents()) {
                this.selectBoxFilter.setComponents(mainModel.getComponents(), deferredFeature);
            }
            if (mainModel.getGroups() && mainModel.getGroups().length > 0) {
                this.multiSelect.setSelectedItems(this.convertToObjects(mainModel.getGroups()));
            }
        },

        updateProductDropAndFeatureDetails: function () {
            var selection = this.selectBoxFilter.getSelection();
            this.model.setProduct(selection.product);
            this.model.setDrop(selection.drop);
            this.model.setProductFeature(selection.feature);
            this.model.setComponents(selection.components);
            this.model.setGroups(this.getItemsObjectsIfUndefined(this.multiSelect.getSelectedItems()));

        },

         getItemsObjectsIfUndefined: function (valuesArray) {
                var values = [];
                if (valuesArray && valuesArray.length > 0) {
                    valuesArray.forEach(function (valueObj) {
                        if (typeof (valueObj.itemObj) === 'undefined') {
                          valuesArray.forEach(function (valueObj) {
                             values.push({name: valueObj.title, id: valueObj.value, title: valueObj.title});
                                              });
                        }
                      values.push(valueObj.itemObj);
                    });
                }
                return values;
            },

        clear: function () {
            this.multiSelect.clear();
        },

        convertToObjects: function (ids) {
                      var objects = [];
                      if (!ids) {
                          return objects;
                      }
                      ids.forEach(function (id) {
                          objects.push({name: id.title, value: id.id, title: id.title});
                      });
                      return objects;
                  },

        showAutoUpdateCheckbox: function (product) {
            if (product.dropCapable) {
                this.view.getAutoUpdateHolder().setStyle('display', 'block');
                this.view.getAutoUpdateCheckbox().setProperty('checked', this.model.getAutoCreate());
            } else {
                this.view.getAutoUpdateHolder().setStyle('display', 'none');
            }
        },

        updateReferences: function () {
            loadReferences.call(this);
        }

    });

    function loadReferences () {
        this.modelReferences.setProductId(encodeURIComponent(ContextFilter.getActiveProductId()));
        this.modelReferences.fetch({
            reset: true
        });
    }

    return TestPlanFormWidget;

});
