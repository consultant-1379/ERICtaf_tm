package com.ericsson.cifwk.tm.infrastructure.stubs;

import com.ericsson.cifwk.tm.tce.TceClient;

import java.util.UUID;

public class TceClientStub implements TceClient {

    @Override
    public String getContextIdByName(String contextName) {
        return UUID.randomUUID().toString();
    }
}
