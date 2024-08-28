package com.ericsson.cifwk.tm.application.services.impl;


import com.ericsson.cifwk.tm.application.services.PostService;
import com.ericsson.cifwk.tm.domain.model.posts.Post;
import com.ericsson.cifwk.tm.domain.model.posts.PostObjectNameReference;
import com.ericsson.cifwk.tm.domain.model.posts.PostRepository;

import javax.inject.Inject;
import java.util.List;

public class PostServiceImpl implements PostService {

    @Inject
    private PostRepository postRepository;

    @Override
    public List<Post> findPosts(Long objectId, PostObjectNameReference objectNameReference) {
        return postRepository.findPosts(objectId, objectNameReference);

    }

}
