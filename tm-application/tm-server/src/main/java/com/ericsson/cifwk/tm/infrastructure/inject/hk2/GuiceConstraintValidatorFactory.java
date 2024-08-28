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

import com.ericsson.cifwk.tm.infrastructure.Bootstrap;
import com.google.inject.Injector;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;

public class GuiceConstraintValidatorFactory implements ConstraintValidatorFactory {

    private final Injector injector;

    public GuiceConstraintValidatorFactory() {
        injector = Bootstrap.getApplicationInjector();
    }

    @Override
    public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
        return injector.getInstance(key);
    }

    @Override
    public void releaseInstance(ConstraintValidator<?, ?> instance) {
        // NOOP
    }

}
