package com.ericsson.cifwk.tm.integration;

import com.ericsson.cifwk.tm.presentation.dto.IsoInfo;

import java.util.List;

public interface IsoRetrievalService {

    List<IsoInfo> getIsos(String product, String drop);
}
