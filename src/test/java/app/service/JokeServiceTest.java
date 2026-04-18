package app.service;

import app.entity.Joke;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class JokeServiceTest {

    @Inject
    JokeService jokeService;

    @Test
    void getRandomJoke() {
        assertNotNull(jokeService.getRandomJoke().id);
    }

    @Test
    void addJoke() {
        String text = "This is a test joke";
        Joke j = new Joke();
        j.joke = text;

        assertEquals(text, jokeService.addJoke(j).joke);
    }

    @Test
    void updateJoke() {
        String text = "This is a test joke";
        Joke j = new Joke();
        j.joke = text;

        Joke joke = jokeService.addJoke(j);
        joke.joke = "This is an updated test joke";

        assertEquals("This is an updated test joke", jokeService.updateJoke(joke).joke);
    }

    @Test
    void deleteJoke() {
        String text = "This is a test joke";
        Joke j = new Joke();
        j.joke = text;

        Joke joke = jokeService.addJoke(j);
        jokeService.deleteJoke(joke.id);

        assertTrue(Joke.findByIdOptional(joke.id).isEmpty());
    }
}