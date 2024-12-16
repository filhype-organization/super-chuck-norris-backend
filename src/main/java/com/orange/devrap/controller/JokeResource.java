package com.orange.devrap.controller;

import com.orange.devrap.entity.Joke;
import com.orange.devrap.service.JokeService;
import io.quarkus.vertx.web.Body;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RoutingExchange;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class JokeResource {

    @Inject
    JokeService jokeService;

    @Route(path = "api/v1/jokes/getRandomJoke", methods = Route.HttpMethod.GET)
    void getAsyncRandomJoke(RoutingExchange ex) {
        jokeService.GetRandomJoke()
                .subscribe().with(
                        joke -> ex.response().putHeader("content-type", "application/json").end(JsonObject.mapFrom(joke).encode()),
                        error -> ex.response().setStatusCode(500).end()
                );
    }

    @Route(path = "api/v1/jokes/add", methods = Route.HttpMethod.POST)
    void addJoke(RoutingExchange ex, @Body Joke joke) {
        jokeService.AddJoke(joke)
                .subscribe().with(
                        j -> ex.response().putHeader("content-type", "application/json").end(JsonObject.mapFrom(j).encode()),
                        error -> ex.response().setStatusCode(500).end()
                );
    }
}
