package com.ericsson.cifwk.tm.presentation.validation;

import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class FixedSizeFieldTest {

    private Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidate() {
        StringWrapper wrapper = new StringWrapper();
        wrapper.setValue(generateString(255));
        Set<ConstraintViolation<StringWrapper>> violations = validator.validate(wrapper);

        assertEquals(0, violations.size());

        wrapper.setValue(generateString(300));
        violations = validator.validate(wrapper);

        assertEquals(1, violations.size());
    }

    private String generateString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(i % 10);
        }
        return sb.toString();
    }

    private static class StringWrapper {

        @FixedSizeField("value")
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

}
