/*******************************************************************************
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/
package com.ericsson.cifwk.tm.application.security.impl;

import com.ericsson.cifwk.tm.application.security.UserSessionService;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.SubjectThreadState;
import org.apache.shiro.util.LifecycleUtils;
import org.apache.shiro.util.ThreadState;
import org.junit.AfterClass;
import org.junit.Before;
import org.mockito.Mock;

import static org.mockito.Mockito.when;

public abstract class ShiroTestCase {

    private static ThreadState subjectThreadState;

    @Mock
    protected UserSessionService userSessionService;

    @Mock
    protected Subject subject;
    UserInfo userInfo = new UserInfo();

    @Before
    public final void setUpShiroSession() {
        setSubject(subject);
        when(subject.getSession()).thenReturn(new SimpleSession());
        when(subject.getSession(true)).thenReturn(new SimpleSession());
        userInfo.setId(1L);
        userInfo.setUserId("taf");
        when(userSessionService.getCurrentUser()).thenReturn(userInfo);
    }

    @AfterClass
    public static void tearDownShiro() {
        clearSubject();
        try {
            org.apache.shiro.mgt.SecurityManager securityManager = SecurityUtils.getSecurityManager();
            LifecycleUtils.destroy(securityManager);
        } catch (UnavailableSecurityManagerException e) {
            // ignore
        }
        SecurityUtils.setSecurityManager(null);
    }

    protected void setSubject(Subject subject) {
        clearSubject();
        subjectThreadState = new SubjectThreadState(subject);
        subjectThreadState.bind();
    }

    protected static void clearSubject() {
        if (subjectThreadState != null) {
            subjectThreadState.clear();
            subjectThreadState = null;
        }
    }


}
