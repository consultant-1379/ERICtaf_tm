package com.ericsson.cifwk.tm.application.services;

public enum ReportType {
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    HTML("text/html"),
    PDF("application/pdf");
    private final String mime;

    ReportType(String mime) {
        this.mime = mime;
    }

    public String getMime() {
        return mime;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
