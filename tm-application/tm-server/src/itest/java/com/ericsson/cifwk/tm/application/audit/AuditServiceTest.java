package com.ericsson.cifwk.tm.application.audit;

import com.ericsson.cifwk.tm.application.BaseServiceLayerTest;
import com.ericsson.cifwk.tm.application.services.impl.AuditServiceImpl;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.domain.model.shared.AuditRevisionEntity;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.testdesign.Scope;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestType;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 *
 */
public class AuditServiceTest extends BaseServiceLayerTest {

    private AuditServiceImpl auditService;

    @Before
    public void setUp() {
        persistence().cleanupTables();
        auditService = new AuditServiceImpl(persistence().em());
    }

    @Test
    public void testRetrieveEntityHistory() {
        Product product = fixture().persistProduct();
        ProductFeature feature = fixture().persistFeature("feature", product);
        TestType testType = fixture().persistTestType("Functional", product);
        TechnicalComponent comp = fixture().persistTechnicalComponent("comp", feature);
        TestCase testCase = fixture().persistTestCase("ID", comp, feature, testType);
        final TestCaseVersion testCaseVersion = testCase.getCurrentVersion();

        persistence().inTransaction(new Runnable() {
            @Override
            public void run() {
                testCaseVersion.setDescription("NEW");
            }
        });

        Map<Number, AuditRevisionEntity> map = auditService.loadAuditRevisions(testCaseVersion);

        assertThat(map.values(), hasSize(2));
        assertThat(map.values().iterator().next().getUser(), notNullValue());
    }

    @Test
    public void testNoAudits() {
        final String uuid = UUID.randomUUID().toString();
        persistence().inTransaction(new Runnable() {
            @Override
            public void run() {
                // Insert natively to bypass Envers
                String sql = "INSERT INTO `SCOPES` (`id`, `deleted`, `name`) VALUES (NULL, 0, ?)";
                persistence().em()
                        .createNativeQuery(sql)
                        .setParameter(1, uuid)
                        .executeUpdate();
            }
        });
        String hql = "from Scope as s where name = ?";
        Scope scope = persistence().em()
                .createQuery(hql, Scope.class)
                .setParameter(1, uuid)
                .getSingleResult();

        Map<Number, AuditRevisionEntity> map = auditService.loadAuditRevisions(scope);

        assertThat(map.values(), hasSize(0));
    }

}
