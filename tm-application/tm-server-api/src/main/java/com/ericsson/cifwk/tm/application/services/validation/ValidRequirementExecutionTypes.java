package com.ericsson.cifwk.tm.application.services.validation;
/*
 * COPYRIGHT Ericsson (c) 2016.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

import com.ericsson.cifwk.tm.common.HasName;

public enum ValidRequirementExecutionTypes implements HasName {
    STORY("Story"), IMPROVEMENT("Improvement");
    private String name;

    ValidRequirementExecutionTypes(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public static boolean isValid(String type) {
        for (ValidRequirementExecutionTypes validType : values()) {
            if (validType.getName().equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return getName();
    }
}
