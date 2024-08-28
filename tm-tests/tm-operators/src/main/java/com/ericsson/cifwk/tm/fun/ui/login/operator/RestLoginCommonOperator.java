/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.login.operator;

import com.ericsson.cifwk.taf.annotations.TestStep;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.data.User;
import com.ericsson.cifwk.taf.data.UserType;
import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.cifwk.taf.tools.http.constants.ContentType;
import com.ericsson.cifwk.taf.tools.http.constants.HttpStatus;
import com.ericsson.cifwk.tm.fun.ui.common.DynamicUser;
import com.ericsson.cifwk.tm.fun.ui.common.HttpToolHolder;
import com.ericsson.cifwk.tm.fun.ui.common.operator.RestCommonOperator;
import com.ericsson.cifwk.tm.fun.ui.login.results.AuthenticationResult;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.UserCredentials;
import com.google.common.net.HttpHeaders;

import java.util.List;

public class RestLoginCommonOperator extends RestCommonOperator {

    private Host host;

    public void start(Host host) {
        this.host = host;
        HttpToolHolder httpToolHolder = httpToolHolderProvider.get();
        httpToolHolder.init(host);
        httpTool = httpToolHolder.getHttpTool();
    }

    @TestStep(id = "loginWithUser", description = "login with user {0}")
    public AuthenticationResult loginWithUser(User user) {
        return loginInternal(user);
    }

    @TestStep(id = "login", description = "Makes login with first use from hostProperties.json file")
    public AuthenticationResult loginWithDefaultUser() {
        List<User> users = host.getUsers(UserType.WEB);
        User user = null;
        if (!users.isEmpty()) {
            user = users.get(0);
        }

        return loginInternal(user);
    }

    @TestStep(id = "login", description = "login with dynamic user")
    public AuthenticationResult login() {
        User dynamicUser = DynamicUser.create();
        return loginInternal(dynamicUser);
    }

    @TestStep(id = "loginAsAdmin", description = "login with admin user")
    public AuthenticationResult loginAsAdmin() {
        User dynamicUser = DynamicUser.createAdmin();
        return loginInternal(dynamicUser);
    }

    private AuthenticationResult loginInternal(User user) {
        String requestUrl = "/tm-server/api/login";

        UserCredentials userCredentials = new UserCredentials(user);
        String userCredentialsJson = json.toJson(userCredentials, UserCredentials.class);

        HttpResponse response = httpTool.request()
                .header(HttpHeaders.ACCEPT, JSON_MIME_TYPE)
                .body(userCredentialsJson)
                .contentType(ContentType.APPLICATION_JSON)
                .post(requestUrl);

        try {
            checkHttpStatus(requestUrl, response, HttpStatus.OK);
        } catch (Exception e) {
            if (response.getResponseCode().equals(HttpStatus.FORBIDDEN)) {
                return AuthenticationResult.error(getErrorMessage(response), user.getUsername());
            }
            if (user == null) {
                return AuthenticationResult.error(e.getMessage(), "No User Information");
            }
            return AuthenticationResult.error(e.getMessage(), user.getUsername());
        }
        return AuthenticationResult.success(true, user.getUsername());
    }

    @TestStep(id = "logout", description = "Makes logout from app")
    public AuthenticationResult logout() {
        String requestUrl = "/tm-server/api/login";

        HttpResponse response = httpTool.request()
                .header(HttpHeaders.ACCEPT, JSON_MIME_TYPE)
                .contentType(ContentType.APPLICATION_JSON)
                .delete(requestUrl);

        try {
            checkHttpStatus(requestUrl, response, HttpStatus.OK);
            checkContentType(requestUrl, response, ContentType.APPLICATION_JSON);
        } catch (Exception e) {
            return AuthenticationResult.error(e.getMessage(), null);
        }
        return AuthenticationResult.success(false, null);
    }

}
