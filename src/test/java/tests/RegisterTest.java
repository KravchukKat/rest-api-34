package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

@Tag("Api")
public class RegisterTest {

    String apiKey = "reqres-free-v1";

    @BeforeAll
    static void setupEnvironment() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }

    @Test
    void successfulRegisterTest() {
        String registerData = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\"}";

        given()
                .header("x-api-key", apiKey)
                .body(registerData)
                .contentType(JSON)
                .log().uri()

                .when()
                .post("/register")

                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("id", is(4))
                .body("token", notNullValue());

    }

    @Test
    void unsuccessfulRegister400Test() {
        String registerData = "";

        given()
                .header("x-api-key", apiKey)
                .body(registerData)
                .contentType(JSON)
                .log().uri()

                .when()
                .post("/register")

                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing email or username"));
    }

    @Test
    void errorUserNotFoundTest() {
        String registerData = "{\"email\": \"evedat.holt@reqres.in\", \"password\": \"pistol\"}";

        given()
                .header("x-api-key", apiKey)
                .body(registerData)
                .contentType(JSON)
                .log().uri()

                .when()
                .post("/register")

                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Note: Only defined users succeed registration"));
    }

    @Test
    void missingEmailTest() {
        String registerData = "{\"password\": \"pistol\"}";

        given()
                .header("x-api-key", apiKey)
                .body(registerData)
                .contentType(JSON)
                .log().uri()

                .when()
                .post("/register")

                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing email or username"));
    }

    @Test
    void errorPasswordTest() {
        String registerData = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"pistolet\"}";

        given()
                .header("x-api-key", apiKey)
                .body(registerData)
                .contentType(JSON)
                .log().uri()

                .when()
                .post("/register")

                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("id", is(4))
                .body("token", notNullValue());
    }

    @Test
    void missingPasswordTest() {
        String registerData = "{\"email\": \"eve.holt@reqres.in\"}";

        given()
                .header("x-api-key", apiKey)
                .body(registerData)
                .contentType(JSON)
                .log().uri()

                .when()
                .post("/register")

                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    void wrongBodyTest() {
        String registerData = "%}";

        given()
                .header("x-api-key", apiKey)
                .body(registerData)
                .contentType(JSON)
                .log().uri()

                .when()
                .post("/register")

                .then()
                .log().status()
                .log().body()
                .statusCode(400);
    }

}
