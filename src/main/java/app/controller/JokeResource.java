package app.controller;

import app.entity.Joke;
import app.service.JokeService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.RestPath;

import java.util.List;
import java.util.UUID;

@Path("/")
public class JokeResource {

    @Inject
    JokeService jokeService;

    @GET
    @Produces("application/json")
    public Response getAllJokes(@QueryParam("page") int page,
            @QueryParam("size") int size) {
        var count = jokeService.countJokes();
        List<Joke> listJoke = jokeService.getAllJokes(page, size);
        return Response.ok(listJoke)
                .header("X-Total-Count", count)
                .build();
    }

    @GET
    @Path("getRandomJoke")
    @Produces("application/json")
    @RolesAllowed({"user","admin","read","write"})
    public Joke getRandomJoke() {
        return jokeService.getRandomJoke();
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    @RolesAllowed({"user","admin","read","write"})
    public Joke getJokeById(@RestPath UUID id) {
        return jokeService.getJokeById(id);
    }

    @ResponseStatus(201)
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @RolesAllowed({"admin","write"})
    public Joke addJoke(Joke joke) {
        return jokeService.addJoke(joke);
    }

    @PUT
    @Produces("application/json")
    @Consumes("application/json")
    @RolesAllowed({"admin","write"})
    public Joke updateJoke(Joke joke) {
        return jokeService.updateJoke(joke);
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed({"admin","write"})
    public void deleteJoke(@RestPath UUID id) {
        jokeService.deleteJoke(id);
    }
}
