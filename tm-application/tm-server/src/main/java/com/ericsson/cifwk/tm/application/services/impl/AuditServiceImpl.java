package com.ericsson.cifwk.tm.application.services.impl;

import com.ericsson.cifwk.tm.application.services.AuditService;
import com.ericsson.cifwk.tm.domain.model.shared.AuditRevisionEntity;
import com.ericsson.cifwk.tm.common.Identifiable;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.google.common.collect.Sets;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class AuditServiceImpl implements AuditService {

    private final AuditReader auditReader;

    @Inject
    public AuditServiceImpl(EntityManager entityManager) {
        auditReader = AuditReaderFactory.get(entityManager);
    }

    @Override
    public Map<Number, AuditRevisionEntity> loadAuditRevisions(Identifiable<?> identifiable) {
        List<Number> numbers = fetchRevisionNumbers(identifiable);
        if (numbers.isEmpty()) {
            return Collections.emptyMap();
        } else {
            return auditReader.findRevisions(AuditRevisionEntity.class, Sets.newLinkedHashSet(numbers));
        }
    }

    @Override
    public <I> List<Number> fetchRevisionNumbers(Identifiable<I> identifiable) {
        I id = identifiable.getId();
        Class<? extends Identifiable> type = identifiable.getClass();
        return auditReader.getRevisions(type, id);
    }

    @Override
    public TestCaseVersion findTestCaseVersionAuditRevision(TestCaseVersion testCaseVersion, Number number) {
        return auditReader.find(TestCaseVersion.class, testCaseVersion.getId(), number);

    }

}
