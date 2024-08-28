package com.ericsson.cifwk.tm.infrastructure.stubs;

import com.ericsson.cifwk.tm.integration.IsoRetrievalService;
import com.ericsson.cifwk.tm.presentation.dto.IsoInfo;
import com.google.common.collect.Lists;

import java.util.List;

public class IsoRetrievalServiceStub implements IsoRetrievalService {

    @Override
    public List<IsoInfo> getIsos(String product, String drop) {
        return Lists.newArrayList(createIso("1.0.1"), createIso("1.2.0"));
    }

    private IsoInfo createIso(String version) {
        final String name = "ERICenm_CXP9027091";
        return new IsoInfo(name, version);
    }
}
