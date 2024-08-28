package com.ericsson.cifwk.tm.infrastructure.logging;

import com.ericsson.cifwk.tm.application.security.UserSessionService;
import com.ericsson.cifwk.tm.application.services.UserProfileService;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import com.google.inject.Singleton;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.EtmPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Singleton
public class LoggingFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);

    @Inject
    private EtmMonitor etmMonitor;

    @Inject
    private Provider<UserSessionService> userSessionServiceProvider;

    @Inject
    private Provider<UserProfileService> userProfileServiceProvider;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        EtmPoint point = etmMonitor.createPoint("HTTP " + request.getMethod() + ' ' + request.getRequestURI());
        String projectName = getCurrentUsersProjectName();

        try {
            chain.doFilter(servletRequest, servletResponse);
        } finally {
            point.collect();
            LOGGER.info("HTTP {} {}, {} [{}] [{}s]",
                    request.getMethod(),
                    request.getRequestURI(),
                    request.getQueryString(),
                    projectName,
                    Math.round(point.getTransactionTime()) / 1000.);
        }

    }

    private String getCurrentUsersProjectName() {
        UserInfo userInfo = userSessionServiceProvider.get().getCurrentUser();
        if (userInfo == null) {
            return null;
        }
        return userProfileServiceProvider.get().findProductNameByUserId(userInfo.getId());
    }

}
