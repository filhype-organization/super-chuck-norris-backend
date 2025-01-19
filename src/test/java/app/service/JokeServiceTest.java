package app.service;

import app.entity.Joke;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class JokeServiceTest {

    @Inject
    JokeService jokeService;

    @Test
    void getRandomJoke() {
        assert jokeService.GetRandomJoke().id != null;
    }

    @Test
    void addJoke() {
        String text = "This is a test joke";
        Joke j = new Joke();
        j.joke = text;

        assert jokeService.AddJoke(j).joke.equals(text);
    }

    @Test
    void updateJoke() {
        String text = "This is a test joke";
        Joke j = new Joke();
        j.joke = text;

        Joke joke = jokeService.AddJoke(j);
        joke.joke = "This is an updated test joke";

        assert jokeService.UpdateJoke(joke).joke.equals("This is an updated test joke");
    }

    @Test
    void deleteJoke() {
        String text = "This is a test joke";
        Joke j = new Joke();
        j.joke = text;

        Joke joke = jokeService.AddJoke(j);
        jokeService.DeleteJoke(joke.id);

        assert Joke.findByIdOptional(joke.id).isEmpty();
    }
}