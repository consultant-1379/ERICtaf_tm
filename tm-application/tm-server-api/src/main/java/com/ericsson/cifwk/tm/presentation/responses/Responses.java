/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.responses;

import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.fasterxml.jackson.annotation.JsonView;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.lang.annotation.Annotation;
import java.util.List;

public final class Responses {

    private Responses() {
    }

    public static Response badRequest(Throwable e) {
        return badRequest(e.getMessage(), e.getClass().getName());
    }

    public static Response badRequest(String message) {
        return badRequest(message, "");
    }

    public static Response badRequest(String message, String developerMessage) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new BadRequest(message, developerMessage))
                .build();
    }

    public static Response badRequest(List<BadRequest> badRequests) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(badRequests)
                .build();
    }

    public static Response badCredentials(String message) {
        return Response
                .status(Response.Status.FORBIDDEN)
                .entity(new BadRequest(message, ""))
                .build();
    }

    public static Response notAuthorized(String message) {
        return Response
                .status(Response.Status.UNAUTHORIZED)
                .entity(new BadRequest(message, ""))
                .build();
    }

    public static Response notFound() {
        return Response
                .status(Response.Status.NOT_FOUND)
                .build();
    }


    public static Response noContent() {
        return Response
                .status(Response.Status.NO_CONTENT)
                .build();
    }

    public static Response file(StreamingOutput stream, String fileName, String extension) {
        return Response
                .ok(stream)
                .header("Content-Disposition", "attachment; filename=" + fileName + extension)
                .build();
    }

    public static Response ok() {
        return Response
                .ok()
                .build();
    }

    public static Response ok(Object dto) {
        return Response
                .ok(dto)
                .build();
    }

    public static <T> Response ok(T dto, Class<? extends DtoView<T>> view) {
        if (view == null) {
            return badRequest("No Json View defined");
        }
        return Response
                .ok()
                .entity(dto, getJsonView(view))
                .build();
    }

    public static Response serverError() {
        return Response.serverError().build();
    }

    public static Response created(Object dto) {
        return Response
                .status(Response.Status.CREATED)
                .entity(dto)
                .build();
    }

    public static Response created() {
        return Response
                .status(Response.Status.CREATED)
                .build();
    }

    public static Response isFound(boolean flag) {
        return flag ? ok() : notFound();
    }

    public static Response nullable(Object dto) {
        return dto != null ? ok(dto) : notFound();
    }

    public static <T> Response nullable(T dto, Class<? extends DtoView<T>> view) {
        return dto != null ? ok(dto, view) : notFound();
    }

    public static Annotation[] getJsonView(final Class<? extends DtoView> view) {
        JsonView annotation = new JsonView() {
            @Override
            public Class<?>[] value() {
                return new Class<?>[]{view};
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return JsonView.class;
            }
        };

        return new Annotation[]{annotation};
    }

}
