define([
    'jscore/core',
    'tablelib/Table',
    'tablelib/plugins/Selection',
    'tablelib/plugins/SortableHeader',
    'tablelib/plugins/ColorBand',
    '../../../../common/table/plugins/IdentifiableTable/IdentifiableTable',
    '../../../../common/table/plugins/IdentifiableRows/IdentifiableRows',
    '../../../../common/table/plugins/ColorRows/ColorRows',
    '../../../../common/table/TableHelper',
    './TestCaseSearchWidgetView',
    '../../../../common/Constants',
    '../../../testCases/search/TestCaseSearchFormWidget/TestCaseSearchFormWidget',
    '../../../testCases/models/testCases/TestCasesCollection',
    'tablelib/plugins/ResizableHeader'
], function (core, Table, Selection, SortableHeader, ColorBand, IdentifiableTable, IdentifiableRows, ColorRows,
             TableHelper, View, Constants, TestCaseSearchFormWidget, TestCasesCollection, ResizableHeader) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true */

        View: View,

        init: function (options) {
            this.region = options.region;
            this.collectionToCompare = options.collection;
            this.toggle = false;

            this.testCasesCollection = new TestCasesCollection();

            this.searchColumns = [
                {
                    title: 'Test Case ID',
                    attribute: 'testCaseId',
                    width: '150px',
                    resizable: true
                },
                {
                    title: 'Title',
                    attribute: 'title',
                    resizable: true
                },
                {
                    title: 'Version',
                    attribute: 'version',
                    width: '70px',
                    resizable: true
                }
            ];

            this.testCaseSearchFormWidget = new TestCaseSearchFormWidget({
                region: this
            });
        },

        onViewReady: function () {
            this.view.afterRender();
            createTestCasesTable.call(this);

            this.testCasesCollection.addEventHandler('reset', function (data) {
                this.toggle = false;

                var result = data.toJSON();
                setRowColor.call(this, result);

                this.tableHelper.updatePage();
                this.testCaseSearchTable.setData(result);
                this.tableHelper.decreaseRequestsCount();

                checkAndDisableCheckBoxes.call(this, this.toggle);
            }, this);

            this.collectionToCompare.addEventHandler('remove', function () {
                this.testCasesCollection.trigger('reset', this.testCasesCollection);
            }, this);

            this.testCaseSearchTable.addEventHandler('checkheader', function () {
                this.toggle = !this.toggle;
                checkAndDisableCheckBoxes.call(this, this.toggle);
            }, this);

            this.testCaseSearchTable.attachTo(this.view.getSearchTable());
            this.testCaseSearchFormWidget.attachTo(this.view.getSearchOptions());
            this.view.getAddButton().addEventHandler('click', this.saveExecutions, this);

        },

        saveExecutions: function () {
            var selected = this.getSelectedRows();
            var models = [];
            selected.forEach(function (selection) {
                var model = this.testCasesCollection.getModel(selection.id);
                models.push(model);
            }.bind(this));

            this.tableHelper.increaseRequestsCount();

            this.trigger('addSelected', models);
            this.testCasesCollection.trigger('reset', this.testCasesCollection);
        },

        runSearch: function (criterions) {
            this.testCaseSearchTable.setData([]);
            this.testCasesCollection.setSearchQuery(criterions);
            this.testCasesCollection.fetch({
                reset: true,
                data: {
                    view: 'version'
                }
            });
            this.tableHelper.increaseRequestsCount();
        },

        getSelectedRows: function () {
            var checkedRows = this.testCaseSearchTable.getCheckedRows(),
                checkedObjects = [];

            checkedRows.forEach(function (checkedRow) {
                var objectData = checkedRow.getData();
                objectData._rowIndex = checkedRow.getIndex();
                checkedObjects.push(objectData);
            });
            return checkedObjects;
        }

    });

    function setRowColor (data) {
        data.forEach(function (row) {
            this.collectionToCompare.each(function (item) {
                if (row.testCaseId === item.getTestCaseId()) {
                    row.rowColor = Constants.color.GREEN_40;
                }
            }.bind(this));
        }.bind(this));
    }

    function checkAndDisableCheckBoxes (toggle) {
        this.testCaseSearchTable.checkRows(function (row) {
            var rowData = row.getData();
            if (rowData.rowColor) {
                row.getElement().find('.ebCheckbox').setProperty('disabled', true);
                return true;
            } else {
                return toggle;
            }
        });
    }

    function createTestCasesTable () {
        this.testCaseSearchTable = new Table({
            plugins: [
                new ColorBand({
                    color: function (row) {
                        return row.getData().rowColor ? Constants.color.GREEN : Constants.color.WHITE;
                    }
                }),
                new Selection({
                    checkboxes: true,
                    selectableRows: false,
                    multiselect: true,
                    bind: true
                }),
                new IdentifiableTable({
                    attribute: 'id',
                    identifier: 'TMS_TestCasesSearch_GenericTestCaseSearch'
                }),
                new IdentifiableRows({
                    rowIdentifier: 'title'
                }),
                new ResizableHeader(),
                new ColorRows()
            ],
            modifiers: [
                {name: 'striped'}
            ],
            tooltips: true,
            columns: this.searchColumns
        });

        this.tableHelper = new TableHelper({
            eventBus: this.eventBus,
            collection: this.testCasesCollection,
            parent: this.view.getSearchTable(),
            table: this.testCaseSearchTable,
            data: {
                view: 'version'
            },
            isPaginated: true
        });
    }

});
