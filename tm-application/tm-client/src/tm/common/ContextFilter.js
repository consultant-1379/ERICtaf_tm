define(function () {
    'use strict';

    return {
        requirementIdParam: '',
        projectIdParam: null,
        searchQuery: '',
        searchGroupQuery: '',
        profileProject: null,
        profileProduct: null,
        isAdvancedSearch: false,
        projectsCollection: null,
        profileSearch: null,
        userId: null,

        profileReady: null,
        openedInfoPopup: null,

        productIdParam: null,
        featureIdParam: null,

        getProfileProjectId: function () {
            if (this.profileProject != null) {
                return this.profileProject.externalId;
            }
            return null;
        },

        getActiveProjectId: function () {
            return this.projectIdParam || this.getProfileProjectId();
        },

        getActiveProductId: function () {
            if (this.profileProduct != null) {
                return this.productIdParam || this.profileProduct.name;
            }
            return null;
        },

        getActiveFeatureId: function () {
            return this.featureIdParam || '';
        }
    };

});
