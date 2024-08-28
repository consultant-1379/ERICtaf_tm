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

import com.ericsson.cifwk.tm.application.security.SecretService;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.inject.Inject;
import java.math.BigInteger;
import java.security.GeneralSecurityException;

public class SecretServiceImpl implements SecretService {

    private static final int ITERATIONS = 4096;
    private static final int KEY_LENGTH = 256;
    private static final byte[] SALT = "Alice was silent".getBytes();

    private final String secretHash;

    @Inject
    public SecretServiceImpl(SecretConfiguration secretConfiguration) {
        secretHash = secretConfiguration.getSecretHash();
    }

    @Override
    public boolean validate(String secret) {
        try {
            String hash = generateHash(Strings.nullToEmpty(secret));
            return secretHash.equals(hash);
        } catch (GeneralSecurityException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public String generateHash(String password) throws GeneralSecurityException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), SALT, ITERATIONS, KEY_LENGTH);
        SecretKey secretKey = keyFactory.generateSecret(keySpec);
        return toHex(secretKey.getEncoded());
    }

    private String toHex(byte[] bytes) {
        return new BigInteger(1, bytes).toString(16);
    }

}
