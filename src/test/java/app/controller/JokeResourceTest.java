package app.controller;

import app.entity.Joke;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@QuarkusTest
class JokeResourceTest {

    static final String basePath = "/api/jokes/v1";

    @ConfigProperty(name = "quarkus.oidc.client-id")
    String clientId;

    @ConfigProperty(name = "quarkus.oidc.credentials.secret")
    String clientSecret;

    @ConfigProperty(name = "test-url-keycloak")
    String authServerUrl;

    protected String GetAccessToken() {
        return "Bearer " + given().contentType("application/x-www-form-urlencoded")
                .formParam("grant_type", "client_credentials")
                .formParam("client_id", clientId)
                .formParam("client_secret", clientSecret)
                .when()
                .post(authServerUrl)
                .then()
                .statusCode(200)
                .extract().path("access_token");
    }

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
        String token = GetAccessToken();

        given().basePath(basePath)
                .header("Authorization", token)
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
        String token = GetAccessToken();

        Joke j  = Joke.findAll().firstResult();
        j.joke = newJoke;

        given().basePath(basePath)
                .header("Authorization", token)
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
        String token = GetAccessToken();

        given()
                .when().basePath(basePath)
                .header("Authorization", token)
                .pathParams("id", j.id)
                .delete("{id}")
                .then()
                .statusCode(204);
    }
}