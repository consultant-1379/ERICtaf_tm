/*global define*/
define([
    'jscore/core',
    'text!./AutocompleteInput.html'
], function (core, template) {
    'use strict';

    return core.View.extend({

        getTemplate: function () {
            return template;
        },

        getInput: function () {
            return this.getElement().find('.eaTM-AutocompleteInput-input');
        }

    });

});
