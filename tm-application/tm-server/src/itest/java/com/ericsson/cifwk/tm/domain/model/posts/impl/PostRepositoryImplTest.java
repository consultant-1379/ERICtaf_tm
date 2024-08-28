package com.ericsson.cifwk.tm.domain.model.posts.impl;

import com.ericsson.cifwk.tm.application.BaseServiceLayerTest;
import com.ericsson.cifwk.tm.domain.model.posts.Post;
import com.ericsson.cifwk.tm.domain.model.posts.PostObjectNameReference;
import com.ericsson.cifwk.tm.domain.model.posts.PostRepository;
import com.ericsson.cifwk.tm.domain.model.users.User;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static com.ericsson.cifwk.tm.domain.model.posts.PostObjectNameReference.TEST_CASE;
import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.buildPost;
import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.buildUser;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class PostRepositoryImplTest extends BaseServiceLayerTest {

    @Inject
    PostRepository postRepository;

    private static AtomicLong OBJECT_ID = new AtomicLong(100L);
    private final String MESSAGE_1 = "test_message_001";
    private final String MESSAGE_2 = "test_message_002";

    @Before
    public void setUp() {
    }

    @Test
    public void testFind() {
        long objectId = getNextObjectId();
        Post defaultPost = generatePost(objectId, getUser(), TEST_CASE);

        defaultPost.setMessage(MESSAGE_1);
        Post post = createPost(defaultPost);

        Post result = postRepository.find(post.getId());

        assertThat(result, notNullValue());
        assertEquals(post.getMessage(), result.getMessage());
        assertEquals(post.getCreatedAt(), result.getCreatedAt());
        assertEquals(post.isDeleted(), result.isDeleted());
        assertEquals(post.getUser().getId(), result.getUser().getId());
    }


    @Test
    public void testFindPosts() {
        long objectId = getNextObjectId();
        Post defaultPost = generatePost(objectId, getUser(), TEST_CASE);

        defaultPost.setMessage(MESSAGE_1);
        Post post = createPost(defaultPost);

        List<Post> result = postRepository.findPosts(post.getObjectId(), TEST_CASE);

        assertThat(result, notNullValue());
        assertEquals(1, result.size());
        Post resultPost = result.get(0);
        assertEquals(post.getMessage(), resultPost.getMessage());
        assertEquals(post.getCreatedAt(), resultPost.getCreatedAt());
        assertEquals(post.isDeleted(), resultPost.isDeleted());
        assertEquals(post.getUser().getId(), resultPost.getUser().getId());
    }

    @Test
    public void testFindPosts_ExceptDeleted() {
        long objectId = getNextObjectId();

        Post defaultPost1 = generatePost(objectId, getUser(), TEST_CASE);
        defaultPost1.setMessage(MESSAGE_1);
        Post post1 = createPost(defaultPost1);

        Post defaultPost2 = generatePost(objectId, getUser(), TEST_CASE);
        defaultPost2.setMessage(MESSAGE_1);
        defaultPost2.delete();
        Post post2 = createPost(defaultPost2);

        List<Post> result = postRepository.findPosts(objectId, TEST_CASE);

        assertThat(result, notNullValue());
        assertEquals(1, result.size());
        Post resultPost = result.get(0);
        assertEquals(post1.getMessage(), resultPost.getMessage());
        assertEquals(post1.getCreatedAt(), resultPost.getCreatedAt());
        assertEquals(post1.isDeleted(), resultPost.isDeleted());
        assertEquals(post1.getUser().getId(), resultPost.getUser().getId());
    }


    private Post generatePost(
            long objectId,
            User user,
            PostObjectNameReference objectReference) {

        Post postThread = buildPost()
                .withObjectId(objectId)
                .withUser(user)
                .withObjectNameReference(objectReference).build();

        postThread = fixture().persistPost(postThread);
        return postThread;
    }

    private Post createPost(Post post) {
        Post newPost = fixture().persistPost(post);
        return newPost;
    }

    private User getUser() {
        return buildUser().build();
    }

    private long getNextObjectId() {
        return OBJECT_ID.getAndIncrement();
    }
}
