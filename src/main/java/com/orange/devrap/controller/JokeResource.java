package com.orange.devrap.controller;

import com.orange.devrap.entity.Joke;
import com.orange.devrap.service.JokeService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.annotations.Param;
import org.jboss.resteasy.reactive.RestPath;

@Path("api/v1/jokes")
@ApplicationScoped
public class JokeResource {

    @Inject
    JokeService jokeService;

    @GET
    @Path("getRandomJoke")
    @Produces("application/json")
    public Response getRandomJoke() {
        Joke j = jokeService.GetRandomJoke();
        if (j == null) {
            return Response.status(500).entity(new Joke(null, "no joke found")).build();
        }
        return Response.ok(j).build();
    }

    @GET
    @Path("getJokeById/{id}")
    @Produces("application/json")
    public Response getJokeById(@RestPath String id) {
        Joke j = jokeService.GetJokeById(Long.parseLong(id));
        if (j == null) {
            throw new NotFoundException("Joke not found");
        }
        return Response.ok(j).build();
    }

    @POST
    @Path("add")
    @Produces("application/json")
    public Joke addJoke(Joke joke) {
        return jokeService.AddJoke(joke);
    }

}
