package com.ericsson.cifwk.tm.application.queries;

import com.ericsson.cifwk.tm.application.annotations.QuerySet;
import com.ericsson.cifwk.tm.application.services.IsoService;
import com.ericsson.cifwk.tm.presentation.dto.IsoInfo;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;

@QuerySet
public class IsoQuerySet {

    @Inject
    private IsoService isoService;

    public Response getIsosByProductAndDrop(Long dropId) {
        try {
            List<IsoInfo> isos = isoService.getIsosByDrop(dropId);
            return Responses.nullable(isos);
        } catch (IllegalArgumentException e) {
            return Responses.badRequest(e.getMessage());
        }
    }
}
