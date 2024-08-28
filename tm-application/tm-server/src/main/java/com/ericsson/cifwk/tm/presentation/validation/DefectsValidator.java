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

import com.ericsson.cifwk.tm.application.services.DefectService;
import com.ericsson.cifwk.tm.domain.model.requirements.Defect;
import com.ericsson.cifwk.tm.infrastructure.mapping.Sanitization;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Set;

public class DefectsValidator implements ConstraintValidator<Defects, Set<String>> {

    @Inject
    private Provider<DefectService> defectService;

    @Override
    public void initialize(Defects constraintAnnotation) {
        // empty init
    }

    @Override
    public boolean isValid(Set<String> defectIds, ConstraintValidatorContext context) {
        Set<String> parsedDefectIds = Sanitization.normalizeCommaSeparated(defectIds);

        List<Defect> validDefects = defectService.get().findAll(parsedDefectIds);

        return parsedDefectIds.size() == validDefects.size();
    }
}
