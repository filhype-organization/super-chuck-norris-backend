package com.orange.devrap.controller;

import com.orange.devrap.entity.Joke;
import com.orange.devrap.service.JokeService;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestPath;

import java.util.Optional;
import java.util.UUID;

@Path("api/jokes/v1")
public class JokeResource {

    @Inject
    JokeService jokeService;

    @GET
    @Path("getRandomJoke")
    @Produces("application/json")
    public Joke getRandomJoke() {
        Joke j = jokeService.GetRandomJoke();
        if (j == null) {
            throw new NotFoundException("Joke not found");
        }
        return j;
    }

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Optional<PanacheEntityBase> getJokeById(@RestPath UUID id) {
        //        if (joke.isEmpty()) {
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
        return Joke.findByIdOptional(id);

    }

    @POST
    @Path("add")
    @Produces("application/json")
    @Consumes("application/json")
    public Joke addJoke(Joke joke) {
        return jokeService.AddJoke(joke);
    }

}
