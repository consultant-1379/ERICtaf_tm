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

import com.ericsson.cifwk.tm.application.security.UserSessionService;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import com.google.inject.Singleton;
import org.slf4j.MDC;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

@Singleton
public class MdcFilter implements Filter {

    private static final String USER_KEY = "username";

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Inject
    private UserSessionService userSessionService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        UserInfo currentUser = userSessionService.getCurrentUser();
        String username;
        if (currentUser != null) {
            username = currentUser.getUserId();
        } else {
            username = "anonymo";
        }
        MDC.put(USER_KEY, username);

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove(USER_KEY);
        }
    }

}
