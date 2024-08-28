package com.ericsson.cifwk.tm.application.queries;

import com.ericsson.cifwk.tm.application.annotations.QuerySet;
import com.ericsson.cifwk.tm.domain.model.posts.Post;
import com.ericsson.cifwk.tm.domain.model.posts.PostObjectNameReference;
import com.ericsson.cifwk.tm.domain.model.posts.PostRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.PostMapper;
import com.ericsson.cifwk.tm.presentation.dto.CommentsThreadInfo;
import com.ericsson.cifwk.tm.presentation.dto.PostInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.CommentsView;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.ericsson.cifwk.tm.presentation.responses.Responses;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;

@QuerySet
public class PostQuerySet {
    @Inject
    private PostRepository postRepository;

    @Inject
    private PostMapper postMapper;

    public Response findPosts(
            Long objectId,
            PostObjectNameReference objectNameReference,
            Class<? extends DtoView<CommentsThreadInfo>> view) {

        List<Post> posts = postRepository.findPosts(objectId, objectNameReference);
        List<PostInfo> postInfos = Lists.transform(posts, new Function<Post, PostInfo>() {
            @Override
            public PostInfo apply(Post input) {
                return postMapper.mapEntity(input, PostInfo.class);
            }
        });

        CommentsThreadInfo result = new CommentsThreadInfo();
        result.setObjectId(objectId);
        result.setTotalCommentsCount(getCommentsCount(postInfos));
        if (CommentsView.Detailed.class.equals(view)) {
            result.setPosts(postInfos);
        }

        return Responses.ok(result);
    }

    private long getCommentsCount(List<PostInfo> posts) {
        return posts.size();
    }

}
