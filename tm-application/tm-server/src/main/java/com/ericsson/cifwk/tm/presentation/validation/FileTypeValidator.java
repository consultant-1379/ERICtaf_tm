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

import com.google.common.collect.Lists;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class FileTypeValidator implements ConstraintValidator<FileType, FormDataMultiPart> {

    private String message;
    private List<String> fileTypes;

    @Override
    public void initialize(FileType annotation) {
        fileTypes = Lists.newArrayList("txt", "csv", "py", "sh", "bash", "pyw", "pyo", "pyc", "pyd",
                "log", "zip", "gzip", "png", "jpg", "gz");

        message = "Only files of type: (" + StringUtils.join(fileTypes, ", ") + ") are supported";
    }

    @Override
    public boolean isValid(FormDataMultiPart formDataMultiPart, ConstraintValidatorContext validationContext) {
        if (formDataMultiPart == null) {
            return true;
        }

        validationContext.disableDefaultConstraintViolation();
        validationContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();

        List<FormDataBodyPart> parts = formDataMultiPart.getFields("file");
        for (FormDataBodyPart part : parts) {
            FormDataContentDisposition file = part.getFormDataContentDisposition();
            String extension = FilenameUtils.getExtension(file.getFileName());

            return fileTypes.stream().filter(s -> s.equalsIgnoreCase(extension)).findFirst().isPresent();

        }

        return true;
    }
}
