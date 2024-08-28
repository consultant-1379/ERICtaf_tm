package com.ericsson.cifwk.tm.presentation.validation;

import com.ericsson.cifwk.tm.domain.model.execution.TestExecutionResult;
import com.ericsson.cifwk.tm.presentation.dto.TestExecutionInfo;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TestExecutionFailValidator implements ConstraintValidator<HasDefectWhenFailed, TestExecutionInfo> {

    @Override
    public void initialize(HasDefectWhenFailed hasDefectWhenFailed) {
        // no initialization needed
    }

    @Override
    public boolean isValid(TestExecutionInfo testExecutionInfo, ConstraintValidatorContext constraintValidatorContext) {
        boolean failed = testExecutionInfo.getResult().getTitle().equalsIgnoreCase(TestExecutionResult.FAIL.getName());
        if (failed && testExecutionInfo.getDefectIds().isEmpty()) {
            return false;
        }
        return true;
    }
}
