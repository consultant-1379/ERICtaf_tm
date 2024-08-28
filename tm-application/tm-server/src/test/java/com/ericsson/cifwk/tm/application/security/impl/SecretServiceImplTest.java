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
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class SecretServiceImplTest {

    private SecretService secretService;

    @Before
    public void setUp() {
        SecretConfiguration secretConfiguration = mock(SecretConfiguration.class);
        secretService = new SecretServiceImpl(secretConfiguration);
    }

    @Test
    public void testGenerateConsistently() throws Exception {
        String hash = "8ea204c379eb724cd0e7ffdba34b5990ecdcd040c9f5123dd203818680784775";
        assertThat(secretService.generateHash("test"), equalTo(hash));
        assertThat(secretService.generateHash("test"), equalTo(hash));
    }

}
