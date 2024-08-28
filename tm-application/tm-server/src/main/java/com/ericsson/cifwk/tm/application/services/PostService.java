package com.ericsson.cifwk.tm.application.services;

import com.ericsson.cifwk.tm.application.services.impl.PostServiceImpl;
import com.ericsson.cifwk.tm.domain.model.posts.PostObjectNameReference;
import com.ericsson.cifwk.tm.domain.model.posts.Post;
import com.google.inject.ImplementedBy;

import java.util.List;

@ImplementedBy(PostServiceImpl.class)
public interface PostService {
    /**
     * Find threads of users comments in 'POST_THREADS' and 'POSTS' tables
     *
     * @param objectId is ID of instance of object, like as 'Test Case ID'
     * @param objectNameReference is reference to screen which belong comment threads
     *
     */
    List<Post> findPosts(Long objectId, PostObjectNameReference objectNameReference);

}
