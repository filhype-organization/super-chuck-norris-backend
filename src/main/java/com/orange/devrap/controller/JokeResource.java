package com.orange.devrap.controller;

import lombok.val;
import com.orange.devrap.service.ReactiveJokeService;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RoutingExchange;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Produces;

@ApplicationScoped
public class JokeResource {

    @Inject
    ReactiveJokeService jokeService;

    @Produces("application/json")
    @Route(path = "api/v1/jokes/getRandomJoke2", methods = Route.HttpMethod.GET)
    void getAsyncRandomJoke(RoutingExchange ex) {
        //Joke j = jokeService.GetRandomJoke().await().indefinitely();
        val j = jokeService.GetRandomJoke().await().indefinitely();
//        if(j ==null){
//            Response.status(500).entity(new Joke(null,"no joke found")).build();
//        }
        ex.ok(String.valueOf(j));
    }

}
