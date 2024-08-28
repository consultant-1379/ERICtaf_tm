package com.ericsson.cifwk.tm.integration.ciportal;

import com.ericsson.cifwk.tm.presentation.dto.DropInfo;

import java.util.List;

public interface DropRetrievalService {

    List<DropInfo> getDrops(String product);
}
