/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.infrastructure.inject.hk2;

import org.glassfish.jersey.server.validation.ValidationConfig;

import javax.ws.rs.ext.ContextResolver;

public class ValidationContextResolver implements ContextResolver<ValidationConfig> {

    @Override
    public ValidationConfig getContext(Class<?> type) {
        return new ValidationConfig()
                .constraintValidatorFactory(new GuiceConstraintValidatorFactory());
    }

}
