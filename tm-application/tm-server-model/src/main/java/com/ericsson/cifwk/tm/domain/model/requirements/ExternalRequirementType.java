/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.model.requirements;

import com.ericsson.cifwk.tm.common.NamedWithId;

public enum ExternalRequirementType implements NamedWithId<Integer> {

    EPIC(1, "Epic"),
    STORY(2, "Story"),
    SUBTASK(3, "Sub-task"),
    UNKNOWN(4, "Unknown");

    private final int id;
    private final String title;

    private ExternalRequirementType(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public static ExternalRequirementType fromName(String name) {
        switch (name) {
            case "Epic":
                return EPIC;
            case "Story":
                return STORY;
            case "Sub-task":
                return SUBTASK;
            default:
                return UNKNOWN;
        }
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public String getName() {
        return title;
    }
}
