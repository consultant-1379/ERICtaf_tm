package com.ericsson.cifwk.tm.presentation.resources.impl;

import com.ericsson.cifwk.tm.application.CommandProcessor;
import com.ericsson.cifwk.tm.application.commands.CreatePostCommand;
import com.ericsson.cifwk.tm.application.commands.DeletePostCommand;
import com.ericsson.cifwk.tm.application.queries.PostQuerySet;
import com.ericsson.cifwk.tm.application.security.UserSessionService;
import com.ericsson.cifwk.tm.domain.model.posts.Post;
import com.ericsson.cifwk.tm.domain.model.posts.PostObjectNameReference;
import com.ericsson.cifwk.tm.infrastructure.mapping.PostMapper;
import com.ericsson.cifwk.tm.presentation.RequestPreconditions;
import com.ericsson.cifwk.tm.presentation.annotations.Controller;
import com.ericsson.cifwk.tm.presentation.dto.CommentsThreadInfo;
import com.ericsson.cifwk.tm.presentation.dto.PostInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.CommentsViewFactory;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.ericsson.cifwk.tm.presentation.resources.PostResource;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.Date;

import static com.ericsson.cifwk.tm.domain.model.posts.PostObjectNameReference.mapObjectReferenceByKey;

@Controller
public class PostController implements PostResource {

    private PostObjectNameReference postObjectNameReference;
    private Long commentedObjectId;

    @Inject
    private CommandProcessor commandProcessor;

    @Inject
    private CreatePostCommand createPostCommand;

    @Inject
    private DeletePostCommand deletePostCommand;

    @Inject
    private PostQuerySet postQuerySet;

    @Inject
    private PostMapper postMapper;

    @Inject
    private UserSessionService userSessionService;

    @Inject
    private CommentsViewFactory commentsViewFactory;

    @Override
    public Response getPosts(String view) {
        Class<? extends DtoView<CommentsThreadInfo>> dtoView = commentsViewFactory.getByName(view);
        PostObjectNameReference objectNameRef = mapObjectReferenceByKey(postObjectNameReference.getKey());
        return postQuerySet.findPosts(commentedObjectId, objectNameRef, dtoView);
    }

    @Override
    public Response createPost(PostInfo postInfo) {
        RequestPreconditions.checkArgument(commentedObjectId != null, "Commented object should be defined.");
        RequestPreconditions.checkArgument(postInfo != null, "No data defined.");
        RequestPreconditions.checkArgument(postInfo.getMessage() != null, "Empty comment cannot be created.");
        RequestPreconditions.checkArgument(postInfo.getMessage().length() > 0, "Empty comment cannot be created.");

        UserInfo currentUser = getUser(null);
        if (postInfo.getUser() == null) {
            postInfo.setUser(currentUser);
        }
        if (postInfo.getCreatedAt() == null) {
            postInfo.setCreatedAt(new Date());
        }

        PostObjectNameReference objectReference =
                PostObjectNameReference.mapObjectReferenceByKey(postObjectNameReference.getKey());
        ReferenceDataItem referenceDataObject = new ReferenceDataItem(
                String.valueOf(objectReference.getValue()), objectReference.getKey());

        postInfo.setObjectNameReference(referenceDataObject);
        postInfo.setObjectId(commentedObjectId);

        Post newPost = postMapper.mapDto(postInfo, new Post());
        return commandProcessor.process(createPostCommand, newPost);

    }

    @Override
    public Response deletePost(Long postId) {
        return commandProcessor.process(deletePostCommand, postId);
    }

    public void setRelatedResource(PostObjectNameReference reference, Long objectId) {
        postObjectNameReference = reference;
        commentedObjectId = objectId;
    }

    private UserInfo getUser(String userId) {
        if (userId != null) {
            return null;
        } else {
            return userSessionService.getCurrentUser();
        }
    }

}
