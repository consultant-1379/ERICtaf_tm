package com.ericsson.cifwk.tm.tce.impl;

import com.ericsson.cifwk.tm.common.ClientFactory;
import com.ericsson.cifwk.tm.common.UrlHelper;
import com.ericsson.cifwk.tm.tce.TceClient;
import com.ericsson.gic.tms.presentation.dto.ContextBean;
import com.ericsson.gic.tms.presentation.dto.jsonapi.DocumentList;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.GenericType;
import java.util.List;
import java.util.NoSuchElementException;


public class TceClientImpl implements TceClient {

    private static final GenericType<DocumentList<ContextBean>> CONTEXT_LIST = new GenericType<DocumentList<ContextBean>>() {};

    private Client client;

    @Inject
    private UrlHelper urlHelper;

    public TceClientImpl() {
        client = ClientFactory.newClient();
    }

    @Override
    public String getContextIdByName(String contextName) {
        DocumentList<ContextBean> wrappedContexts  = client
                .target(urlHelper.getContextListUrl())
                .request()
                .get(CONTEXT_LIST);

        List<ContextBean> contexts = wrappedContexts.unwrap();

        ContextBean context = contexts.stream()
                .filter(contextBean -> contextBean.getName().equals(contextName))
                .findAny()
                .orElseThrow(NoSuchElementException::new);

        return context.getId();
    }
}
