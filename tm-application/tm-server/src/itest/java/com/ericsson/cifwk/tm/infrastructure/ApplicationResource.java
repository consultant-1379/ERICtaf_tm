/*******************************************************************************
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.cifwk.tm.infrastructure;

import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.testdesign.Scope;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.domain.model.users.UserProfile;
import com.google.inject.Injector;
import org.junit.rules.ExternalResource;

import javax.persistence.EntityManager;

public class ApplicationResource extends ExternalResource {

    public static final String DEFAULT_USER_ID = "taf";

    public static final String PRODUCT_EXTERNAL_ID = "TMS_product_id";
    public static final String PRODUCT_NAME = "TMS Product";

    public static final String PROJECT_EXTERNAL_ID = "TMS_project_id";
    public static final String PROJECT_NAME = "TMS Project";

    private final EmbeddedServer server;
    private final PersistenceHelper persistence;
    private final RestClient client;

    public ApplicationResource() {
        System.setProperty("monitoring.performance.enabled", "false");
        System.setProperty("server.port", "8081");

        server = new EmbeddedServer();
        client = new RestClient(server.getHttpPort());
        persistence = new PersistenceHelper();
    }

    public EmbeddedServer server() {
        return server;
    }

    public RestClient client() {
        return client;
    }

    public PersistenceHelper persistence() {
        return persistence;
    }

    public void cleanUp() {
        Injector injector = Bootstrap.getApplicationInjector();
        EntityManager entityManager = injector.getInstance(EntityManager.class);
        persistence.setEntityManager(entityManager);
        persistence.cleanupTables();
    }

    public void insertFixtureData() {
        persistence.inTransaction(new Runnable() {
            @Override
            public void run() {
                Product product = addProduct();
                addProject(product);
                addTestCaseGroups();
            }
        });
    }

    public void insertProfileForUser(String userName, UserProfile userProfile) {
        User user = persistence.em()
                .createQuery("select u from User u where u.externalId = :id", User.class)
                .setParameter("id", userName)
                .getSingleResult();

        userProfile.setUser(user);
        persistence.persistInTransaction(userProfile);
    }

    public void addProjectForProfile(UserProfile userProfile, Product product) {
        userProfile.setProduct(product);
        persistence.persistInTransaction(userProfile);
    }

    @Override
    protected void before() throws Throwable {
        if (!server.isStarted()) {
            server.start();
        }
    }

    @Override
    protected void after() {
        if (server.isStarted()) {
            server.stop();
        }
    }

    private Product addProduct() {
        Product product = new Product(PRODUCT_EXTERNAL_ID);
        product.setName(PRODUCT_NAME);
        persistence.em().persist(product);
        return product;
    }

    private Project addProject(Product product) {
        Project project = new Project(PROJECT_EXTERNAL_ID);
        project.setName(PROJECT_NAME);
        product.addProject(project);
        persistence.em().persist(project);
        return project;
    }

    private void addTestCaseGroups() {
        persistence.em().persist(new Scope("Group 1"));
        persistence.em().persist(new Scope("Group 2"));
    }

}
