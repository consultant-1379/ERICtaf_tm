package com.ericsson.cifwk.tm.application.queries;

import com.ericsson.cifwk.tm.application.annotations.QuerySet;
import com.ericsson.cifwk.tm.domain.model.requirements.Defect;
import com.ericsson.cifwk.tm.domain.model.requirements.DefectRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.DefectMapper;
import com.ericsson.cifwk.tm.presentation.dto.DefectInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.DefectViewFactory;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

/**
 * @author egergle
 */
@QuerySet
public final class DefectQuerySet {

    @Inject
    private DefectMapper defectMapper;

    @Inject
    private DefectRepository defectRepository;

    @Inject
    private DefectViewFactory defectViewFactory;

    public Response getDefects(String defectId, String view) {
        Defect defect = defectRepository.findByExternalId(defectId);
        if (defect == null) {
            return Responses.badRequest("Defect does not exist");
        }
        Class<? extends DtoView<DefectInfo>> dtoView = defectViewFactory.getByName(view);
        DefectInfo defectInfo = defectMapper.mapEntity(defect, new DefectInfo(), dtoView);
        return Responses.ok(defectInfo);
    }

}

