package com.ericsson.cifwk.tm.presentation.dto;

import java.util.Date;

public class VersionModification extends Modification {

    private String version;
    private String status;

    public VersionModification() {
        // needed for json mapping
    }

    public VersionModification(String username, Date timestamp, String version, String status) {
        super(username, timestamp);
        this.version = version;
        this.status = status;
    }


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
