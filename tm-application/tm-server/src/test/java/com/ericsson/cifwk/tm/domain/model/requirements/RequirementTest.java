package com.ericsson.cifwk.tm.domain.model.requirements;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class RequirementTest {

    private Requirement requirement;

    @Before
    public void setUp() {
        requirement = new Requirement();
    }

    @Test
    public void getRootParent() {
        assertNotNull(requirement.getRootParent());

        Requirement parent = new Requirement();
        requirement.setParent(parent);
        assertSame(parent, requirement.getRootParent());

        Requirement root = new Requirement();
        parent.setParent(root);
        assertSame(root, requirement.getRootParent());
    }

}
