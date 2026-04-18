package app.service;

import app.entity.Joke;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class JokeService {

    public Joke getRandomJoke() {
        long count = Joke.count();
        if (count == 0) {
            throw new NotFoundException("No jokes available");
        }
        int random = (int) (Math.random() * count);
        Joke j = Joke.findAll().page(random, 1).firstResult();
        if (j == null) {
            throw new NotFoundException("Joke not found");
        }
        return j;
    }

    public Long countJokes() {
        return Joke.count();
    }

    public List<Joke> getAllJokes(int pageIndex, int pageSize) {
        return Joke.findAll().page(pageIndex, pageSize).list();
    }

    public Joke getJokeById(UUID id) {
        return (Joke) Joke.findByIdOptional(id).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public Joke addJoke(Joke joke) {
        joke.persist();
        return joke;
    }

    @Transactional
    public Joke updateJoke(Joke jo) {
        var j = Joke.findByIdOptional(jo.id);
        if (j.isEmpty()) {
            throw new NotFoundException("Joke not found");
        }
        Joke existing = (Joke) j.get();
        existing.joke = jo.joke;
        existing.persist();
        return existing;
    }

    @Transactional
    public void deleteJoke(UUID id) {
        boolean deleted = Joke.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("Joke not found");
        }
    }
}
