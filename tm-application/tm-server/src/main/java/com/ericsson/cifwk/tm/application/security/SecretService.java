/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.security;

import com.ericsson.cifwk.tm.application.security.impl.SecretServiceImpl;
import com.google.inject.ImplementedBy;

import java.security.GeneralSecurityException;

@ImplementedBy(SecretServiceImpl.class)
public interface SecretService {

    boolean validate(String secret);

    String generateHash(String password) throws GeneralSecurityException;

}
