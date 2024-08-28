/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.model.testdesign;

import com.ericsson.cifwk.tm.common.NamedWithId;

import java.util.Optional;

public enum Priority implements NamedWithId<Integer> {

    BLOCKER(1, "Blocker"),
    NORMAL(2, "Normal"),
    MINOR(3, "Minor");

    private final int id;
    private final String name;

    Priority(int id, String name) {
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

    public static Optional<Priority> getEnum(String value) {
        for (Priority priority : Priority.class.getEnumConstants()) {
            if (priority.getName().equalsIgnoreCase(value)) {
                return Optional.of(priority);
            }
        }
        return Optional.empty();
    }

}
