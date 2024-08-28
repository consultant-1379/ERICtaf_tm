/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation;

import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;

public final class RequestPreconditions {

    private RequestPreconditions() {
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression a boolean expression
     * @param message exception message
     * @throws BadRequestException if {@code expression} is false
     */
    public static void checkArgument(boolean expression, String message) {
        if (!expression) {
            Response response = Responses.badRequest(message);
            throw new BadRequestException(response);
        }
    }

}
