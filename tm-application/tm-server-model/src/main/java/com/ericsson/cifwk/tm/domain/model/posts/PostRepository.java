package com.ericsson.cifwk.tm.domain.model.posts;

import com.ericsson.cifwk.tm.domain.model.posts.impl.PostRepositoryImpl;
import com.ericsson.cifwk.tm.domain.model.shared.BaseRepository;
import com.google.inject.ImplementedBy;

import java.util.List;

@ImplementedBy(PostRepositoryImpl.class)
public interface PostRepository extends BaseRepository<Post, Long> {

    Post find(Long id);

    List<Post> findPosts(Long objectId, PostObjectNameReference objectNameReference);
}
