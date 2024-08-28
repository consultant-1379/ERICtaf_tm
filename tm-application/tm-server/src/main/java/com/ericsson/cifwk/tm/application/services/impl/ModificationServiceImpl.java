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

import com.ericsson.cifwk.tm.application.services.AuditService;
import com.ericsson.cifwk.tm.application.services.ModificationService;
import com.ericsson.cifwk.tm.common.Identifiable;
import com.ericsson.cifwk.tm.domain.model.shared.AuditRevisionEntity;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.infrastructure.mapping.AuditRevisionMapper;
import com.ericsson.cifwk.tm.presentation.dto.Modification;
import com.ericsson.cifwk.tm.presentation.dto.VersionModification;
import com.google.common.collect.Lists;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ModificationServiceImpl implements ModificationService {

    @Inject
    private AuditService auditService;

    @Inject
    private AuditRevisionMapper auditRevisionMapper;

    @Override
    public List<Modification> getModifications(Identifiable<?> entity) {
        Map<Number, AuditRevisionEntity> audit = auditService.loadAuditRevisions(entity);

        List<Modification> modifications = Lists.newArrayList();
        for (AuditRevisionEntity rev : audit.values()) {
            Modification modification = auditRevisionMapper.mapEntity(rev, Modification.class);
            modifications.add(modification);
        }
        Collections.sort(modifications, new Comparator<Modification>() {
            @Override
            public int compare(Modification one, Modification two) {
                return two.getTimestamp().compareTo(one.getTimestamp());
            }
        });
        return modifications;
    }

    @Override
    public List<VersionModification> getVersionModifications(TestCaseVersion testCaseVersion) {
        Map<Number, AuditRevisionEntity> audit = auditService.loadAuditRevisions(testCaseVersion);

        List<VersionModification> modifications = Lists.newArrayList();
        for (Map.Entry<Number, AuditRevisionEntity>  rev : audit.entrySet()) {
            Modification modification = auditRevisionMapper.mapEntity(rev.getValue(), Modification.class);
            TestCaseVersion version = auditService.findTestCaseVersionAuditRevision(testCaseVersion, rev.getKey());
            modifications.add(new VersionModification(
                    modification.getUsername(),
                    modification.getTimestamp(),
                    version.getVersion(),
                    version.getTestCaseStatus().getName()
                    ));
        }

        Collections.sort(modifications, new Comparator<Modification>() {
            @Override
            public int compare(Modification one, Modification two) {
                return two.getTimestamp().compareTo(one.getTimestamp());
            }
        });

        return modifications;
    }

}
