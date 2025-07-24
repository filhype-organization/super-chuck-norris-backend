package app.controller;

import app.entity.Joke;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.specification.RequestSpecification;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@QuarkusTest
class JokeResourceTest {

    private static final String BASE_PATH = "/api/v1/jokes";
    private RequestSpecification requestSpec;

    @ConfigProperty(name = "quarkus.oidc.client-id")
    String clientId;

    @ConfigProperty(name = "quarkus.oidc.credentials.secret")
    String clientSecret;

    @ConfigProperty(name = "test-url-keycloak")
    String authServerUrl;

    @BeforeEach
    void setUp() {
        String accessToken = getAccessToken();
        requestSpec = given()
                .basePath(BASE_PATH)
                .header("Authorization", accessToken)
                .contentType("application/json");
    }

    private String getAccessToken() {
        return "Bearer " + given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("grant_type", "client_credentials")
                .formParam("client_id", clientId)
                .formParam("client_secret", clientSecret)
                .when()
                .post(authServerUrl)
                .then()
                .statusCode(200)
                .extract().path("access_token");
    }

    private Joke getFirstJoke() {
        return Joke.findAll().firstResult();
    }

    @Test
    void getRandomJoke() {
        requestSpec
                .when()
                .get("getRandomJoke")
                .then()
                .statusCode(200)
                .body(containsString("created_at"));
    }

    @Test
    void getJokeById() {
        Joke joke = getFirstJoke();
        requestSpec
                .pathParams("id", joke.id)
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .body(containsString("created_at"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"This is a test joke"})
    void addJoke(String newJoke) {
        requestSpec
                .body(String.format("{\"joke\":\"%s\"}", newJoke))
                .when()
                .post()
                .then()
                .statusCode(201)
                .body(containsString(newJoke));
    }

    @ParameterizedTest
    @ValueSource(strings = {"This is a test2 joke"})
    void updateJoke(String newJoke) {
        Joke joke = getFirstJoke();
        joke.joke = newJoke;

        requestSpec
                .body(joke)
                .when()
                .put()
                .then()
                .statusCode(200)
                .body(containsString(newJoke));
    }

    @Test
    void deleteJoke() {
        Joke joke = getFirstJoke();
        requestSpec
                .pathParams("id", joke.id)
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }
}