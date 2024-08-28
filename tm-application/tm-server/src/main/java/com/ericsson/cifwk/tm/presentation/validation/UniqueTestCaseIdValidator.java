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
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.google.common.base.Strings;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueTestCaseIdValidator implements ConstraintValidator<UniqueTestCaseId, TestCaseInfo> {

    @Inject
    private Provider<TestCaseRepository> testCaseRepository;

    @Override
    public void initialize(UniqueTestCaseId annotation) {
        // empty init
    }

    @Override
    public boolean isValid(TestCaseInfo testCaseInfo, ConstraintValidatorContext validationContext) {

        // emptiness should be checked by different validator
        String newTestCaseId = testCaseInfo.getTestCaseId();
        if (Strings.isNullOrEmpty(newTestCaseId)) {
            return true;
        }

        // newTestCaseId is unique
        TestCaseRepository repository = testCaseRepository.get();
        TestCase entity = repository.findByExternalId(newTestCaseId);
        if (entity == null) {
            return true;
        }

        // it might be that we have found entity currently being updated
        return entity.getId().equals(testCaseInfo.getId());
    }

}
