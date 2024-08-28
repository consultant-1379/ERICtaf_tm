package com.ericsson.cifwk.tm.application.services;

import com.ericsson.cifwk.tm.application.services.impl.AuditServiceImpl;
import com.ericsson.cifwk.tm.domain.model.shared.AuditRevisionEntity;
import com.ericsson.cifwk.tm.common.Identifiable;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.google.inject.ImplementedBy;

import java.util.List;
import java.util.Map;

@ImplementedBy(AuditServiceImpl.class)
public interface AuditService {
    Map<Number, AuditRevisionEntity> loadAuditRevisions(Identifiable<?> identifiable);

    <I> List<Number> fetchRevisionNumbers(Identifiable<I> identifiable);

    TestCaseVersion findTestCaseVersionAuditRevision(TestCaseVersion testCaseVersion, Number number);
}
