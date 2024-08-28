/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.resources.impl;


import com.ericsson.cifwk.tm.application.security.AuthenticationService;
import com.ericsson.cifwk.tm.application.security.UserSessionService;
import com.ericsson.cifwk.tm.infrastructure.toggler.FeatureToggler;
import com.ericsson.cifwk.tm.presentation.annotations.Controller;
import com.ericsson.cifwk.tm.presentation.dto.AuthenticationStatus;
import com.ericsson.cifwk.tm.presentation.dto.LoginCookieCredentials;
import com.ericsson.cifwk.tm.presentation.dto.UserCredentials;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import com.ericsson.cifwk.tm.presentation.resources.LoginResource;
import com.ericsson.cifwk.tm.presentation.responses.Responses;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.Map;

@Controller
public class LoginController implements LoginResource {

    @Inject
    private AuthenticationService authenticationService;

    @Inject
    private FeatureToggler featureToggler;

    @Inject
    private UserSessionService userSessionService;

    private static final int EXPIRE_DAYS = 7;

    @Override
    public Response status() {
        boolean loggedIn = authenticationService.isLoggedIn();
        AuthenticationStatus status = new AuthenticationStatus(loggedIn);
        if (loggedIn) {
            UserInfo currentUser = userSessionService.getCurrentUser();
            String userId = currentUser.getUserId();
            status.setUserId(userId);
            Map<String, Boolean> features = featureToggler.retrieveFeatures();
            status.setFeatures(features);
        }
        return Responses.ok(status);
    }

    @Override
    public Response login(UserCredentials userCredentials) {
        if (StringUtils.isNotBlank(userCredentials.getSessionId())) {
            String sessionId = authenticationService.login(userCredentials.getSessionId());
            LoginCookieCredentials loginCookieCredentials = new LoginCookieCredentials(sessionId, 0);
            if (StringUtils.isNotBlank(sessionId)) {
                return Responses.ok(loginCookieCredentials);
            }
            return Responses.badCredentials("User session is no longer valid");
        } else {
            boolean storeSession = BooleanUtils.isTrue(userCredentials.getStoreSession());
            String sessionId = authenticationService.login(userCredentials.getUsername(),
                    userCredentials.getPassword(), storeSession);
            if (storeSession && StringUtils.isNotBlank(sessionId)) {
                LoginCookieCredentials loginCookieCredentials = new LoginCookieCredentials(sessionId,
                        DateTime.now().plusDays(EXPIRE_DAYS).getMillis());
                return Responses.ok(loginCookieCredentials);
            } else if (StringUtils.isNotBlank(sessionId)) {
                return Responses.ok();
            }
        }
        return Responses.badCredentials("Please enter a correct Ericsson ID and password.");
    }

    @Override
    public Response logout() {
        authenticationService.logout();
        return Responses.ok(new AuthenticationStatus(false));
    }

}
