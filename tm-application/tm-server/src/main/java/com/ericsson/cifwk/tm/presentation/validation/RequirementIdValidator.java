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

import com.ericsson.cifwk.tm.application.services.RequirementService;
import com.google.common.base.Strings;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RequirementIdValidator implements ConstraintValidator<RequirementId, String> {

    @Inject
    private Provider<RequirementService> requirementService;

    @Override
    public void initialize(RequirementId annotation) {
        // empty init
    }

    @Override
    public boolean isValid(String requirementId, ConstraintValidatorContext validationContext) {

        // emptiness should be checked by different validator
        if (Strings.isNullOrEmpty(requirementId)) {
            return true;
        }

        return requirementService.get().findOrImport(requirementId) != null;
    }

}
