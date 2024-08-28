/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.services.validation;

/**
 * Checked exception indicating that a validation failed in a service method.
 */
public class ServiceValidationException extends Exception {

    private final String developerMessage;

    public ServiceValidationException(String message) {
        this(message, "");
    }

    public ServiceValidationException(String message, String developerMessage) {
        super(message);
        this.developerMessage = developerMessage;
    }

    public ServiceValidationException(String message, Throwable cause) {
        super(message, cause);
        developerMessage = cause.getMessage();
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }
}
