/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.security.impl;

import com.ericsson.cifwk.tm.integration.annotations.Integration;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang.StringUtils.isEmpty;

/**
 * Class is responsible for user authentication ONLY.
 */
@Integration
public class AuthenticationServiceImpl extends AbstractAuthenticationService {

    @Override
    protected AuthenticationToken getToken(String username, String credentials, boolean rememberMe) {
        checkArgument(!isEmpty(credentials), "Empty passwords are not allowed");
        return new UsernamePasswordToken(username, credentials, rememberMe);
    }
}
