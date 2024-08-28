define([
    'jscore/core',
    'jscore/ext/utils/base/underscore',
    'widgets/SelectBox',
    './CriterionWidgetView',
    '../../../../../../common/widgets/actionIcon/ActionIcon',
    '../../../../../../ext/stringUtils',
    '../../../../../../common/Constants',
    'widgets/Button'
], function (core, _, SelectBox, View, ActionIcon, stringUtils, Constants, Button) {
    'use strict';

    var Criterion = core.Widget.extend({
        /*jshint validthis:true */

        View: View,

        DEFAULT_FIELD_OBJECT: {name: 'Field name', value: ''},
        DEFAULT_CONDITION_OBJECT: {name: 'Condition', value: ''},
        DEFAULT_TYPE_AND: '&',
        DEFAULT_TYPE_OR: '|',

        init: function (options) {
            this.criterion = {
                field: '',
                condition: '',
                value: '',
                type: this.DEFAULT_TYPE_AND
            };
            this.itemIndex = options.index;
            this.fields = _.sortBy(getFields(options.screenId), 'name');
            this.conditions = getConditions();
        },

        onViewReady: function () {
            this.view.afterRender();

            this.fieldSelectBox = new SelectBox({
                items: this.fields,
                value: this.DEFAULT_FIELD_OBJECT,
                modifiers: [
                    {name: 'width', value: 'small'}
                ]
            });
            this.fieldSelectBox.attachTo(this.view.getField());
            this.fieldSelectBox.view.getRoot().setAttribute('id',
                'TMS_TestCaseSearch_Criterion-fieldSelectBox-' + this.itemIndex);

            this.conditionSelectBox = new SelectBox({
                items: this.conditions,
                value: this.DEFAULT_CONDITION_OBJECT,
                modifiers: [
                    {name: 'width', value: 'small'}
                ]
            });
            this.conditionSelectBox.attachTo(this.view.getCondition());
            this.conditionSelectBox.view.getRoot().setAttribute('id',
                'TMS_TestCaseSearch_Criterion-conditionSelectBox-' + this.itemIndex);

            this.actionIcon = new ActionIcon({
                iconKey: 'close',
                interactive: true
            });
            this.actionIcon.attachTo(this.view.getActions());

            this.buttonWidget = new Button({
                caption: this.criterion.type
            });

            this.buttonWidget.attachTo(this.view.getTypeHolder());

            if (this.options.data !== undefined) {
                var data = this.options.data,
                    fieldValue = _.findWhere(this.fields, {value: data.field}),
                    conditionValue = _.findWhere(this.conditions, {value: data.operator}),
                    value = data.value;
                if (fieldValue !== undefined) {
                    this.fieldSelectBox.setValue(fieldValue);
                    this.conditionSelectBox.setValue(conditionValue);
                    this.view.getValueInput().setValue(value);
                    if (data.type.length === 0) {
                        data.type = this.DEFAULT_TYPE_AND;
                    }
                    this.buttonWidget.setCaption(data.type);

                    this.criterion.field = data.field;
                    this.criterion.condition = data.operator;
                    this.criterion.value = data.value;
                    this.criterion.type = data.type;
                }
            }

            this.actionIcon.addEventHandler('click', this.onClearCondition, this);
            this.fieldSelectBox.addEventHandler('change', this.onFieldSelectBoxChange, this);
            this.conditionSelectBox.addEventHandler('change', this.onConditionSelectBoxChange, this);
            this.view.getValueInput().addEventHandler('input', this.onValueInputChange, this);
            this.buttonWidget.addEventHandler('click', this.onButtonClick, this);
        },

        setIndex: function (index) {
            this.itemIndex = index;
        },

        onClearCondition: function () {
            this.trigger(Criterion.CLEAR_EVENT, this.itemIndex);
        },

        onFieldSelectBoxChange: function () {
            this.criterion.field = this.fieldSelectBox.getValue().value;
        },

        onConditionSelectBoxChange: function () {
            this.criterion.condition = this.conditionSelectBox.getValue().value;
        },

        onValueInputChange: function () {
            this.criterion.value = stringUtils.trim(this.view.getValueInput().getValue());
        },

        onButtonClick: function () {
            if (this.criterion.type === this.DEFAULT_TYPE_AND) {
                this.buttonWidget.setCaption(this.DEFAULT_TYPE_OR);
                this.criterion.type = this.DEFAULT_TYPE_OR;
            } else {
                this.buttonWidget.setCaption(this.DEFAULT_TYPE_AND);
                this.criterion.type = this.DEFAULT_TYPE_AND;
            }
        },

        getCriterionUrl: function () {
            return this.criterion.type + this.criterion.field + this.criterion.condition + this.criterion.value;
        },

        isValidField: function (value) {
            var fieldValue = _.findWhere(this.fields, {value: value});
            return fieldValue !== undefined;
        },

        markInvalid: function () {
            this.view.getField().setModifier('invalid', this.criterion.field === '' ? 'true' : 'false');
            this.view.getCondition().setModifier('invalid', this.criterion.condition === '' ? 'true' : 'false');
            this.view.getValueHolder().setModifier('invalid', this.criterion.value === '' ? 'true' : 'false');
        },

        isValid: function () {
            return this.criterion.field !== '' && this.criterion.condition !== '' && this.criterion.value !== '';
        }

    }, {
        CLEAR_EVENT: 'clearItem'
    });

    return Criterion;

    function getFields (screenId) {
        var fields =  [
            {name: 'Any field', title: 'Any field', value: 'any'},
            {name: 'Component', title: 'Component', value: 'component'},
            {name: 'Execution type', title: 'Execution type', value: 'executionType'},
            {name: 'Group', title: 'Group', value: 'group'},
            {name: 'Priority', title: 'Priority', value: 'priority'},
            {name: 'Project ID', title: 'Project ID', value: 'projectId'},
            {name: 'Project Name', title: 'Project Name', value: 'projectName'},
            {name: 'Product Name', title: 'Product Name', value: 'productName'},
            {name: 'Feature Name', title: 'Feature Name', value: 'featureName'},
            {name: 'Requirement Label', title: 'Requirement Label', value: 'requirementLabel'},
            {name: 'Type', title: 'Type', value: 'type'},
            {name: 'Updated By ID', title: 'Updated By ID', value: 'updatedBy'},
            {name: 'Status', title: 'Status', value: 'status'}
        ];

        if (screenId === Constants.pages.TEST_PLAN_EXECUTION) {
            fields.splice(1, 0, {name: 'Assignee', title: 'Assignee', value: 'user'});
        }
        return fields;
    }

    function getConditions () {
        return [
            {name: 'equals', title: 'equals', value: '='},
            {name: 'not equal', title: 'not equal', value: '!='},
            {name: 'contains', title: 'contains', value: '~'}
        ];
    }

});
