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

import com.ericsson.cifwk.tm.domain.model.requirements.ProjectRepository;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExistingProjectIdValidator implements ConstraintValidator<ExistingProject, String> {

    @Inject
    private Provider<ProjectRepository> projectRepository;

    @Override
    public void initialize(ExistingProject annotation) {
        // empty init
    }

    @Override
    public boolean isValid(String projectId, ConstraintValidatorContext validationContext) {

        // emptiness should be checked by different validator
        if (projectId == null) {
            return true;
        }

        return projectRepository.get().findByExternalId(projectId) != null;
    }

}
