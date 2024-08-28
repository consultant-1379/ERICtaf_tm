/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.services.impl;


import com.ericsson.cifwk.tm.application.services.DefectService;
import com.ericsson.cifwk.tm.domain.model.requirements.Defect;
import com.ericsson.cifwk.tm.domain.model.requirements.DefectRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.requirements.ProjectRepository;
import com.ericsson.cifwk.tm.integration.DefectManagement;
import com.ericsson.cifwk.tm.integration.jira.dto.ExternalDefect;
import com.google.common.collect.Sets;
import com.google.inject.persist.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DefectServiceImpl implements DefectService {

    private final Logger logger = LoggerFactory.getLogger(DefectServiceImpl.class);

    @Inject
    private DefectRepository defectRepository;

    @Inject
    private ProjectRepository projectRepository;

    @Inject
    private DefectManagement defectManagement;

    @Override
    public List<Defect> findAll(Set<String> defectIds) {
        if (defectIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<Defect> defects = defectRepository.findAllByExternalId(defectIds);

        if (defects.size() != defectIds.size()) {
            Collection<? extends Defect> missingDefects = fetchAndSaveMissingDefects(defectIds, defects);
            defects.addAll(missingDefects);
        }
        return defects;
    }

    @Override
    public void fetchAndSaveUpdatedDefects() {
        Set<ExternalDefect> defects =
                defectManagement.fetchUpdated(DefectManagement.EXPIRY_HOURS);

        saveUpdatedDefects(defects);
    }

    @Override
    public void fetchAndSaveUpdatedDefects(String jql) {
        Set<ExternalDefect> defects =
                defectManagement.fetchUpdated(jql);

        saveUpdatedDefects(defects);
    }

    @Transactional
    public Defect saveOrUpdate(String externalId, ExternalDefect externalDefect) {
        try {
            Defect defect = findOrCreateDefect(externalId);
            merge(defect, externalDefect);
            return defect;
        } catch (Exception e) {
            logger.error("Unable to persist Defect - " + externalDefect.toString());
            return null;
        }
    }

    @Transactional
    protected Defect findOrCreateDefect(String externalId) {
        Defect defect = defectRepository.findByExternalId(externalId);

        if (defect == null) {
            defect = new Defect(externalId);
            defectRepository.save(defect);
        }

        return defect;
    }

    private void saveUpdatedDefects(Set<ExternalDefect> defects) {
        int totalSuccess = 0;
        int totalFailed = 0;

        for (ExternalDefect defect : defects) {
            try { // transaction per defect
                saveOrUpdate(defect.getId(), defect);
                totalSuccess++;
            } catch (Exception ex) {
                totalFailed++;
                logger.error("Unable to persist defect [{} title={}] error={}",
                        defect.getId(), defect.getTitle(), ex.getMessage(), ex);
            }
        }
        logger.info("{} defects updated in Jira and {} defects failed", totalSuccess, totalFailed);

    }

    private Set<? extends Defect> fetchAndSaveFromManagementSystem(Set<String> defectIds) {
        Set<Defect> defects = Sets.newHashSet();
        for (String defectId : defectIds) {
            ExternalDefect externalDefect = defectManagement.fetchById(defectId);

            if (externalDefect != null) {
                Defect defect = saveOrUpdate(externalDefect.getId(), externalDefect);

                if (defect != null) {
                    defects.add(defect);
                }
            }
        }

        return defects;
    }

    private Collection<? extends Defect> fetchAndSaveMissingDefects(Set<String> defectIds, List<Defect> defects) {
        Set<String> foundExternalIds = new HashSet<>(defects.size());
        for (Defect foundDefect : defects) {
            foundExternalIds.add(foundDefect.getExternalId());
        }
        Set<String> missingExternalIds = Sets.difference(defectIds, foundExternalIds);

        return fetchAndSaveFromManagementSystem(missingExternalIds);
    }

    private void merge(Defect defect, ExternalDefect externalDefect) {
        defect.setExternalSummary(externalDefect.getSummary());
        defect.setExternalTitle(externalDefect.getTitle());
        defect.setExternalStatusName(externalDefect.getStatusName());
        defect.setDeliveredIn(externalDefect.getDeliveredIn());

        Project project = projectRepository.findByExternalId(externalDefect.getProject());
        defect.setProject(project);
    }

}
