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

import com.ericsson.cifwk.tm.common.Identifiable;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class HasIdValidator implements ConstraintValidator<HasId, Object> {

    @Override
    public void initialize(HasId constraintAnnotation) {
        // empty init
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value instanceof Identifiable) {
            Identifiable identifiable = (Identifiable) value;
            return identifiable.getId() != null;
        } else {
            return false;
        }
    }

}
