package com.ericsson.cifwk.tm.domain.model.shared;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AuditedEntityTest {

    private AuditedEntity entity;

    @Before
    public void setUp() {
        entity = new AuditedEntity();
    }

    @Test
    public void delete() {
        assertFalse(entity.isDeleted());
        entity.delete();
        assertTrue(entity.isDeleted());
    }

}
