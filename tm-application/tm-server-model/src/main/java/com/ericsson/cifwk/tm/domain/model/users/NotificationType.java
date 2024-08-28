/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.model.users;

import com.ericsson.cifwk.tm.common.NamedWithId;

public enum NotificationType implements NamedWithId<Integer> {

    INFO(1, "Info"),
    SUCCESS(2, "Success"),
    WARNING(3, "Warning"),
    INVALID(4, "Invalid"),
    ERROR(5, "Error");

    private final int id;
    private final String name;

    NotificationType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
