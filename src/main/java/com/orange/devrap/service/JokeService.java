package com.orange.devrap.service;

import com.orange.devrap.entity.Joke;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class JokeService {

    public Joke GetRandomJoke() {
        long count = Joke.count();
        int random = (int) (Math.random() * count);

        return Joke.findAll().page(random, 1).firstResult();
    }

    public Joke GetJokeById(Long id) {
        return Joke.findById(id);
    }

    @Transactional
    public Joke AddJoke(Joke joke) {
        joke.persist();
        return joke;
    }
}
