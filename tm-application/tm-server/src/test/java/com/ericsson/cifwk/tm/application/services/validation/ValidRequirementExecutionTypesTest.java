package com.ericsson.cifwk.tm.application.services.validation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by egergle on 09/03/2016.
 */
public class ValidRequirementExecutionTypesTest {

    @Test
    public void testIsValid() throws Exception {
        assertEquals(ValidRequirementExecutionTypes.isValid("improvement"), true);
        assertEquals(ValidRequirementExecutionTypes.isValid("Story"), true);
    }

    @Test
    public void testIsNotValid() throws Exception {
        assertEquals(ValidRequirementExecutionTypes.isValid("Epic"), false);
        assertEquals(ValidRequirementExecutionTypes.isValid("MR"), false);
    }
}