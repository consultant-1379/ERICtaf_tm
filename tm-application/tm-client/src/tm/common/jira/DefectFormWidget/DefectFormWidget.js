define([
    'jscore/core',
    'jscore/ext/net',
    'jscore/ext/utils/base/underscore',
    'jscore/ext/binding',
    './DefectFormWidgetView',
    '../../models/jira/LabelsCollection',
    '../../ReferenceHelper',
    'widgets/Combobox',
    '../../ModelHelper',
    '../../widgets/multiSelect/MultiSelect',
    '../../../ext/stringUtils',
    '../../models/jira/JiraDefectModel',
    '../../Constants',
    '../../notifications/NotificationRegion/NotificationRegion',
    './DefectHolderWidget/DefectHolderWidget',
    '../../textWidget/TextWidget',
    'container/api',
    'widgets/SelectBox'
], function (core, net, _, binding, View, LabelsCollection, ReferenceHelper, Combobox,
             ModelHelper, MultiSelect, stringUtils, JiraDefectModel, Constants, Notifications,
             ComboFormWidget, InputWidget, containerApi, SelectBox) {
    /*jshint validthis:true*/
    'use strict';

    var CreateDefectWidget = core.Widget.extend({

        View: View,
        COMPONENT: 'components',
        FIX_VERSIONS: 'fixVersions',
        HIGHLIGHT: 'highlight',

        init: function (options) {
            this.eventBus = options.eventBus;
            this.projectId = options.projectId || '';
            this.userProfile = options.userProfile;
            this.metadataCollection = options.metadataCollection;
            this.references = options.references;
            this.comboBoxes = {};
            this.multiSelects = {};
            this.requiredTextFields = {};
            this.requiredFields = [];
            this.textFields = ['summary', 'environment'];
            this.foreignProjectId = '';
        },

        onViewReady: function () {
            this.view.afterRender();
            createInputComponents.call(this);
            this.view.setReporter(this.userProfile.getUserName());

            this.view.getCreateButton().addEventHandler('click', function () {
                save.call(this);
            }.bind(this));

            this.genericReferenceHelper = new ReferenceHelper({
                references: this.references
            });

            this.selectBox = new SelectBox();
            this.selectBox.setItems(this.genericReferenceHelper.getReferenceItems('project'));
            this.selectBox.addEventHandler('change', function () {
                this.eventBus.publish(Constants.events.UPDATE_USER_PROFILE_WITH_PROJECT,
                    this.selectBox.getValue());

                this.eventBus.publish(Constants.events.PROJECT_CHANGED);
            }.bind(this));
            this.selectBox.attachTo(this.view.getProjectHolder());

        },

        setProjectId: function (projectId) {
            this.projectId = projectId;
        },

        setProjectSelect: function (value) {
            this.selectBox.setValue(value);
        },

        validateFields: function () {
            var summary = this.view.getSummaryInput().getValue();
            var result = true;
            clearHighlights.call(this);

            if (summary.length < 1) {
                this.view.getSummaryInput().setModifier(this.HIGHLIGHT);
                result = false;
            }

            this.requiredFields.forEach(function (required) {
                if (this.multiSelects[required]) {
                    var values = this.multiSelects[required].getSelectedItems();
                    if (values.length < 1) {
                        this.multiSelects[required].setModifier(this.HIGHLIGHT);
                        result = false;
                    }

                    var itemIds = extractItemIds.call(this, values);
                    if (itemIds == null) {
                        this.multiSelects[required].setModifier(this.HIGHLIGHT);
                        result = false;
                    }

                } else if (this.comboBoxes[required]) {
                    var comboValue = this.comboBoxes[required].getValue();
                    if (comboValue.value.length < 1) {
                        this.comboBoxes[required].setModifier(this.HIGHLIGHT);
                        result = false;
                    }

                } else if (this.requiredTextFields[required]) {
                    var textValue = this.requiredTextFields[required].getValue();
                    if (textValue.length < 1) {
                        this.requiredTextFields[required].highlightTextField();
                        result = false;
                    }
                }
            }.bind(this));

            return result;

        },

        saveDefectModel: function () {
            var model = getPopulatedModel.call(this);

            model.save({}, {
                statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                    201: function (response) {
                        clearFormFields.call(this);
                        var defectId = response.key;
                        this.eventBus.publish(Constants.events.JIRA_DEFECT_CREATED, defectId);
                        uploadAttachments.call(this, defectId);
                        sendNotification.call(this, 'success', 'Bug ' + defectId + ' was created successfully');
                    }.bind(this),

                    400: function () {
                        sendNotification.call(this, 'error', 'Bad Request. Please ensure you enter values for all mandatory fields');
                    }.bind(this),

                    401: function () {
                        sendNotification.call(this, 'error', 'Bad Request. Unauthorised Access');
                    }.bind(this),

                    403: function () {
                        sendNotification.call(this, 'error', 'You do not have the required permissions to create an issue for this project');
                    }.bind(this),

                    500: function () {
                        sendNotification.call(this, 'error', 'A server error has occurred. Bug was not created');
                    }.bind(this)
                })
            });
        }

    });

    return CreateDefectWidget;

    /*************** PRIVATE FUNCTIONS ******************/

    function sendNotification (type, message) {
        var options = Notifications.NOTIFICATION_TYPES[type];
        options.canDismiss = true;
        options.canClose = true;
        this.eventBus.publish(Constants.events.NOTIFICATION, message, options);
    }

    function createInputComponents () {
        var referenceIds = getReferenceIds.call(this);

        this.referenceHelper = new ReferenceHelper({
            references: this.metadataCollection
        });

        referenceIds.forEach(function (id) {
            var ref = this.metadataCollection.getById(id);
            var type = ref.getType();
            var fieldName = ref.getFieldName();
            var itemsSize = ref.getItems().length;
            var label = ref.getLabel();
            var required = ref.getRequired();
            if (id === 'project') {
                this.foreignProjectId = getForeignProjectId.call(this, id);
            } else if (type === 'string' && itemsSize === 0) {
                createTextField.call(this, id, fieldName, label);
            } else if (type === 'array') {
                createMultiSelect.call(this, id, fieldName, label);
            } else {
                createCombobox.call(this, id, fieldName, label);
            }

            if (required) {
                this.requiredFields.push(fieldName);
            }
        }, this);

        createLabelsMultiSelect.call(this);
    }

    function getReferenceIds () {
        var ids = [];
        this.metadataCollection.each(function (reference) {
            ids.push(reference.getId());
        });
        return ids;
    }

    function createTextField (id, fieldName, label) {
        var inputForm = new InputWidget({id: id, label: label});
        inputForm.attachTo(this.view.getRequiredTextHolder());
        this.requiredTextFields[fieldName] = inputForm;
    }

    function createCombobox (id, fieldName, label) {
        var placeholder = 'Type for a list of options';
        var combo = new Combobox({
            placeholder: placeholder,
            items: this.referenceHelper.getReferenceItems(id),
            autoComplete: {
                caseInsensitive: true
            }
        });
        var comboForm = new ComboFormWidget({title: id, widget: combo, label: label});
        comboForm.attachTo(this.view.getComboHolder());

        this.comboBoxes[fieldName] = combo;
    }

    function createMultiSelect (id, fieldName, label) {
        var multiSelect = new MultiSelect({
            items: this.referenceHelper.getReferenceItems(id),
            modifiers: [
                {name: 'width', value: 'fullBlock'}
            ]
        });
        multiSelect.addEventHandler(MultiSelect.SEARCH_CHANGED, function () {
            this.defectIdMultiSelect.prepareComponentList();
        }, this);

        var comboForm = new ComboFormWidget({title: id, widget: multiSelect, label: label});
        comboForm.attachTo(this.view.getMultiHolder());

        this.multiSelects[fieldName] = multiSelect;
    }

    function createLabelsMultiSelect () {
        var collection = new LabelsCollection();
        this.labelsMultiSelect = new MultiSelect({
            items: collection,
            modifiers: [
                {name: 'width', value: 'fullBlock'}
            ]
        });
        this.labelsMultiSelect.attachTo(this.view.getBlockByReferenceId('labels-input'));
        this.labelsMultiSelect.addEventHandler(MultiSelect.SEARCH_CHANGED, function (searchValue) {
            searchValue = stringUtils.trim(searchValue);
            collection.setSearchValue(searchValue);
            collection.fetch({reset: true});
            this.labelsMultiSelect.prepareComponentList();
        }, this);

    }

    function extractItemIds (selectedItems) {
        var itemIds = [];
        _.each(selectedItems, function (selectedItem) {
            if (selectedItem.itemObj == null) {
                itemIds = null;
                return;
            }
            var selectedItemId = selectedItem.itemObj.id;
            itemIds.push({
                id: selectedItemId
            });
        });
        return itemIds;
    }

    function extractLabels (selectedLabels) {
        var labels = [];
        _.each(selectedLabels, function (selectedLabel) {
            labels.push(selectedLabel.name);
        });
        return labels;
    }

    function uploadAttachments (defectId) {
        var data = new FormData();
        var attachments = this.view.getAttachmentInput().getProperty('files');

        if (attachments.length > 0) {
            for (var i = 0; i < attachments.length; i++) {
                data.append('file', attachments[i]);
            }
            net.ajax({
                url: '/tm-server/api/jira/attachments/' + defectId,
                type: 'POST',
                dataType: 'json',
                contentType: false,
                processData: false,
                data: data,

                error: function () {
                    sendNotification.call(this, 'error', 'Attachments for ' + defectId + ' could not be saved');
                }.bind(this)

            });
        }
    }

    function getPopulatedModel () {
        var fields = {};

        _.each(this.textFields, function (key) {
            var text = this.view.getTextFieldValue(key);
            fields[key] = text;
        }, this);

        _.each(this.multiSelects, function (multiSelect, key) {
            var selectedItems = multiSelect.getSelectedItems();
            var itemIds = extractItemIds.call(this, selectedItems);
            fields[key] = itemIds;
        }, this);

        _.each(this.requiredTextFields, function (requiredFields, key) {
            var value = requiredFields.getValue();
            fields[key] = value;
        }, this);

        _.each(this.comboBoxes, function (combo, key) {
            var value = combo.getValue();
            if (value.itemObj && value.itemObj.id) {
                var selectedItemId = combo.getValue().itemObj.id;
                fields[key] = {
                    id: selectedItemId
                };
            }
        });

        var labels = extractLabels(this.labelsMultiSelect.getSelectedItems());

        fields.labels = labels;

        fields.project = {
            id: this.foreignProjectId
        };

        // (id = '1') will create a bug type in Requirment management system
        fields.issuetype = {
            id: '1'
        };
        fields.reporter = {
            name: this.userProfile.getUserId()
        };

        var defectModel = new JiraDefectModel();

        defectModel.setFields(fields);

        return defectModel;
    }

    function clearFormFields () {
        _.each(this.textFields, function (key) {
            this.view.setTextFieldValue(key, '');
        }, this);

        _.each(this.multiSelects, function (multiSelect) {
            multiSelect.setSelectedItems([]);
        });

        _.each(this.comboBoxes, function (comboBox) {
            comboBox.setValue({
                name: '',
                title: '',
                value: ''
            });
        });

        this.labelsMultiSelect.setSelectedItems([]);
    }

    function getForeignProjectId (id) {
        var project = this.referenceHelper.getReferenceItems(id);
        var result = '';
        if (project == null) {
            return;
        }
        project.forEach(function (items) {
            if (items.value === 'id') {
                result = items.title;
            }
        });

        return result;
    }

    function clearHighlights () {
        this.view.getSummaryInput().removeModifier('highlight');

        this.requiredFields.forEach(function (required) {
            if (this.multiSelects[required]) {
                this.multiSelects[required].removeModifier(this.HIGHLIGHT);

            } else if (this.comboBoxes[required]) {
                this.comboBoxes[required].removeModifier(this.HIGHLIGHT);

            } else if (this.requiredTextFields[required]) {
                this.requiredTextFields[required].removeHighlightTextField();
            }
        }.bind(this));
    }

    function save () {
        if (this.validateFields()) {
            this.saveDefectModel();
            containerApi.getEventBus().publish('flyout:hide');
        }
    }

});
