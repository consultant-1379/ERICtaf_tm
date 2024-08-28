/**
 * ****************************************************************************
 * COPYRIGHT Ericsson (c) 2014.
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 * ****************************************************************************
 */

package com.ericsson.cifwk.tm.infrastructure;

import com.ericsson.cifwk.tm.domain.model.dataimport.Import;
import com.ericsson.cifwk.tm.domain.model.domain.Drop;
import com.ericsson.cifwk.tm.domain.model.domain.ISO;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.posts.Post;
import com.ericsson.cifwk.tm.domain.model.requirements.Defect;
import com.ericsson.cifwk.tm.domain.model.requirements.Feature;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.domain.model.shared.FeatureToggle;
import com.ericsson.cifwk.tm.domain.model.testdesign.ReviewGroup;
import com.ericsson.cifwk.tm.domain.model.testdesign.Scope;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestField;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestLink;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestStep;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestSuite;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestTeam;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestType;
import com.ericsson.cifwk.tm.domain.model.testdesign.VerifyStep;
import com.ericsson.cifwk.tm.domain.model.trs.TrsJobRecord;
import com.ericsson.cifwk.tm.domain.model.trs.TrsResultRecord;
import com.ericsson.cifwk.tm.domain.model.trs.TrsSessionRecord;
import com.ericsson.cifwk.tm.domain.model.users.Notification;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.domain.model.users.UserProfile;
import com.ericsson.cifwk.tm.domain.model.users.UserSession;
import com.google.common.base.Throwables;
import com.google.inject.persist.PersistService;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Table;

public final class PersistenceHelper {

    private PersistService persistService;
    private EntityManager entityManager;

    public PersistenceHelper() {
    }

    @Inject
    PersistenceHelper(PersistService persistService) {
        this.persistService = persistService;
        try {
            persistService.start();
        } catch (IllegalStateException e) {
            // FIXME: No other way to tell that PersistService is started
        }
    }

    @PreDestroy
    public void stop() {
        try {
            persistService.stop();
            LeakCleaner.clean();
        } catch (IllegalStateException e) {
            // FIXME: No other way to tell that PersistService is stopped
        }
    }

    @Inject
    void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityManager em() {
        return entityManager;
    }

    public void cleanupTables() {

        disableConstraints();

        truncateTables(
                getTableName(VerifyStep.class),
                getTableName(TestStep.class),
                getTableName(TestField.class),
                "DEFECTS_TEST_EXECUTIONS",
                "REQUIREMENT_TEST_CASES",
                "SCOPE_TEST_CASES",
                getTableName(Scope.class),
                getTableName(TestLink.class),
                "IMPORT_TEST_CASES",
                "TEST_EXECUTIONS",
                "TEST_PLAN_ITEMS",
                getTableName(TestSuite.class),
                getTableName(TestTeam.class),
                getTableName(TestCaseVersion.class),
                getTableName(TestCase.class),
                getTableName(TechnicalComponent.class),
                getTableName(TestType.class),
                getTableName(Import.class),
                getTableName(UserProfile.class),
                getTableName(UserSession.class),
                getTableName(User.class),
                "REQUIREMENT_FEATURES",
                getTableName(Feature.class),
                getTableName(Requirement.class),
                getTableName(TestCampaign.class),
                getTableName(Project.class),
                getTableName(Product.class),
                getTableName(ProductFeature.class),
                getTableName(Defect.class),
                getTableName(Notification.class),
                getTableName(Post.class),
                getTableName(FeatureToggle.class),
                getTableName(Drop.class),
                getTableName(ISO.class),
                getTableName(TrsJobRecord.class),
                getTableName(TrsSessionRecord.class),
                getTableName(TrsResultRecord.class),
                getTableName(ReviewGroup.class),
                "REVIEW_GROUP_USERS"
        );

        enableConstraints();
    }

    private void disableConstraints() {
        // ~ avoid integrity checks, i.e. disable foreign keys during inserts to My SQL & H2
        executeNative("SET FOREIGN_KEY_CHECKS = 0");
    }

    private void enableConstraints() {
        // ~ enable integrity checks, i.e. enable foreign keys during inserts to My SQL & H2
        executeNative("SET FOREIGN_KEY_CHECKS = 1");
    }

    private void executeNative(final String... queries) {
        inTransaction(new Runnable() {
            @Override
            public void run() {
                for (String query : queries) {
                    entityManager.createNativeQuery(query).executeUpdate();
                }
            }
        });
    }

    private String getTableName(Class<?> entityClass) {
        Table table = entityClass.getAnnotation(Table.class);
        return table.name();
    }

    private void truncateTables(final String... names) {
        inTransaction(new Runnable() {
            @Override
            public void run() {
                for (String name : names) {
                    entityManager.createNativeQuery("DELETE FROM " + name)
                            .executeUpdate();
                }
            }
        });
    }

    public void inTransaction(Runnable runnable) throws PersistenceException {
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        try {
            runnable.run();
            tx.commit();
        } catch (PersistenceException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw Throwables.propagate(e.getCause());
        }
    }

    public void persistInTransaction(final Object... entities) {
        inTransaction(new Runnable() {
            @Override
            public void run() {
                for (Object entity : entities) {
                    entityManager.persist(entity);
                }
            }
        });
    }

}
