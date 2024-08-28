/*-----------------------------------------------------------------------------
 ******************************************************************************
 * COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************
 -----------------------------------------------------------------------------*/
package com.ericsson.cifwk.tm.auth;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShiroLDAPTest {

    private static Logger logger = LoggerFactory.getLogger(ShiroLDAPTest.class);

    @Test
    @Ignore
    public void testLDAP() {
        // Using the IniSecurityManagerFactory, which will use the an INI file as the security file.
        Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-mock.ini");

        // Setting up the SecurityManager...
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        Subject user = SecurityUtils.getSubject();

        logger.info("User is authenticated:  " + user.isAuthenticated());

        // (cn=evicovc)
        UsernamePasswordToken token = new UsernamePasswordToken("sn=evicovc,dc=ericsson,dc=se", "secret");

        user.login(token);

        logger.info("User is authenticated:  " + user.isAuthenticated());
    }
}
