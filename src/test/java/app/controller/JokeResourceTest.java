package app.controller;

import app.entity.Joke;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@QuarkusTest
class JokeResourceTest {

    static final String basePath = "/api/jokes/v1";

    @Test
    void getRandomJoke() {
        given()
                .when().basePath(basePath)
                .get("getRandomJoke")
                .then()
                .statusCode(200)
                .body(containsString("created_at"));
    }

    @Test
    void getJokeById() {
        Joke j  = Joke.findAll().firstResult();

        given()
                .when().basePath(basePath)
                .pathParams("id", j.id)
                .get("{id}")
                .then()
                .statusCode(200)
                .body(containsString("created_at"));
    }

    @Test
    void addJoke() {
        String newJoke = "This is a test joke";

        given().basePath(basePath)
                .body(String.format("{\"joke\":\"%s\"}", newJoke))
                .contentType("application/json")
                .when()
                .post()
                .then()
                .statusCode(201)
                .body(containsString(newJoke));
    }

    @Test
    void updateJoke() {
        String newJoke = "This is a test2 joke";
        Joke j  = Joke.findAll().firstResult();
        j.joke = newJoke;

        given().basePath(basePath)
                .body(j)
                .contentType("application/json")
                .when()
                .put()
                .then()
                .statusCode(200)
                .body(containsString(newJoke));
    }

    @Test
    void deleteJoke() {
        Joke j  = Joke.findAll().firstResult();

        given()
                .when().basePath(basePath)
                .pathParams("id", j.id)
                .delete("{id}")
                .then()
                .statusCode(204);
    }
}