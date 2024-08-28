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
import com.ericsson.cifwk.tm.infrastructure.mapping.Sanitization;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

public class RequirementIdSetValidator implements ConstraintValidator<RequirementId, Set<String>> {

    @Inject
    private Provider<RequirementService> requirementService;

    @Override
    public void initialize(RequirementId annotation) {
        // empty init
    }

    @Override
    public boolean isValid(Set<String> requirementIds, ConstraintValidatorContext validationContext) {
        Set<String> parsedRequirementIds = Sanitization.normalizeCommaSeparated(requirementIds);
        return requirementService.get().findAllOrImport(parsedRequirementIds).size() == parsedRequirementIds.size();
    }

}
