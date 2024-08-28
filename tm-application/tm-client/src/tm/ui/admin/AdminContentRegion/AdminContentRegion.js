define([
    'jscore/core',
    './AdminContentRegionView',
    'jscore/ext/utils/base/underscore',
    '../../../common/Constants',
    '../../../common/ContextFilter',
    '../../../common/Animation',
    'widgets/Dialog',
    '../../../common/ModelHelper',
    '../../../common/models/UserProfileModel',
    '../../../common/models/ReferencesCollection',
    '../../../common/Navigation',
    'widgets/Tabs',
    '../GenericAdminWidget/GenericAdminWidget',
    '../GenericProductWidget/GenericProductWidget',
    '../ProductWidget/ProductWidget',
    '../GenericProductWidgetExternalId/GenericProductWidgetExternalId',
    '../UserAccessWidget/UserAccessWidget',
    '../ReviewGroupWidget/ReviewGroupWidget',
    '../models/TeamCollection',
    '../models/TeamModel',
    '../models/SuiteCollection',
    '../models/SuiteModel',
    '../models/GroupCollection',
    '../models/GroupModel',
    '../models/ComponentCollection',
    '../models/ComponentModel',
    '../models/ProjectCollection',
    '../models/ProjectModel',
    '../models/FeatureCollection',
    '../models/FeatureModel',
    '../models/ProductCollection',
    '../models/ProductModel',
    '../models/TestTypeCollection',
    '../models/TestTypeModel',
    '../models/TestCampaignGroupCollection',
    '../models/TestCampaignGroupModel',
    '../models/DropCollection',
    '../models/DropModel'
], function (core, View, _, Constants, ContextFilter, Animation, Dialog, ModelHelper, UserProfileModel,
             ReferencesCollection, Navigation, Tabs, GenericAdminWidget, GenericProductWidget, ProductWidget,
             GenericProductWidgetExternalId, UserAccessWidget, ReviewGroupWidget, TeamCollection, TeamModel,
             SuiteCollection, SuiteModel, GroupCollection, GroupModel, ComponentCollection, ComponentModel,
             ProjectCollection, ProjectModel, FeatureCollection, FeatureModel, ProductCollection, ProductModel,
             TestTypeCollection, TestTypeModel, TestCampaignGroupCollection, TestCampaignGroupModel, DropCollection, DropModel) {
    'use strict';

    return core.Region.extend({
        /*jshint validthis:true */

        View: View,

        init: function () {
            this.eventBus = this.getContext().eventBus;
        },

        onViewReady: function () {
            this.view.afterRender();

            this.animation = new Animation(this.getElement(), this.eventBus);
            this.animation.showOn(Constants.events.SHOW_VIEW_ADMIN, this.validateUser.bind(this));
            this.animation.hideOn(Constants.events.HIDE_VIEW_ADMIN);
            this.animation.markCurrentOn(Constants.events.MARK_VIEW_ADMIN_CURRENT,
                this.validateUser.bind(this));

            this.modelReferences = new ReferencesCollection();
            this.modelReferences.addReferences(
                'product',
                'feature'
            );
            initialize.call(this);
            this.fetchReferences();
        },

        validateUser: function () {
            getCurrentUser.call(this);
        },

        fetchReferences: function () {
            this.modelReferences.fetch({
                reset: true
            });
        }

    });

    function getCurrentUser () {
        this.userProfile = new UserProfileModel();
        this.userProfile.fetch({
            reset: true,
            statusCode: ModelHelper.authenticationHandler(this.eventBus, {
                200: function (data) {
                    if (!data.administrator) {
                        showAuthenticationDialog.call(this);
                    }
                }.bind(this)
            })
        });
    }

    function showAuthenticationDialog () {
        var dialog = new Dialog({
            header: 'This user is not authorized to view this page',
            type: 'error',
            content: 'Please request access from the application administrator',
            buttons: [
                {
                    caption: 'Close',
                    action: function () {
                        dialog.hide();
                        Navigation.navigateTo(Navigation.getWelcomeUrl());
                    }
                }
            ]
        });

        dialog.show();
    }

    function initialize () {
        var tabs = new Tabs({
            enabled: true,
            maxTabs: 11,
            tabs: [
                {title: 'User Access', content: new UserAccessWidget({eventBus: this.eventBus})},
                {
                    title: 'Product',
                    content: new ProductWidget({
                        eventBus: this.eventBus,
                        region: this,
                        collection: ProductCollection,
                        model: ProductModel
                    })
                },
                {
                    title: 'Features',
                    content: new GenericProductWidget({
                        eventBus: this.eventBus,
                        references: this.modelReferences,
                        region: this,
                        collection: FeatureCollection,
                        model: FeatureModel
                    })
                },
                {
                    title: 'Components',
                    content: new GenericAdminWidget({
                        eventBus: this.eventBus,
                        references: this.modelReferences,
                        region: this,
                        collection: ComponentCollection,
                        model: ComponentModel
                    })
                },
                {
                    title: 'Test Types',
                    content: new GenericProductWidget({
                        eventBus: this.eventBus,
                        references: this.modelReferences,
                        region: this,
                        collection: TestTypeCollection,
                        model: TestTypeModel
                    })
                },
                {
                    title: 'Groups',
                    content: new GenericProductWidget({
                        eventBus: this.eventBus,
                        references: this.modelReferences,
                        region: this,
                        collection: GroupCollection,
                        model: GroupModel
                    })
                },
                {title: 'Review Group', content: new ReviewGroupWidget({eventBus: this.eventBus})},
                {
                    title: 'Projects',
                    content: new GenericProductWidgetExternalId({
                        eventBus: this.eventBus,
                        references: this.modelReferences,
                        region: this,
                        collection: ProjectCollection,
                        model: ProjectModel
                    })
                },
                {
                    title: 'Teams',
                    content: new GenericAdminWidget({
                        eventBus: this.eventBus,
                        references: this.modelReferences,
                        region: this,
                        collection: TeamCollection,
                        model: TeamModel
                    })
                },
                {
                    title: 'Suites',
                    content: new GenericAdminWidget({
                        eventBus: this.eventBus,
                        references: this.modelReferences,
                        region: this,
                        collection: SuiteCollection,
                        model: SuiteModel
                    })
                },
                {
                    title: 'Test Campaign Group',
                    content: new GenericProductWidget({
                        eventBus: this.eventBus,
                        references: this.modelReferences,
                        region: this,
                        collection: TestCampaignGroupCollection,
                        model: TestCampaignGroupModel
                    })
                },
                {
                    title: 'Product Drop',
                    content: new GenericProductWidget({
                        eventBus: this.eventBus,
                        references: this.modelReferences,
                        region: this,
                        collection: DropCollection,
                        model: DropModel
                    })
                }
            ]
        });

        tabs.addEventHandler('tabselect', function () {
            this.fetchReferences();
        }.bind(this));

        tabs.attachTo(this.view.getDetailsBlock());
    }
});
