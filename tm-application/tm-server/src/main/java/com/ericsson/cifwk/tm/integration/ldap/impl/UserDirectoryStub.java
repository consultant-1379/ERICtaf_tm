/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */
package com.ericsson.cifwk.tm.integration.ldap.impl;

import com.ericsson.cifwk.tm.integration.ldap.LDAPSearchType;
import com.ericsson.cifwk.tm.integration.ldap.UserDirectory;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;

public class UserDirectoryStub implements UserDirectory {

    @Override
    public UserInfo findByUsernameOrEmail(String query) {
        return find(query);
    }

    @Override
    public UserInfo findInLDAP(String query, LDAPSearchType searchType) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(query);
        userInfo.setEmail(query + "@ericsson.se.mock");
        userInfo.setName("NameOf" + query);
        userInfo.setSurname("SurnameOf" + query);
        return userInfo;
    }

    private UserInfo find(String query) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(query);
        userInfo.setEmail(query + "@ericsson.se.mock");
        userInfo.setName("NameOf" + query);
        userInfo.setSurname("SurnameOf" + query);
        return userInfo;
    }


}
