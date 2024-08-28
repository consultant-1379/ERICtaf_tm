package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.domain.model.posts.Post;
import com.ericsson.cifwk.tm.domain.model.posts.PostObjectNameReference;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.infrastructure.mapping.UserMapper;
import com.ericsson.cifwk.tm.presentation.BaseControllerLevelTest;
import com.ericsson.cifwk.tm.presentation.dto.CommentsThreadInfo;
import com.ericsson.cifwk.tm.presentation.dto.PostInfo;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static com.ericsson.cifwk.tm.domain.model.posts.PostObjectNameReference.TEST_CASE;
import static com.ericsson.cifwk.tm.test.ResponseAsserts.assertStatus;
import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.buildPost;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class PostControllerTest extends BaseControllerLevelTest {

    private final String URL_ROOT = "/tm-server/api/";
    private final String URL_FIND_POSTS = "test-cases/{objectId}/comments";
    private final String URL_CREATE_POST = "test-cases/{objectId}/comments";
    private final String URL_DELETE_POST = "test-cases/{objectId}/comments/{id}";

    private static final GenericType<PostInfo> POST_INFO_ITEM =
            new GenericType<PostInfo>() {
            };
    private static final GenericType<CommentsThreadInfo> COMMENTS_THREAD_INFO =
            new GenericType<CommentsThreadInfo>() {
            };

    //static incremental - to avoid unique constraint exception.
    private static AtomicLong testObjectIdIncr = new AtomicLong(101010L);

    private final String TEST_MESSAGE_01 = "test_message_01";

    @Before
    public void setUp() {
    }

    @Test
    public void testGetPosts_OnePost() {
        Post post = buildPost()
                .withObjectId(getNewObjectId())
                .withUser(getUser())
                .withMessage(TEST_MESSAGE_01)
                .withObjectNameReference(TEST_CASE).build();

        post = fixture().persistPost(post);

        Response response = buildFindPostsRequest(post.getObjectId())
                .queryParam("view", "detailed")
                .request()
                .get();
        assertStatus(response, Response.Status.OK);

        CommentsThreadInfo commentsInfo = response.readEntity(COMMENTS_THREAD_INFO);
        response.close();
        assertNotNull(commentsInfo);

        List<PostInfo> postsInfo = commentsInfo.getPosts();
        assertThat(postsInfo.size(), equalTo(1));

        PostInfo result = postsInfo.get(0);
        assertThat(result.getObjectId(), equalTo(post.getObjectId()));
        assertThat(Long.parseLong(result.getObjectNameReference().getId()), equalTo(post.getObjectNameReference()));
        assertThat(result.getMessage(), equalTo(post.getMessage()));
    }


    @Test
    public void testGetPosts_NoPosts() {
        Post post = buildPost()
                .withObjectId(getNewObjectId())
                .withUser(getUser())
                .withMessage(TEST_MESSAGE_01)
                .withObjectNameReference(TEST_CASE).build();

        post = fixture().persistPost(post);

        Response response = buildFindPostsRequest(post.getObjectId() + 1)
                .request()
                .get();
        assertStatus(response, Response.Status.OK);

        CommentsThreadInfo commentsInfo = response.readEntity(COMMENTS_THREAD_INFO);
        response.close();
        assertNotNull(commentsInfo);
        assertThat(commentsInfo.getTotalCommentsCount(), equalTo(0L));

        List<PostInfo> postsInfo = commentsInfo.getPosts();
        assertThat(postsInfo.size(), equalTo(0));
    }


    @Test
    public void testCreatePost_Create() {
        Long objectId = getNewObjectId();
        PostInfo postInfo = new PostInfo();
        UserMapper userMapper = new UserMapper();
        postInfo.setUser(userMapper.mapEntity(getUser(), new UserInfo()));
        postInfo.setMessage(TEST_MESSAGE_01);

        Entity<PostInfo> body = Entity.entity(postInfo, MediaType.APPLICATION_JSON);
        Response response = buildCreatePostsRequest(objectId)
                .queryParam("postInfo", postInfo)
                .request()
                .post(body);
        PostInfo createdPost = response.readEntity(POST_INFO_ITEM);
        response.close();
        assertNotNull(createdPost.getCreatedAt());
        assertThat(createdPost.getObjectId(), equalTo(objectId));
        assertThat(createdPost.getUser().getId(), equalTo(postInfo.getUser().getId()));
        assertThat(createdPost.getMessage(), equalTo(postInfo.getMessage()));
        assertThat(createdPost.getObjectNameReference().getId(),
                equalTo(Long.toString(PostObjectNameReference.TEST_CASE.getValue())));
    }


    @Test
    public void testCreatePost_WrongCreateWithEmptyMessage() {
        Long objectId = getNewObjectId();
        PostInfo postInfo = new PostInfo();
        UserMapper userMapper = new UserMapper();
        postInfo.setUser(userMapper.mapEntity(getUser(), new UserInfo()));
        postInfo.setMessage("");

        Entity<PostInfo> body = Entity.entity(postInfo, MediaType.APPLICATION_JSON);
        Response response = buildCreatePostsRequest(objectId)
                .queryParam("postInfo", postInfo)
                .request()
                .post(body);
        PostInfo createdPost = response.readEntity(POST_INFO_ITEM);
        response.close();
        assertStatus(response, Response.Status.BAD_REQUEST);

    }


    @Test
    public void testDeletePost_MarkAsDeleted() {
        Post post = buildPost()
                .withObjectId(getNewObjectId())
                .withUser(getUser())
                .withMessage(TEST_MESSAGE_01)
                .withObjectNameReference(TEST_CASE).build();

        post = fixture().persistPost(post);

        Response response = buildDeletePostsRequest(post.getObjectId(), post.getId())
                .request()
                .delete();
        assertStatus(response, Response.Status.NO_CONTENT);
        response.close();

        //To check result used REST
        Response responseCheckResult = buildFindPostsRequest(post.getObjectId())
                .queryParam("view", "detailed")
                .request()
                .get();
        assertStatus(responseCheckResult, Response.Status.OK);

        CommentsThreadInfo commentsInfo = responseCheckResult.readEntity(COMMENTS_THREAD_INFO);
        responseCheckResult.close();
        assertNotNull(commentsInfo);
        assertThat(commentsInfo.getTotalCommentsCount(), equalTo(0L));

        List<PostInfo> postsInfo = commentsInfo.getPosts();
        assertThat(postsInfo.size(), equalTo(0));

    }

    @Test
    public void testGetStatisticsThread_GetExistingThread() {
        Long objectId = getNewObjectId();
        Post post1 = buildPost()
                .withObjectId(objectId)
                .withUser(getUser())
                .withMessage(TEST_MESSAGE_01)
                .withObjectNameReference(TEST_CASE).build();
        post1 = fixture().persistPost(post1);

        Post post2 = buildPost()
                .withObjectId(objectId)
                .withUser(getUser())
                .withMessage(TEST_MESSAGE_01)
                .withObjectNameReference(TEST_CASE).build();
        post2 = fixture().persistPost(post2);

        Post post3 = buildPost()
                .withObjectId(objectId)
                .withUser(getUser())
                .withMessage(TEST_MESSAGE_01)
                .withDeleted(true)
                .withObjectNameReference(TEST_CASE).build();
        post3 = fixture().persistPost(post3);

        Response response = buildFindPostsRequest(objectId)
                .request()
                .get();
        assertStatus(response, Response.Status.OK);


        CommentsThreadInfo commentsInfo = response.readEntity(COMMENTS_THREAD_INFO);
        response.close();
        assertNotNull(commentsInfo);
        assertThat(commentsInfo.getTotalCommentsCount(), equalTo(2L));

    }

    private WebTarget buildFindPostsRequest(Long objectId) {
        return app.client().path(
                URL_ROOT + URL_FIND_POSTS.replace("{objectId}", Long.toString(objectId))
        );
    }

    private WebTarget buildCreatePostsRequest(Long objectId) {
        return app.client().path(
                URL_ROOT + URL_CREATE_POST.replace("{objectId}", Long.toString(objectId))
        );
    }

    private WebTarget buildDeletePostsRequest(Long objectId, Long postId) {
        String path = URL_ROOT + URL_DELETE_POST;
        path = path.replace("{objectId}", Long.toString(objectId));
        path = path.replace("{id}", Long.toString(postId));
        return app.client().path(path);
    }

    private User getUser() {
        User user = app.persistence().em()
                .createQuery("SELECT u from User u WHERE u.externalId='taf'", User.class)
                .getResultList()
                .iterator()
                .next();
        return user;
    }

    private long getNewObjectId() {
        return testObjectIdIncr.incrementAndGet();
    }
}
