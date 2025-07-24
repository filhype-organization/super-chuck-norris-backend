package app.service;

import app.entity.Joke;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class JokeService {

    public Joke GetRandomJoke() {
        long count = Joke.count();
        int random = (int) (Math.random() * count);
        return Joke.findAll().page(random, 1).firstResult();
    }

    public Long CountJokes() {
        return Joke.count();
    }

    public List<Joke> GetAllJokes(int pageIndex, int pageSize) {
        return Joke.findAll().page(pageIndex, pageSize).list();
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
