package com.ericsson.cifwk.tm.presentation.dto;

import java.util.Map;

/**
 *
 */
public class AuthenticationStatus {

    private boolean authenticated;
    private Map<String, Boolean> features;
    private String userId;

    public AuthenticationStatus() {
    }

    public AuthenticationStatus(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setFeatures(Map<String, Boolean> features) {
        this.features = features;
    }

    public Map<String, Boolean> getFeatures() {
        return features;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
