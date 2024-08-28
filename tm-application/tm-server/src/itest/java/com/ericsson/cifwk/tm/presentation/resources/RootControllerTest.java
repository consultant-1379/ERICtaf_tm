/**
 * ****************************************************************************
 * COPYRIGHT Ericsson (c) 2014.
 * <p/>
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 * ****************************************************************************
 */

package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.presentation.BaseControllerLevelTest;
import com.ericsson.cifwk.tm.presentation.dto.ApiInfo;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class RootControllerTest extends BaseControllerLevelTest {

    @Test
    public void testApiInfo() {
        Response response = app.client().path("/tm-server/api/").request().get();
        assertEquals(200, response.getStatus());

        ApiInfo apiInfo = response.readEntity(ApiInfo.class);
        response.close();
        assertNotEquals("", apiInfo.getVersion());
    }

    @Test
    public void testStatic() {
        Response response = app.client().path("/tm-server").request().get();
        response.close();
        assertEquals(200, response.getStatus());
    }

}
