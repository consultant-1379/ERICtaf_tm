package com.ericsson.cifwk.tm.presentation.dto;

import java.util.Date;

/**
 *
 */
public class Modification {

    private String username;
    private Date timestamp;

    public Modification() {
    }

    public Modification(String username, Date timestamp) {
        this.username = username;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
