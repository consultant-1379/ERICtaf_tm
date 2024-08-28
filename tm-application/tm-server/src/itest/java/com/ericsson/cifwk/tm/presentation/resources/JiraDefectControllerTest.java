package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.presentation.BaseControllerLevelTest;
import com.ericsson.cifwk.tm.presentation.dto.CompletionInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceData;
import com.google.common.collect.Lists;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ericsson.cifwk.tm.test.ResponseAsserts.assertStatus;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by egergle on 09/07/2015.
 */
public class JiraDefectControllerTest extends BaseControllerLevelTest {

    public static final String META_DATA_URL = "/tm-server/api/jira/defect-metadata/";
    public static final String LABEL_URL = "/tm-server/api/jira/labels/";
    public static final String CREATE_DEFECT_URL = "tm-server/api/jira/create-defect";
    public static final String PROJECT = "TORF";

    private static final GenericType<List<ReferenceData>> LIST_REFERENCE_DATA =
            new GenericType<List<ReferenceData>>() {
            };

    @Test
    public void testGetMetaData() {

        Response response = app.client()
                .path(META_DATA_URL)
                .queryParam("projectId", PROJECT)
                .request().get();

        assertStatus(response, Response.Status.OK);
        List<ReferenceData> data = response.readEntity(LIST_REFERENCE_DATA);

        response.close();

        assertThat(data.size(), equalTo(7));
        assertThat(data.get(0).getId(), equalTo("components"));
        assertThat(data.get(1).getId(), equalTo("fixVersions"));
        assertThat(data.get(2).getId(), equalTo("teamName"));
        assertThat(data.get(3).getId(), equalTo("foundInRelease"));
        assertThat(data.get(4).getId(), equalTo("foundInSprint"));
        assertThat(data.get(5).getId(), equalTo("deliveredInSprint"));
        assertThat(data.get(6).getId(), equalTo("project"));

    }

    @Test
    public void testGetLabels() {

        Response response = app.client()
                .path(LABEL_URL)
                .queryParam("query", "")
                .request().get();

        assertStatus(response, Response.Status.OK);
        CompletionInfo data = response.readEntity(CompletionInfo.class);

        response.close();

        assertThat(data.getItems().size(), equalTo(3));
        assertThat(data.getItems().get(0).getValue(), equalTo("15B"));
        assertThat(data.getItems().get(1).getValue(), equalTo("16A"));
        assertThat(data.getItems().get(2).getValue(), equalTo("16B"));

    }

    @Test
    public void testCreateDefect() {
        Response response = app.client().path(CREATE_DEFECT_URL)
                .request()
                .post(Entity.entity(createDefect(), MediaType.APPLICATION_JSON));

        assertStatus(response, Response.Status.CREATED);

        Map<String, Object> data = response.readEntity(Map.class);
        response.close();
        assertThat("DURACI-3078", equalTo(data.get("key")));

    }

    private Map<String, Object> createDefect() {
        Map<String, Object> defect = new HashMap<>();
        Map<String, String> item1 = new HashMap<>();
        item1.put("id", "1");

        Map<String, String> item2 = new HashMap<>();
        item1.put("id", "2");

        List ids = Lists.newArrayList();
        ids.add(item1);
        ids.add(item2);

        defect.put("summary", "test");
        defect.put("components", ids);
        defect.put("fixVersions", ids);

        return defect;
    }

}