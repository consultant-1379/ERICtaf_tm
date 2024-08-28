package com.ericsson.cifwk.tm.application.services;

import com.ericsson.cifwk.tm.application.services.impl.TestCaseSubscriptionServiceImpl;
import com.google.inject.ImplementedBy;

import javax.ws.rs.core.Response;

@ImplementedBy(TestCaseSubscriptionServiceImpl.class)
public interface TestCaseSubscriptionService {

    Response subscribe(String testCaseId, String userId);

    Response unsubscribe(String testCaseId, String userId);

    Response isUserSubscribed(String testCaseId, String userId);

    boolean silentSubscribe(String testCaseId, String userId);

}
