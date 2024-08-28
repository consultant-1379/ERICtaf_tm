define([
    'jscore/core',
    'widgets/Accordion',
    './AssociatedCommentsWidgetView',
    './comment/CommentWidget',
    './AccordionBar/AccordionBarWidget',
    '../../../common/ModelHelper',
    '../../../common/widgets/actionLink/ActionLink',
    '../../models/AssociatedComments/PostModel',
    '../../models/AssociatedComments/PostsCollection'
], function (core, Accordion, View, CommentWidget, AccordionBarWidget, ModelHelper, ActionLink, PostModel,
             PostsCollection) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true */

        View: View,

        init: function (options) {
            this.eventBus = options.eventBus;
            this.resourceRoot = options.resourceRoot;

            this.objectId = null;

            this.posts = null;
            this.userProfile = null;
            this.postsReceived = false;
            this.currentUserReceived = false;

            this.postsCollection = new PostsCollection();
            this.postsCollection.setResourceRoot(this.resourceRoot);

            this.accordionWidgets = {};
        },

        onViewReady: function () {
            this.view.afterRender();
            setVisibleErrorMessage.call(this, false);
            initAddActionLink.call(this);
        },

        setPostFetchParameters: function (objectId) {
            this.objectId = objectId;
            fetchComments.call(this, objectId);
        },

        setCurrentUser: function (currentUser) {
            this.userProfile = currentUser;
            this.currentUserReceived = true;
            setPosts.call(this);
        },

        updatePosts: function (collection) {
        // doesn't remove comments that are deleted but its something we have to live with.
            this.postsCollection.each(function (model1) {
                collection.each(function (model2) {
                    if (model1.id === model2.id) {
                        collection.removeModel(model2);
                    }
                }.bind(this));
            }.bind(this));

            collection.each(function (postModel) {
                this.postsCollection.addModel(postModel);
                addAccordion.call(this, postModel);
            }.bind(this));
        },

        onAddCommentClick: function () {
            if (isReadyToCreateNew.call(this)) {
                createNewPost.call(this, this.view.getEditAreaText().getValue());
            }
        },

        deletePost: function (postModel) {
            var accordionWidget = this.accordionWidgets[postModel.getId()];
            accordionWidget.detach();
            accordionWidget.destroy();

            postModel.setResourceRoot(this.resourceRoot);
            postModel.setObjectId(this.objectId);
            postModel.destroy({
                statusCode: ModelHelper.statusCodeHandler(this.eventBus)
            });
            this.postsCollection.removeModel(postModel);
        }

    });

    function isReadyToCreateNew () {
        if (this.view.getEditAreaText().getValue().length < 1) {
            return false;
        }
        return this.objectId;
    }

    function populatePosts () {
        this.postsCollection.sort(comparePosts);
        this.postsCollection.each(function (postModel) {
            addAccordion.call(this, postModel);
        }.bind(this));
    }

    function initAddActionLink () {
        this.addActionLink = new ActionLink({
            icon: {iconKey: 'save', interactive: true, title: 'Add'},
            link: {text: 'Add'},
            action: this.onAddCommentClick.bind(this)
        });
        this.addActionLink.attachTo(this.view.getEditAreaButtonsHolder());
    }

    function addAccordion (post) {
        var commentWidget = new CommentWidget({
            post: post
        });

        var accordionBarWidget = new AccordionBarWidget({
            post: post,
            eventBus: this.eventBus,
            currentUser: this.userProfile,
            parentWidget: this
        });

        var accordion = new Accordion({
            title: accordionBarWidget,
            content: commentWidget
        });
        accordion.trigger('expand');
        accordion.attachTo(this.view.getPostsHolder());

        this.accordionWidgets[post.getId()] = accordion;
    }

    function setVisibleErrorMessage (isVisible, message) {
        var errorMessageElement = this.view.getErrorMessage();
        if (isVisible) {
            errorMessageElement.setStyle('display', 'block');
            errorMessageElement.setText(message);
        } else {
            errorMessageElement.setStyle('display', 'none');
            errorMessageElement.setText('');
        }
    }

    function fetchComments (objectId) {
        this.postsCollection.setObjectId(objectId);
        this.postsCollection.fetch({
            reset: true,
            data: {
                view: 'detailed'
            },
            statusCode: ModelHelper.authenticationHandler(this.eventBus, {
                200: function () {
                    this.postsReceived = true;
                    setPosts.call(this);
                }.bind(this)
            })
        });
    }

    function setPosts () {
        if (this.currentUserReceived && this.postsReceived) {
            populatePosts.call(this);
        }
    }

    function createNewPost (message) {
        var post = new PostModel();
        post.setResourceRoot(this.resourceRoot);
        post.setObjectId(this.objectId);
        post.setMessage(message);

        post.save({}, {
            statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                201: function () {
                    setVisibleErrorMessage.call(this, false);
                    this.view.getEditAreaText().setValue('');

                    addAccordion.call(this, post);
                    this.postsCollection.addModel(post);
                }.bind(this),
                400: function () {
                    setVisibleErrorMessage.call(this, true, 'Bad request error.');
                }.bind(this),
                401: function () {
                    setVisibleErrorMessage.call(this, true, 'Unauthorized, this request requires user authentication.');
                }.bind(this),
                404: function () {
                    setVisibleErrorMessage.call(this, true,
                        'The server has not found anything matching the Request-URI.');
                }.bind(this),
                500: function () {
                    setVisibleErrorMessage.call(this, true, 'Some errors appeared on server when saving data.');
                }.bind(this)
            })
        });
    }

    function comparePosts (postModel1, postModel2) {
        var createdAt1 = postModel1.getAttribute('createdAt'),
            createdAt2 = postModel2.getAttribute('createdAt');

        if (createdAt1 > createdAt2) {
            return 1;
        }
        if (createdAt1 < createdAt2) {
            return -1;
        }
        return 0;
    }

});
