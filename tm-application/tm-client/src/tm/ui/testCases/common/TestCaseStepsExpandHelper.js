define([
], function () {
    'use strict';

    var TestCaseStepsExpandHelper = function (testCaseStepsWidget) {
        this.widget = testCaseStepsWidget;
    };

    TestCaseStepsExpandHelper.prototype.expandCollapseAllSteps = function () {
        var stepsAreExpanded = this.widget.stepsAreExpanded;
        this.widget._stepWidgets.forEach(function (widget) {
            if (stepsAreExpanded) {
                widget.trigger('collapse');
            } else {
                widget.trigger('expand');
            }
        }.bind(this));
    };

    TestCaseStepsExpandHelper.prototype.modifyExpandCollapseAllIconStyle = function () {
        var stepsState = this.checkAllStepsState();
        if (stepsState.allExpanded) {
            this.widget.stepsAreExpanded = true;
        } else if (stepsState.allCollapsed) {
            this.widget.stepsAreExpanded = false;
        }
        this.changeExpandCollapseBtnStyle();
    };

    TestCaseStepsExpandHelper.prototype.applyUpArrowStyle = function () {
        this.widget.view.getExpandCollapseIcon().removeModifier('downArrow');
        this.widget.view.getExpandCollapseIcon().setModifier('upArrow', '10px');
        this.widget.view.getExpandCollapseButton().setAttribute('title', 'Collapse');
    };

    TestCaseStepsExpandHelper.prototype.applyDownArrowStyle = function () {
        this.widget.view.getExpandCollapseIcon().removeModifier('upArrow');
        this.widget.view.getExpandCollapseIcon().setModifier('downArrow', '10px');
        this.widget.view.getExpandCollapseButton().setAttribute('title', 'Expand');
    };

    TestCaseStepsExpandHelper.prototype.changeExpandCollapseBtnStyle = function () {
        if (this.widget.stepsAreExpanded) {
            this.applyUpArrowStyle();
        } else {
            this.applyDownArrowStyle();
        }
    };

    TestCaseStepsExpandHelper.prototype.checkAllStepsState = function () {
        var allCollapsed = true;
        var allExpanded = true;
        for (var i = 0; i < this.widget._stepWidgets.length; i++) {
            if (this.widget._stepWidgets[i]._expanded) {
                allCollapsed = false;
            } else {
                allExpanded = false;
            }
        }
        return {allCollapsed: allCollapsed, allExpanded: allExpanded};
    };

    return TestCaseStepsExpandHelper;
});
