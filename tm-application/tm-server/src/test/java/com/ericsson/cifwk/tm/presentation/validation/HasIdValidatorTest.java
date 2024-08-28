package com.ericsson.cifwk.tm.presentation.validation;

import com.ericsson.cifwk.tm.common.Identifiable;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HasIdValidatorTest {

    private HasIdValidator validator;

    @Before
    public void setUp() {
        validator = new HasIdValidator();
    }

    @Test
    public void isValid() {
        assertTrue(validator.isValid(new Parent(), null));
        assertTrue(validator.isValid(new Child(), null));
        assertFalse(validator.isValid(new NonIdentifiable(), null));
        assertFalse(validator.isValid(new Object(), null));
    }

    private static class Parent implements Identifiable<Long> {
        private Long id = 1L;

        @Override
        public Long getId() {
            return id;
        }
    }

    private static class Child extends Parent {
    }

    private static class NonIdentifiable {
        private Long id = 1L;
    }

}
