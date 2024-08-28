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

import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.domain.model.users.UserProfile;
import com.ericsson.cifwk.tm.domain.model.users.UserProfileRepository;
import com.ericsson.cifwk.tm.domain.model.users.UserRepository;
import com.ericsson.cifwk.tm.integration.ldap.LDAPSearchType;
import com.ericsson.cifwk.tm.integration.ldap.UserDirectory;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import com.google.inject.persist.Transactional;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.IniRealm;

import javax.inject.Inject;

/**
 * Class is responsible for user authentication ONLY.
 */
public class AuthenticationServiceMockImpl extends AbstractAuthenticationService {

    private static final String DYNAMIC_PRINCIPAL_PREFIX = "tms-AI-";
    private static final String ADMIN_PRINCIPAL_PREFIX = "tms-admin-";

    @Inject
    protected Realm realm;

    @Inject
    protected UserRepository userRepository;

    @Inject
    protected UserDirectory userDirectory;

    @Inject
    protected UserProfileRepository userProfileRepository;

    @Override
    protected AuthenticationToken getToken(String username, String credentials, boolean rememberMe) {
        return new UsernamePasswordToken(username, credentials, rememberMe);
    }

    @Override
    @Transactional
    public String login(String username, String credentials, boolean rememberMe) {
        IniRealm iniRealm = (IniRealm) this.realm;
        UserProfile userProfile = new UserProfile();
        boolean accountExists = !iniRealm.accountExists(username);
        if (username.startsWith(DYNAMIC_PRINCIPAL_PREFIX) && accountExists) {
            iniRealm.addAccount(username, credentials, "web", "VIEWER");

        } else if (username.startsWith(ADMIN_PRINCIPAL_PREFIX) && accountExists) {
            iniRealm.addAccount(username, credentials, "web", "VIEWER");
            userProfile.setAdministrator(true);
        }
        User user = userRepository.findByExternalId(username);
        if (user == null) {
            user = new User(username);
            UserInfo ui = userDirectory.findInLDAP(username, LDAPSearchType.USER);
            user.setExternalAttributes(ui.getEmail(), ui.getName(), ui.getSurname());
            userRepository.persist(user);
            if (userProfile.isAdministrator()) {
                userProfile.setUser(user);
                persistProfile(userProfile);
            }

        }

        return super.login(username, credentials, rememberMe);
    }

    @Transactional
    private void persistProfile(UserProfile userProfile) {
        userProfileRepository.persist(userProfile);
    }
}
