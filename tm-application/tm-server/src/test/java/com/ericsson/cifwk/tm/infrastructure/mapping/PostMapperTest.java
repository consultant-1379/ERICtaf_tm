package com.ericsson.cifwk.tm.infrastructure.mapping;

import com.ericsson.cifwk.tm.domain.model.posts.Post;
import com.ericsson.cifwk.tm.domain.model.posts.PostObjectNameReference;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.presentation.dto.PostInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class PostMapperTest {

    private final String TEST_MESSAGE_01 = "Test message 1";

    private PostMapper mapper;

    @Before
    public void setUp() {
        mapper = new PostMapper();
    }

    @Test
    public void testMapEntity_WithoutObjectName() throws Exception {
        User user = new User("id");

        Post entity = new Post();
        entity.setId(10101L);
        entity.setCreatedAt(Calendar.getInstance().getTime());
        entity.setDeleted(true);
        entity.setDeletedAt(Calendar.getInstance().getTime());
        entity.setMessage(TEST_MESSAGE_01);
        entity.setUser(user);

        PostInfo postInfo = mapper.mapEntity(entity, new PostInfo());

        assertThat(entity.getId(), equalTo(postInfo.getId()));
        assertThat(entity.getCreatedAt(), equalTo(postInfo.getCreatedAt()));
        assertThat(entity.getDeletedAt(), equalTo(postInfo.getDeletedAt()));
        assertThat(entity.isDeleted(), equalTo(postInfo.isDeleted()));
        assertThat(entity.getMessage(), equalTo(postInfo.getMessage()));
    }

    @Test
    public void testMapEntity_WithObjectName() throws Exception {
        User user = new User("id");

        Post entity = new Post();
        entity.setId(10101L);
        entity.setCreatedAt(Calendar.getInstance().getTime());
        entity.setDeleted(true);
        entity.setDeletedAt(Calendar.getInstance().getTime());
        entity.setMessage(TEST_MESSAGE_01);
        entity.setUser(user);
        entity.setObjectNameReference(PostObjectNameReference.TEST_CASE.getValue());

        PostInfo postInfo = mapper.mapEntity(entity, new PostInfo());

        assertThat(entity.getId(), equalTo(postInfo.getId()));
        assertThat(entity.getCreatedAt(), equalTo(postInfo.getCreatedAt()));
        assertThat(entity.getDeletedAt(), equalTo(postInfo.getDeletedAt()));
        assertThat(entity.isDeleted(), equalTo(postInfo.isDeleted()));
        assertThat(entity.getMessage(), equalTo(postInfo.getMessage()));
        assertThat(entity.getObjectNameReference().toString(), equalTo(postInfo.getObjectNameReference().getId()));
    }

    @Test
    public void testDto_WithoutUser() throws Exception {
        ReferenceDataItem dataItem = new ReferenceDataItem(
                Long.toString(PostObjectNameReference.TEST_CASE.getValue()),
                PostObjectNameReference.TEST_CASE.getKey());

        PostInfo dto = new PostInfo();
        dto.setId(10101L);
        dto.setCreatedAt(Calendar.getInstance().getTime());
        dto.setDeleted(true);
        dto.setDeletedAt(Calendar.getInstance().getTime());
        dto.setMessage(TEST_MESSAGE_01);
        dto.setObjectNameReference(dataItem);

        Post post = mapper.mapDto(dto, new Post());

        assertThat(dto.getId(), equalTo(post.getId()));
        assertThat(dto.getCreatedAt(), equalTo(post.getCreatedAt()));
        assertThat(dto.getDeletedAt(), equalTo(post.getDeletedAt()));
        assertThat(dto.isDeleted(), equalTo(post.isDeleted()));
        assertThat(dto.getMessage(), equalTo(post.getMessage()));
        assertThat(dto.getObjectNameReference().getId(), equalTo(post.getObjectNameReference().toString()));
    }

}
