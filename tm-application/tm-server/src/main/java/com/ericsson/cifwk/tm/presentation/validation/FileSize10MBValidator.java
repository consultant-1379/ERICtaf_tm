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

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class FileSize10MBValidator implements ConstraintValidator<FileSize10MB, FormDataMultiPart> {

    @Override
    public void initialize(FileSize10MB annotation) {
        // empty init
    }

    @Override
    public boolean isValid(FormDataMultiPart formDataMultiPart, ConstraintValidatorContext validationContext) {
        int fileSize;
        final int tenMB = 10485760;
        if (formDataMultiPart == null) {
            return true;
        }

        List<FormDataBodyPart> parts = formDataMultiPart.getFields("file");

        for (FormDataBodyPart part : parts) {
            InputStream inputStream = part.getValueAs(InputStream.class);
            try {
                byte[] bytes = IOUtils.toByteArray(inputStream);
                fileSize = bytes.length;
            } catch (IOException e) {
                return false;
            }
            if (fileSize > tenMB) {
                return false;
            }

        }

        return true;
    }

}
