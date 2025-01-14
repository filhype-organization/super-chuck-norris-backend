package app.service;

import app.entity.Joke;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.UUID;

@ApplicationScoped
public class JokeService {

    public Joke GetRandomJoke() {
        long count = Joke.count();
        int random = (int) (Math.random() * count);
        return Joke.findAll().page(random, 1).firstResult();
    }

    @Transactional
    public Joke AddJoke(Joke joke) {
        joke.persist();
        return joke;
    }

    @Transactional
    public Joke UpdateJoke(Joke jo) {
        var j = Joke.findByIdOptional(jo.id);
        if (j.isEmpty()) {
            return null;
        }
        ((Joke) j.get()).joke = jo.joke;
        j.get().persist();
        return (Joke) j.get();
    }

    @Transactional
    public void DeleteJoke(UUID id) {
       Joke.deleteById(id);
    }
}
