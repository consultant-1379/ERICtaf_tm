package com.ericsson.cifwk.tm.presentation.validation;


import com.ericsson.cifwk.tm.application.security.UserManagementService;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserAuthorityValidator implements ConstraintValidator<Authorized, Object> {

    @Inject
    private UserManagementService userManagementService;

    @Override
    public void initialize(Authorized authorized) {
        // no initialization needed
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        return userManagementService.isAuthorized();
    }
}
