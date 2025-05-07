package tests;

import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import models.SuccessfulRegisterBodyModel;
import models.SuccessfulRegisterResponseModel;
import models.UnsuccessfulRegisterBodyModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static specs.Specs.*;

public class RegisterSpecModelTests {

    @BeforeAll
    static void setupEnvironment() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
        Configuration.remote = System.getProperty("browserRemote", "https://user1:1234@selenoid.autotests.cloud/wd/hub");
    }

    @Test
    @DisplayName("Проверка успешной авторизации и получения токена")
    void successfulRegisterTest() {

        SuccessfulRegisterBodyModel registerData = new SuccessfulRegisterBodyModel();
        registerData.setEmail("eve.holt@reqres.in");
        registerData.setPassword("pistol");

        SuccessfulRegisterResponseModel response = step("Успешный регистрационный тест", () ->
                given(loginRequestSpec)
                        .body(registerData)

                        .when()
                        .post("/register")

                        .then()
                        .spec(loginResponseSpec)
                        .extract().as(SuccessfulRegisterResponseModel.class));

        step("Проверка ответа", () ->
                assertNotNull(response.getToken()));
    }

    @Test
    @DisplayName("Проверка неуспешной авторизации. Отсутствует email и пароль")
    void unsuccessfulRegister400Test() {

        SuccessfulRegisterBodyModel registerData = new SuccessfulRegisterBodyModel();

        UnsuccessfulRegisterBodyModel response = step("Неуспешный регистрационный тест", () ->
                given(loginRequestSpec)
                        .body(registerData)

                        .when()
                        .post("/register")

                        .then()
                        .spec(errorResponseSpec)
                        .extract().as(UnsuccessfulRegisterBodyModel.class));

        step("Проверка ответа", () ->
                assertEquals("Missing email or username", response.getError()));
    }

    @Test
    @DisplayName("Проверка неуспешной авторизации. Невалидный email")
    void errorUserNotFoundTest() {

        SuccessfulRegisterBodyModel registerData = new SuccessfulRegisterBodyModel();
        registerData.setEmail("evedat.holt@reqres.in");
        registerData.setPassword("pistol");

        UnsuccessfulRegisterBodyModel response = step("Авторизация с невалидным email", () ->
                given(loginRequestSpec)
                        .body(registerData)

                        .when()
                        .post("/register")

                        .then()
                        .spec(errorUserResponseSpec)
                        .extract().as(UnsuccessfulRegisterBodyModel.class));

        step("Проверка ответа", () ->
                assertEquals("Note: Only defined users succeed registration", response.getError()));
    }

    @Test
    @DisplayName("Проверка неуспешной авторизации. Отсутствует email")
    void missingEmailTest() {

        SuccessfulRegisterBodyModel registerData = new SuccessfulRegisterBodyModel();
        registerData.setPassword("pistol");

        UnsuccessfulRegisterBodyModel response = step("Авторизация с пропущенным email", () ->
                given(loginRequestSpec)
                        .body(registerData)

                        .when()
                        .post("/register")

                        .then()
                        .spec(missingEmailResponseSpec)
                        .extract().as(UnsuccessfulRegisterBodyModel.class));

        step("Проверка ответа", () ->
                assertEquals("Missing email or username", response.getError()));
    }

    @Test
    @DisplayName("Проверка неуспешной авторизации. Невалидный пароль")
    void errorPasswordTest() {

        SuccessfulRegisterBodyModel registerData = new SuccessfulRegisterBodyModel();
        registerData.setEmail("eve.holt@reqres.in");
        registerData.setPassword("pistolet");

        UnsuccessfulRegisterBodyModel response = step("Авторизация с невалидным паролем", () ->
                given(loginRequestSpec)
                        .body(registerData)

                        .when()
                        .post("/register")

                        .then()
                        .spec(errorPasswordResponseSpec)
                        .extract().as(UnsuccessfulRegisterBodyModel.class));

        step("Проверка ответа", () ->
                assertEquals("Note: Only defined users succeed registration", response.getError()));
    }

    @Test
    @DisplayName("Проверка неуспешной авторизации. Отсутствует пароль")
    void missingPasswordTest() {

        SuccessfulRegisterBodyModel registerData = new SuccessfulRegisterBodyModel();
        registerData.setEmail("eve.holt@reqres.in");

        UnsuccessfulRegisterBodyModel response = step("Авторизация с пропущенным паролем", () ->
                given(loginRequestSpec)
                        .body(registerData)

                        .when()
                        .post("/register")

                        .then()
                        .spec(missingPasswordResponseSpec)
                        .extract().as(UnsuccessfulRegisterBodyModel.class));

        step("Проверка ответа", () ->
                assertEquals("Missing password", response.getError()));
    }
}


