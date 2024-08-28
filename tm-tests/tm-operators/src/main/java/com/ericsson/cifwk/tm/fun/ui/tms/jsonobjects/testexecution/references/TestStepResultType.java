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

public enum TestStepResultType implements Reference {

    PASS("1", "Pass"),
    FAIL("2", "Fail");

    private final String id;
    private final String title;

    TestStepResultType(String id, String title) {
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
        for (TestStepResultType testStepResultType: TestStepResultType.class.getEnumConstants()) {
            if (testStepResultType.getTitle().equals(value)) {
                return new ReferenceDataItem(testStepResultType.getId(), testStepResultType.getTitle());
            }
        }
        return null;
    }

}
