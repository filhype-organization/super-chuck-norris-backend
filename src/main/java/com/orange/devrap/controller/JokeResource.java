package com.orange.devrap.controller;

import com.orange.devrap.entity.Joke;
import com.orange.devrap.service.JokeService;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@Path("api/v1/jokes")
@ApplicationScoped
public class JokeResource {

    @Inject
    JokeService jokeService;

    @Produces("application/json")
    @Path("/getById/{id:\\d+}")
    @GET
    public Uni<Response> GetJokeById(Long id) {
        return jokeService.GetJokeById(id)
                .onItem().transform(Unchecked.function(joke -> {
                    if (joke == null) {
                        throw new NotFoundException("Joke not found");
                    } else {
                        return Response.ok(joke).build();
                    }
                }));
    }

    @Produces("application/json")
    @Path("/getRandomJoke")
    @GET
    public Uni<Response> GetRandomJoke() {
        return jokeService.GetRandomJoke()
                .onItem().transform(joke -> Response.ok(joke).build());
    }

    @Consumes("application/json")
    @Produces("application/json")
    @Path("/add")
    @POST
    public Uni<Response> AddJoke(Joke joke) {
        return jokeService.AddJoke(joke)
                .onItem().transform(j -> Response.status(201).entity(j).build());
    }

    @Consumes("application/json")
    @Produces("application/json")
    @Path("/update")
    @PUT
    public Uni<Response> UpdateJoke(Joke joke) {
        return jokeService.UpdateJoke(joke)
                .onItem().transform(j -> Response.ok(j).build());
    }

    @Consumes("application/json")
    @Path("/delete")
    @DELETE
    public Uni<Response> DeleteJoke(Joke joke) {
        return jokeService.DeleteJoke(joke.id)
                .onItem().transform(j -> Response.noContent().build());
    }
}

