package com.ericsson.cifwk.tm.application.services.validation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by egergle on 09/03/2016.
 */
public class ValidRequirementTestCaseTypesTest {

    @Test
    public void testIsValid() throws Exception {
        assertEquals(ValidRequirementTestCaseTypes.isValid("Epic"), true);
        assertEquals(ValidRequirementTestCaseTypes.isValid("Story"), true);
        assertEquals(ValidRequirementTestCaseTypes.isValid("Improvement"), true);
        assertEquals(ValidRequirementTestCaseTypes.isValid("MR"), true);
    }

    @Test
    public void testIsNotValid() throws Exception {
        assertEquals(ValidRequirementTestCaseTypes.isValid("Support"), false);
        assertEquals(ValidRequirementTestCaseTypes.isValid("Bug"), false);
    }
}