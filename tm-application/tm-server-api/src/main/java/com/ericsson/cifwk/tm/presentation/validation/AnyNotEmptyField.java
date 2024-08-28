package com.ericsson.cifwk.tm.presentation.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {})
public @interface AnyNotEmptyField {

    String message() default "At least one field out of {value} must not be null or empty";

    String[] value() default {};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
