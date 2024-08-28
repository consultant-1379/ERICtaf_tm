define([
    'jscore/ext/utils/base/underscore'
], function (_) {
    'use strict';

    var ReferenceHelper = function (options) {
        this.references = options.references;
    };

    ReferenceHelper.prototype.getSelectedReference = function (refId, refObj) {
        var defaultRef = {name: ''};
        if (!refObj) {
            return defaultRef;
        }
        var selected = this.getSelectedReferences(refId, [refObj]);
        if (selected.length > 0) {
            return selected[0];
        } else {
            return defaultRef;
        }
    };

    ReferenceHelper.prototype.getSelectedReferences = function (refId, refObjects) {
        var references = this.references.getById(refId);
        var selectedRefs = [];
        if (!references || refObjects.length === 0) {
            return selectedRefs;
        }

        references.getItems().forEach(function (referenceObj) {
            refObjects.forEach(function (refObj) {
                if (refObj != null) {
                    if (referenceObj.value === refObj.id) {
                        selectedRefs.push(referenceObj);
                    }
                }
            });
        });
        return selectedRefs;
    };

    ReferenceHelper.prototype.getReferenceItems = function (referenceId) {
        var refs = this.references.getById(referenceId);
        return refs != null ? refs.getItems() : [];
    };

    ReferenceHelper.existsItem = function (items, item) {
        return _.find(items, function (itemObj) {
            return item.value === itemObj.value;
        });
    };

    ReferenceHelper.getItemObj = function (valueObj) {
        if (valueObj) {
            return valueObj.itemObj;
        }
        return undefined;
    };

    ReferenceHelper.getItemsObjects = function (valuesArray) {
        var values = [];
        if (valuesArray && valuesArray.length > 0) {
            valuesArray.forEach(function (valueObj) {
                values.push(valueObj.itemObj);
            });
        }
        return values;
    };

    ReferenceHelper.getArrayFromMultiSelect = function (objectsArray) {
        var values = [];
        if (objectsArray && objectsArray.length > 0) {
            objectsArray.forEach(function (obj) {
                values.push(obj.value);
            });
        }
        return values;
    };

    ReferenceHelper.prototype.findReferenceByName = function (refId, value) {
        var references = this.references.getById(refId);
        var result = {};
        references.getItems().forEach(function (referenceObj) {
            if (referenceObj.title === value) {
                result = referenceObj;
                return true;
            }
        }.bind(this));

        return result;
    };

    return ReferenceHelper;

});
