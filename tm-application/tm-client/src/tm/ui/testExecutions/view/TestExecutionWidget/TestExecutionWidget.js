/*global define*/
define([
    'jscore/core',
    './TestExecutionWidgetView',
    'widgets/Accordion',
    '../../../../common/widgets/GenericFileUpload/FileUpload'
], function (core, View, Accordion, FileUpload) {
    'use strict';

    return core.Widget.extend({

        view: function () {
            return new View({
                execution: this.options.execution
            });
        },

        init: function (options) {
            this.model = options.model;
            this.region = options.region;
            this.eventBus = options.eventBus;
        },

        onViewReady: function () {
            this.filesWidget = new FileUpload({
                region: this.region,
                model: this.model,
                editMode: false,
                eventBus: this.eventBus,
                category: 'test-executions'
            });

            this.accordion = new Accordion({
                title: 'Files',
                content: this.filesWidget
            });

            this.accordion.addEventHandler('beforeExpand', function () {
                this.filesWidget.fetchFiles(this.model);
            }, this);

            this.accordion.attachTo(this.view.getFilesBlock());
        }

    });

});
