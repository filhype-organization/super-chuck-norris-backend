package app.controller;

import app.entity.Joke;
import app.service.JokeService;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.NoCache;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.RestPath;

import java.util.List;
import java.util.UUID;

@Path("/api/v1/jokes")
public class JokeResource {

    @Inject
    JokeService jokeService;

    @GET
    public Response getAllJokes(@QueryParam("page") int page,
            @QueryParam("size") int size) {
        var count = jokeService.CountJokes();
        return Response.ok(jokeService.GetAllJokes(page, size))
                .header("X-Total-Count", count)
                .build();
    }

    @GET
    @Path("/getRandomJoke")
    @Produces("application/json")
    @RolesAllowed({"user","admin","read","write"})
    public Joke getRandomJoke() {
        Joke j = jokeService.GetRandomJoke();
        if (j == null) {
            throw new NotFoundException("Joke not found");
        }
        return j;
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    @RolesAllowed({"user","admin","read","write"})
    public PanacheEntityBase getJokeById(@RestPath UUID id) {
        return Joke.findByIdOptional(id).orElseThrow(NotFoundException::new);
    }

    @NoCache
    @ResponseStatus(201)
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @RolesAllowed({"admin","write"})
    public Joke addJoke(Joke joke) {
        return jokeService.AddJoke(joke);
    }

    @NoCache
    @PUT
    @Produces("application/json")
    @Consumes("application/json")
    @RolesAllowed({"admin","write"})
    public Joke updateJoke(Joke joke) {
        Joke j = jokeService.UpdateJoke(joke);
        if (j == null) {
            throw new NotFoundException("Joke not found");
        }
        return j;
    }

    @NoCache
    @DELETE
    @Path("{id}")
    @RolesAllowed({"admin","write"})
    public void deleteJoke(@RestPath UUID id) {
        Joke j = (Joke) Joke.findByIdOptional(id).orElseThrow(NoClassDefFoundError::new);
        jokeService.DeleteJoke(j.id);
    }
}
