define([
    'jscore/core',
    './TestCaseImportContentRegionView',
    'jscore/base/jquery',
    'tablelib/Table',
    'tablelib/plugins/Selection',
    'tablelib/plugins/SortableHeader',
    '../../../../common/table/plugins/IdentifiableTable/IdentifiableTable',
    '../../../../common/table/plugins/IdentifiableRows/IdentifiableRows',
    '../../../../common/table/filters/StringFilterCell/StringFilterCell',
    'jscore/ext/utils/base/underscore',
    '../../../../common/Constants',
    '../../../../common/ValidationHelper',
    '../../../../common/ContextFilter',
    '../../../../common/Animation',
    '../../../../common/table/TableHelper',
    '../../../testCases/models/testCases/TestCasesCollection',
    '../../../../common/notifications/NotificationRegion/NotificationRegion',
    '../../../../common/ModelHelper',
    '../../../../ext/domUtils',
    '../../../../common/cells/CellFactory',
    '../../../../common/requirement/pill/RequirementPillsArrayWidget',
    '../../../../common/Navigation'
], function (core, View, $, Table, Selection, SortableHeader, IdentifiableTable, IdentifiableRows, StringFilterCell, _,
             Constants, ValidationHelper, ContextFilter, Animation, TableHelper, TestCasesCollection, NotificationRegion,
             ModelHelper, domUtils, CellFactory, RequirementPillsArrayWidget, Navigation) {

    'use strict';

    return core.Region.extend({
        /*jshint validthis:true */

        View: View,

        init: function () {
            this.eventBus = this.getContext().eventBus;
            this.testCaseCollection = new TestCasesCollection();
        },

        onViewReady: function () {
            this.view.afterRender();

            this.animation = new Animation(this.getElement(), this.eventBus);
            this.animation.showOn(Constants.events.SHOW_VIEW_TEST_CASE_IMPORT);
            this.animation.hideOn(Constants.events.HIDE_VIEW_TEST_CASE_IMPORT);
            this.animation.markCurrentOn(Constants.events.MARK_VIEW_TEST_CASE_IMPORT_CURRENT);

            this.view.getImportButton().addEventHandler('click', this.importFiles, this);
            this.view.getErrorPanel().setModifier('hide');

            this.columns = [
                {
                    title: 'Test Case ID',
                    attribute: 'testCaseId',
                    secondHeaderCellType: StringFilterCell,
                    cellType: CellFactory.link({
                        isObject: true,
                        url: function (rowObj) {
                            return '#' + Navigation.getTestCaseDetailsUrl(rowObj.testCaseId);
                        }
                    }),
                    width: '200px',
                    sortable: true,
                    resizable: true
                },
                {
                    title: 'Requirement IDs',
                    attribute: 'requirements',
                    secondHeaderCellType: StringFilterCell,
                    cellType: CellFactory.widget({
                        widget: RequirementPillsArrayWidget
                    }),
                    width: '200px',
                    sortable: true,
                    resizable: true
                },
                {
                    title: 'Title',
                    attribute: 'title',
                    cellType: CellFactory.object(),
                    secondHeaderCellType: StringFilterCell,
                    sortable: true,
                    resizable: true
                },
                {
                    title: 'Description',
                    attribute: 'description',
                    cellType: CellFactory.object(),
                    secondHeaderCellType: StringFilterCell,
                    sortable: true,
                    resizable: true
                }
            ];

            this.view.getInput().addEventHandler('change', function () {
                if (this.view.getInput().getProperty('files').length === 0) {
                    this.view.getImportButton().setModifier('disabled');
                } else {
                    this.view.getImportButton().removeModifier('disabled');
                }
            }.bind(this));
        },

        clearInput: function () {
            this.view.getInput().setText('');
        },

        clearErrorPanel: function () {
            this.view.getErrorPanel().setText('');
        },

        importFiles: function () {
            this.view.showLoadingBar();
            var data = new FormData();
            var files = this.view.getInput().getProperty('files');

            if (files.length > 0) {
                for (var i = 0; i < files.length; i++) {
                    data.append('file', files[i]);
                }

                $.ajax({
                    url: '/tm-server/api/test-cases/import',
                    type: 'POST',
                    contentType: false,
                    processData: false,
                    dataType: 'application/json',
                    data: data,

                    statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                        200: function (items) {
                            this.clearInput();
                            this.createTableData(items);
                            var options = NotificationRegion.NOTIFICATION_TYPES.success;
                            options.canDismiss = true;
                            options.canClose = true;
                            this.eventBus.publish(Constants.events.NOTIFICATION, 'Import was successful', options);
                            this.view.getErrorPanel().setModifier('hide');
                            this.view.hideLoadingBar();
                        }.bind(this),
                        400: function (message) {
                            this.clearInput();
                            removeTable.call(this);
                            this.view.getErrorPanel().setText(ValidationHelper.getValidationErrors(message));
                            this.view.getErrorPanel().removeModifier('hide');
                            this.view.getErrorPanel().setStyle('height', '');
                            domUtils.textareaAutoAdjust(this.view.getErrorPanel());
                            this.view.hideLoadingBar();
                        }.bind(this),
                        500: function () {
                            this.clearInput();
                            removeTable.call(this);
                            var options = NotificationRegion.NOTIFICATION_TYPES.error;
                            options.canDismiss = true;
                            options.canClose = true;
                            this.eventBus.publish(Constants.events.NOTIFICATION, 'Import was not successful', options);
                            this.view.hideLoadingBar();
                        }.bind(this)
                    })

                });
            }
        },

        createTableData: function (tableData) {
            var items = JSON.parse(tableData.responseText);
            this.testCaseCollection = new TestCasesCollection();
            _.each(items, function (item) {
                this.testCaseCollection.addModel(item);
            }.bind(this));

            createTable.call(this);
        }

    });

    function createTable () {
        removeTable.call(this);
        this.testCaseTable = new Table({
            plugins: [
                new Selection({
                    selectableRows: false,
                    bind: true
                }),
                new IdentifiableTable({
                    attribute: 'id',
                    identifier: 'TMS_Import_TestCaseTable'
                }),
                new IdentifiableRows({
                    rowIdentifier: 'title'
                })
            ],
            modifiers: [
                {name: 'striped'}
            ],
            tooltips: true,
            columns: this.columns
        });

        this.tableHelper = new TableHelper({
            eventBus: this.eventBus,
            collection: this.testCaseCollection,
            parent: this.view.getTableHolder(),
            table: this.testCaseTable,
            isPaginated: false
        });

        this.tableHelper.applySortAndFilter();

        this.testCaseTable.attachTo(this.view.getTableHolder());
        this.testCaseTable.setData(this.testCaseCollection.toJSON());
    }

    function removeTable () {
        if (this.testCaseTable) {
            this.testCaseTable.destroy();
        }
    }

});
