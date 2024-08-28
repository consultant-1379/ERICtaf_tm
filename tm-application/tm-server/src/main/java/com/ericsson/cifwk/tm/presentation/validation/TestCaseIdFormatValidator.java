package com.ericsson.cifwk.tm.presentation.validation;

import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class TestCaseIdFormatValidator implements ConstraintValidator<CorrectlyFormattedTestCaseId, TestCaseInfo> {

    @Override
    public void initialize(CorrectlyFormattedTestCaseId correctlyFormattedTestCaseId) {
        // no initialization necessary
    }

    @Override
    public boolean isValid(TestCaseInfo testCaseInfo, ConstraintValidatorContext constraintValidatorContext) {
        String testcaseId = testCaseInfo.getTestCaseId();
        if (testcaseId == null) {
            return true;
        }
        if (testcaseId.contains("/")) {
            return false;
        }
        if (Pattern.matches("^[0-9]+$", testcaseId)) {
            return false;
        }
        return true;
    }
}
