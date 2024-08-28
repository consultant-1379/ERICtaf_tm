/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */
package com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common;

import com.ericsson.cifwk.taf.data.User;

public class UserCredentials {

    private String username;

    private String password;

    public UserCredentials(User user) {
        if (user != null) {
            username = user.getUsername();
            password = user.getPassword();
        } else {
            username = "";
            password = "";
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
