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

import org.apache.commons.io.FilenameUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class XLSXFileTypeValidator implements ConstraintValidator<XLSXFileType, FormDataMultiPart> {

    @Override
    public void initialize(XLSXFileType annotation) {
        // empty init
    }

    @Override
    public boolean isValid(FormDataMultiPart formDataMultiPart, ConstraintValidatorContext validationContext) {
        if (formDataMultiPart == null) {
            return true;
        }

        List<FormDataBodyPart> parts = formDataMultiPart.getFields("file");
        for (FormDataBodyPart part : parts) {
            FormDataContentDisposition file = part.getFormDataContentDisposition();
            String extension = FilenameUtils.getExtension(file.getFileName());

            if (!"xlsx".equals(extension)) {
                return false;
            }
        }

        return true;
    }

}
