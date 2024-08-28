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

import com.ericsson.cifwk.tm.application.security.UserManagementService;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.google.common.base.Strings;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExistingUserValidator implements ConstraintValidator<ExistingUser, String> {

    @Inject
    private Provider<UserManagementService> userManagementServiceProvider;

    @Override
    public void initialize(ExistingUser constraintAnnotation) {
        // empty init
    }

    @Override
    public boolean isValid(String externalId, ConstraintValidatorContext context) {
        // emptiness should be checked by different validator
        if (Strings.isNullOrEmpty(externalId)) {
            return true;
        }

        UserManagementService userManagementService = userManagementServiceProvider.get();
        User user = userManagementService.fetchUser(externalId);
        return user != null;
    }

}
