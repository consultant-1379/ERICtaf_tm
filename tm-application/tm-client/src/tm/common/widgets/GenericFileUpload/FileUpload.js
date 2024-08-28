/*global define*/
define([
    'jscore/core',
    './FileUploadView',
    'jscore/ext/net',
    'jscore/base/jquery',
    'widgets/Notification',
    'widgets/Dialog',
    'tablelib/Table',
    '../../../common/cells/CellFactory',
    '../../models/GenericFileCollection',
    '../../../common/table/TableHelper',
    '../../../common/ModelHelper',
    '../../../common/notifications/NotificationRegion/NotificationRegion',
    '../../../common/Constants',
    '../../../common/DateHelper',
    '../../../common/UserProfile'
], function (core, View, net, $, Notification, Dialog, Table, CellFactory, GenericFileCollection, TableHelper, ModelHelper,
             NotificationRegion, Constants, DateHelper, UserProfile) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true */

        View: View,

        init: function () {
            this.fileCollection = new GenericFileCollection();
            this.model = this.options.model;
            this.eventBus = this.options.eventBus;
            this.editMode = this.options.editMode;
            this.category = this.options.category;
            this.deletableFiles = [];
            this.filesUrl = '/tm-server/api/files/';
            this.continueSaving = true;

            this.columns = [
                {
                    title: 'Filename',
                    attribute: 'fileName',
                    cellType: CellFactory.link({
                        isObject: true,
                        url: function (obj) {
                            return this.filesUrl + this.product + '/' + this.category + '/' + this.model.getId() + '/' + obj.fileName;
                        }.bind(this)
                    }),
                    resizable: true,
                    sortable: true
                },
                {
                    title: 'Type',
                    attribute: 'type',
                    cellType: CellFactory.object(),
                    resizable: true,
                    sortable: true
                },
                {
                    title: 'Author',
                    attribute: 'author',
                    cellType: CellFactory.object(),
                    resizable: true,
                    sortable: true
                },
                {
                    title: 'Created',
                    attribute: 'created',
                    cellType: CellFactory.format({
                        mapper: function (timeString) {
                            return DateHelper.formatStringToDatetime(timeString);
                        }
                    }),
                    resizable: true,
                    sortable: true
                }
            ];

        },

        onViewReady: function () {
            this.view.afterRender();

            if (this.editMode) {
                this.columns.push({
                    cellType: CellFactory.icons({
                        icons: [{
                            title: 'Delete',
                            icon: 'delete',
                            action: this.onRemoveCellAction.bind(this)
                        }]
                    }),
                    width: '50px'
                });
            } else {
                this.view.hideInputFileHolder();
            }

            createFileTable.call(this);
        },

        setModel: function (model) {
            this.model = model;
            setProduct.call(this);
        },

        clearInput: function () {
            this.view.getInputFile().setValue('');
        },

        onRemoveCellAction: function (model) {
            this.deletableFiles.push(model.fileName);

            var rowIndexToRemove = null;

            this.fileTable.getRows().forEach(function (itemObj, index) {
                if (model.fileName === itemObj.getData().fileName) {
                    rowIndexToRemove = index;
                }
            });
            if (rowIndexToRemove != null) {
                this.fileTable.removeRow(rowIndexToRemove);
            }

        },

        saveFiles: function () {
            var df = $.Deferred();
            var data = new FormData();
            var files = this.view.getInputFile().getProperty('files');

            if (files.length > 0 && this.continueSaving) {
                for (var i = 0; i < files.length; i++) {
                    data.append('file', files[i]);
                }

                data.append('author', UserProfile.username);

                $.ajax({
                    url: this.filesUrl + this.product + '/' + this.category + '/' + this.model.getId(),
                    type: 'POST',
                    contentType: false,
                    processData: false,
                    dataType: 'application/json',
                    data: data,

                    statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                        201: function () {
                            this.clearInput();
                            df.resolve();
                        }.bind(this),
                        400: function (message) {
                            this.clearInput();
                            var options = NotificationRegion.NOTIFICATION_TYPES.error;
                            options.canDismiss = true;
                            options.canClose = true;

                            if (message) {
                                this.eventBus.publish(Constants.events.NOTIFICATION, message.responseText, options);
                            }
                            df.reject();
                        }.bind(this)
                    }, df)

                });
                return df.promise();
            }
            this.continueSaving = true;
            return df.resolve();
        },

        validateFiles: function () {
            var filesToSave = this.view.getInputFile().getProperty('files');
            var fileNames = [];
            var result = false;

            for (var key in filesToSave) {
                if (filesToSave[key].name != null) {
                    fileNames.push(filesToSave[key].name);
                }
            }
            this.fileTable.getRows().forEach(function (itemObj) {
                if (fileNames.indexOf(itemObj.getData().fileName) >= 0) {
                    result = true;
                }
            });

            if (result) {
                return false;
            }

            return true;
        },

        clearTable: function () {
            this.fileTable.setData([]);
        },

        fetchFiles: function (model) {
            this.model = model;
            setProduct.call(this);
            this.fileCollection.setProduct(this.product);
            this.fileCollection.setCategory(this.category);
            this.fileCollection.setId(this.model.getId());
            this.fileCollection.fetch({
                reset: true,
                statusCode: {
                    400: function () {
                        this.clearTable();
                    }.bind(this),
                    204: function () {
                        this.view.getTableHolder().setModifier('hidden');
                    }.bind(this)
                }

            });
        },

        deleteFiles: function () {
            var df = $.Deferred();
            if (this.deletableFiles.length > 0) {
                var url = this.filesUrl + this.product + '/' + this.category + '/' + this.model.getId() + '?filenames=' + this.deletableFiles.join(',');
                net.ajax({
                    url: url,
                    type: 'DELETE',
                    statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                        204: function () {
                            df.resolve();
                        }.bind(this)
                    }, df)

                });
                this.deletableFiles = [];
                return df.promise();
            }
            return df.resolve();
        }
    });

    function createFileTable () {
        this.fileTable = new Table({
            tooltips: true,
            columns: this.columns,
            modifiers: [
                {name: 'striped'}
            ]
        });

        this.fileTable.attachTo(this.view.getTableHolder());
        this.tableHelper = new TableHelper({
            collection: this.fileCollection,
            table: this.fileTable,
            isPaginated: false
        });
        this.tableHelper.applyColumnResize();
        this.tableHelper.applyLocalCollectionReset();
        this.tableHelper.applyLocalSort();

        this.view.getTableHolder().setModifier('hidden');
        this.fileCollection.addEventHandler('reset', function () {
            this.view.getTableHolder().removeModifier('hidden');
        }.bind(this));
    }

    function setProduct () {
        this.product = this.category === 'test-cases' ? this.model.getFeature().product.name : this.model.getProduct();
    }
});
