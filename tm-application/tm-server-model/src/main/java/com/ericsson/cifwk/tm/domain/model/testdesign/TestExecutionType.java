package com.ericsson.cifwk.tm.domain.model.testdesign;

import com.ericsson.cifwk.tm.common.NamedWithId;

import java.util.Optional;

/**
 *
 */
public enum TestExecutionType implements NamedWithId<Integer> {

    MANUAL(1, "Manual"),
    AUTOMATED(2, "Automated");

    private final int id;
    private final String name;

    TestExecutionType(int id, String name) {
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

    public static Optional<TestExecutionType> getEnum(String value) {
        for (TestExecutionType testExecutionType : TestExecutionType.class.getEnumConstants()) {
            if (testExecutionType.getName().equalsIgnoreCase(value)) {
                return Optional.of(testExecutionType);
            }
        }
        return Optional.empty();
    }
}
