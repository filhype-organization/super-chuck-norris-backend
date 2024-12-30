package com.orange.devrap.service;

import com.orange.devrap.entity.Joke;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JokeService {

    @WithSession
    public Uni<Joke> GetJokeById(Long id) {
        return Joke.findById(id);
    }

    @WithSession
    public Uni<Joke> GetRandomJoke() {
        return Joke.count()
                .map(count -> (int) (Math.random() * count))
                .chain(random -> Joke.findAll().page(random, 1).firstResult());
    }

    @WithTransaction
    public Uni<Joke> AddJoke(Joke joke) {
        return joke.persist().map(j -> joke);
    }

    @WithTransaction
    public Uni<Joke> UpdateJoke(Joke updateJoke) {
        return Joke.<Joke>findById(updateJoke.id)
                .onItem().ifNotNull().invoke(findJoke -> {
                    findJoke.joke = updateJoke.joke;
                    findJoke.created_at = updateJoke.created_at;
                    findJoke.persist();
                });
    }

    @WithTransaction
    public Uni<Boolean> DeleteJoke(Long id) {
        return Joke.deleteById(id);
    }

}
