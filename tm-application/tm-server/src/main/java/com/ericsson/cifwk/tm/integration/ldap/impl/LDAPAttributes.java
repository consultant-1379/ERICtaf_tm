/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.integration.ldap.impl;

enum LDAPAttributes {
    SAM_ACCOUNT_NAME("sAMAccountName"),
    GIVEN_NAME("givenName"),
    LAST_NAME("sn"),
    CN("cn"),
    MAIL("mail"),
    MEMBER_OF("memberOf");


    private String name;

    LDAPAttributes(String name) {
        this.name = name;
    }

    public static String[] getAll() {
        String[] result = new String[values().length];
        for (int i = 0; i < values().length; i++) {
            result[i] = values()[i].getName();
        }
        return result;
    }

    public String getName() {
        return name;
    }
}
