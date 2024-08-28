define([
    'jscore/core',
    'jscore/ext/binding',
    'jscore/ext/utils/base/underscore',
    './TestCaseFormWidgetView',
    'widgets/SelectBox',
    'widgets/MultiSelectBox',
    '../../../../common/widgets/multiSelect/MultiSelect',
    '../../../../common/ReferenceHelper',
    '../../../../common/ContextFilter',
    '../../../../common/Constants',
    '../../models/requirements/RequirementIdsCollection',
    '../../../../ext/stringUtils',
    'widgets/Switcher',
    'widgets/ExpandableList'
], function (core, binding, _, View, SelectBox, MultiSelectBox, MultiSelect, ReferenceHelper, ContextFilter, Constants,
             RequirementIdsCollection, stringUtils, Switcher, ExpandableList) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true*/

        View: View,

        DEFAULT_TEST_CASE_STATUS_OBJECT: 'Preliminary',

        DEFAULT_TEST_CASE_PRIORITY_OBJECT: {
            name: 'Minor',
            title: 'Minor',
            value: '3',
            itemObj: {
                id: '3',
                title: 'Minor'
            }
        },

        SHOW_AUTOMATION_CANDIDATE_CHECKBOX: 'Manual',

        AUTOMATION_CANDIDATE_YES_OPTION: {
            id: '1',
            title: 'Yes'
        },

        AUTOMATION_CANDIDATE_NO_OPTION: {
            id: '2',
            title: 'No'
        },

        init: function (options) {
            this.currentModel = options.model;
            this.references = options.references;
            this.events = options.events;
            this.parentRegion = options.region;
        },

        onViewReady: function () {
            this.view.afterRender();

            this.referenceHelper = new ReferenceHelper({
                references: this.references
            });

            bindModel.call(this);
            createSelectBoxes.call(this);
            createAcSwitcher.call(this);
            createIntrusiveSwitcher.call(this);
        },

        updateFields: function (screenId) {
            updateUnboundParams.call(this, screenId);
        },

        updateModelData: function () {
            var model = this.currentModel,
                requirementMultiSelect = this.requirementIdMultiSelect;

            if (model.getTestCaseId()) {
                model.setTestCaseId(model.getTestCaseId().trim());
            }

            // apply model with Technical Components
            var selectedComponents = _.map(this.componentSelect.getSelectedItems(), function (componentItem) {
                return ReferenceHelper.getItemObj(componentItem);
            });
            model.setTechnicalComponents(selectedComponents);
            // apply model with Groups
            model.setGroup(ReferenceHelper.getItemsObjects(this.groupMultiSelect.getSelectedItems()));
            model.setFeature(ReferenceHelper.getItemObj(this.featureSelect.getValue()));

            // apply model with Requirements
            requirementMultiSelect.onTypedItemResolve();
            model.setRequirementIds(ReferenceHelper.getArrayFromMultiSelect(requirementMultiSelect.getSelectedItems()));
        },

        updateReferencesData: function () {
            removeSelectBoxes.call(this);
            createSelectBoxes.call(this);
        },

        onProductSelectChange: function () {
            _.defer(function () {
                var product = this.productSelect.getValue();
                this.parentRegion.refreshReferences(product);
                this.featureSelect.setValue('');
            }.bind(this));
        },

        onFeatureSelectChange: function () {
            _.defer(function () {
                var product = this.productSelect.getValue();
                var feature = this.featureSelect.getValue();
                this.parentRegion.refreshReferences(product, feature);
                this.componentSelect.setValue([]);
            }.bind(this));
        },

        onRequirementIdMultiSelectChange: function () {
            _.defer(function () {
                this.currentModel.setRequirementIds(
                    ReferenceHelper.getArrayFromMultiSelect(this.requirementIdMultiSelect.getSelectedItems())
                );
            }.bind(this));
        },

        setProduct: function (product) {
            this.productSelect.setValue(product);
        },

        setModel: function (model) {
            this.currentModel = model;
            bindModel.call(this);
        }

    });

    /*************** PRIVATE FUNCTIONS ******************/

    function updateUnboundParams (screenId) {
        var mainModel = this.currentModel,
            refHelper = this.referenceHelper;
        updateWidgetsFromReferences.call(this);

        // update selectBox
        this.typeSelect.setValue(refHelper.getSelectedReference('type', mainModel.getTypeObj()));
        this.executionTypeSelect.setValue(refHelper.getSelectedReference('executionType',
            mainModel.getExecutionTypeObj()));
        this.testCaseTeamSelect.setValue(refHelper.getSelectedReference('team',
            mainModel.getTeamObj()));
        this.testCaseSuiteSelect.setValue(refHelper.getSelectedReference('suite',
            mainModel.getSuiteObj()));
        toggleAutomationCandidate.call(this);
        initIntrusiveSwitcher.call(this);

        if (mainModel.attributes.testCaseId) {
            if (mainModel.getTestCaseStatusObj()) {
                this.view.getTestCaseStatus().setText(mainModel.getTestCaseStatusObj().title);
            }
            this.prioritySelect.setValue(refHelper.getSelectedReference('priority', mainModel.getPriorityObj()));
        } else {
            this.view.getTestCaseStatus().setText(this.DEFAULT_TEST_CASE_STATUS_OBJECT);
            this.prioritySelect.setValue(this.DEFAULT_TEST_CASE_PRIORITY_OBJECT);
        }

        // TODO: VOV: Make binding for MultiSelectBoxes
        // update multiSelectBox
        this.groupMultiSelect.setValue(refHelper.getSelectedReferences('group', mainModel.getGroup() || []));
        this.contextMultiSelect.setValue(refHelper.getSelectedReferences('context', mainModel.getContext() || []));

        // update multiSelect
        this.requirementIdMultiSelect.setSelectedItems(convertToObjects(mainModel.getRequirementIds()));

        if (screenId !== Constants.pages.TEST_CASE_CREATE) {
            var productName = mainModel.getProduct().externalId;
            var findProductReference = refHelper.findReferenceByName('product', productName);
            this.productSelect.setValue(refHelper.getSelectedReference('product', findProductReference.itemObj));

            this.featureSelect.setValue(
                refHelper.getSelectedReference('feature', convertToObject(mainModel.getFeature())));
            this.componentSelect.setValue(
                refHelper.getSelectedReferences('component', mainModel.getTechnicalComponents()));
        }

        setDefaultModelParams.call(this);
        createGroupList.call(this);
        createContextList.call(this);
        createComponentList.call(this);
    }

    function setDefaultModelParams () {
        this.currentModel.setPriority(ReferenceHelper.getItemObj(this.prioritySelect.getValue()));
    }

    function createAcSwitcher () {
        this.acSwitcher = new Switcher({
            value: false,
            onLabel: 'Yes',
            offLabel: 'No',
            offColor: 'red'
        });
        addAcSwitcherEventHandler.call(this);
        this.acSwitcher.attachTo(this.view.getAcSwitcher());
    }

    function addAcSwitcherEventHandler () {
        this.acSwitcher.addEventHandler('change', function () {
            var isYes = this.acSwitcher.getValue();
            var acOption = isYes ? this.AUTOMATION_CANDIDATE_YES_OPTION : this.AUTOMATION_CANDIDATE_NO_OPTION;
            this.currentModel.setAutomationCandidate(acOption);
        }.bind(this));
    }

    function toggleAutomationCandidate () {
        var executionTypeObj = this.executionTypeSelect.getValue();
        if (executionTypeObj.title === this.SHOW_AUTOMATION_CANDIDATE_CHECKBOX) {
            var automationCandidate = this.currentModel.getAutomationCandidate();
            this.acSwitcher.setValue(_.isEqual(automationCandidate, this.AUTOMATION_CANDIDATE_YES_OPTION));
            this.view.showAutomationCandidateBlock();
        } else {
            this.view.hideAutomationCandidateBlock();
            this.currentModel.setAutomationCandidate(this.AUTOMATION_CANDIDATE_NO_OPTION);
        }
    }

    function createIntrusiveSwitcher () {
        this.intrusiveSwitcher = new Switcher({
            value: false,
            onLabel: 'Yes',
            offLabel: 'No',
            offColor: 'blue'
        });

        addIntrusiveSwitcherEventHandler.call(this);
        this.intrusiveSwitcher.attachTo(this.view.getIntrusiveSwitcher());
    }

    function addIntrusiveSwitcherEventHandler () {
        this.intrusiveSwitcher.addEventHandler('change', function () {
            var intrusive = this.intrusiveSwitcher.getValue();
            this.currentModel.setIntrusive(intrusive);
        }, this);
    }

    function initIntrusiveSwitcher () {
        var intrusive = this.currentModel.isIntrusive();
        if (!_.isUndefined(intrusive)) {
            this.intrusiveSwitcher.setValue(intrusive);
        } else {
            this.intrusiveSwitcher.setValue(false);
        }
        this.intrusiveSwitcher.trigger('change');
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

    function convertToObject (obj) {
        if (!obj) {
            return obj;
        }
        return {id: obj.id.toString(), title: obj.name};
    }

    function bindModel () {
        var mainModel = this.currentModel;

        binding.bindModel(mainModel, 'title', this.view.getTitle(), 'value');
        binding.bindModel(mainModel, 'testCaseId', this.view.getTestCaseId(), 'value');
        binding.bindModel(mainModel, 'description', this.view.getDescription(), 'value');
        binding.bindModel(mainModel, 'precondition', this.view.getPreCondition(), 'value');
        binding.bindModel(mainModel, 'intrusiveComment', this.view.getIntrusiveComment(), 'value');
    }

    function createSelectBoxes () {
        createSingleSelectBoxes.call(this);
        createMultiSelectBoxes.call(this);
    }

    function setModelFromSelectBox (modelAttributeFn, selectBox) {
        _.defer(function () {
            modelAttributeFn.call(this, ReferenceHelper.getItemObj(selectBox.getValue()));
        }.bind(this.currentModel));
    }

    function setModelFromMultiSelectBox (modelAttributeFn, multiSelectBox) {
        _.defer(function () {
            modelAttributeFn.call(this, ReferenceHelper.getItemsObjects(multiSelectBox.getSelectedItems()));
        }.bind(this.currentModel));
    }

    function createSelectBox (attachTo, onChangeFn, selectBoxId, defaultValue) {
        var selectBox = new SelectBox({
            items: [],
            modifiers: [
                {name: 'width', value: 'fullBlock'}
            ]
        });
        if (defaultValue) {
            selectBox.setValue(defaultValue);
        }
        selectBox.attachTo(attachTo);
        selectBox.addEventHandler('change', onChangeFn);
        selectBox.view.getButton().setAttribute('id', selectBoxId);
        return selectBox;
    }

    function createMultiSelectBox (attachTo, modelSetFn, multiSelectBoxId) {
        var multiSelectBox = new MultiSelectBox({
            selectDeselectAll: true,
            items: [],
            modifiers: [
                {name: 'width', value: 'fullBlock'}
            ]
        });
        multiSelectBox.attachTo(attachTo);
        multiSelectBox.addEventHandler('change', function () {
            setModelFromMultiSelectBox.call(this, modelSetFn, multiSelectBox);
        }, this);
        multiSelectBox.view.getButton().setAttribute('id', multiSelectBoxId);
        return multiSelectBox;
    }

    function updateWidgetsFromReferences () {
        this.componentSelect.setItems(this.referenceHelper.getReferenceItems('component'));
        this.productSelect.setItems(this.referenceHelper.getReferenceItems('product'));
        this.featureSelect.setItems(this.referenceHelper.getReferenceItems('feature'));
        this.typeSelect.setItems(this.referenceHelper.getReferenceItems('type'));
        this.executionTypeSelect.setItems(this.referenceHelper.getReferenceItems('executionType'));
        this.prioritySelect.setItems(this.referenceHelper.getReferenceItems('priority'));
        this.contextMultiSelect.setItems(this.referenceHelper.getReferenceItems('context'));
        this.groupMultiSelect.setItems(this.referenceHelper.getReferenceItems('group'));
        this.testCaseSuiteSelect.setItems(this.referenceHelper.getReferenceItems('suite'));
        this.testCaseTeamSelect.setItems(this.referenceHelper.getReferenceItems('team'));
    }

    function removeSelectBoxes () {
        removeSelectBox.call(this, this.componentSelect);
        removeSelectBox.call(this, this.productSelect);
        removeSelectBox.call(this, this.featureSelect);
        removeSelectBox.call(this, this.typeSelect);
        removeSelectBox.call(this, this.executionTypeSelect);
        removeSelectBox.call(this, this.prioritySelect);
        removeSelectBox.call(this, this.contextMultiSelect);
        removeSelectBox.call(this, this.groupMultiSelect);
        removeSelectBox.call(this, this.requirementIdMultiSelect);
        removeSelectBox.call(this, this.testCaseSuiteSelect);
        removeSelectBox.call(this, this.testCaseTeamSelect);
    }

    function removeSelectBox (selectBox) {
        if (selectBox) {
            selectBox.detach();
            selectBox.removeEventHandler('change');
            selectBox.destroy();
        }
    }

    function createGroupList () {
        if (this.groupList) {
            this.groupList.destroy();
        }
        this.groupList = new ExpandableList({
            showAll: true,
            items: this.groupMultiSelect.getSelectedItems()
        });
        this.groupList.attachTo(this.view.getGroupSelect());
    }

    function createComponentList () {
        if (this.componentList) {
            this.componentList.destroy();
        }
        this.componentList = new ExpandableList({
            showAll: true,
            items: this.componentSelect.getSelectedItems()
        });
        this.componentList.attachTo(this.view.getComponentSelect());
    }

    function createContextList () {
        if (this.contextList) {
            this.contextList.destroy();
        }
        this.contextList = new ExpandableList({
            showAll: true,
            items: this.contextMultiSelect.getSelectedItems()
        });
        this.contextList.attachTo(this.view.getContextSelect());
    }

    function createSingleSelectBoxes () {
        this.productSelect = createSelectBox.call(this,
            this.view.getProductSelect(),
            this.onProductSelectChange.bind(this),
            'TMS_CreateTestCase_TestCaseForm-productSelect'
        );

        this.featureSelect = createSelectBox.call(this,
            this.view.getFeatureSelect(),
            this.onFeatureSelectChange.bind(this),
            'TMS_CreateTestCase_TestCaseForm-featureSelect'
        );
        this.typeSelect = createSelectBox.call(this,
            this.view.getTypeSelect(),
            function () {
                setModelFromSelectBox.call(this, this.currentModel.setType, this.typeSelect);
            }.bind(this),
            'TMS_CreateTestCase_TestCaseForm-typeSelect'
        );
        this.executionTypeSelect = createSelectBox.call(this,
            this.view.getExecutionTypeSelect(),
            function () {
                toggleAutomationCandidate.call(this);
                setModelFromSelectBox.call(this, this.currentModel.setExecutionType, this.executionTypeSelect);
            }.bind(this),
            'TMS_CreateTestCase_TestCaseForm-executionTypeSelect'
        );
        this.prioritySelect = createSelectBox.call(this,
            this.view.getPrioritySelect(),
            function () {
                setModelFromSelectBox.call(this, this.currentModel.setPriority, this.prioritySelect);
            }.bind(this),
            'TMS_CreateTestCase_TestCaseForm-prioritySelect',
            this.DEFAULT_TEST_CASE_PRIORITY_OBJECT
        );
        this.testCaseSuiteSelect = createSelectBox.call(this,
            this.view.getSuiteSelect(),
            function () {
                setModelFromSelectBox.call(this, this.currentModel.setSuite, this.testCaseSuiteSelect);
            }.bind(this),
            'TMS_CreateTestCase_TestCaseForm-testCaseSuiteSelect'
        );
        this.testCaseTeamSelect = createSelectBox.call(this,
            this.view.getTeamSelect(),
            function () {
                setModelFromSelectBox.call(this, this.currentModel.setTeam, this.testCaseTeamSelect);
            }.bind(this),
            'TMS_CreateTestCase_TestCaseForm-testCaseTeamSelect'
        );
    }

    function createMultiSelectBoxes () {
        // MultiSelectBoxes
        this.contextMultiSelect = createMultiSelectBox.call(this,
            this.view.getContextSelect(),
            this.currentModel.setContext,
            'TMS_CreateTestCase_TestCaseForm-contextSelect'
        );

        this.contextMultiSelect.addEventHandler('change', function () {
            createContextList.call(this);
        }.bind(this));

        this.groupMultiSelect = createMultiSelectBox.call(this,
            this.view.getGroupSelect(),
            this.currentModel.setGroup,
            'TMS_CreateTestCase_TestCaseForm-groupSelect'
        );

        this.componentSelect = createMultiSelectBox.call(this,
            this.view.getComponentSelect(),
            function () {
                //do nothing
            }.bind(this),
            'TMS_CreateTestCase_TestCaseForm-componentSelect'
        );

        this.componentSelect.addEventHandler('change', function () {
            createComponentList.call(this);
        }.bind(this));

        this.groupMultiSelect.addEventHandler('change', function () {
            createGroupList.call(this);
        }.bind(this));

        // CustomMultiSelectBox
        var collection = new RequirementIdsCollection();
        collection.setTypes(['Story', 'Epic', 'MR', 'Improvement']);
        this.requirementIdMultiSelect = new MultiSelect({
            items: collection,
            modifiers: [
                {name: 'width', value: 'fullBlock'}
            ]
        });
        this.requirementIdMultiSelect.attachTo(this.view.getRequirementIdMultiSelect());
        this.requirementIdMultiSelect.addEventHandler('change', this.onRequirementIdMultiSelectChange, this);
        this.requirementIdMultiSelect.addEventHandler(MultiSelect.SEARCH_CHANGED, function (searchValue) {
            searchValue = stringUtils.trim(searchValue);
            collection.setSearchValue(searchValue);
            collection.fetch({reset: true});
            this.requirementIdMultiSelect.prepareComponentList();
        }, this);
    }
});
