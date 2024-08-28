/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.services.validation;

import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;

import java.util.List;

public class ExecutionValidation {


    public void validateRequirementsTypes(List<Requirement> requirements)
            throws ServiceValidationException {
        try {
            for (Requirement requirement : requirements) {
                ServiceValidation.check(ValidRequirementExecutionTypes.isValid(requirement.getExternalType()),
                        "Invalid requirement type has been specified for test execution");
            }
        } catch (NullPointerException e) {
            throw new ServiceValidationException(e.getMessage());
        }

    }

}
