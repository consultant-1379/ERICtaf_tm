/*******************************************************************************
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.testdesign.Scope;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestType;
import com.ericsson.cifwk.tm.presentation.BaseControllerLevelTest;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceData;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ReferenceControllerTest extends BaseControllerLevelTest {

    private static final String REFERENCES_REST_API_URL = "/tm-server/api/references";
    public static final String PARAM_REFERENCE_ID = "referenceId";
    public static final String PARAM_PRODUCT_ID = "productId";
    public static final String PARAM_ENABLED = "enabled";

    private Scope scope;
    private Product product;
    private TestType testType;

    @Before
    public void setUp() {
        app.cleanUp();

        Project project = fixture().persistProject("project1");
        product = fixture().persistProduct("p123", "Test Product", project);
        testType = fixture().persistTestType("Functional", product);
        scope = fixture().persistScope("scope1", product);
    }

    @Test
    public void getReferences() {
        String[] supportedReferences = new String[]{
                "priority",
                "jiraIssueType",
                "context",
                "group",
                "type",
                "executionType",
                "executionResult",
                "testCaseStatus"
        };

        Response response = app.client()
                .path(REFERENCES_REST_API_URL)
                .queryParam("referenceId", (Object[]) supportedReferences)
                .request().get();
        assertEquals(200, response.getStatus());

        ReferenceData[] referenceData = response.readEntity(ReferenceData[].class);
        assertEquals(8, referenceData.length);

        int i = -1;
        assertReferenceData(referenceData[++i], "priority", "1", "Blocker");
        assertReferenceData(referenceData[++i], "jiraIssueType", "1", "Epic");
        assertReferenceData(referenceData[++i], "context", "1", "REST");
        assertReferenceData(referenceData[++i], "group", scope.getId().toString(), scope.getName());
        assertReferenceData(referenceData[++i], "type", testType.getId().toString(), "Functional");
        assertReferenceData(referenceData[++i], "executionType", "1", "Manual");
        assertReferenceData(referenceData[++i], "executionResult", "1", "Not started");
        assertReferenceData(referenceData[++i], "testCaseStatus", "1", "Approved");
    }

    @Test
    public void getUnknownReferences() {
        String[] supportedReferences = new String[]{"unknown"};
        Response response = app.client()
                .path(REFERENCES_REST_API_URL)
                .queryParam("referenceId", (Object[]) supportedReferences)
                .request().get();
        response.close();
        assertEquals(400, response.getStatus());
    }

    @Test
    public void getRepositoryReferencesWithParams() {
        String scopeName1 = "scope1";
        String scopeName2 = "scope2";
        String scopeName3 = "scope3";
        String scopeName4 = "scope4";
        String scopeName5 = "scope5";

        fixture().persistScope(scopeName2, product);

        fixture().persistScope(scopeName3, product);

        fixture().persistScope(scopeName4, null); //global items
        fixture().persistScope(scopeName5, null, false);

        String externalId = product.getExternalId();
        Response response = app.client()
                .path(REFERENCES_REST_API_URL)
                .queryParam(PARAM_REFERENCE_ID, "group")
                .queryParam(PARAM_PRODUCT_ID, externalId)
                .queryParam(PARAM_ENABLED, "true")
                .request().get();
        assertEquals(200, response.getStatus());

        ReferenceData[] referenceData = response.readEntity(ReferenceData[].class);

        assertEquals(1, referenceData.length);
        assertReferenceData(referenceData[0], "group", scope.getId().toString(), scope.getName());

        List<ReferenceDataItem> groupReferences = referenceData[0].getItems();

        assertThat(groupReferences, hasSize(4));
        assertThat(groupReferences.get(0), hasProperty("title", equalTo(scopeName1)));
        assertThat(groupReferences.get(1), hasProperty("title", equalTo(scopeName2)));
        assertThat(groupReferences.get(2), hasProperty("title", equalTo(scopeName3)));
        assertThat(groupReferences.get(3), hasProperty("title", equalTo(scopeName4)));
    }

    private void assertReferenceData(ReferenceData referenceData, String expectedId, String firstExpectedItemId, String firstExpectedItemValue) {
        assertEquals(expectedId, referenceData.getId());
        ReferenceDataItem item = referenceData.getItems().iterator().next();
        assertEquals(firstExpectedItemId, item.getId());
        assertEquals(firstExpectedItemValue, item.getTitle());
    }
}
