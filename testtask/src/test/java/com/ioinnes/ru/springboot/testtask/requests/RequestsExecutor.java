package com.ioinnes.ru.springboot.testtask.requests;

import com.ioinnes.ru.springboot.testtask.entity.UserDTO;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.specification.RequestSpecification;
import org.springframework.stereotype.Component;

import static com.jayway.restassured.RestAssured.given;

@Component
public class RequestsExecutor {
    public UserDTO executeExistingIDGetRequest(RequestSpecification requestSpecification) {
        return given()
                .spec(requestSpecification)
                .when()
                .get("/")
                .then()
                .statusCode(200)
                .extract().as(UserDTO.class);
    }

    public String executeNotExistingIDGetRequest(RequestSpecification requestSpecification) {
        return given()
                .spec(requestSpecification)
                .when()
                .get("/")
                .then()
                .statusCode(200)
                .extract().body().asString();
    }


    public String executePostRequest(RequestSpecification requestSpecification) {
        return given()
                .spec(requestSpecification)
                .accept(ContentType.TEXT)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract().body().asString();
    }

    public void executeDeleteRequest(RequestSpecification requestSpecification) {
        given()
                .spec(requestSpecification)
                .accept(ContentType.TEXT)
                .when()
                .delete()
                .then()
                .statusCode(200)
                .extract().body().asString();
    }

    public String executePutRequest(RequestSpecification requestSpecification) {
        return given()
                .spec(requestSpecification)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract().body().asString();
    }
}
