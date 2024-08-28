package com.ericsson.cifwk.tm.domain.model.testdesign;

import com.ericsson.cifwk.tm.common.NamedWithId;

public enum AutomationCandidate implements NamedWithId<Integer> {

    YES(1, "Yes"),
    NO(2, "No");

    private final int id;
    private final String name;

    AutomationCandidate(int id, String name) {
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
