package com.ericsson.cifwk.tm.presentation;

import com.ericsson.cifwk.tm.presentation.responses.BadRequest;
import com.ericsson.cifwk.tm.presentation.responses.Responses;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ResponsesTest {

    @Test
    public void testBadRequest() {
        String message = "fail";
        Response response = Responses.badRequest(message);
        BadRequest badRequest = (BadRequest) response.getEntity();

        assertEquals(message, badRequest.getMessage());
        assertNotNull(badRequest.getDeveloperMessage());
    }

}
