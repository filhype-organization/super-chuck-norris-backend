package com.orange.devrap.controller;

import com.orange.devrap.entity.Joke;
import com.orange.devrap.service.JokeService;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.jboss.resteasy.reactive.RestResponse;

@Path("api/v1/jokes")
@ApplicationScoped
public class JokeResource {

    @Inject
    JokeService jokeService;

    @Produces("application/json")
    @Path("/{id:\\d+}")
    @GET
    public Uni<Joke> GetJokeById(Long id) {
        return jokeService.GetJokeById(id)
                .onItem().transform(Unchecked.function(joke -> {
                    if (joke == null) {
                        throw new NotFoundException("Joke not found");
                    } else {
                        return joke;
                    }
                }));
    }

    @Produces("application/json")
    @Path("/getRandomJoke")
    @GET
    public Uni<Joke> GetRandomJoke() {
        return jokeService.GetRandomJoke();
    }

    @Consumes("application/json")
    @Produces("application/json")
    @Path("/add")
    @POST
    public Uni<RestResponse<Joke>> AddJoke(Joke joke) {
        return jokeService.AddJoke(joke).replaceWith(RestResponse.status(RestResponse.Status.CREATED, joke));
    }

    @Consumes("application/json")
    @Produces("application/json")
    @Path("/update")
    @PUT
    public Uni<Joke> UpdateJoke(Joke joke) {
        return jokeService.UpdateJoke(joke);
    }

    @Path("/{id:\\d+}")
    @DELETE
    public Uni<RestResponse<Void>> DeleteJoke(Long id) {
        return jokeService.DeleteJoke(id).onItem().invoke(j -> {
            if (!j) {
                throw new NotFoundException("Joke not found");
            }
        }).replaceWith(RestResponse.status(RestResponse.Status.NO_CONTENT, null));
    }
}

