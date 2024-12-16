package com.orange.devrap.controller;

import com.orange.devrap.entity.Joke;
import com.orange.devrap.service.JokeService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("api/v1/jokes")
@ApplicationScoped
public class ReactiveJokeResource {

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

    @POST
    @Path("add")
    @Produces("application/json")
    public Joke addJoke(Joke joke) {
        return jokeService.AddJoke(joke);
    }

}
