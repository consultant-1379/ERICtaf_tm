package com.ericsson.cifwk.tm.domain.model.posts.impl;


import com.ericsson.cifwk.tm.domain.model.posts.Post;
import com.ericsson.cifwk.tm.domain.model.posts.PostObjectNameReference;
import com.ericsson.cifwk.tm.domain.model.posts.PostRepository;
import com.ericsson.cifwk.tm.domain.model.shared.impl.BaseRepositoryImpl;
import com.googlecode.genericdao.search.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.List;

public class PostRepositoryImpl
        extends BaseRepositoryImpl<Post, Long>
        implements PostRepository {

    public static final String POST_ID = "id";
    public static final String OBJECT_ID = "objectId";
    public static final String OBJECT_NAME_REF = "objectNameReference";
    public static final String IS_DELETED = "isDeleted";

    private static final Logger LOGGER = LoggerFactory.getLogger(PostRepositoryImpl.class);

    @Override
    public Post find(Long id) {
        Search search = new Search(Post.class);
        search.addFilterEqual(POST_ID, id);
        try {
            return searchUnique(search);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Post> findPosts(Long objectId, PostObjectNameReference objectNameReference) {

        Search search = new Search(Post.class);

        search.addFilterEqual(OBJECT_ID, objectId);
        search.addFilterEqual(OBJECT_NAME_REF, objectNameReference.getValue());
        search.addFilterEqual(IS_DELETED, false);

        try {
            return search(search);
        } catch (NoResultException e) {
            LOGGER.error("NoResultException in PostRepositoryImpl", e);
            return Collections.emptyList();
        }
    }


}
