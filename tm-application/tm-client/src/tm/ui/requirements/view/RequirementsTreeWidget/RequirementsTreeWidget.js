define([
    'jscore/core',
    'widgets/Tree',
    'widgets/utils/domUtils',
    './RequirementsTreeWidgetView',
    '../../models/RequirementsCollection',
    '../../../../common/widgets/actionIcon/ActionIcon',
    '../../../../common/ContextFilter'
], function (core, Tree, domUtils, View, RequirementsCollection, ActionIcon, ContextFilter) {
    'use strict';

    /*jshint validthis:true */

    return core.Widget.extend({

        View: View,

        init: function (options) {
            this.needTreeRedraw = true;
            this.isFiltered = false;
            this.isRefinedFiltered = false;
            this.requirementsCollection = options.requirementsCollection;
            this.region = options.region;
        },

        onViewReady: function () {
            this.view.afterRender();

            this.requirementsCollection.addEventHandler('reset', this.onCollectionReset, this);
            this.view.getFilterInput().addEventHandler('keydown', this.onFilterValueInput, this);

            this.filterClearIcon = new ActionIcon({
                iconKey: 'close',
                interactive: true,
                hide: true
            });
            this.filterClearIcon.addEventHandler('click', this.onFilterClearIconClick, this);
            this.filterClearIcon.attachTo(this.view.getFilterClearIcon());

            this.filterIcon = new ActionIcon({
                iconKey: 'filter',
                interactive: true,
                hide: true
            });
            this.filterIcon.addEventHandler('click', this.onFilterIconClick, this);
            this.filterIcon.attachTo(this.view.getFilterIcon());

            updateFilterClearIcon.call(this);

            core.Window.addEventHandler('resize', this.onWindowResize.bind(this));
        },

        requestTreeRedraw: function () {
            this.needTreeRedraw = true;
        },

        onFilterClearIconClick: function (e) {
            e.preventDefault();
            this.clearFilter();
            this.region.refreshRequirements('');
        },

        onFilterIconClick: function () {
            var search = this.view.getFilterInput().getValue();
            updateFilterClearIcon.call(this);
            this.needTreeRedraw = true;
            if (this.isRefinedFiltered && this.isFiltered) {
                this.region.unfilterRequirements();
                this.filterIcon.setIcon('filter');
                this.isRefinedFiltered = false;
            } else {
                this.isRefinedFiltered = true;
                this.region.filterRequirements(search);
                this.filterIcon.setIcon('undo');
            }
        },

        onFilterValueInput: function (e) {
            var keyCode = e.originalEvent.keyCode || e.originalEvent.which;
            if (keyCode === 13) {
                var search = this.view.getFilterInput().getValue();
                this.region.refreshRequirements(search);
                this.region.refreshPageHash('');
                this.filterIcon.setIcon('filter');
                this.isRefinedFiltered = false;
            }
            updateFilterClearIcon.call(this);
        },

        onCollectionReset: function () {
            if (!this.needTreeRedraw) {
                this.needTreeRedraw = true;
                return;
            }
            this.clearContent();
            this.createTree(this.requirementsCollection);

            if (!this.isResized) {
                this.onWindowResize();
                this.isResized = true;
            }
        },

        onWindowResize: function () {
            var windowDimensions = domUtils.getWindowDimensions(),
                treeBlockPosition = this.view.getTreeHolder().getPosition();

            var maxHeight = windowDimensions.height - treeBlockPosition.top - 3;
            maxHeight = (maxHeight > 300 ? maxHeight : 300);

            this.view.getTreeHolder().setStyle('max-height', maxHeight);
        },

        createTree: function (collection) {
            if (collection.size() === 0) {
                this.setMessage('No requirements found for current project.');
            }
            var items = parseCollection.call(this, collection);
            this.tree = new Tree({
                items: items
            });
            if (this.isFiltered || ContextFilter.requirementIdParam !== '') {
                expandParentTreeItemsAndSelectItem.call(this, this.tree.getItems());
            }
            enableMatching(this.tree);
            this.tree.attachTo(this.view.getTreeHolder());
            this.itemselectEventId = this.tree.addEventHandler('itemselect', this.onTreeItemSelect, this);
        },

        onTreeItemSelect: function (event) {
            var treeItem = event.getDefinition();
            this.needTreeRedraw = false;
            this.region.setDetailsContent(treeItem);
            this.region.refreshPageHash(treeItem.id);
            this.view.getFilterInput().setValue(treeItem.id);
            updateFilterClearIcon.call(this);
        },

        setMessage: function (message) {
            this.message = message;
            this.view.getEmptyTreeResult().setText(message);
        },

        setSearchInput: function (search) {
            this.view.getFilterInput().setValue(search);
            updateFilterClearIcon.call(this);
        },

        clearMessage: function () {
            this.message = null;
            this.view.getEmptyTreeResult().setText('');
        },

        clearFilter: function () {
            this.view.getFilterInput().setValue('');
            updateFilterClearIcon.call(this);
        },

        clearContent: function () {
            if (this.message) {
                this.clearMessage();
            }
            if (this.tree) {
                this.tree.removeEventHandler('itemselect', this.itemselectEventId);
                this.tree.detach();
                this.tree.destroy();
            }
        }

    });

    // recursive function
    function expandParentTreeItemsAndSelectItem (items) {
        items.forEach(function (treeItem) {
            if (treeItem.getItems().length > 0) {
                treeItem.expand();
                expandParentTreeItemsAndSelectItem.call(this, treeItem.getItems());
            }
            if (treeItem.getDefinition().id === ContextFilter.requirementIdParam) {
                treeItem.select();
                this.region.setDetailsContent(treeItem.getDefinition());
            }
        }.bind(this));
    }

    // recursive function
    function parseCollection (collection) {
        var items = [];
        collection.each(function (treeItem) {
            items.push(createTreeItem(treeItem));
        });
        return items;
    }

    function createTreeItem (treeItemModel) {
        var summary = (treeItemModel.getSummary() === null ? 'Requirement is not found!' : treeItemModel.getSummary()),
            item = {
                id: treeItemModel.getExternalId(),
                type: treeItemModel.getType(),
                label: treeItemModel.getExternalId() + ' (' + treeItemModel.getTestCasesCount() + ')',
                title: treeItemModel.getExternalId() + '\n' + summary,
                summary: treeItemModel.getSummary(),
                matching: treeItemModel.getMatching() !== undefined ? treeItemModel.getMatching() : true,
                statusName: treeItemModel.getExternalStatusName(),
                deliveredIn: treeItemModel.getDelivered() ? treeItemModel.getDelivered() : ''
            },
            children = treeItemModel.getChildren();

        if (children && children.length > 0) {
            var childrenCollection = new RequirementsCollection(children);
            item.children = parseCollection(childrenCollection);
        }

        if (item.type === 'MR') {
            item.icon = {prefix: 'ebIcon', name: 'm'};
        } else if (item.type === 'Epic') {
            item.icon = {prefix: 'ebIcon', name: 'e'};
        } else if (item.type === 'Story') {
            item.icon = {prefix: 'ebIcon', name: 's'};
        } else if (item.type === 'Improvement') {
            item.icon = {prefix: 'ebIcon', name: 'i'};
        } else {
            item.icon = {prefix: 'ebIcon', name: 'u'};
        }
        return item;
    }

    // recursive function
    function enableMatching (tree) {
        var items = tree.getItems();
        items.forEach(function (item) {
            var labelEl = item.getElement().find('.ebTreeItem-label');
            if (!item.getDefinition().matching) {
                labelEl.setModifier('grey', '40', 'ebColor');
            }
            labelEl.setAttribute('title', item.getDefinition().title);
            enableMatching(item);
        });
    }

    function updateFilterClearIcon () {
        var isEmpty = this.view.getFilterInput().getValue().length === 0;
        this.isFiltered = !isEmpty;
        this.filterClearIcon.setHidden(isEmpty);
        this.filterIcon.setHidden(isEmpty);
    }

});
