package com.ericsson.cifwk.tm.presentation.dto;

import com.ericsson.cifwk.tm.presentation.dto.view.CommentsView;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.ArrayList;
import java.util.List;

public class CommentsThreadInfo {

    private Long objectId;
    private Long totalCommentsCount;

    @JsonView(CommentsView.Detailed.class)
    private List<PostInfo> posts = new ArrayList();

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public List<PostInfo> getPosts() {
        return posts;
    }

    public void setPosts(List<PostInfo> posts) {
        this.posts = posts;
    }

    public Long getTotalCommentsCount() {
        return totalCommentsCount;
    }

    public void setTotalCommentsCount(Long totalPostsCount) {
        this.totalCommentsCount = totalPostsCount;
    }
}
