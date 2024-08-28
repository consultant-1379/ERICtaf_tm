/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.table;

public enum TestCasesColumn implements TableColumn {

    TEST_CASE_ID("Test Case ID", "testCaseId"),
    TITLE("Title", "title"),
    DESCRIPTION("Description", "description"),
    REQUIREMENT_IDS("Requirement IDs", "requirements");

    private final String title;
    private final String value;

    private TestCasesColumn(String title, String value) {
        this.title = title;
        this.value = value;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getValue() {
        return value;
    }

}
