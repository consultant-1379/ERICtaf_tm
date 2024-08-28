/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.infrastructure.security;

import com.google.common.net.HttpHeaders;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecurityAuthenticationFilter extends FormAuthenticationFilter {

    public static final String TM_LOGIN_URL = "/login/";

    @Override
    public String getLoginUrl() {
        return TM_LOGIN_URL;
    }

    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String method = httpRequest.getMethod();
        Subject subject = this.getSubject(request, response);
        if (subject.isRemembered()) {
            return true;
        }
        return "HEAD".equals(method) || "GET".equals(method) || super.onPreHandle(request, response, mappedValue);
    }

    @Override
    protected void saveRequestAndRedirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        saveRequest(request);

        if (isAjaxRequest(request)) {
            sendChallenge(response);
        } else {
            redirectToLogin(request, response);
        }
    }

    protected void sendChallenge(ServletResponse response) {
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    private boolean isAjaxRequest(ServletRequest request) {
        return ((ShiroHttpServletRequest) request).getHeader(HttpHeaders.X_REQUESTED_WITH) != null;
    }

}
