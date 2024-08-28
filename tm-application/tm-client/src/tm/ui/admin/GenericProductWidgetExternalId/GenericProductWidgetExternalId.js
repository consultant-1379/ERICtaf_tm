define([
    'jscore/core',
    'jscore/ext/utils/base/underscore',
    './GenericProductWidgetExternalIdView',
    '../../../common/Constants',
    '../../../common/Navigation',
    '../../../common/table/TableHelper',
    'tablelib/Table',
    'tablelib/plugins/Selection',
    'tablelib/plugins/SortableHeader',
    'tablelib/plugins/SecondHeader',
    '../../../common/cells/CellFactory',
    '../../../common/table/filters/StringFilterCell/StringFilterCell',
    'widgets/SelectBox',
    '../../../common/ReferenceHelper',
    '../../../common/ModelHelper',
    '../../../common/notifications/NotificationRegion/NotificationRegion',
    '../../../common/table/plugins/IdentifiableRows/IdentifiableRows',
    'widgets/Dialog'
], function (core, _, View, Constants, Navigation, TableHelper, Table, Selection, SortableHeader, SecondHeader,
             CellFactory, StringFilterCell, SelectBox, ReferenceHelper, ModelHelper, NotificationRegion,
             IdentifiableRows, Dialog) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true */

        View: View,

        init: function (options) {
            this.eventBus = options.eventBus;
            this.references = options.references;
            this.region = options.region;
            this.collection = new options.collection();
            this.modelToUse = options.model;
        },

        onViewReady: function () {
            this.view.afterRender();
            this.columns = [
                {
                    title: 'Name',
                    attribute: 'name',
                    cellType: CellFactory.object(),
                    secondHeaderCellType: StringFilterCell,
                    sortable: true,
                    resizable: true
                },
                {
                    title: 'External Id',
                    attribute: 'externalId',
                    cellType: CellFactory.object(),
                    sortable: true,
                    resizable: true
                },
                {
                    title: 'Product',
                    attribute: 'product.name',
                    cellType: CellFactory.object(),
                    sortable: true,
                    resizable: true
                },
                {
                    cellType: CellFactory.icons({
                        icons: [{
                            title: 'Delete',
                            icon: 'delete',
                            action: this.onRemoveCellAction.bind(this)
                        }]
                    }),
                    width: '50px'
                }

            ];

            this.referenceHelper = new ReferenceHelper({
                references: this.references
            });

            createTable.call(this);
            createSelectBoxes.call(this);

            this.references.addEventHandler('reset', function () {
                this.productSelect.setItems(this.referenceHelper.getReferenceItems('product'));
            }.bind(this));

            this.collection.addEventHandler('reset', function () {
                showCreateButton.call(this);
            }.bind(this));

            this.view.getEditButton().addEventHandler('click', function () {
                var selectedRow = this.table.getSelectedRows()[0];
                if (selectedRow) {
                    var data = selectedRow.getData();
                    data.name = this.view.getName().getValue().trim();
                    data.externalId = this.view.getExternalId().getValue().trim();
                    addProduct.call(this, data);
                    if (data.name.length > 0) {
                        update.call(this, data);
                        this.clear();
                    } else {
                        emptyInputNotification.call(this);
                    }
                }
            }.bind(this));

            this.view.getCreateButton().addEventHandler('click', function () {
                var data = {};
                data.name = this.view.getName().getValue().trim();
                data.externalId = this.view.getExternalId().getValue().trim();
                if (data.name.length <= 0 || data.externalId <= 0) {
                    emptyInputNotification.call(this);
                    return;
                }
                addProduct.call(this, data);
                create.call(this, data);
                this.clear();
            }.bind(this));

        },

        clear: function () {
            this.view.getName().setValue('');
            this.view.getExternalId().setValue('');
        },

        onRemoveCellAction: function (model) {
            if (model) {
                this.deleteCell(model);
                this.clear();
            }
        },

        deleteCell: function (model) {
            var dialog = new Dialog({
                header: 'Confirm Delete',
                type: 'warning',
                content: 'Are you sure you want to remove "' + model.name + '"?',
                buttons: [{
                    caption: 'Delete',
                    color: 'red',
                    action: function () {
                        remove.call(this, model);
                        dialog.hide();
                    }.bind(this)
                },
                {
                    caption: 'Cancel',
                    action: function () {
                        dialog.hide();
                    }
                }]
             });
             dialog.show();
         }
    });

    function getData (product) {
        if (product) {
            this.collection.setProduct(product);
        }

        this.collection.fetch({
            reset: true
        });
    }

    function remove (model) {
        var foundModel = {};
        this.collection.each(function (modelItem, index) {
            if (modelItem.id === model.id) {
                foundModel = this.collection.getAtIndex(index);
                return;
            }
        }.bind(this));

        foundModel.destroy({
            wait: true,
            statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                204: function () {
                    getData.call(this);
                    var options = NotificationRegion.NOTIFICATION_TYPES.success;
                    options.canDismiss = true;
                    options.canClose = true;
                    this.eventBus.publish(Constants.events.NOTIFICATION, 'Deleted successfully', options);
                    showCreateButton.call(this);
                }.bind(this)
            })
        });
    }

    function update (model) {
        var name = model.name;
        var foundModel = this.collection.findBy(function (modelItem) {
            if (modelItem.id === model.id) {
                return true;
            }
        });
        foundModel.setName(name);
        foundModel.setProduct(model.product);
        foundModel.setExternalId(model.externalId);
        foundModel.save({}, {
            statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                200: function () {
                    var productId = this.productSelect.getValue().title;
                    getData.call(this, productId);
                    var options = NotificationRegion.NOTIFICATION_TYPES.success;
                    options.canDismiss = true;
                    options.canClose = true;
                    this.eventBus.publish(Constants.events.NOTIFICATION, 'Updated successfully', options);
                    showCreateButton.call(this);
                }.bind(this),
                400: function (data) {
                    this.eventBus.publish(Constants.events.SHOW_ERROR_BLOCK, data.responseJSON);
                    var productId = this.productSelect.getValue().title;
                    getData.call(this, productId);
                }.bind(this)
            })
        });
    }

    function create (model) {
        var newModel = new this.modelToUse();
        newModel.setName(model.name);
        newModel.setProduct(model.product);
        newModel.setExternalId(model.externalId);
        newModel.save({}, {
            statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                201: function () {
                    getData.call(this);
                    var options = NotificationRegion.NOTIFICATION_TYPES.success;
                    options.canDismiss = true;
                    options.canClose = true;
                    this.eventBus.publish(Constants.events.NOTIFICATION, 'Created successfully', options);
                }.bind(this)
            })
        });
    }

    function createTable () {
        this.table = new Table({
            plugins: [
                new Selection({
                    selectableRows: true
                }),
                new IdentifiableRows({
                    rowIdentifier: 'name'
                }),
                 new SecondHeader()
            ],
            modifiers: [
                {name: 'striped'}
            ],
            tooltips: true,
            columns: this.columns
        });

        this.table.attachTo(this.view.getTableHolder());

        this.tableHelper = new TableHelper({
            eventBus: this.eventBus,
            collection: this.collection,
            parent: this.view.getTableHolder(),
            table: this.table,
            isPaginated: true
        });

        this.tableHelper.applyColumnResize();
        this.tableHelper.applySortAndFilter();
        this.tableHelper.applyCollectionReset();

        this.table.addEventHandler('rowselect', function () {
            var selectedRow = this.table.getSelectedRows()[0];
            if (selectedRow) {
                var data = selectedRow.getData();
                this.productSelect.setValue(data.product);
                this.view.getName().setValue(data.name);
                this.view.getExternalId().setValue(data.externalId);
                hideCreateButton.call(this);
            } else {
                this.clear();
                showCreateButton.call(this);
            }
        }.bind(this));
    }

    function createSelectBoxes () {

        this.productSelect = new SelectBox({
            items: [],
            modifiers: [
                {name: 'width', value: 'fullBlock'}
            ]
        });

        this.productSelect.attachTo(this.view.getProductHolder());
        this.productSelect.addEventHandler('change', onProductChange, this);
    }

    function onProductChange () {
        var productId = this.productSelect.getValue().title;
        this.references.setProductId(productId);
        this.region.fetchReferences();
        var selectedRow = this.table.getSelectedRows()[0];

        if (selectedRow) {
            return;
        }
        getData.call(this, productId);
    }

    function hideCreateButton () {
        this.view.getEditButton().setModifier('show');
        this.view.getCreateButton().setModifier('hide');
    }

    function showCreateButton () {
        this.view.getEditButton().removeModifier('show');
        this.view.getCreateButton().removeModifier('hide');
    }

    function emptyInputNotification () {
        var options = NotificationRegion.NOTIFICATION_TYPES.error;
        options.canDismiss = true;
        options.canClose = true;
        this.eventBus.publish(Constants.events.NOTIFICATION, 'Please enter a enter data in all fields', options);
    }

    function addProduct (data) {
        var value = this.productSelect.getValue();
        if (!_.isEmpty(value)) {
            if (value.itemObj) {
                var feature = value.itemObj;
                feature.name = feature.title;
                data.product = feature;
            } else {
                data.product = value;
            }
        }
    }
});
