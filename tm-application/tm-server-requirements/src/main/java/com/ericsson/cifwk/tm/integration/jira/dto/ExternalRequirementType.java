/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.integration.jira.dto;

import com.ericsson.cifwk.tm.common.NamedWithId;

public enum ExternalRequirementType implements NamedWithId<Integer> {

    MR(1,"MR"),
    EPIC(2, "Epic"),
    STORY(3, "Story"),
    SUBTASK(4, "Sub-task"),
    IMPROVEMENT(5, "Improvement"),
    UNKNOWN(6, "Unknown");

    private final int id;
    private final String title;

    private ExternalRequirementType(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public static ExternalRequirementType fromName(String name) {
        switch (name) {
            case "MR":
                return MR;
            case "Epic":
                return EPIC;
            case "Story":
                return STORY;
            case "Improvement":
                return IMPROVEMENT;
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
