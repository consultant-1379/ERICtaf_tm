package com.ericsson.cifwk.tm.infrastructure.stubs;

import com.ericsson.cifwk.tm.integration.ciportal.DropRetrievalService;
import com.ericsson.cifwk.tm.presentation.dto.DropInfo;
import com.google.common.collect.Lists;

import java.util.List;

public class DropRetrievalServiceStub implements DropRetrievalService {

    @Override
    public List<DropInfo> getDrops(String product) {
        DropInfo newDrop = new DropInfo("ENM", "16.9");
        return Lists.newArrayList(newDrop);
    }
}
