package com.ericsson.cifwk.tm.presentation.validation;

import com.ericsson.cifwk.tm.presentation.dto.TestExecutionInfo;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class MultipleTestExecutionIsoValidator implements ConstraintValidator<HaveIsosIfRequired, List<TestExecutionInfo>> {

    @Inject
    private TestExecutionIsoValidation isoValidation;

    @Override
    public void initialize(HaveIsosIfRequired haveIsosIfRequired) {
        // no initialization required
    }

    @Override
    public boolean isValid(List<TestExecutionInfo> testExecutionInfos, ConstraintValidatorContext constraintValidatorContext) {
        for (TestExecutionInfo testExecutionInfo : testExecutionInfos) {
            if (!isoValidation.isValid(testExecutionInfo)) {
                return false;
            }
        }
        return true;
    }
}
