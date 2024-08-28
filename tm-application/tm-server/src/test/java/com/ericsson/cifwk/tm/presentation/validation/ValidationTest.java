package com.ericsson.cifwk.tm.presentation.validation;

import com.ericsson.cifwk.tm.common.Identifiable;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ValidationTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void hasId() {
        Set<ConstraintViolation<IdHolder>> violations = validator.validate(new IdHolder(1L));
        assertEquals(0, violations.size());

        violations = validator.validate(new IdHolder(null));
        assertEquals(1, violations.size());
        assertEquals("Field 'id' must not be null", violations.iterator().next().getMessage());
    }

    @HasId
    private static class IdHolder implements Identifiable<Long> {

        private Long id;

        private IdHolder(Long id) {
            this.id = id;
        }

        @Override
        public Long getId() {
            return id;
        }
    }

}
