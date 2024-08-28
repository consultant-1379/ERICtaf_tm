package com.ericsson.cifwk.tm.application.services;

import com.ericsson.cifwk.tm.application.services.impl.TrsResendServiceImpl;
import com.google.inject.ImplementedBy;

@ImplementedBy(TrsResendServiceImpl.class)
public interface TrsResendService {

    void recordUnsentExecutions();

}
