package com.ioinnes.ru.springboot.testtask.requests;

import com.ioinnes.ru.springboot.testtask.entity.UserDTO;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.filter.log.RequestLoggingFilter;
import com.jayway.restassured.filter.log.ResponseLoggingFilter;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.specification.RequestSpecification;
import org.springframework.stereotype.Component;

@Component
public class RequestsCreator {
    public RequestSpecification createGetRequestSpecification(int id) {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri("http://localhost:8080/users/" + id)
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new RequestLoggingFilter())
                .build();
    }

    public RequestSpecification createPostRequestSpecification(String imageURI, String userName, String email, String mobileNumber) {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri("http://localhost:8080/users/")
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new RequestLoggingFilter())
                .setContent(new UserDTO(imageURI, userName, email, mobileNumber))
                .build();
    }

    public RequestSpecification createDeleteRequestSpecification(int id) {
        return new RequestSpecBuilder()
                .setBaseUri("http://localhost:8080/users/" + id)
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();
    }

    public RequestSpecification createPutRequestSpecification(int id, String status) {
        return new RequestSpecBuilder()
                .setBaseUri("http://localhost:8080/users/" + id)
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .addParameter("status", status)
                .build();
    }

}
