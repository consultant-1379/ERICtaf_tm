/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.resources.impl;

import com.ericsson.cifwk.tm.application.queries.DefectQuerySet;
import com.ericsson.cifwk.tm.domain.model.requirements.Defect;
import com.ericsson.cifwk.tm.domain.model.requirements.DefectRepository;
import com.ericsson.cifwk.tm.presentation.annotations.Controller;
import com.ericsson.cifwk.tm.presentation.dto.CompletionInfo;
import com.ericsson.cifwk.tm.presentation.resources.DefectResource;
import com.ericsson.cifwk.tm.presentation.responses.CompletionHelper;
import com.google.common.base.Function;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;

@Controller
public class DefectController implements DefectResource {

    @Inject
    private DefectRepository defectRepository;

    @Inject
    private DefectQuerySet defectQuerySet;

    @Override
    public CompletionInfo getCompletion(String search, int limit) {
        if (search.isEmpty()) {
            return CompletionInfo.empty();
        }

        List<Defect> defects = defectRepository.findMatchingExternalId(search, limit);
        return CompletionHelper.completion(search, defects, new Function<Defect, String>() {
            @Override
            public String apply(Defect defect) {
                return defect.getExternalId();
            }
        });
    }

    @Override
    public Response getDefects(String defectId, String view) {
        return defectQuerySet.getDefects(defectId, view);
    }

}
