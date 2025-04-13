package app.controller;

import app.entity.Joke;
import app.service.JokeService;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.security.PermissionsAllowed;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.jboss.resteasy.reactive.NoCache;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.RestPath;
import java.util.UUID;

@Path("/api/jokes/v1")
public class JokeResource {

    @Inject
    JokeService jokeService;

    @GET
    @Path("/getRandomJoke")
    @Produces("application/json")
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
    public PanacheEntityBase getJokeById(@RestPath UUID id) {
        return Joke.findByIdOptional(id).orElseThrow(NotFoundException::new);
    }

    @NoCache
    @ResponseStatus(201)
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @PermissionsAllowed("admin")
    public Joke addJoke(Joke joke) {
        return jokeService.AddJoke(joke);
    }

    @NoCache
    @PUT
    @Produces("application/json")
    @Consumes("application/json")
    @RolesAllowed("admin")
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
    @RolesAllowed("admin")
    public void deleteJoke(@RestPath UUID id) {
        Joke j = (Joke) Joke.findByIdOptional(id).orElseThrow(NoClassDefFoundError::new);
        jokeService.DeleteJoke(j.id);
    }
}
