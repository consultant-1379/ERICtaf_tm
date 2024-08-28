package com.ericsson.cifwk.tm.application.requests;

/**
 * @author ebuzdmi
 */
public final class GenerateReportRequest {

    private long testPlanId;
    private String query;
    private String extension;
    private Class requestView;
    private Class requestTypeView;

    public GenerateReportRequest(String query, String extension, Class view, Class requestTypeView, long testPlanId) {
        this.testPlanId = testPlanId;
        this.query = query;
        this.extension = extension;
        this.requestView = view;
        this.requestTypeView = requestTypeView;
    }

    public long getTestPlanId() {
        return testPlanId;
    }

    public String getQuery() {
        return query;
    }

    public String getExtension() {
        return extension;
    }

    public Class getRequestView() {
        return requestView;
    }

    public Class getRequestTypeView() {
        return requestTypeView;
    }
}
