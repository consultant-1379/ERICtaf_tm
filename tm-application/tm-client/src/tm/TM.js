/*global location*/
define([
    'jscore/core',
    'jscore/ext/net',
    'jscore/base/jquery',
    'jscore/ext/utils/base/underscore',
    './common/Constants',
    './common/ValidationHelper',
    './common/ContextFilter',
    './common/UserProfile',
    './common/Pages',
    './common/Navigation',
    './common/ModelHelper',
    './routing/Router',
    './common/Breadcrumbs',
    'text!./appInfo.json',
    './TMView',
    './common/ActionBarRegion/ActionBarRegionFactory',
    './common/models/ReferencesCollection',
    './common/models/product/ProductsCollection',
    './common/models/project/ProjectsCollection',
    './common/models/UserProfileModel',
    './common/notifications/NotificationRegion/NotificationRegion',
    './jsBlockers/SystemBarHelper'
], function (core, net, $, _, Constants, ValidationHelper, ContextFilter, UserProfile, Pages, Navigation, ModelHelper, Router,
             Breadcrumbs, appInfo, View, ActionBarRegionFactory, ReferencesCollection,
             ProductsCollection, ProjectsCollection, UserProfileModel, NotificationRegion, SystemBarHelper) {
    'use strict';

    return core.App.extend({
        /*jshint validthis:true */

        View: View,

        init: function (options) {
            this.namespace = options.namespace;
            this.feedback = options.properties.feedback ? options.properties.feedback : {};

            this.pagesObj = {};
            this.currentPage = null;
            this.referencesCollection = new ReferencesCollection();
            this.isAppLoaded = false;

            this.projectsCollection = new ProjectsCollection();

            this.profileDf = $.Deferred();
            ContextFilter.profileReady = this.profileDf.promise();

        },

        onStart: function () {
            this.view.afterRender();

            this.eventBus = this.getContext().eventBus;
            this.eventBus.subscribe(Constants.events.AUTHENTICATION_REQUIRED, this.onAuthenticationRequired, this);

            net.ajax({
                url: Navigation.TMS_LOGIN_LINK,
                dataType: 'json',
                success: function (data) {
                    if (data.authenticated) {
                        this.onSuccessAuthentication();
                    } else {
                        this.eventBus.publish(Constants.events.AUTHENTICATION_REQUIRED);
                    }
                }.bind(this)
            });
        },

        onSuccessAuthentication: function () {
            initReferences.call(this);
            $.ajax({
                url: this.feedback.url,
                type: 'get',
                cache: true,
                dataType: 'script'
            });

            var appInfoObj = JSON.parse(appInfo);
            this.view.getVersion().setText(appInfoObj.version);
            this.view.getBuildDate().setText(appInfoObj.buildDate);

            var notificationRegion = new NotificationRegion({
                context: this.getContext()
            });
            notificationRegion.start(this.view.getNotificationsBlock());

            this.pagesObj = new Pages(this.getContext(), this.referencesCollection);

            var actionBarFactory = new ActionBarRegionFactory(this.getContext());
            _.each(this.pagesObj.pages, function (page) {
                page.actionBar = actionBarFactory.create(page.id, {
                    references: this.referencesCollection,
                    pages: this.pagesObj.pages
                });
            }, this);

            this.router = new Router(this.pagesObj.pages, this.eventBus);
            this.router.registerHandler(applyPageData.bind(this));

            this.eventBus.subscribe(Constants.events.CHANGE_HEADING_TITLE, this.onChangeHeadingTitle, this);
            this.eventBus.subscribe(Constants.events.UPDATE_URL_WITH_SEARCH, this.onUpdateUrlWithSearchQuery, this);
            this.eventBus.subscribe(Constants.events.UPDATE_SELECTED_PROJECT, this.onUpdateSelectedProject, this);
            this.eventBus.subscribe(Constants.events.RELOAD_USER_PROFILE, importSelected, this);

            this.eventBus.subscribe(Constants.events.UPDATE_USER_PROFILE_WITH_PROJECT, this.onUserProfileProjectUpdate,
                this);
            this.eventBus.subscribe(Constants.events.SEARCH_BY_VALUE, this.onSearchByValue, this);

            this.eventBus.subscribe(Constants.events.SAVE_SAVED_SEARCH, this.onSaveSearch, this);

            core.Window.addEventHandler('resize', function () {
                this.eventBus.publish(Constants.events.WINDOW_RESIZE);
            }.bind(this));

            core.Window.addEventHandler('keydown', function (e) {
                var keyCode = e.originalEvent.keyCode || e.originalEvent.which;
                var event = e.originalEvent;
                if (keyCode === 8 && !(event.target.tagName.toLowerCase() === 'input' || event.target.tagName.toLowerCase() === 'textarea')) {
                    event.preventDefault(); // disable backspace
                }
            }.bind(this));

            SystemBarHelper.createSystemBarLink();

            importProjectsAndProducts.call(this);
            importSelected.call(this);
            fetchReferences.call(this);

            updateIssueCollectorFields.call(this);
        },

        onStop: function () {
            this.router.stop();
        },

        onAuthenticationRequired: function () {
            location.href = 'login/' + location.hash;
        },

        onSearchByValue: function (searchValue) {
            ContextFilter.searchQuery = getCriterionUrl.call(this, searchValue);
            ContextFilter.isAdvancedSearch = false;
            publishUpdateEvents.call(this);
        },

        onSaveSearch: function (criteria) {
            criteria.query = ContextFilter.searchQuery;
            this.userProfile.addSavedSearch(criteria);
            this.onUserProfileProjectUpdate(null, null);
        },

        onUserProfileProjectUpdate: function (projectObj, productObj) {
            updateProject.call(this, projectObj);
            updateProduct.call(this, productObj);

            this.userProfile.save({}, {
                reset: true,
                statusCode: ModelHelper.authenticationHandler(this.eventBus, {
                    200: function (model) {
                        if (!_.isEmpty(projectObj)) {
                            this.eventBus.publish(Constants.events.PROJECT_CHANGED);
                        }
                        if (!_.isEmpty(productObj)) {
                            this.eventBus.publish(Constants.events.PRODUCT_CHANGED);
                        }
                        this.userProfile.setSavedSearch(model.savedSearch);
                        ContextFilter.profileSearch = this.userProfile.getSavedSearch();
                        this.eventBus.publish(Constants.events.UPDATE_SAVED_SEARCH);
                        ContextFilter.searchQuery = getCriterionUrl.call(this, ContextFilter.projectIdParam);
                        ContextFilter.isAdvancedSearch = false;
                    }.bind(this),
                    400: function (messageObj) {
                        var options = NotificationRegion.NOTIFICATION_TYPES.error;
                        options.canDismiss = true;
                        options.canClose = true;
                        var data = ValidationHelper.getValidationErrors(messageObj);
                        this.eventBus.publish(Constants.events.NOTIFICATION, data, options);
                        importSelected.call(this);
                    }.bind(this)
                })
            });
        },

        onUpdateSelectedProject: function () {
            if (ContextFilter.isAdvancedSearch) {
                publishUpdateEvents.call(this);
                return;
            }

            if (ContextFilter.searchQuery !== '') {
                publishUpdateEvents.call(this);
                return;
            }

            var anyValue = this.router.getParameterFromSearchQuery(Constants.params.ANY);

            ContextFilter.searchQuery = getCriterionUrl.call(this, anyValue);
            ContextFilter.isAdvancedSearch = false;

            publishUpdateEvents.call(this);
        },

        onUpdateUrlWithSearchQuery: function (criterionsUrl, isAdvanced) {
            ContextFilter.searchQuery = criterionsUrl;
            ContextFilter.isAdvancedSearch = isAdvanced;

            if (this.currentPage !== null) {
                var pageUrl;

                if (this.currentPage.id === Constants.pages.TEST_CASE_SEARCH) {
                    pageUrl = Navigation.getTestCaseListUrlWithParams(ContextFilter.isAdvancedSearch,
                        ContextFilter.searchQuery);
                } else if (this.currentPage.id === Constants.pages.REQUIREMENTS_TREE) {
                    pageUrl = Navigation.getRequirementsTreeUrlWithParams(ContextFilter.projectIdParam,
                        ContextFilter.requirementIdParam);
                } else if (this.currentPage.id === Constants.pages.TEST_PLAN_LIST) {
                    pageUrl = Navigation.getTestPlansListUrlWithParams(ContextFilter.projectIdParam);
                }

                if (pageUrl) {
                    Navigation.navigateTo(pageUrl);
                }
            }
        },

        onChangeHeadingTitle: function (title) {
            this.view.getPageTitle().setText(title);
        }

    });

    /*************** PRIVATE FUNCTIONS ******************/

    function applyPageData (pageToShow, itemId, flags) {
        var pageToHide = this.currentPage;
        this.currentPage = pageToShow;

        this.view.getPageTitle().setText(pageToShow.title);

        var params = {
            screenId: pageToShow.id,
            options: flags || {}
        };
        if (itemId != null) {
            params.testCaseId = itemId;
            params.itemId = itemId;
        }

        if (pageToHide !== null && pageToShow.id === pageToHide.id) {
            pageToShow.content.start(this.view.getContentBlock());
            this.eventBus.publish(pageToShow.events.current, params);
        } else {
            if (pageToHide !== null) {
                pageToHide.breadcrumb.detach();
                if (pageToHide.actionBar) {
                    pageToHide.actionBar.hide();
                }
                this.eventBus.publish(pageToHide.events.hide);

                if (pageToShow.id === Constants.pages.TEST_CASE_SEARCH && ContextFilter.searchQuery === '') {
                    var criterionUrl = getCriterionUrl.call(this, ContextFilter.projectIdParam);
                    this.eventBus.publish(Constants.events.UPDATE_URL_WITH_SEARCH, criterionUrl, false);
                }
            }

            if (pageToShow.isAdded) {
                pageToShow.breadcrumb.attach();
                if (pageToShow.actionBar) {
                    pageToShow.actionBar.show();
                }

                this.eventBus.publish(pageToShow.events.show, params);
            } else {
                pageToShow.breadcrumb.attachTo(this.view.getNavigation());
                if (pageToShow.actionBar) {
                    pageToShow.actionBar.start(this.view.getQuickActionBar());
                }
                pageToShow.content.start(this.view.getContentBlock());
                if (this.isAppLoaded) {
                    this.eventBus.publish(pageToShow.events.show, params);
                } else {
                    this.eventBus.publish(pageToShow.events.current, params);
                }
                this.isAppLoaded = true;
                pageToShow.isAdded = true;
            }
        }

        var breadcrumbs = Breadcrumbs.getEditBreadcrumbs(pageToShow.id, itemId, this.pagesObj);
        if (breadcrumbs != null) {
            pageToShow.breadcrumb.detach();
            pageToShow.breadcrumb.destroy();
            pageToShow.breadcrumb = breadcrumbs;
            pageToShow.breadcrumb.attachTo(this.view.getNavigation());
        }

        if (params && pageToShow.actionBar) {
            pageToShow.actionBar.setUrlItemId(itemId);
            pageToShow.actionBar.setFlags(flags);
        }

        // Apply modification for specific page types
        var pageTypes = {
            blank: this.view.hideHeader.bind(this.view),
            normal: this.view.showHeader.bind(this.view)
        };
        if (pageToShow.type) {
            pageTypes[pageToShow.type]();
        } else {
            pageTypes.normal();
        }
    }

    function initReferences () {
        this.referencesCollection.addReferences(
            'priority',
            'project',
            'context',
            'type',
            'executionType',
            'executionResult',
            'testCaseStatus'
        );
        this.referencesCollection.addEventHandler('reset', function () {
            this.eventBus.publish(Constants.events.REFERENCES_RECEIVED);
        }, this);
    }

    function fetchReferences () {
        this.referencesCollection.fetch({
            reset: true,
            statusCode: {
                401: function () {
                    this.eventBus.publish(Constants.events.AUTHENTICATION_REQUIRED);
                }.bind(this)
            }
        });
    }

    function importProjectsAndProducts () {
        this.productsCollection = new ProductsCollection();
        this.projectsCollection = new ProjectsCollection();

        this.productsCollection.fetch({
            reset: true,
            statusCode: ModelHelper.authenticationHandler(this.eventBus, {})
        });

        this.projectsCollection.fetch({
            reset: true,
            statusCode: ModelHelper.authenticationHandler(this.eventBus, {})
        });
    }

    function importSelected () {
        this.userProfile = new UserProfileModel();
        this.userProfile.fetch({
            reset: true,
            statusCode: ModelHelper.authenticationHandler(this.eventBus, {
                200: function () {
                    ContextFilter.profileProject = transformIdToString(this.userProfile.getProject());
                    ContextFilter.profileProduct = transformIdToString(this.userProfile.getProduct());

                    this.profileDf.resolve(ContextFilter.profileProject);
                    UserProfile.username = this.userProfile.getUserName();
                    this.eventBus.publish(Constants.events.PRODUCT_CHANGED);
                    this.eventBus.publish(Constants.events.PROJECT_CHANGED);
                    ContextFilter.profileSearch = this.userProfile.getSavedSearch();
                    this.eventBus.publish(Constants.events.UPDATE_SAVED_SEARCH);
                    ContextFilter.userId = this.userProfile.getUserId();
                }.bind(this)
            })
        });
    }

    function transformIdToString (obj) {
        if (obj && obj.hasOwnProperty('id')) {
            obj.id = String(obj.id);
        }
        return obj;
    }

    function getCriterionUrl (searchValue) {
        var params = [];

        if (searchValue !== undefined && searchValue !== '') {
            params.push(Constants.params.ANY + '~' + searchValue);
        }
        return params.join('&');
    }

    function publishUpdateEvents () {
        this.eventBus.publish(
            Constants.events.UPDATE_URL_WITH_SEARCH,
            ContextFilter.searchQuery,
            ContextFilter.isAdvancedSearch
        );
    }

    function updateProduct (productObj) {
        var productModel = null;

        if (!_.isEmpty(productObj)) {
            productModel = this.productsCollection.getModel(productObj.value);
        }

        if (!_.isNull(productModel) && !_.isUndefined(productModel)) {
            productObj = productModel.toJSON();
        }

        ContextFilter.productIdParam = '';
        ContextFilter.profileProduct = null;

        if (!_.isEmpty(productObj)) {
            ContextFilter.productIdParam = productObj.externalId;
            ContextFilter.profileProduct = transformIdToString(productObj);
            this.userProfile.setProduct(productObj);
        }
    }

    function updateProject (projectObj) {
        var projectModel = null;
        if (!_.isEmpty(projectObj)) {
            projectModel = this.projectsCollection.getModel(projectObj.value);
        }

        if (!_.isNull(projectModel) && !_.isUndefined(projectModel)) {
            projectObj = projectModel.toJSON();
        }

        ContextFilter.projectIdParam = '';
        ContextFilter.profileProject = null;
        if (!_.isEmpty(projectObj)) {
            ContextFilter.profileProject = transformIdToString(projectObj);
            ContextFilter.projectIdParam = projectObj.externalId;
            this.userProfile.setProject(projectObj);
        }
    }

    function updateIssueCollectorFields () {
        window.ATL_JQ_PAGE_PROPS = $.extend(window.ATL_JQ_PAGE_PROPS, {
            fieldValues: this.feedback
        });
    }

});
