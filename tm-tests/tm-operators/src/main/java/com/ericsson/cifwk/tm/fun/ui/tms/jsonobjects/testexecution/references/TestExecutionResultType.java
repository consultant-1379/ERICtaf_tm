/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.testexecution.references;

import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.references.Reference;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;

public enum TestExecutionResultType implements Reference {

    NOT_STARTED("1", "Not started"),
    PASS("2", "Pass"),
    PASSED_WITH_EXCEPTION("3", "Passed with exception"),
    FAIL("4", "Fail"),
    WIP("5", "Work in progress"),
    BLOCKED("6", "Blocked");

    private final String id;
    private final String title;

    TestExecutionResultType(String id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public static ReferenceDataItem getEnum(String value) {
        for (TestExecutionResultType testExecutionResultType: TestExecutionResultType.class.getEnumConstants()) {
            if (testExecutionResultType.getTitle().equals(value)) {
                return new ReferenceDataItem(testExecutionResultType.getId(), testExecutionResultType.getTitle());
            }
        }
        return null;
    }

}
