package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.http.ContentType.JSON;

public class Specs {

    public static RequestSpecification loginRequestSpec = with()
            .filter(withCustomTemplates())
            .log().all()
            .contentType(JSON)
            .header("x-api-key", "reqres-free-v1");

    public static ResponseSpecification responseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .build();

    public static ResponseSpecification getResponseSpecification(int statusCode) {
        return new ResponseSpecBuilder()
                .log(ALL)
                .expectStatusCode(statusCode)
                .build();
    }
}
