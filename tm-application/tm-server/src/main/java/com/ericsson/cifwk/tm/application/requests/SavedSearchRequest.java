package com.ericsson.cifwk.tm.application.requests;

import com.ericsson.cifwk.tm.presentation.dto.SavedSearchInfo;

/**
 * @author egergle
 */
public final class SavedSearchRequest {

    private String userId;
    private SavedSearchInfo savedSearchInfo;

    public SavedSearchRequest(String userId, SavedSearchInfo savedSearchInfo) {
        this.userId = userId;
        this.savedSearchInfo = savedSearchInfo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public SavedSearchInfo getSavedSearchInfo() {
        return savedSearchInfo;
    }

    public void setSavedSearchInfo(SavedSearchInfo savedSearchInfo) {
        this.savedSearchInfo = savedSearchInfo;
    }
}
