package com.ericsson.cifwk.tm.fun.ui.common;

import com.ericsson.cifwk.taf.data.User;
import com.ericsson.cifwk.taf.data.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class DynamicUser {

    private static final Logger logger = LoggerFactory.getLogger(DynamicUser.class);
    private static final String DYNAMIC_PRINCIPAL_PREFIX = "tms-AI-";
    private static final String ADMIN_PRINCIPAL_PREFIX = "tms-admin-";

    private DynamicUser() {
    }

    public static User create() {
        User user = new User(constructUsername(DYNAMIC_PRINCIPAL_PREFIX), "no-password", UserType.WEB);
        logger.info("Create dynamic user: {}", user);
        return user;
    }

    public static User createAdmin() {
        User user = new User(constructUsername(ADMIN_PRINCIPAL_PREFIX), "no-password", UserType.WEB);
        logger.info("Create dynamic user: {}", user);
        return user;
    }

    private static String constructUsername(String principalPrefix) {
        return principalPrefix + UUID.randomUUID();
    }
}
