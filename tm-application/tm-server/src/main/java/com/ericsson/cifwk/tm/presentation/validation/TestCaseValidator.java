package com.ericsson.cifwk.tm.presentation.validation;


import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TestCaseValidator implements ConstraintValidator<ValidTestCaseComponent, TestCaseInfo> {

    @Inject
    private TestCaseContextValidation testCaseValidation;

    @Override
    public void initialize(ValidTestCaseComponent validTestCaseComponent) {
        // no initialization needed
    }

    @Override
    public boolean isValid(TestCaseInfo testCaseInfo, ConstraintValidatorContext constraintValidatorContext) {
        return testCaseValidation.isValid(testCaseInfo);
    }
}
