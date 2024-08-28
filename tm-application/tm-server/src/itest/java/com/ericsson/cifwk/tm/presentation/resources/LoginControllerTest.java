/*******************************************************************************
 * COPYRIGHT Ericsson (c) 2014.
 * <p/>
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.domain.model.users.UserSession;
import com.ericsson.cifwk.tm.presentation.BaseControllerLevelTest;
import com.ericsson.cifwk.tm.presentation.dto.AuthenticationStatus;
import com.ericsson.cifwk.tm.presentation.dto.LoginCookieCredentials;
import com.ericsson.cifwk.tm.presentation.dto.UserCredentials;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ericsson.cifwk.tm.test.ResponseAsserts.assertStatus;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

public class LoginControllerTest extends BaseControllerLevelTest {

    public static final String URI = "/tm-server/api/login";

    @Test
    public void successfulLogin() {
        logout();
        assertFalse(status().isAuthenticated());
        assertEquals(200, login("taf").getStatus());
        assertTrue(status().isAuthenticated());
    }

    @Test
    public void failedLogin() {
        logout();
        assertFalse(status().isAuthenticated());
        assertEquals(403, login("wrong password").getStatus());
        assertFalse(status().isAuthenticated());
    }

    @Test
    public void userCreated() throws InterruptedException {

        // ~ single session expected
        List<UserSession> userSessions = getUserSessions();
        assertEquals(1, userSessions.size());

        UserSession userSession = userSessions.iterator().next();
        assertNotNull(userSession);

        // ~ single user is created
        assertEquals(1, getUsers().size());
        assertSessionDeleted(false);

        logout();
        assertSessionDeleted(true);

        // user is created only once
        login();
        assertEquals(1, getUsers().size());
        assertEquals(2, getUserSessions().size());
    }

    @Test
    public void featuresAvailable() {
        Response response = request().get();
        AuthenticationStatus status = response.readEntity(AuthenticationStatus.class);
        Map<String, Boolean> features = status.getFeatures();
        assertThat(features.values(), hasSize(2));
    }

    @Test
    @Ignore
    //TODO this is untestable the user needs to be not authenticated to use rememberMe. but they always are.
    public void returnUserSessionId() {
        Response response = loginAndReturnSessionId("taf2", null, null);
        assertStatus(response, Response.Status.OK);
        LoginCookieCredentials loginCookieCredentials = response.readEntity(LoginCookieCredentials.class);

        assertNotNull(loginCookieCredentials.getSessionId());
        assertNotNull(loginCookieCredentials.getPeriod());
        
        Map<String, NewCookie> cookies = new HashMap();
        String rememberMe = response.getHeaders().get("Set-Cookie").toString().split(";")[0];
        NewCookie cookie = new NewCookie("rememberMe", rememberMe.split("=")[1]);
        cookies.put("rememberMe", cookie);

        Response response2 = loginAndReturnSessionId("null", loginCookieCredentials.getSessionId(), cookies);

        logout();
        assertStatus(response2, Response.Status.OK);
    }

    @Test
    public void loginWithBadSessionId() {
        Response response = loginAndReturnSessionId("taf", "I'm a wrong Id", null);
        response.close();
        assertStatus(response, Response.Status.FORBIDDEN);
    }

    private void assertSessionDeleted(boolean deleted) {

        List<UserSession> userSessions = getUserSessions();
        assertEquals(1, userSessions.size());

        UserSession userSession = getFirstUserSessions(userSessions);

        app.persistence().em().detach(userSession);

        assertEquals(deleted, userSession.getDeletedAt() != null);
    }

    private List<User> getUsers() {
        return app.persistence().em().createQuery("select u from User u", User.class).getResultList();
    }

    private UserSession getFirstUserSessions(List<UserSession> userSessions) {
        return userSessions.iterator().next();
    }

    private List<UserSession> getUserSessions() {
        return app.persistence().em().createQuery("select us from UserSession us", UserSession.class).getResultList();
    }

    private AuthenticationStatus status() {
        return request().get(AuthenticationStatus.class);
    }

    private Response login() {
        return app.client().login();
    }

    private Response login(String password) {
        return request().post(Entity.entity(new UserCredentials("taf", password), MediaType.APPLICATION_JSON));
    }

    private Response loginAndReturnSessionId(String password, String sessionId, Map<String, NewCookie> cookies) {
        UserCredentials userCredentials = new UserCredentials("taf2", password);
        if (sessionId == null) {
            userCredentials.setStoreSession(true);
        } else {
            userCredentials.setSessionId(sessionId);
        }

        if (cookies != null) {
            return request().cookie(cookies.get("rememberMe")).post(Entity.entity(userCredentials, MediaType.APPLICATION_JSON));
        }

        return request().post(Entity.entity(userCredentials, MediaType.APPLICATION_JSON));
    }

    private AuthenticationStatus logout() {
        return app.client().logout().readEntity(AuthenticationStatus.class);
    }

    private Invocation.Builder request() {
        return app.client().path(URI).request();
    }

}
