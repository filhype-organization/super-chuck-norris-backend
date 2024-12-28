package com.orange.devrap.controller;

import com.orange.devrap.entity.Joke;
import com.orange.devrap.service.JokeService;
import io.quarkus.vertx.web.Body;
import io.quarkus.vertx.web.Param;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RoutingExchange;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("api/v1/jokes")
@ApplicationScoped
public class JokeResource {

    @Inject
    JokeService jokeService;

    @Route(path = "api/v1/jokes/getById/:id", methods = Route.HttpMethod.GET)
    void getAsyncJokeById(@Param Long id, RoutingExchange ex) {
        jokeService.GetJokeById(id)
                .subscribe().with(
                        joke -> {
                            if (joke == null) {
                                ex.response().setStatusCode(404).end("Joke not found");
                            } else {
                                ex.response()
                                        .putHeader("content-type", "application/json")
                                        .end(JsonObject.mapFrom(joke).encode());
                            }
                        },
                        error -> ex.response().setStatusCode(500).end("Internal Server Error")
                );
    }

    @Produces("application/json")
    @Path("/getById2/{id:\\d+}")
    @GET
    public Uni<Joke> GetJokeById(Long id) {
        Uni<Joke> j = jokeService.GetJokeById(id);
        if(j == null) {
            return new Uni<void>().
        }else {
            return j;
        }
    }

    @Route(path = "api/v1/jokes/getRandomJoke", methods = Route.HttpMethod.GET)
    void getAsyncRandomJoke(RoutingExchange ex) {
        jokeService.GetRandomJoke()
            .subscribe().with(
                    joke -> ex.response().putHeader("content-type", "application/json").end(JsonObject.mapFrom(joke).encode()),
                    error -> ex.response().setStatusCode(500).end()
            );
    }

    @Route(path = "api/v1/jokes/add", methods = Route.HttpMethod.POST)
    public void addJoke(RoutingExchange ex, @Body Joke joke) {
        jokeService.AddJoke(joke).subscribe().with(
                j -> ex.response().setStatusCode(201).putHeader("content-type", "application/json").end(JsonObject.mapFrom(j).encode()),
                error -> ex.response().setStatusCode(500).end()
        );

    }

    @Route(path = "api/v1/jokes/update", methods = Route.HttpMethod.PUT)
    public void updateJoke(RoutingExchange ex, @Body Joke joke) {
        jokeService.UpdateJoke(joke).subscribe().with(
                j -> ex.response().putHeader("content-type", "application/json").end(JsonObject.mapFrom(j).encode()),
                error -> ex.response().setStatusCode(500).end()
        );
    }

    @Route(path = "api/v1/jokes/delete", methods = Route.HttpMethod.DELETE)
    public void deleteJoke(RoutingExchange ex, @Body Joke joke) {
        jokeService.DeleteJoke(joke.id).subscribe().with(
                j -> ex.response().setStatusCode(204).end(),
                error -> ex.response().setStatusCode(500).end()
        );
    }


}
