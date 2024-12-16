package com.orange.devrap.service;

import com.orange.devrap.entity.Joke;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReactiveJokeService {

    public Uni<Joke> GetRandomJoke() {
        long count = Joke.count();
        int random = (int) (Math.random() * count);

        return Joke.findAll().page(random, 1).firstResult();
    }

//    @WithTransaction
//    public Uni<Joke> AddJoke(Joke joke) {
//        joke.persist();
//        return (Uni<Joke>) joke;
//    }
}
