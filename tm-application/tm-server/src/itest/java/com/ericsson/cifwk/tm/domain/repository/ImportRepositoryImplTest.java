/*******************************************************************************
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.cifwk.tm.domain.repository;

import com.ericsson.cifwk.tm.application.BaseServiceLayerTest;
import org.hibernate.exception.ConstraintViolationException;
import com.ericsson.cifwk.tm.domain.model.dataimport.Import;
import com.ericsson.cifwk.tm.domain.model.dataimport.ImportResult;
import com.ericsson.cifwk.tm.domain.model.dataimport.ImportType;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.domain.model.users.User;
import org.junit.Test;

import javax.persistence.PersistenceException;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ImportRepositoryImplTest extends BaseServiceLayerTest {

    @Test(expected = PersistenceException.class)
    public void testRequired() {

        Import importEntity = new Import();

        // ~ throw common exception because H2 breaks on transaction requirement, but MySQL doesn't
        try {
            persistence.persistInTransaction(importEntity);
            persistence.em().flush();
        } catch (PersistenceException | ConstraintViolationException e) {
            throw new PersistenceException(e);
        }
    }

    @Test
    public void testCascade() {
        User userEntity = fixture().persistUser("testCascadeUser");

        Requirement requirementEntity = new Requirement("testCascadeRequirement");

        persistence.persistInTransaction(requirementEntity);

        Date importCreatedAt = new Date();
        Import importEntity = createImport(userEntity, requirementEntity, importCreatedAt);

        persistence.persistInTransaction(importEntity);

        User persistedUser = persistence.em()
                .createQuery("select u from User u where u.externalId = :externalId", User.class)
                .setParameter("externalId", userEntity.getExternalId())
                .getSingleResult();

        assertEquals(userEntity.getExternalId(), persistedUser.getExternalId());

        List<Import> persistedImports = persistence.em()
                .createQuery("select i from Import i", Import.class)
                .getResultList();

        assertEquals(1, persistedImports.size());

        Import persistedImport = persistence.em()
                .createQuery("select i from Import i where i.createdAt = :createdAt", Import.class)
                .setParameter("createdAt", importCreatedAt, TemporalType.TIMESTAMP)
                .getSingleResult();

        assertEquals(userEntity.getExternalId(), persistedImport.getAuthor().getExternalId());
    }

    public Import createImport(User author, Requirement requirement, Date importCreatedAt) {
        Import importEntity = new Import();
        importEntity.setResult(ImportResult.SUCCESS);
        importEntity.setCreatedAt(importCreatedAt);
        importEntity.setAuthor(author);
        importEntity.setRequirement(requirement);
        importEntity.setType(ImportType.FREEMIND);
        return importEntity;
    }

}