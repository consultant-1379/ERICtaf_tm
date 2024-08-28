package com.ericsson.cifwk.tm.presentation.dto;

/**
 *
 */
public class LoginCookieCredentials {

    private String sessionId;

    private long period;

    public LoginCookieCredentials() {
    }

    public LoginCookieCredentials(String sessionId, long period) {
        this.sessionId = sessionId;
        this.period = period;
    }

    public long getPeriod() {
        return period;
    }

    public String getSessionId() {
        return sessionId;
    }
}
