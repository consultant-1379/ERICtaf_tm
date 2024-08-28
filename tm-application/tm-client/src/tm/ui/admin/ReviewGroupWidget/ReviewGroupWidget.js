define([
    'jscore/core',
    'jscore/ext/utils/base/underscore',
    './ReviewGroupWidgetView',
    '../../../common/Constants',
    '../../../common/Navigation',
    '../../../common/table/TableHelper',
    'tablelib/Table',
    'tablelib/plugins/Selection',
    'tablelib/plugins/SortableHeader',
    'tablelib/plugins/SecondHeader',
    '../../../common/cells/CellFactory',
    '../../../common/table/filters/StringFilterCell/StringFilterCell',
    '../../../common/ModelHelper',
    '../../../common/notifications/NotificationRegion/NotificationRegion',
    '../models/ReviewGroupCollection',
    '../models/ReviewGroupModel',
    '../../../common/AutocompleteInput/AutocompleteInput',
    '../../../common/models/completion/CompletionsCollection',
    '../../../common/table/plugins/IdentifiableTable/IdentifiableTable',
    '../../../common/table/plugins/IdentifiableRows/IdentifiableRows',
    'widgets/Dialog'
], function (core, _, View, Constants, Navigation, TableHelper, Table, Selection, SortableHeader, SecondHeader,
             CellFactory, StringFilterCell, ModelHelper, NotificationRegion, ReviewGroupCollection, ReviewGroupModel,
             AutocompleteInput, CompletionsCollection, IdentifiableTable, IdentifiableRows, Dialog) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true */

        View: View,

        init: function (options) {
            this.eventBus = options.eventBus;
            this.collection = new ReviewGroupCollection();
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

            this.userColumns = [
                {
                    title: 'User',
                    attribute: 'userName',
                    cellType: CellFactory.object(),
                    secondHeaderCellType: StringFilterCell,
                    sortable: true,
                    resizable: true
                },
                {
                    cellType: CellFactory.icons({
                        icons: [{
                            title: 'Delete',
                            icon: 'delete',
                            action: this.onRemoveUserAction.bind(this)
                        }]
                    }),
                    width: '50px'
                }

            ];

            createTable.call(this);
            createUserTable.call(this);

            this.collection.addEventHandler('reset', function () {
                this.clear();
            }.bind(this));

            this.view.getCreateButton().addEventHandler('click', function () {
                var group = this.view.getGroupInput().getValue();
                var user = this.userSelectBox.getValueObj();
                if (_.isEmpty(group)) {
                    var options = NotificationRegion.NOTIFICATION_TYPES.error;
                    options.canDismiss = true;
                    options.canClose = true;
                    this.eventBus.publish(Constants.events.NOTIFICATION, 'Group field is empty', options);
                    return;
                }

                create.call(this, group, user);
                this.clear();
            }.bind(this));

            this.view.getEditButton().addEventHandler('click', function () {
                var groupName = this.view.getGroupInput().getValue();
                var user = this.userSelectBox.getValueObj();
                if (_.isEmpty(groupName)) {
                    var options = NotificationRegion.NOTIFICATION_TYPES.error;
                    options.canDismiss = true;
                    options.canClose = true;
                    this.eventBus.publish(Constants.events.NOTIFICATION, 'Group field is empty', options);
                    return;
                }
                var model = this.collection.getModel(this.selected.id);
                var users = model.getUsers();

                setUsers.call(this, users, user, model);

                model.setName(groupName);

                update.call(this, model);
                this.clear();
            }.bind(this));

            createAutoComplete.call(this);
            getData.call(this);
        },

        clear: function () {
            this.userSelectBox.setValue('');
            this.view.getGroupInput().setValue('');
            this.userTable.setData([]);
        },

        onUserCompletion: function (search, cb) {
            this.userCompletionCollection.setSearch(search);
            this.userCompletionCollection.fetch({
                reset: true,
                statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                    200: function (users) {
                        var data = this.createUserData(users);
                        cb(data);
                    }.bind(this)
                })
            });
        },

        updateSelectedUser: function (user) {
            this.user = user;
        },

        createUserData: function (users) {
            var userList = [];
            if (users.length > 0) {
                users.forEach(function (user) {
                    var userObj = {};
                    userObj.value = user.userName;
                    userObj.name = user.userName;
                    userObj.userId = user.externalId;
                    userObj.userName = user.userName;
                    userList.push(userObj);
                });
            }
            return userList;
        },

        onRemoveCellAction: function (model) {
            this.deleteReviewGroup(model);
            this.clear();
        },

        deleteReviewGroup: function (model) {
            var dialog = new Dialog({
                header: 'Confirm Delete',
                type: 'warning',
                content: 'Are you sure you want to remove "' + model.name + '"?',
                buttons: [{
                    caption: 'Delete',
                    color: 'red',
                    action: function () {
                        var foundModel = {};
                        this.collection.each(function (modelItem, index) {
                            if (modelItem.id === model.id) {
                                foundModel = this.collection.getAtIndex(index);
                                return;
                            }
                        }.bind(this));
                        remove.call(this, foundModel);
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
        },

        onRemoveUserAction: function (model) {
            this.deleteUser(model);
            this.clear();
        },

        deleteUser: function (model) {
            var dialog = new Dialog({
                header: 'Confirm Delete',
                type: 'warning',
                content: 'Are you sure you want to remove "' + model.userName + '"?',
                buttons: [{
                    caption: 'Delete',
                    color: 'red',
                    action: function () {
                        var foundModel = this.collection.getModel(this.selected.id);
                        var users = foundModel.getUsers();
                        var newUsers = [];

                        users.forEach(function (item) {
                            if (item.id !== model.id) {
                                newUsers.push(item);
                            }
                        });
                        foundModel.setUsers(newUsers);
                        update.call(this, foundModel, false);
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

    function getData () {
        this.collection.fetch({
            reset: true,
            success: function () {
                if (this.selected) {
                    var id = this.selected.id;
                    var row = {};
                    this.table.selectRows(function (row) {
                        var data = row.getData();
                        if (data.id === id) {
                            row = data;
                            return true;
                        } else {
                            return false;
                        }
                    }.bind(this));

                    this.table.trigger('rowselect', row);
                }
            }.bind(this)
        });
    }

    function update (model) {
        model.save({}, {
            statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                200: function () {
                    getData.call(this);
                    var options = NotificationRegion.NOTIFICATION_TYPES.success;
                    options.canDismiss = true;
                    options.canClose = true;
                    this.eventBus.publish(Constants.events.NOTIFICATION, 'Updated successfully', options);
                }.bind(this)
            })
        });
    }

    function create (name, user) {
        var model = new ReviewGroupModel();
        model.setName(name);

        var users = model.getUsers();
        setUsers.call(this, users, user, model);

        model.save({}, {
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

    function remove (model) {
        model.destroy({
            wait: true,
            success: function () {
                getData.call(this);
                var options = NotificationRegion.NOTIFICATION_TYPES.success;
                options.canDismiss = true;
                options.canClose = true;
                this.eventBus.publish(Constants.events.NOTIFICATION, 'Deleted successfully', options);
            }.bind(this),
            error: function () {
                var options = NotificationRegion.NOTIFICATION_TYPES.error;
                options.canDismiss = true;
                options.canClose = true;
                this.eventBus.publish(Constants.events.NOTIFICATION, 'Group was not deleted', options);
            }.bind(this)
        });
    }

    function createTable () {
        this.table = new Table({
            plugins: [
                new Selection({
                    selectableRows: true
                }),
                new IdentifiableTable({
                    attribute: 'id',
                    identifier: 'TMS_Admin_ReviewGroupTable'
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

        this.table.attachTo(this.view.getGroupTableHolder());

        this.tableHelper = new TableHelper({
            eventBus: this.eventBus,
            collection: this.collection,
            parent: this.view.getGroupTableHolder(),
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
                this.selected = data;
                this.view.getGroupInput().setValue(data.name);
                this.userTable.setData(data.users);
                hideCreateButton.call(this);
            } else {
                this.selected = null;
                this.userTable.setData([]);
                showCreateButton.call(this);
                this.clear();
            }
        }.bind(this));
    }

    function createUserTable () {
        this.userTable = new Table({
            plugins: [
                new IdentifiableTable({
                    attribute: 'id',
                    identifier: 'TMS_Admin_ReviewGroupUserTable'
                }),
                new IdentifiableRows({
                    rowIdentifier: 'userName'
                })
            ],
            modifiers: [
                {name: 'striped'}
            ],
            tooltips: true,
            columns: this.userColumns
        });

        this.userTable.attachTo(this.view.getUserTableHolder());
    }

    function createAutoComplete () {
        this.userCompletionCollection = new CompletionsCollection();
        this.userCompletionCollection.setResource('users');

        this.userSelectBox = new AutocompleteInput({
            placeholder: 'Enter User ID',
            completions: this.completions,
            refresh: this.onUserCompletion.bind(this),
            userObject: this.updateSelectedUser.bind(this)
        });

        this.userSelectBox.attachTo(this.view.getInputHolder());
    }

    function hideCreateButton () {
        this.view.getEditButton().setModifier('show');
        this.view.getCreateButton().setModifier('hide');
    }

    function showCreateButton () {
        this.view.getEditButton().removeModifier('show');
        this.view.getCreateButton().removeModifier('hide');
    }

    function setUsers (users, user, model) {
        if (users === undefined && !_.isEmpty(user)) {
            model.setUsers([user]);
        } else if (!_.isEmpty(user)) {
            model.getUsers().push(user);
        }
    }
});
