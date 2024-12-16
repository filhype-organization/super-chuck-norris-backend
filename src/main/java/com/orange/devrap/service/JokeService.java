package com.orange.devrap.service;

import com.orange.devrap.entity.Joke;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JokeService {

    @WithSession
    public Uni<Joke> GetRandomJoke() {
        return Joke.count()
                .map(count -> (int) (Math.random() * count))
                .chain(random -> Joke.findAll().page(random, 1).firstResult());
    }

    @WithSession
    public Uni<Joke> AddJoke(Joke joke) {
        return joke.persist().map(j -> joke);
    }
}
