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

public enum ReviewType implements NamedWithId<Integer> {

    MINOR(1, "Minor"),
    MAJOR(2, "Major"),
    UNKNOWN(3, "Unknown");

    private final int id;
    private final String name;

    ReviewType(int id, String name) {
        this.id = id;
        this.name = name;
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

    public static Optional<ReviewType> getEnum(String value) {
        for (ReviewType reviewType : ReviewType.class.getEnumConstants()) {
            if (reviewType.getName().equalsIgnoreCase(value)) {
                return Optional.of(reviewType);
            }
        }
        return Optional.empty();
    }
}
