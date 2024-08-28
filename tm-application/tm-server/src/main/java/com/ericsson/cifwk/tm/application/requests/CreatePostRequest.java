package com.ericsson.cifwk.tm.application.requests;

import com.ericsson.cifwk.tm.presentation.dto.PostInfo;

public class CreatePostRequest {

    private final Long postThreadId;
    private final PostInfo postInfo;

    public CreatePostRequest(Long postThreadId, PostInfo postInfo) {
        this.postThreadId = postThreadId;
        this.postInfo = postInfo;
    }

    public Long getPostThreadId() {
        return postThreadId;
    }

    public PostInfo getPostInfo() {
        return postInfo;
    }
}
