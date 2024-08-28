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

public enum TestCaseStatus implements NamedWithId<Integer> {

    APPROVED(1, "Approved"),
    PRELIMINARY(2, "Preliminary"),
    REVIEW(3, "Review"),
    REJECTED(4, "Rejected"),
    CANCELLED(5, "Cancelled");

    private final int id;
    private final String name;

    TestCaseStatus(int id, String name) {
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

    public static Optional<TestCaseStatus> getByName(String value) {
        for (TestCaseStatus testCaseStatus : TestCaseStatus.class.getEnumConstants()) {
            if (testCaseStatus.getName().equalsIgnoreCase(value)) {
                return Optional.of(testCaseStatus);
            }
        }
        return Optional.empty();
    }
}
