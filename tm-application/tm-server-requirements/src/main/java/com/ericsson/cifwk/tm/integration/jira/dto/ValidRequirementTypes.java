package com.ericsson.cifwk.tm.integration.jira.dto;
/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

import com.ericsson.cifwk.tm.common.HasName;
import com.google.common.base.Joiner;

public enum ValidRequirementTypes implements HasName {
    STORY("Story"), EPIC("Epic"), MR("MR"), IMPROVEMENT("Improvement");
    private String name;

    private ValidRequirementTypes(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public static String toJqlString() {
        StringBuilder builder = new StringBuilder("issuetype = ");
        return Joiner.on(" or issuetype = ").appendTo(builder, values()).toString();
    }

    public static boolean isValid(String type) {
        for (ValidRequirementTypes validRequirementType : values()) {
            if (validRequirementType.getName().equalsIgnoreCase(type)) {
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
