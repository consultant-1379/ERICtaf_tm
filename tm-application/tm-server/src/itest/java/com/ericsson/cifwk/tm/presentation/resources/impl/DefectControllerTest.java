package com.ericsson.cifwk.tm.presentation.resources.impl;

import com.ericsson.cifwk.tm.domain.model.requirements.Defect;
import com.ericsson.cifwk.tm.presentation.BaseControllerLevelTest;
import com.ericsson.cifwk.tm.presentation.dto.DefectInfo;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static com.ericsson.cifwk.tm.test.ResponseAsserts.assertStatus;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by egergle on 27/01/2016.
 */
public class DefectControllerTest  extends BaseControllerLevelTest {

    public static final String DEFECTS_URL = "/tm-server/api/defects/";

    private String defectId;

    @Before
    public void setup() {
        this.defectId = "CIP-1111";
        app.persistence().persistInTransaction(createDefect(this.defectId));
    }

    @Test
    public void testGetDefects() throws Exception {

        Response response = app
                .client()
                .path(DEFECTS_URL + this.defectId)
                .queryParam("view", "detailed")
                .request()
                .get();

        assertStatus(response, Response.Status.OK);
        DefectInfo defectInfo = response.readEntity(DefectInfo.class);

        assertThat(defectInfo.getExternalId(), equalTo(this.defectId));

    }

    @Test
    public void testGetWrongDefects() throws Exception {
        String defectId = "CIP-50000";
        Response response = app
                .client()
                .path(DEFECTS_URL + defectId)
                .queryParam("view", "detailed")
                .request()
                .get();

        assertStatus(response, Response.Status.BAD_REQUEST);

    }

    public Defect createDefect(String externalId) {
        Defect defect = new Defect();
        defect.setDeliveredIn("13.13");
        defect.setExternalSummary("This is a Test");
        defect.setExternalTitle("Bug for Testing");
        defect.setExternalId(externalId);
        return defect;
    }
}