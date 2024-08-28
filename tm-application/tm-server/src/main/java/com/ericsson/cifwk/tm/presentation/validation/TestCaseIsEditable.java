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

import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseStatus;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class TestCaseIsEditable implements ConstraintValidator<Editable, TestCaseInfo> {

    @Inject
    private TestCaseRepository testCaseRepository;

    @Override
    public void initialize(Editable annotation) {
        // empty init
    }

    @Override
    public boolean isValid(TestCaseInfo testCaseInfo, ConstraintValidatorContext validationContext) {
        Optional<Long> testCaseOption = Optional.ofNullable(testCaseInfo.getId());
        if (testCaseOption.isPresent()) {
            Optional<TestCase> testCase = Optional.ofNullable(testCaseRepository.find(testCaseOption.get()));
            if (testCase.isPresent()) {
                TestCaseVersion currentVersion = testCase.get().getCurrentVersion();
                return isCorrectStatus(currentVersion);
            }
        }
        return true;
    }

    private boolean isCorrectStatus(TestCaseVersion currentVersion) {
        if (currentVersion.getTestCaseStatus() == null) {
            return false;
        }
        if (currentVersion.getTestCaseStatus().equals(TestCaseStatus.APPROVED)
                || currentVersion.getTestCaseStatus().equals(TestCaseStatus.REVIEW)) {
            return false;
        }
        return true;
    }

}
