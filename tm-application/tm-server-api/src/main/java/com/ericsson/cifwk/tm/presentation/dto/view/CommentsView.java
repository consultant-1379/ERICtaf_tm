package com.ericsson.cifwk.tm.presentation.dto.view;

import com.ericsson.cifwk.tm.presentation.dto.CommentsThreadInfo;

public final class CommentsView {

    private CommentsView() {
    }

    public static class Simple implements DtoView<CommentsThreadInfo> {
    }

    public static class Detailed implements DtoView<CommentsThreadInfo> {
    }

}
