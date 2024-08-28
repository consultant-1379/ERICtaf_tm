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

import org.apache.commons.beanutils.PropertyUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RangeFieldsValidator implements ConstraintValidator<RangeFields, Object> {

    private String fromFieldName;
    private String toFieldName;

    @Override
    public void initialize(RangeFields constraintAnnotation) {
        fromFieldName = constraintAnnotation.from();
        toFieldName = constraintAnnotation.to();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        Comparable from;
        Comparable to;
        try {
            from = (Comparable) PropertyUtils.getSimpleProperty(object, fromFieldName);
            to = (Comparable) PropertyUtils.getSimpleProperty(object, toFieldName);
        } catch (Exception e) {
            return false;
        }
        if (from == null || to == null) {
            // emptiness should be checked by different validator
            return true;
        } else {
            return from.compareTo(to) < 0;
        }
    }

}
