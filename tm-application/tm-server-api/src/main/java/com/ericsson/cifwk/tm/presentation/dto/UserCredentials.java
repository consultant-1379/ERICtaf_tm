package com.ericsson.cifwk.tm.presentation.dto;

import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 */
public class UserCredentials {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    private Boolean storeSession;

    private String sessionId;

    public UserCredentials() {
    }

    public UserCredentials(String username, String password) {
        this.username = username;
        this.password = password;
        storeSession = false;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getStoreSession() {
        return storeSession;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setStoreSession(Boolean storeSession) {
        this.storeSession = storeSession;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
