package com.orange.devrap.controller.exception;

import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CustomExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        int status = 500;
        if (exception instanceof WebApplicationException) {
            status = ((WebApplicationException) exception).getResponse().getStatus();
        }

        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder()
                .add("errors", Json.createArrayBuilder()
                        .add(Json.createObjectBuilder()
                                .add("status", status)
                                .add("title", exception.getMessage())
                        )
                );

        return Response.status(status)
                .entity(jsonBuilder.build())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
