/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.services.helper;

import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponentRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestCaseServiceHelper {

    @Inject
    private TechnicalComponentRepository componentRepository;

    public Set<TechnicalComponent> findTechnicalComponents(TestCaseInfo testCaseInfo, Long productFeatureId) {
        Set<ReferenceDataItem> componentInfos = testCaseInfo.getTechnicalComponents();
        Set<TechnicalComponent> technicalComponents = Sets.newHashSet();
        if (componentInfos == null) {
            return technicalComponents;
        }
        componentInfos.forEach(c ->
            technicalComponents.add(componentRepository.findByFeatureAndName(productFeatureId, c.getTitle()))
        );
        return technicalComponents;
    }

    @VisibleForTesting
    public void diffRequirements(TestCaseVersion testCaseVersion, List<Requirement> requirements) {
        Set<Requirement> testCaseRequirements = testCaseVersion.getRequirements();
        Map<String, Requirement> before = requirementsMap(testCaseRequirements);
        Map<String, Requirement> after = requirementsMap(requirements);
        MapDifference<String, Requirement> difference = Maps.difference(before, after);

        Map<String, Requirement> deleted = difference.entriesOnlyOnLeft();
        for (Requirement requirement : testCaseRequirements) {
            if (deleted.containsKey(requirement.getExternalId())) {
                testCaseVersion.removeRequirement(requirement);
            }
        }

        Map<String, Requirement> added = difference.entriesOnlyOnRight();
        for (Requirement requirement : added.values()) {
            testCaseVersion.addRequirement(requirement);
        }
    }

    private HashMap<String, Requirement> requirementsMap(Collection<Requirement> requirements) {
        HashMap<String, Requirement> map = new HashMap<>(requirements.size());
        for (Requirement requirement : requirements) {
            map.put(requirement.getExternalId(), requirement);
        }
        return map;
    }
}
