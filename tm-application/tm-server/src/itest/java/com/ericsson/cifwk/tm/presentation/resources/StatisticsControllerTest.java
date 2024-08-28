package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.domain.statistics.StatisticsObject;
import com.ericsson.cifwk.tm.presentation.BaseControllerLevelTest;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by egergle on 15/02/2017.
 */
public class StatisticsControllerTest extends BaseControllerLevelTest {

    public static final String STATISTICS_URL = "/tm-server/api/statistics/";

    private static final GenericType<List<StatisticsObject>> STATISTICS_OBJECT = new GenericType<List<StatisticsObject>>() {
    };

    @Test
    public void getTestCaseData() {
        createTestCasesForStatistics();

        Response response = app.client().path(STATISTICS_URL + "test-cases").request().get();
        assertEquals(200, response.getStatus());

        List<StatisticsObject> statisticsObjects = response.readEntity(STATISTICS_OBJECT);
        assertEquals(statisticsObjects.size(), 2);

        assertEquals(statisticsObjects.get(0).getValue(), 2);
        assertEquals(statisticsObjects.get(1).getValue(), 1);

    }

    @Test
    public void getUserData() {
        createUserDataForStatistics();

        Response response = app.client().path(STATISTICS_URL + "users").request().get();
        assertEquals(200, response.getStatus());

        List<StatisticsObject> statisticsObjects = response.readEntity(STATISTICS_OBJECT);
        assertEquals(statisticsObjects.size(), 3);

        assertEquals(statisticsObjects.get(0).getValue(), 6);
        assertEquals(statisticsObjects.get(1).getValue(), 10);
        assertEquals(statisticsObjects.get(2).getValue(), 12);

    }

    private void createTestCasesForStatistics() {
        Product enm = new Product("ENM");
        enm.setName("ENM");
        Product oss = new Product("OSS");
        oss.setName("OSS");

        Project enmProject = new Project();
        enmProject.setProduct(enm);
        enmProject.setName("test1");
        enmProject.setExternalId("test1");

        Project ossProject = new Project();
        ossProject.setProduct(oss);
        ossProject.setName("test2");
        ossProject.setExternalId("test2");

        app.persistence().persistInTransaction(enm, oss, enmProject, ossProject);

        Requirement requirement = fixture().persistRequirement("TORF-2309", enmProject);
        Requirement requirement2 = fixture().persistRequirement("OSS-2310", ossProject);

        TestCase test1 = fixture().persistTestCase("test1", enm);
        TestCase test2 = fixture().persistTestCase("test2", enm);
        TestCase test3 = fixture().persistTestCase("test3", oss);

        test1.getCurrentVersion().addRequirement(requirement);
        test2.getCurrentVersion().addRequirement(requirement);
        test3.getCurrentVersion().addRequirement(requirement2);

        app.persistence().persistInTransaction(test1, test2, test3);
    }

    private void createUserDataForStatistics() {
        User user = fixture().persistUser();
        Date date = new Date();
        for (int i = 0; i <= 10; i++) {
            fixture().persistUserSession(user, date);
        }

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date result = cal.getTime();

        User user2 = fixture().persistUser();
        for (int i = 0; i <= 9; i++) {
            fixture().persistUserSession(user2, result);
        }

        cal.add(Calendar.MONTH, -1);
        result = cal.getTime();

        for (int i = 0; i <= 5; i++) {
            fixture().persistUserSession(user2, result);
        }

    }

}