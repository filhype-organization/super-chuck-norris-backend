package app.configuration;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.*;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import org.jboss.resteasy.reactive.server.SimpleResourceInfo;

import java.time.LocalDateTime;

@ApplicationScoped
public class ApplicationConfiguration {

    @ServerExceptionMapper(Exception.class)
    public Response HandleException(SimpleResourceInfo simplifiedResourceInfo, Exception thisException,
                                    ContainerRequestContext containerRequestContext, UriInfo uriInfo, HttpHeaders httpHeaders, Request request) {
        int status = 500;
        if (thisException instanceof WebApplicationException) {
            status = ((WebApplicationException) thisException).getResponse().getStatus();
        }

        ExceptionMessage exceptionMessage = new ExceptionMessage(LocalDateTime.now(), status, thisException.getMessage(), uriInfo.getPath(), containerRequestContext.getMethod());

        return Response.status(status)
                .entity(exceptionMessage)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}