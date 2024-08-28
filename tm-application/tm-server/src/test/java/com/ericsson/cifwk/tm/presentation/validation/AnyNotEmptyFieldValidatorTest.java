package com.ericsson.cifwk.tm.presentation.validation;

import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class AnyNotEmptyFieldValidatorTest {

    private Validator validator;

    @Before
    public void setUp() throws Exception {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidate() {
        FieldHolder nulls = new FieldHolder(null, null);
        FieldHolder nullAndEmpty = new FieldHolder(null, "");
        FieldHolder secondNotEmpty = new FieldHolder(null, "foo");
        FieldHolder firstNotNull = new FieldHolder(2, null);

        assertThat(validator.validate(nulls), hasSize(1));
        assertThat(validator.validate(secondNotEmpty), hasSize(0));
        assertThat(validator.validate(firstNotNull), hasSize(0));

        Set<ConstraintViolation<FieldHolder>> violations = validator.validate(nullAndEmpty);

        assertThat(violations, hasSize(1));
        assertThat(violations.iterator().next(), hasProperty("message",
                equalTo("At least one field out of [field1, field2] must not be null or empty")));
    }

    @AnyNotEmptyField({"field1", "field2"})
    public static class FieldHolder {
        private Integer field1;
        private String field2;
        private String field3;

        public FieldHolder(Integer field1, String field2) {
            this.field1 = field1;
            this.field2 = field2;
        }

        public Integer getField1() {
            return field1;
        }

        public String getField2() {
            return field2;
        }

        public String getField3() {
            return field3;
        }
    }
}
