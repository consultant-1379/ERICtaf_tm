/*global define*/
define([
    'jscore/core',
    'jscore/ext/binding',
    './TestExecutionResultWidgetView',
    'widgets/SelectBox',
    '../../../../common/widgets/multiSelect/MultiSelect',
    '../../models/DefectIdsCollection',
    '../../../testCases/models/requirements/RequirementIdsCollection',
    '../../../../ext/stringUtils'
], function (core, binding, View, SelectBox, MultiSelect, DefectIdsCollection, RequirementIdsCollection, stringUtils) {
    /*jshint validthis:true*/
    'use strict';

    var TestExecutionResultWidget = core.Widget.extend({

        View: View,

        onViewReady: function () {
            this.view.afterRender();
            this.currentModel = this.options.model;
            this.references = this.options.references;
            this.isoCollection = this.options.isoCollection;
            this.fileWidget = this.options.fileWidget;

            createSelectBoxes.call(this);
            this.fileWidget.attachTo(this.view.getFilesBlock());

            this.executionTime = this.view.getExecutionTime();
            this.executionTime.addEventHandler('keyup', function () {
                this.currentModel.setExecutionTime(this.executionTime.getValue().trim());
            }.bind(this));

            this.kpiMeasurement = this.view.getKpiMeasurement();
            this.kpiMeasurement.addEventHandler('keyup', function () {
                this.currentModel.setKpiMeasurement(this.kpiMeasurement.getValue().trim());
            }.bind(this));
        },

        onResultSelectChange: function () {
            // TODO: VOV: Should be changed after UI SDK fix
            setTimeout(function () {
                var result = getItemObj.call(this, this.resultSelect.getValue());
                this.currentModel.setExecutionResult(result);
                hideMultiSelectIdBlocks.call(this);
            }.bind(this), 1);
        },

        updateReferencesData: function () {
            removeSelectBoxes.call(this);
            createSelectBoxes.call(this);
        },

        updateFields: function () {
            populateFields.call(this);
        },

        defectIdMultiSelectChange: function () {
            setTimeout(function () {
                this.currentModel.setDefectIds(
                    getArrayFromMultiSelect.call(this, this.defectIdMultiSelect.getSelectedItems()));
            }.bind(this), 1);
        },

        requirementIdMultiSelectChange: function () {
            setTimeout(function () {
                this.currentModel.setRequirementIds(
                    getArrayFromMultiSelect.call(this, this.requirementIdMultiSelect.getSelectedItems()));
            }.bind(this), 1);
        },

        populateFields: function (model) {
            this.view.getComment().setValue(model.getComment());
            this.resultSelect.setValue(createResultObj(model.getExecutionResultTitle()));
            this.view.getExecutionTime().setValue(model.getExecutionTime());
            this.view.getKpiMeasurement().setValue(model.getKpiMeasurement());
            this.defectIdMultiSelect.setSelectedItems(convertToObjects(model.getDefectIds()));
            this.requirementIdMultiSelect.setSelectedItems(convertToObjects(model.getRequirementIds()));
            this.currentModel.setExecutionResult(model.getExecutionResultObj());

            hideMultiSelectIdBlocks.call(this);
        },

        clear: function () {
            this.resultSelect.setValue(TestExecutionResultWidget.RESULT_DEFAULT_VALUE);
            this.view.getComment().setValue('');
            this.view.getExecutionTime().setValue('');
            this.view.getKpiMeasurement().setValue('');
            this.defectIdMultiSelect.setSelectedItems(convertToObjects([]));
            this.requirementIdMultiSelect.setSelectedItems(convertToObjects([]));
        },

        updateModelData: function () {
            this.defectIdMultiSelect.onTypedItemResolve();
            this.requirementIdMultiSelect.onTypedItemResolve();

            var passId = getExecutionResultId.call(this, 'Pass');

            if (this.currentModel.getExecutionResultId() === passId) {
                this.currentModel.setDefectIds([]);
                this.currentModel.setRequirementIds([]);
            } else {
                this.currentModel.setDefectIds(
                    getArrayFromMultiSelect.call(this, this.defectIdMultiSelect.getSelectedItems()));

                this.currentModel.setRequirementIds(
                    getArrayFromMultiSelect.call(this, this.requirementIdMultiSelect.getSelectedItems()));
            }
            this.currentModel.setComment(this.view.getComment().getValue());
        },

        addDefectIdToMultiSelect: function (defectId) {
            var defectIds = this.defectIdMultiSelect.getSelectedItems();
            defectIds.push({name: defectId, value: defectId});
            this.defectIdMultiSelect.setSelectedItems(defectIds);
            this.defectIdMultiSelectChange();
        },

        updateIsoSelect: function (isoList) {
            this.isoSelect.setValue({});
            this.isoSelect.setItems(isoList);
        },

        showIsoSelect: function () {
            this.view.showIsoBlock();
        },

        hideIsoSelect: function () {
            this.view.hideIsoBlock();
        }

    }, {
        RESULT_DEFAULT_VALUE: {name: 'Select result...', title: 'Select result...'}
    });

    return TestExecutionResultWidget;

    /*************** PRIVATE FUNCTIONS ******************/

    function getItemObj (valueObj) {
        if (valueObj) {
            return valueObj.itemObj;
        }
        return undefined;
    }

    function createResultObj (value) {
        return {name: value, title: value};
    }

    function populateFields () {
        var mainModel = this.currentModel;
        hideMultiSelectIdBlocks.call(this);

        // TODO: VOV: Make binding for SelectBoxes
        // update selectBox
        var resultValue = getSelectedReference.call(this,
            this.references.getById('result'),
            mainModel.getExecutionResultObj(),
            TestExecutionResultWidget.RESULT_DEFAULT_VALUE
        );
        this.resultSelect.setValue(resultValue);

        this.defectIdMultiSelect.setSelectedItems(convertToObjects(mainModel.getDefectIds()));
        this.requirementIdMultiSelect.setSelectedItems(convertToObjects(mainModel.getRequirementIds()));
        this.view.getComment().setValue(mainModel.getComment());
        this.view.getExecutionTime().setValue(mainModel.getExecutionTime());
    }

    function getExecutionResultId (title) {
        if (this.references.getById('executionResult')) {
            var executionResultTypes = this.references.getById('executionResult').getItems();

            for (var i = 0; i < executionResultTypes.length; i++) {
                if (executionResultTypes[i].title === title) {
                    return executionResultTypes[i].value;
                }
            }
        }
    }

    function hideMultiSelectIdBlocks () {
        var result = this.currentModel.getExecutionResultObj();
        var passId = getExecutionResultId.call(this, 'Pass');
        if (result && result.id === passId) {
            this.view.getDefectIdsBlock().setModifier('hide', undefined, 'eaTM-TestExecutionResult-defectIdBlock');
            this.view.getRequirementsIdsBlock().setModifier('hide', undefined, 'eaTM-TestExecutionResult-requirementIdBlock');
        } else {
            this.view.getDefectIdsBlock().removeModifier('hide', 'eaTM-TestExecutionResult-defectIdBlock');
            this.view.getRequirementsIdsBlock().removeModifier('hide', 'eaTM-TestExecutionResult-requirementIdBlock');
        }
    }

    function getSelectedReference (references, refObj, defaultValue) {
        var selectedRef = defaultValue;
        if (!references || !refObj) {
            return selectedRef;
        }

        references.getItems().forEach(function (referenceObj) {
            if (referenceObj.value === refObj.id) {
                selectedRef = referenceObj;
            }
        });
        return selectedRef;
    }

    function createSelectBoxes () {
        createResultSelect.call(this);
        createDefectIdSelect.call(this);
        createRequirementIdSelect.call(this);
        createIsoSelect.call(this);
    }

    function createResultSelect () {
        var statusReference = this.references.getById('executionResult') || {
                getItems: function () {
                }
            };

        this.resultSelect = new SelectBox({
            items: statusReference.getItems(),
            modifiers: [
                {name: 'width', value: 'fullBlock'}
            ]
        });
        this.resultSelect.setValue(TestExecutionResultWidget.RESULT_DEFAULT_VALUE);
        this.resultSelect.attachTo(this.view.getResultSelect());
        this.resultSelect.addEventHandler('change', this.onResultSelectChange, this);
        this.resultSelect.view.getButton().setAttribute('id', 'TMS_TestExecution_TestExecutionResult-resultSelect');
    }

    function createDefectIdSelect () {
        var collection = new DefectIdsCollection();
        this.defectIdMultiSelect = new MultiSelect({
            items: collection,
            modifiers: [
                {name: 'width', value: 'fullBlock'}
            ]
        });
        this.defectIdMultiSelect.attachTo(this.view.getDefectIdMultiSelect());
        this.defectIdMultiSelect.addEventHandler('change', this.defectIdMultiSelectChange, this);
        this.defectIdMultiSelect.addEventHandler(MultiSelect.SEARCH_CHANGED, function (searchValue) {
            searchValue = stringUtils.trim(searchValue);
            collection.setSearchValue(searchValue);
            collection.fetch({reset: true});
            this.defectIdMultiSelect.prepareComponentList();
        }, this);
    }

    function createRequirementIdSelect () {
        var requirementCollection = new RequirementIdsCollection();
        requirementCollection.setTypes(['Story', 'Improvement']);
        this.requirementIdMultiSelect = new MultiSelect({
            items: requirementCollection,
            modifiers: [
                {name: 'width', value: 'fullBlock'}
            ]
        });
        this.requirementIdMultiSelect.attachTo(this.view.getRequirementIdMultiSelect());
        this.requirementIdMultiSelect.addEventHandler('change', this.requirementIdMultiSelectChange, this);
        this.requirementIdMultiSelect.addEventHandler(MultiSelect.SEARCH_CHANGED, function (searchValue) {
            searchValue = stringUtils.trim(searchValue);
            requirementCollection.setSearchValue(searchValue);
            requirementCollection.fetch({reset: true});
            this.requirementIdMultiSelect.prepareComponentList();
        }, this);
    }

    function createIsoSelect () {
        this.isoSelect = new SelectBox({
            items: this.isoCollection.toJSON(),
            modifiers: [
                {name: 'width', value: 'fullBlock'}
            ]
        });
        this.isoSelect.addEventHandler('change', function () {
            var iso = this.isoSelect.getValue().itemObj;
            this.currentModel.setIso(iso);
        }, this);
        this.isoSelect.attachTo(this.view.getIsoSelect());
    }

    function removeSelectBoxes () {
        removeSelectBox.call(this, this.resultSelect);
        removeSelectBox.call(this, this.defectIdMultiSelect);
        removeSelectBox.call(this, this.requirementIdMultiSelect);
        removeSelectBox.call(this, this.isoSelect);
    }

    function removeSelectBox (selectBox) {
        if (selectBox) {
            selectBox.detach();
            selectBox.removeEventHandler('change');
            selectBox.destroy();
        }
    }

    function convertToObjects (ids) {
        var objects = [];
        if (!ids) {
            return objects;
        }
        ids.forEach(function (id) {
            objects.push({name: id, value: id});
        });
        return objects;
    }

    function getArrayFromMultiSelect (objectsArray) {
        var values = [];
        if (objectsArray && objectsArray.length > 0) {
            objectsArray.forEach(function (obj) {
                values.push(obj.value);
            });
        }
        return values;
    }

});
