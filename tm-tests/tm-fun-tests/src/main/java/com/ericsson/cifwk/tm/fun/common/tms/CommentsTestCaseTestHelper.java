package com.ericsson.cifwk.tm.fun.common.tms;

import com.ericsson.cifwk.tm.presentation.dto.PostInfo;

import java.util.List;

/**
 * Created by egergle on 04/01/2017.
 */
public class CommentsTestCaseTestHelper {

    private CommentsTestCaseTestHelper() {
        //empty
    }

    public static boolean commentsContains(List<PostInfo> comments, PostInfo comment) {
        for (PostInfo item : comments) {
            if (item.getId() != null && comment.getId()!= null) {
                if (item.getId().equals(comment.getId())) {
                    return true;
                }
            } else {
                if (item.getMessage().equals(comment.getMessage())) {
                    return true;
                }
            }

        }
        return false;
    }

}
