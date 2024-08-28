package com.ericsson.cifwk.tm.presentation.validation;

import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by ezhigci on 17/07/2017.
 */
public class TestCaseIdFormatValidatorTest {

    @Test
    public void isValid() throws Exception {
        TestCaseIdFormatValidator testCaseIdFormatValidator = new TestCaseIdFormatValidator();
        TestCaseInfo testCaseInfo = new TestCaseInfo();
        String testCaseId = "123yuio";
        testCaseInfo.setTestCaseId(testCaseId);
        boolean validTestCaseId = testCaseIdFormatValidator.isValid(testCaseInfo, null);
        assertTrue(validTestCaseId);

        testCaseInfo.setTestCaseId(null);
        validTestCaseId = testCaseIdFormatValidator.isValid(testCaseInfo, null);
        assertTrue(validTestCaseId);
    }

    @Test
    public void isInvalid() throws Exception {
        TestCaseIdFormatValidator testCaseIdFormatValidator = new TestCaseIdFormatValidator();
        TestCaseInfo testCaseInfo = new TestCaseInfo();
        String testCaseId = "12356897";
        testCaseInfo.setTestCaseId(testCaseId);
        boolean validTestCaseId = testCaseIdFormatValidator.isValid(testCaseInfo, null);

        assertFalse(validTestCaseId);
    }

    @Test
    public void isInValid_WithForwardSlash() throws Exception {
        TestCaseIdFormatValidator testCaseIdFormatValidator = new TestCaseIdFormatValidator();
        TestCaseInfo testCaseInfo = new TestCaseInfo();
        String testCaseId = "12f/ghfyuio";
        testCaseInfo.setTestCaseId(testCaseId);
        boolean validTestCaseId = testCaseIdFormatValidator.isValid(testCaseInfo, null);

        assertFalse(validTestCaseId);
    }
}