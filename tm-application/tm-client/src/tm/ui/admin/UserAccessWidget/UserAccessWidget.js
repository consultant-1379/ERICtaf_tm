define([
    'jscore/core',
    'jscore/ext/net',
    'jscore/ext/utils/base/underscore',
    './UserAccessWidgetView',
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
    '../models/UserProfileCollection',
    '../../../common/AutocompleteInput/AutocompleteInput',
    '../../../common/models/completion/CompletionsCollection',
    '../../../common/table/plugins/IdentifiableTable/IdentifiableTable',
    '../../../common/table/plugins/IdentifiableRows/IdentifiableRows',
    'widgets/Dialog'
], function (core, net, _, View, Constants, Navigation, TableHelper, Table, Selection, SortableHeader, SecondHeader,
             CellFactory, StringFilterCell, ModelHelper, NotificationRegion, UserProfileCollection, AutocompleteInput,
             CompletionsCollection, IdentifiableTable, IdentifiableRows, Dialog) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true */

        View: View,

        init: function (options) {
            this.eventBus = options.eventBus;
            this.collection = new UserProfileCollection();
        },

        onViewReady: function () {
            this.view.afterRender();
            this.columns = [
                {
                    title: 'Signum',
                    attribute: 'userId',
                    cellType: CellFactory.object(),
                    secondHeaderCellType: StringFilterCell,
                    sortable: true,
                    resizable: true
                },
                {
                    title: 'Name',
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
                            action: this.onRemoveCellAction.bind(this)
                        }]
                    }),
                    width: '50px'
                }

            ];

            createTable.call(this);

            this.collection.addEventHandler('reset', function () {
                this.clear();
            }.bind(this));

            this.view.getAddButton().addEventHandler('click', function () {
                var data = this.userSelectBox.getValueObj();
                if (_.isEmpty(data)) {
                    var options = NotificationRegion.NOTIFICATION_TYPES.error;
                    options.canDismiss = true;
                    options.canClose = true;
                    this.eventBus.publish(Constants.events.NOTIFICATION, 'Please enter a valid name', options);
                    return;
                }
                if (this.collection.findWhere({userId: data.userId})) {
                    var notificationConfig = NotificationRegion.NOTIFICATION_TYPES.warning;
                    notificationConfig.canDismiss = true;
                    notificationConfig.canClose = true;
                    this.eventBus.publish(Constants.events.NOTIFICATION, 'User is already an Administrator',
                        notificationConfig);
                    return;
                }

                update.call(this, data.userId, true);
                this.clear();
            }.bind(this));

            createAutoComplete.call(this);
            getData.call(this);

        },

        clear: function () {
            this.userSelectBox.setValue('');
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
            this.deleteUserAccess(model);
            this.clear();
        },

        deleteUserAccess: function (model) {
            var dialog = new Dialog({
                header: 'Remove User Admin Access',
                type: 'warning',
                content: 'Are you sure you want to remove admin access for ' + model.userId + '?',
                buttons: [{
                    caption: 'Delete',
                    color: 'red',
                    action: function () {
                        update.call(this, model.userId, false);
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
        this.collection.setAdministrator(true);
        this.collection.fetch({
            reset: true
        });
    }

    function update (userId, permissions) {
        net.ajax({
            url: '/tm-server/api/users/' + userId + '/administrator/' + permissions,
            dataType: 'text',
            type: 'PUT',
            success: function () {
                getData.call(this);
                var options = NotificationRegion.NOTIFICATION_TYPES.success;
                options.canDismiss = true;
                options.canClose = true;
                this.eventBus.publish(Constants.events.NOTIFICATION, 'Updated successfully', options);
            }.bind(this),
            error: function (errorMessage) {
                var options = NotificationRegion.NOTIFICATION_TYPES.error;
                options.canDismiss = true;
                options.canClose = true;
                this.eventBus.publish(Constants.events.NOTIFICATION, errorMessage, options);
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
                    identifier: 'TMS_Admin_UserAccessTable'
                }),
                new IdentifiableRows({
                    rowIdentifier: 'userId'
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
                this.userSelectBox.setValue(data.name);
            } else {
                this.clear();
            }
        }.bind(this));
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
});
