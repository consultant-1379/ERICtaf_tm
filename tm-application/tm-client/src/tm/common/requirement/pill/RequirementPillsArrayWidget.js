define([
    'jscore/core',
    'widgets/Tooltip',
    '../../Constants',
    './RequirementPillsArrayWidgetView',
    './item/RequirementPillWidget'
], function (core, Tooltip, Constants, View, RequirementPillWidget) {
    'use strict';

    return core.Widget.extend({

        View: View,

        init: function (options) {
            this.requirements = options.value || [];
            this.requirementIds = [];
        },

        onViewReady: function () {
            if (this.requirements && this.requirements.length > 0) {
                this.requirements.forEach(function (requirement) {
                    var requirementWidget = new RequirementPillWidget({
                        externalId: requirement.externalId,
                        statusName: requirement.externalStatusName,
                        deliveredIn: requirement.deliveredIn,
                        title: this.getTitleString(requirement)
                    });
                    requirementWidget.attachTo(this.view.getElement());
                    this.requirementIds.push(requirement.externalId);
                }.bind(this));
            }
        },

        getTitleString: function (requirement) {
            if (requirement.summary) {
                return requirement.summary; //Requirement
            }
            return requirement.externalSummary; //Defect
        },

        getTooltip: function () {
            return this.requirementIds.join(', ');
        }

    });
})
;
