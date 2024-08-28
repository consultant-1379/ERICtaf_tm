package com.ericsson.cifwk.tm.domain.model.execution;

import com.ericsson.cifwk.tm.common.NamedWithId;

public enum TestExecutionResult implements NamedWithId<Integer> {

    NOT_STARTED(1, "Not started", 5),
    PASS(2, "Pass", 1),
    PASSED_WITH_EXCEPTION(3, "Passed with exception", 2),
    FAIL(4, "Fail", 7),
    WIP(5, "Work in progress", 4),
    BLOCKED(6, "Blocked", 6),
    NA(7, "N/A", 8),
    NOT_EXECUTED(8, "Not executed", 3);

    private final Integer id;
    private final String name;
    private final Integer sortOrder;

    TestExecutionResult(int id, String name, int sortOrder) {
        this.id = id;
        this.name = name;
        this.sortOrder = sortOrder;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    @Override
    public String toString() {
        return name;
    }

}
