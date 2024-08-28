package com.ericsson.cifwk.tm.presentation.validation;

import com.google.common.base.Strings;
import org.apache.commons.beanutils.PropertyUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AnyNotEmptyFieldValidator implements ConstraintValidator<AnyNotEmptyField, Object> {

    private String[] fieldNames;

    @Override
    public void initialize(AnyNotEmptyField constraintAnnotation) {
        fieldNames = constraintAnnotation.value();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        for (String fieldName : fieldNames) {
            try {
                Object field = PropertyUtils.getSimpleProperty(object, fieldName);
                if (notStringOrNotEmpty(field)) {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    private boolean notStringOrNotEmpty(Object object) {
        if (object instanceof String) {
            return !Strings.isNullOrEmpty((String) object);
        } else {
            return object != null;
        }
    }

}
