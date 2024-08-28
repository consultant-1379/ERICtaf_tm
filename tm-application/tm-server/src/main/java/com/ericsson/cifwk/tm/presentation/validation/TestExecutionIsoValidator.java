package com.ericsson.cifwk.tm.presentation.validation;

import com.ericsson.cifwk.tm.presentation.dto.TestExecutionInfo;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TestExecutionIsoValidator implements ConstraintValidator<HasIsoIfRequired, TestExecutionInfo> {

    @Inject
    private TestExecutionIsoValidation isoValidation;

    @Override
    public void initialize(HasIsoIfRequired hasIsoIfRequired) {
        // no initialization needed
    }

    @Override
    public boolean isValid(TestExecutionInfo testExecutionInfo, ConstraintValidatorContext constraintValidatorContext) {
        return isoValidation.isValid(testExecutionInfo);
    }
}
