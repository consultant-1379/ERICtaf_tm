package com.ericsson.cifwk.tm.application.services.validation;

public final class ServiceValidation {

    private ServiceValidation() {
    }

    public static void check(boolean condition, String message) throws ServiceValidationException {
        check(condition, message, "");
    }

    public static void check(boolean condition, String message, String developerMessage)
            throws ServiceValidationException {
        if (!condition) {
            throw new ServiceValidationException(message, developerMessage);
        }
    }

}
