package com.ericsson.cifwk.tm.trs.service;

import com.ericsson.cifwk.tm.tce.TceClient;
import com.ericsson.cifwk.tm.trs.service.impl.TrsRestClientImpl;
import com.ericsson.cifwk.tm.trs.TrsClient;
import com.google.inject.ImplementedBy;

@ImplementedBy(TrsRestClientImpl.class)
public interface TrsRestClient extends TceClient, TrsClient {

}
