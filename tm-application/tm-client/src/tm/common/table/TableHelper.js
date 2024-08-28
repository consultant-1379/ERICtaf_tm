define([
    'jscore/ext/utils/base/underscore',
    'widgets/Pagination',
    'tablelib/plugins/SecondHeader',
    'tablelib/plugins/SortableHeader',
    'tablelib/plugins/ResizableHeader',
    './plugins/IdentifiableTable/IdentifiableTable',
    './plugins/IdentifiableRows/IdentifiableRows',
    './../ModelHelper',
    './../ObjectHelper',
    'widgets/utils/domUtils'
], function (_, Pagination, SecondHeader, SortableHeader, ResizableHeader, IdentifiableTable, IdentifiableRows,
            ModelHelper, ObjectHelper, domUtils) {
    'use strict';

    var TableHelper = function (options) {
        this.isPaginated = options.isPaginated;
        this.localStorageNamespace = options.localStorageNamespace;
        this.collection = options.collection;
        if (this.isPaginated) {
            this.count = this.collection.getTotalCount();
            this.page = this.collection.getPage();
            this.pagination = null;
            this.parent = options.parent;
        }
        this.eventBus = options.eventBus;
        this.data = options.data || {};
        this.table = options.table;
        this.filtersChangedEvent = options.filtersChangedEvent;

        this.sortData = {};
        this.filters = {};

        this.requestsCount = 0;
    };

    var DATA_LOADED_ATTRIBUTE = 'data-loaded';

    TableHelper.prototype.increaseRequestsCount = function () {
        this.table.view.getTable().setAttribute(DATA_LOADED_ATTRIBUTE, false);
        this.requestsCount++;
    };

    TableHelper.prototype.decreaseRequestsCount = function () {
        this.requestsCount--;
        if (this.requestsCount <= 0) {
            this.table.view.getTable().setAttribute(DATA_LOADED_ATTRIBUTE, true);
        }
    };

    TableHelper.prototype.updatePage = function () {
        var currentCount = this.collection.getTotalCount();
        var currentPage = this.collection.getPage();
        if (this.count === currentCount && currentPage === this.page) {
            return;
        }
        this.count = currentCount;
        this.page = currentPage;

        if (this.pagination != null) {
            this.pagination.destroy();
            this.pagination = null;
        }

        var perPage = this.collection.getPerPage();
        if (this.collection.getTotalCount() > perPage) {
            var pages = Math.ceil(this.collection.getTotalCount() / perPage);
            this.pagination = new Pagination({
                pages: pages,
                onPageChange: function (pageNumber) {
                    if (this.collection.getPage() !== pageNumber) {
                        this.collection.setPage(pageNumber);
                        this.page = pageNumber;

                        this.collection.fetch({
                            reset: true,
                            data: this.data,
                            statusCode: ModelHelper.statusCodeHandler(this.eventBus)
                        });

                        this.increaseRequestsCount();
                    }
                }.bind(this)
            });
            this.pagination.attachTo(this.parent);
        }
    };

    TableHelper.prototype.applyColumnResize = function () {
        this.table.addEventHandler('columnresize', function (column) {
            TableHelper.updateColumnWidth(this.localStorageNamespace, column);
        }, this);
    };

    TableHelper.prototype.applySortAndFilter = function () {
        this.table.addEventHandler('sort', this.sort, this);
        this.table.addEventHandler('filter', this.filter, this);
    };

    TableHelper.prototype.applyCollectionReset = function () {
        this.collection.addEventHandler('reset', function (collection) {
            this.updatePage();
            this.table.setData(collection.toJSON());
            this.decreaseRequestsCount();
        }, this);
    };

    TableHelper.prototype.applyLocalSort = function () {
        this.table.addEventHandler('sort', this.localSort, this);
    };

    TableHelper.prototype.applyLocalCollectionReset = function () {
        this.collection.addEventHandler('reset', function (collection) {
            this.table.setData(collection.toJSON());
            this.decreaseRequestsCount();
        }, this);
    };

    TableHelper.prototype.applyLocalFilter = function () {
        this.table.addEventHandler('filter', this.localFilter, this);
    };

    TableHelper.prototype.sort = function (sortMode, sortAttr) {
        var sortObj = {sortMode: sortMode, sortAttr: sortAttr};

        if (_.isEqual(this.sortData, sortObj)) {
            return;
        }
        this.sortData = sortObj;

        this.collection.resetPage();
        this.collection.setSortData(this.sortData);
        this.collection.fetch({
            reset: true,
            data: this.data,
            statusCode: ModelHelper.statusCodeHandler(this.eventBus)
        });

        this.increaseRequestsCount();
    };

    TableHelper.prototype.localSort = function (sortMode, sortAttr) {
        this.increaseRequestsCount();

        var rows = this.table.getRows();

        rows.sort(function (a, b) {
           var aValue = ObjectHelper.findValue(a.getData(), sortAttr),
               bValue = ObjectHelper.findValue(b.getData(), sortAttr);

                return TableHelper.compareValues(aValue, bValue);

        });

        if (sortMode === 'desc') {
            rows.reverse();
        }
        rows.forEach(function (row) {
            row.detach();
            row.attach();
        });

        this.decreaseRequestsCount();
    };

    TableHelper.prototype.filter = function (attribute, value, comparator) {
        if (_.isEmpty(value)) {
            delete this.filters[attribute];
        } else {
            this.filters[attribute] = {value: value, comparator: comparator};
        }
        this.collection.resetPage();
        this.collection.setFiltersData(this.filters);
        this.collection.fetch({
            reset: true,
            data: this.data,
            statusCode: ModelHelper.statusCodeHandler(this.eventBus)
        });
        this.increaseRequestsCount();
        if (this.filtersChangedEvent) {
            this.eventBus.publish(this.filtersChangedEvent, this.filters);
        }
    };

    TableHelper.prototype.localFilter = function (attribute, value, comparator) {
        this.increaseRequestsCount();

        if (_.isEmpty(value)) {
            delete this.filters[attribute];
        } else {
            this.filters[attribute] = {value: value, comparator: comparator};
        }

        if (_.isEmpty(this.filters)) {
            this.table.setData(this.collection.toJSON());
            this.table.trigger('localFilter', this.collection);
            this.decreaseRequestsCount();
            return;
        }

        var collection = this.collection;
        _.each(this.filters, function (filterObj, attribute) {
            collection = collection.searchMap(getRegexpPattern(filterObj), [attribute]);
        });
        this.table.setData(collection.toJSON());
        this.table.trigger('localFilter', collection);

        this.decreaseRequestsCount();
    };

    TableHelper.prototype.clearFilterText = function () {
        var filterInputs = domUtils.findAll('.eaTM-StringFilter-input', this.table.getElement());
        filterInputs.forEach(function (input) {
            input.setValue('');
        });
    };

    TableHelper.prototype.setDataWithFilter = function (newCollection) {
        this.collection = newCollection;
        var collection = this.collection;
        _.each(this.filters, function (filterObj, attribute) {
            collection = collection.searchMap(getRegexpPattern(filterObj), [attribute]);
        });
        this.table.setData(collection.toJSON());
    };

    var patterns = {
        '=': '^%s$',
        '!=': '^(?!%s$)'
    };

    function getRegexpPattern (filterObj) {
        if (!patterns.hasOwnProperty(filterObj.comparator)) {
            return filterObj.value;
        }

        var pattern = patterns[filterObj.comparator];
        return new RegExp(pattern.replace('%s', filterObj.value));
    }

    TableHelper.plugins = function (options, additionalPlugins) {
        var defaultPlugins = [
            new SecondHeader(),
            new SortableHeader(),
            new ResizableHeader(),
            new IdentifiableTable({
                attribute: 'id',
                identifier: options.tableId || 'TMS_Table_' + new Date().getTime()
            }),
            new IdentifiableRows({
                rowIdentifier: options.tableRowAttribute || 'id'
            })
        ];

        if (_.isEmpty(additionalPlugins)) {
            return defaultPlugins;
        }
        return _.union(defaultPlugins, additionalPlugins);
    };

    TableHelper.applyColumnsWidths = function (lsNamespace, columns) {
        var ls = localStorage[lsNamespace];
        if (ls) {
            ls = JSON.parse(ls);
            columns.forEach(function (col) {
                if (ls[col.attribute]) {
                    col.width = ls[col.attribute];
                }
            });
        }
    };

    TableHelper.updateColumnWidth = function (lsNamespace, column) {
        var ls = JSON.parse(localStorage[lsNamespace] || '{}');
        ls[column.attribute] = column.width;
        localStorage[lsNamespace] = JSON.stringify(ls);
    };

    TableHelper.prototype.isFiltered = function () {
        var filterInputs = domUtils.findAll('.eaTM-StringFilter-input', this.table.getElement());
        var result = false;
        filterInputs.forEach(function (input) {

            if (input.getValue()) {
                result = true;
            }
        });
        return result;
    };

    TableHelper.compareValues = function (a, b) {
        var aList = [], bList = [];
        if (a == null || b == null) {
            return 0;
        } else if (!isNaN(a) || !isNaN(b)) {
            return a - b;
        }

        a.replace(/(\d+)|(\D+)/g, function (_, $1, $2) { aList.push([$1 || Infinity, $2 || '']); });
        b.replace(/(\d+)|(\D+)/g, function (_, $1, $2) { bList.push([$1 || Infinity, $2 || '']); });

        while (aList.length && bList.length) {
            var aNext = aList.shift();
            var bNext = bList.shift();
            var result = (aNext[0] - bNext[0]) || aNext[1].localeCompare(bNext[1]);
            if (result) {
                return result;
            }
        }

        return aList.length - bList.length;
    };

    return TableHelper;

});
