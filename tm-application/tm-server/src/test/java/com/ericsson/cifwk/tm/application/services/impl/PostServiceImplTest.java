package com.ericsson.cifwk.tm.application.services.impl;

import com.ericsson.cifwk.tm.domain.model.posts.Post;
import com.ericsson.cifwk.tm.domain.model.posts.PostObjectNameReference;
import com.ericsson.cifwk.tm.domain.model.posts.PostRepository;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static com.ericsson.cifwk.tm.domain.model.posts.PostObjectNameReference.TEST_CASE;
import static com.ericsson.cifwk.tm.domain.model.posts.PostObjectNameReference.UNKNOWN;
import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.buildPost;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostServiceImpl postThreadService = new PostServiceImpl();

    @Test
    public void testFindPosts_WithNoResults() {
        assertTrue(postThreadService.findPosts(-1L, UNKNOWN).isEmpty());
    }

    @Test
    public void testFindPosts_ReturnSinglePost() {
        User user = new User("userId");
        Long objectId = 101L;
        PostObjectNameReference screenRef = TEST_CASE;
        Post post = createPost(objectId, TEST_CASE, user);

        when(postRepository.findPosts(objectId, screenRef))
                .thenReturn(Lists.newArrayList(post));

        List<Post> posts = postRepository.findPosts(objectId, screenRef);


        assertThat(posts.size(), is(1));
        assertThat(posts, hasItem(post));
    }


    private Post createPost(
            Long objectId,
            PostObjectNameReference nameReference,
            User user) {
        return buildPost(objectId, user, nameReference).build();
    }

}
