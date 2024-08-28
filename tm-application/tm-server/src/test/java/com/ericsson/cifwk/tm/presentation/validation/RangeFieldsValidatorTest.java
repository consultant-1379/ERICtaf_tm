/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.validation;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class RangeFieldsValidatorTest {

    private static Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidate() {
        DateTime now = DateTime.now();
        RangeHolder valid = new RangeHolder(now.toDate(), now.plusHours(1).toDate());
        RangeHolder equal = new RangeHolder(now.toDate(), now.toDate());
        RangeHolder reverse = new RangeHolder(now.toDate(), now.minusHours(1).toDate());

        assertEquals(0, validator.validate(valid).size());
        assertEquals(1, validator.validate(equal).size());

        Set<ConstraintViolation<RangeHolder>> violations = validator.validate(reverse);

        assertEquals(1, violations.size());
        assertEquals("Invalid range from 'start' to 'end'", violations.iterator().next().getMessage());
    }

    @RangeFields(from = "start", to = "end")
    public static class RangeHolder {
        private Date start;
        private Date end;

        public RangeHolder(Date start, Date end) {
            this.start = start;
            this.end = end;
        }

        public Date getStart() {
            return start;
        }

        public Date getEnd() {
            return end;
        }
    }

}
