define([
    'jscore/core',
    'jscore/base/jquery',
    'jscore/ext/utils/base/underscore',
    './TestCaseEditContentRegionView',
    'widgets/Dialog',
    'widgets/SelectBox',
    '../../../../common/Constants',
    '../../../../common/ContextFilter',
    '../../../../common/Animation',
    '../../../../common/Navigation',
    '../../../../common/ModelHelper',
    '../../../../common/models/ReferencesCollection',
    '../../../../common/ReferenceHelper',
    '../TestCaseFormWidget/TestCaseFormWidget',
    '../TestCaseFormStepsWidget/TestCaseFormStepsWidget',
    '../../models/testCases/TestCaseModel',
    '../../../../common/widgets/GenericFileUpload/FileUpload',
    '../../../../common/notifications/NotificationRegion/NotificationRegion'
], function (core, $, _, View, Dialog, SelectBox, Constants, ContextFilter, Animation, Navigation, ModelHelper,
             ReferencesCollection, ReferenceHelper, Details, TestSteps, TestCaseModel, FileUpload, NotificationRegion) {
    'use strict';

    return core.Region.extend({
        /*jshint validthis:true */

        View: View,

        init: function () {
            this.eventBus = this.getContext().eventBus;
            this.currentModel = new TestCaseModel();
            this.messagesWidgets = [];
            this.populateWith = {};
            this.configureReferences();
            this.createPageBefore = false;
        },

        configureReferences: function () {
            this.productReferences = new ReferencesCollection();
            this.productReferences.addReferences('product');

            this.modelReferences = new ReferencesCollection();
            this.modelReferences.addReferences(
                'project',
                'product',
                'feature',
                'priority',
                'context',
                'group',
                'type',
                'component',
                'executionType',
                'executionResult',
                'team',
                'suite'
            );
        },

        onViewReady: function () {
            this.view.afterRender();

            this.detailsWidget = new Details({
                model: this.currentModel,
                references: this.modelReferences,
                region: this
            });
            this.detailsWidget.attachTo(this.view.getDetailsBlock());

            this.testStepsWidget = new TestSteps({
                model: this.currentModel,
                region: this
            });
            this.testStepsWidget.attachTo(this.view.getTestStepsBlock());

            this.animation = new Animation(this.getElement(), this.eventBus);
            this.animation.showOn(Constants.events.SHOW_TEST_CASE_FORM, this.redrawPage.bind(this));
            this.animation.hideOn(Constants.events.HIDE_TEST_CASE_FORM, this.onHideContent.bind(this));
            this.animation.markCurrentOn(Constants.events.MARK_TEST_CASE_FORM_CURRENT, this.redrawPage.bind(this));

            this.eventBus.subscribe(Constants.events.TEST_CASE_CREATE_REQUEST, this.onTestCaseCreateRequest, this);
            this.eventBus.subscribe(Constants.events.TEST_CASE_SAVE_REQUEST, this.onTestCaseSaveRequest, this);
            this.eventBus.subscribe(Constants.events.TEST_CASE_COPY_REQUEST, this.onTestCaseCopyRequest, this);

            this.fileUploadWidget = new FileUpload({
                region: this,
                model: this.currentModel,
                editMode: true,
                eventBus: this.eventBus,
                category: 'test-cases'
            });
            this.fileUploadWidget.attachTo(this.view.getFilesHolder());

            createWarningDialog.call(this);
        },

        onHideContent: function () {
            this.eventBus.publish(Constants.events.HIDE_ERROR_BLOCK);
        },

        redrawPage: function (params) {
            var testCaseId, version, screenId;
            if (params) {
                testCaseId = params.testCaseId || '';
                screenId = params.screenId;
                if (params.options) {
                    version = params.options.version;
                    this.populateWith = params.options.query || {};
                    // Backwards-compatible test case ID population
                    if (testCaseId && this.populateWith.testCaseId == null) {
                        this.populateWith.testCaseId = [testCaseId];
                    }
                }
            }
            if (screenId === Constants.pages.TEST_CASE_EDIT) {
                this.createPageBefore = false;
            } else if (screenId === Constants.pages.TEST_CASE_COPY) {
                this.createPageBefore = false;
            } else if (this.createPageBefore) {
                // nothing
            } else {
                this.currentModel.clear();
                this.testStepsWidget.redrawSteps(this.currentModel.getSteps());
                this.createPageBefore = true;
            }
            updateCurrentModel.call(this, testCaseId, version, screenId);
        },

        refreshReferences: function (selectedProduct, selectedFeature) {
            if (_.isEmpty(selectedProduct)) {
                return;
            }

            ContextFilter.productIdParam = selectedProduct.name;
            if (selectedFeature != null) {
                ContextFilter.featureIdParam = selectedFeature.name;
            } else {
                ContextFilter.featureIdParam = null;
            }

            _.defer(function () {
                loadReferences.call(this)
                    .then(populateModel.bind(this))
                    .then(updateWidget.bind(this, Constants.pages.TEST_CASE_CREATE))
                    .then(updateProfile.bind(this, selectedProduct));
            }.bind(this));
        },

        onTestCaseCreateRequest: function () {
            this.eventBus.publish(Constants.events.HIDE_ERROR_BLOCK);

            this.detailsWidget.updateModelData();
            this.currentModel.setSteps(this.testStepsWidget.getValues());

            this.fileUploadWidget.deleteFiles();

            this.currentModel.save({}, {
                statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                    201: function () {
                        this.createPageBefore = false;
                        this.fileUploadWidget.setModel(this.currentModel);
                        this.fileUploadWidget.saveFiles();
                        Navigation.navigateTo(Navigation.getTestCaseDetailsUrl(this.currentModel.getId()));
                    }.bind(this)
                })
            });
        },

        onTestCaseSaveRequest: function () {
            if (this.fileUploadWidget.validateFiles()) {
                saveEditTestCaseData.call(this);
                return;
            }
            this.warningDialog.show();
            this.createPageBefore = false;
        },

        onTestCaseCopyRequest: function () {
            saveEditTestCaseData.call(this);
            this.createPageBefore = false;
        }
    });

    function saveEditTestCaseData () {
        this.eventBus.publish(Constants.events.HIDE_ERROR_BLOCK);

        this.detailsWidget.updateModelData();
        this.currentModel.setSteps(this.testStepsWidget.getValues());

        $.when(this.fileUploadWidget.deleteFiles(), this.fileUploadWidget.saveFiles())
            .done(function () {
                this.currentModel.save({}, {
                    statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                        200: function (model) {
                            Navigation.navigateTo(Navigation.getTestCaseDetailsUrl(model.testCaseId));
                        }.bind(this),
                        201: function (model) {
                            Navigation.navigateTo(Navigation.getTestCaseDetailsUrl(model.id));
                        }.bind(this)
                    })
                });
            }.bind(this));
    }

    function updateCurrentModel (testCaseId, version, screenId) {
        switch (screenId) {
            case Constants.pages.TEST_CASE_EDIT:
                ContextFilter.profileReady
                    .then(loadModel.bind(this, testCaseId, version, screenId))
                    .then(loadReferences.bind(this))
                    .then(updateWidget.bind(this, screenId))
                    .then(fetchFiles.bind(this, true));
                break;
            case Constants.pages.TEST_CASE_COPY:
                ContextFilter.profileReady
                    .then(loadModel.bind(this, testCaseId, version, screenId))
                    .then(loadReferences.bind(this))
                    .then(cleanUpModelForCopy.bind(this))
                    .then(updateWidget.bind(this, screenId))
                    .then(fetchFiles.bind(this, false));
                break;
            case Constants.pages.TEST_CASE_CREATE:
            /* falls through */
            default:
                ContextFilter.profileReady
                    .then(loadProductSelectDialog.bind(this))
                    .then(loadReferences.bind(this))
                    .then(populateModel.bind(this))
                    .then(updateWidget.bind(this, screenId))
                    .then(fetchFiles.bind(this, false));
                break;
        }
    }

    function loadModel (testCaseId, version, screenId) {
        var df = $.Deferred();
        this.currentModel.setId(testCaseId);
        this.currentModel.fetch({
            data: {
                version: version,
                view: 'detailed'
            },
            reset: true,
            statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                200: function (model) {
                    if (screenId === Constants.pages.TEST_CASE_EDIT && this.currentModel.getTestCaseStatus() === 'Approved') {
                        createNewVersion.call(this, df, model.testCaseId);
                        return;
                    }
                    setContext.call(this, model);
                    df.resolve();
                }.bind(this)
            }, df)
        });
        return df.promise();
    }

    function createNewVersion (df, testCaseId) {
        $.ajax({
            url: '/tm-server/api/test-cases/' + testCaseId + '/versions',
            type: 'POST',
            statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                201: function (model) {
                    var options = NotificationRegion.NOTIFICATION_TYPES.success;
                    options.canDismiss = true;
                    options.canClose = true;
                    this.eventBus.publish(Constants.events.NOTIFICATION, 'New test case version was created for editing', options);

                    this.currentModel = new TestCaseModel(model);
                    this.detailsWidget.setModel(this.currentModel);
                    setContext.call(this, model);
                    df.resolve();
                }.bind(this)
            }, df)
        });
    }

    function loadProductSelectDialog () {
        var df = $.Deferred();

        if (_.isNull(ContextFilter.profileProduct)) {
            this.productReferences.fetch({
                reset: true,
                statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                    200: function () {
                        createDialogBoxWithProductSelect.call(this, df);
                    }.bind(this)
                }, df)
            });
        } else {
            this.detailsWidget.setProduct(ContextFilter.profileProduct);
            df.resolve();
        }
        return df.promise();
    }

    function loadReferences () {
        var df = $.Deferred();

        this.modelReferences.setProductId(encodeURIComponent(ContextFilter.getActiveProductId()));
        this.modelReferences.setFeatureId(encodeURIComponent(ContextFilter.getActiveFeatureId()));
        this.modelReferences.fetch({
            reset: true,
            statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                200: function () {
                    df.resolve();
                }
            }, df)
        });

        return df.promise();
    }

    function populateModel () {
        populateFirst.call(this, 'testCaseId');
        populateFirst.call(this, 'title');
        populateFirst.call(this, 'description');
        populateFirst.call(this, 'precondition');
        populateFromReference.call(this, 'type');
        populateFromReference.call(this, 'executionType');
        populateFromReference.call(this, 'priority');
        populateFromReference.call(this, 'testCaseStatus');
        populateFromReference.call(this, 'context', 'contexts', true);
        populateFromReference.call(this, 'group', 'groups', true);
    }

    function createDialogBoxWithProductSelect (df) {
        var productSelectBox = createProductSelectBox.call(this);

        this.dialog = new Dialog({
            header: 'Select a product you are working on:',
            content: productSelectBox,
            buttons: [
                {
                    caption: 'OK',
                    color: 'green',
                    action: function () {
                        var selected = productSelectBox.getValue();
                        if (_.isEmpty(selected)) {
                            return;
                        }

                        this.eventBus.publish(
                            Constants.events.UPDATE_USER_PROFILE_WITH_PROJECT,
                            null, selected
                        );
                        this.detailsWidget.setProduct(selected);
                        this.dialog.hide();
                        df.resolve();
                    }.bind(this)
                },
                {
                    caption: 'Cancel',
                    action: function () {
                        df.reject();
                        this.dialog.hide();
                        Navigation.navigateTo(Navigation.getTestCasesListUrl());
                    }.bind(this)
                }
            ]
        });
        this.dialog.show();
    }

    function createProductSelectBox () {
        var referenceHelper = new ReferenceHelper({
            references: this.productReferences
        });

        var selectBox = new SelectBox({
            items: referenceHelper.getReferenceItems('product'),
            modifiers: [
                {name: 'width', value: 'fullBlock'}
            ]
        });
        selectBox.view.getButton().setAttribute('id', 'TMS_CreateTestCase_DialogBox-productSelect');
        return selectBox;
    }

    function populateFirst (key) {
        var values = this.populateWith[key];
        if (values != null) {
            this.currentModel.setAttribute(key, values[0]);
        }
    }

    function populateFromReference (refId, attribute, multi) {
        if (attribute == null) {
            attribute = refId;
        }
        var pw = this.populateWith[refId];
        if (pw == null) {
            return;
        }
        var attrValue = [];
        this.modelReferences.getReferenceItemsByNames(refId, pw).forEach(function (item) {
            if (item != null && item.itemObj != null) {
                attrValue.push(item.itemObj);
            }
        });
        this.currentModel.setAttribute(attribute, multi ? attrValue : attrValue[0]);
    }

    function updateWidget (screenId, projectId) {
        this.detailsWidget.updateFields(screenId, projectId);
        if (this.testStepsWidget._testStepWidgets.length >= 0 && this.currentModel.getSteps()) {
            this.testStepsWidget.redrawSteps(this.currentModel.getSteps());
        }
    }

    function cleanUpModelForCopy (projectId) {
        var df = $.Deferred();

        var title = this.currentModel.getTitle(),
            testCaseId = this.currentModel.getTestCaseId();

        this.currentModel.setId(null);
        this.currentModel.setTitle('Copy of - ' + title);
        var copyTestCaseId = 'Copy of - ' + testCaseId;
        this.currentModel.setTestCaseId(copyTestCaseId);
        this.populateWith.testCaseId = [copyTestCaseId];

        nullifyIds.call(this, this.currentModel.getSteps());

        this.currentModel.removeAttribute('version');
        this.currentModel.removeAttribute('associatedTestPlans');

        df.resolve(projectId);
        return df.promise();
    }

    function nullifyIds (testSteps) {
        testSteps.forEach(function (testStep) {
            testStep.id = null;
            if (testStep.verifies != null) {
                testStep.verifies.forEach(function (verifyStep) {
                    verifyStep.id = null;
                });
            }
        });
    }

    function fetchFiles (fetch) {
        if (fetch) {
            this.fileUploadWidget.fetchFiles(this.currentModel);
        } else {
            this.fileUploadWidget.clearTable();
        }
    }

    function updateProfile (selectedProduct) {
        var df = $.Deferred();
        this.eventBus.publish(
            Constants.events.UPDATE_USER_PROFILE_WITH_PROJECT,
            null, selectedProduct
        );

        return df.resolve();
    }

    function createWarningDialog () {
        this.warningDialog = new Dialog({
            header: 'Warning some files already exist',
            content: 'Do you wish to overwrite?',
            buttons: [
                {
                    caption: 'OK',
                    color: 'green',
                    action: function () {
                        this.fileUploadWidget.continueSaving = true;
                        this.warningDialog.hide();
                        saveEditTestCaseData.call(this);
                    }.bind(this)
                },
                {
                    caption: 'Cancel',
                    action: function () {
                        this.fileUploadWidget.continueSaving = false;
                        this.warningDialog.hide();
                    }.bind(this)
                }
            ]
        });

    }

    function setContext (model) {
        var feature = model.feature;
        ContextFilter.profileProduct = feature.product;
        ContextFilter.productIdParam = feature.product.name;
        ContextFilter.featureIdParam = feature.name;
    }

});
