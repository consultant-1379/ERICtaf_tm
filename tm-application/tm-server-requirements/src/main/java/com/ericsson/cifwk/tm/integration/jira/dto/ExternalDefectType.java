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

public enum ExternalDefectType implements NamedWithId<Integer> {

    UNKNOWN(0, "Unknown"),
    BUG(1, "Bug"),
    TR(2, "TR"),
    SUPPORT(3, "Support");

    private final int id;
    private final String name;

    private ExternalDefectType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ExternalDefectType fromName(String name) {
        if (name == null) {
            return UNKNOWN;
        }

        switch (name.toLowerCase()) {
            case "bug":
                return BUG;
            case "tr":
                return TR;
            case "support":
                return SUPPORT;
            default:
                return UNKNOWN;
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
