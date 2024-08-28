package com.ericsson.cifwk.tm.presentation.dto;

import com.ericsson.cifwk.tm.common.Identifiable;

public class TestExecutionResultInfo extends ReferenceDataItem implements Identifiable<String> {

    private int sortOrder;

    public TestExecutionResultInfo() {
    }

    public TestExecutionResultInfo(String id, String title, int sortOrder) {
        super(id, title);
        this.sortOrder = sortOrder;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

}
