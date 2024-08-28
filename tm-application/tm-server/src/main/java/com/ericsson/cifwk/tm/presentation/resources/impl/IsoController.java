package com.ericsson.cifwk.tm.presentation.resources.impl;

import com.ericsson.cifwk.tm.application.queries.IsoQuerySet;
import com.ericsson.cifwk.tm.presentation.annotations.Controller;
import com.ericsson.cifwk.tm.presentation.resources.IsoResource;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

@Controller
public class IsoController implements IsoResource {

    @Inject
    private IsoQuerySet isoQuerySet;

    @Override
    public Response getIsos(Long dropId) {
        return isoQuerySet.getIsosByProductAndDrop(dropId);
    }
}
