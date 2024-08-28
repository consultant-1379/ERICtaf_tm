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

import com.ericsson.cifwk.tm.application.services.RequirementService;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.requirements.ProjectRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.domain.model.requirements.RequirementRepository;
import com.ericsson.cifwk.tm.integration.RequirementManagement;
import com.ericsson.cifwk.tm.integration.jira.dto.ExternalRequirement;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Sets;
import com.google.inject.persist.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RequirementServiceImpl implements RequirementService {

    private final Logger logger = LoggerFactory.getLogger(RequirementServiceImpl.class);

    @Inject
    private RequirementManagement requirementManagement;

    @Inject
    private RequirementRepository requirementRepository;

    @Inject
    private ProjectRepository projectRepository;

    void syncWithExternalSystem(Requirement requirement) {
        String externalId = requirement.getExternalId();
        ExternalRequirement externalRequirement = requirementManagement.fetchById(externalId);
        updateFromExternalSystem(requirement, externalRequirement);
    }

    protected void updateFromExternalSystem(Requirement requirement, ExternalRequirement externalRequirement) {
        String externalId = requirement.getExternalId();
        if (externalRequirement == null) {
            logger.warn("Unable to update requirement from external: {}", externalId);
            return;
        }
        logger.info("Updating requirement from external: {}", externalId);

        mapExternalRequirement(requirement, externalRequirement);
        if (requirement.getProject() == null) {
            logger.warn("Cannot find project for requirement: {}", externalId);
            return;
        }
        save(requirement);

        ExternalRequirement externalParent = externalRequirement.getParent();
        if (externalParent != null) {
            String externalParentId = externalParent.getId();
            Requirement parent = requirement.getParent();
            if (parent != null && !parent.getExternalId().equals(externalParentId)) {
                // unlink lost parent
                parent.removeChild(requirement);
                parent = null;
            }
            if (parent == null) {
                // link new parent
                parent = MoreObjects.firstNonNull(
                        requirementRepository.findByExternalId(externalParentId),
                        new Requirement(externalParentId)
                );
                parent.addChild(requirement);
            }
            // update
            updateFromExternalSystem(parent, externalParent);
        }
    }

    @Override
    @Transactional
    public Requirement save(Requirement requirementEntity) {
        return requirementRepository.save(requirementEntity);
    }

    @Override
    public Requirement findOrImport(String externalId) {
        Requirement requirement = requirementRepository.findByExternalId(externalId);

        if (requirement == null) {
            return importFromJira(externalId);
        }
        return requirement;
    }

    @Override
    public List<Requirement> findAllOrImport(Set<String> externalIds) {
        if (externalIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<Requirement> requirements = getAllFromRepo(externalIds);

        Set<String> foundExternalIds = new HashSet<>(requirements.size());
        for (Requirement requirement : requirements) {
            foundExternalIds.add(requirement.getExternalId());
        }

        if (requirements.size() != externalIds.size()) {
            Set<String> missingExternalIds = Sets.difference(externalIds, foundExternalIds);
            List<Requirement> fromJira = importFromJira(missingExternalIds);
            requirements.addAll(fromJira);
        }
        return requirements;
    }

    @Override
    public void fetchAndSaveUpdatedRequirements() {
        Set<ExternalRequirement> requirements =
                requirementManagement.fetchUpdated(RequirementManagement.EXPIRY_HOURS);

        saveUpdatedRequirements(requirements);
    }

    @Override
    public void fetchAndSaveUpdatedRequirements(String jql) {
        Set<ExternalRequirement> requirements =
                requirementManagement.fetchUpdated(jql);

        saveUpdatedRequirements(requirements);
    }

    @Transactional
    public Requirement saveOrUpdate(String externalId, ExternalRequirement externalRequirement) {
        try {
            Requirement requirement = requirementRepository.findByExternalId(externalId);
            if (requirement == null) {
                requirement = new Requirement(externalId);
            }
            updateFromExternalSystem(requirement, externalRequirement);
            return requirement;
        } catch (Exception e) {
            logger.error("Unable to update by requirementId '{}'", externalId, e);
            return null;
        }
    }

    private List<Requirement> importFromJira(Set<String> externalIds) {
        Map<String, ExternalRequirement> externalRequirements =
                requirementManagement.fetchById(externalIds);

        List<Requirement> requirements = new ArrayList<>(externalIds.size());
        for (Map.Entry<String, ExternalRequirement> entry : externalRequirements.entrySet()) {
            Requirement requirement = saveOrUpdate(entry.getKey(), entry.getValue());
            if (requirement != null) {
                requirements.add(requirement);
            }
        }
        return requirements;
    }

    private void saveUpdatedRequirements(Set<ExternalRequirement> requirements) {
        int totalSuccess = 0;
        int totalFailed = 0;
        for (ExternalRequirement requirement : requirements) {
            try {
                saveOrUpdate(requirement.getId(), requirement);
                totalSuccess++;
            } catch (Exception ex) {
                totalFailed++;
                logger.error("Unable to persist requirement [{} title={}] error={}",
                        requirement.getId(), requirement.getTitle(), ex.getMessage(), ex);
            }
        }
        logger.info("{} requirements updated in Jira and {} requirements failed",
                totalSuccess, totalFailed);

    }

    private List<Requirement> getAllFromRepo(Set<String> externalIds) {
        return requirementRepository.findAllByExternalId(externalIds);
    }

    private Requirement importFromJira(String externalId) {
        ExternalRequirement externalRequirement = requirementManagement.fetchById(externalId);
        if (externalRequirement == null) {
            return null;
        }
        return saveOrUpdate(externalId, externalRequirement);
    }

    private void mapExternalRequirement(Requirement requirement, ExternalRequirement externalRequirement) {
        String projectId = externalRequirement.getProject();
        Project project = projectRepository.findByExternalId(projectId);
        requirement.setProject(project);

        requirement.setExternalLabel(externalRequirement.getTitle());
        requirement.setExternalSummary(externalRequirement.getSummary());
        requirement.setExternalStatusName(externalRequirement.getStatusName());

        if (externalRequirement.getType() != null) {
            requirement.setExternalType(externalRequirement.getType().toString());
        }
        if (externalRequirement.getDeliveredIn() != null) {
            requirement.setDeliveredIn(externalRequirement.getDeliveredIn());
        }
    }
}
