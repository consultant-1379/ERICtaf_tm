package com.ericsson.cifwk.tm.presentation.validation;

import com.ericsson.cifwk.tm.domain.model.execution.TestExecutionResult;
import com.ericsson.cifwk.tm.presentation.dto.TestExecutionInfo;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class MultipleTestExecutionFailValidator implements ConstraintValidator<HasDefectWhenFailedMultiple, List<TestExecutionInfo>> {

    @Override
    public void initialize(HasDefectWhenFailedMultiple hasDefectWhenFailedMultiple) {
        // no initialization needed
    }

    @Override
    public boolean isValid(List<TestExecutionInfo> testExecutionInfos, ConstraintValidatorContext constraintValidatorContext) {
        for (TestExecutionInfo testExecutionInfo : testExecutionInfos) {
            boolean failed = testExecutionInfo.getResult().getTitle().equalsIgnoreCase(TestExecutionResult.FAIL.getName());
            if (failed && testExecutionInfo.getDefectIds().isEmpty()) {
                return false;

            }
        }

        return true;
    }
}
