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

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class FileNameValidator implements ConstraintValidator<FileName, FormDataMultiPart> {

    @Override
    public void initialize(FileName fileName) {
        // empty init
    }

    @Override
    public boolean isValid(FormDataMultiPart formDataMultiPart, ConstraintValidatorContext validationContext) {
        if (formDataMultiPart == null) {
            return true;
        }

        List<FormDataBodyPart> parts = formDataMultiPart.getFields("file");
        return !parts.stream()
                .map(file -> file.getFormDataContentDisposition())
                .anyMatch(filter -> filter.getFileName().contains("#"));

    }
}
