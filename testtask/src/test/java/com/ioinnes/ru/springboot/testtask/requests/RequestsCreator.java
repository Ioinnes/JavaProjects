package com.ioinnes.ru.springboot.testtask.requests;

import com.ioinnes.ru.springboot.testtask.entity.ActivityRequestDTO;
import com.ioinnes.ru.springboot.testtask.entity.UserDTO;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.filter.log.RequestLoggingFilter;
import com.jayway.restassured.filter.log.ResponseLoggingFilter;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.specification.RequestSpecification;
import org.springframework.stereotype.Component;


import java.io.File;


@Component
public class RequestsCreator {
    public RequestSpecification createGetRequestSpecification(int id) {
        return createBasisSpecBuilderWithID(id)
                .setContentType(ContentType.JSON)
                .build();
    }

    public RequestSpecification createGetAllRequestSpecification() {
        return createBasisSpecBuilderWithoutID()
                .build();
    }

    public RequestSpecification createPostRequestSpecification(String imageURI, String userName, String email, String mobileNumber) {
        return createBasisSpecBuilderWithoutID()
                .setContentType(ContentType.JSON)
                .setContent(new UserDTO(imageURI, userName, email, mobileNumber))
                .build();
    }

    public RequestSpecification createDeleteRequestSpecification(int id) {
        return  createBasisSpecBuilderWithID(id)
                .build();
    }

    public RequestSpecification createPutRequestSpecification(int id, String status) {
        return createBasisSpecBuilderWithID(id)
                .addParameter("status", status)
                .build();
    }

    public RequestSpecification createPostImageRequestSpecification(File file) {
        return createBasicImageUploadSpecBuilder()
                .addMultiPart("image", file)
                .build();
    }

    public RequestSpecification createGetWithConditionRequestCondition(String status, Integer timestamp) {
        RequestSpecBuilder requestSpecBuilder = createBasicGetWithConditionSpecBuilder();
        if (status == null && timestamp == null) {
            return requestSpecBuilder
                    .setBody(new ActivityRequestDTO(null, null))
                    .build();
        } else if (status == null)
            return requestSpecBuilder
                    .setBody(new ActivityRequestDTO(null, timestamp))
                    .build();
        else if (timestamp == null)
            return requestSpecBuilder
                    .setBody(new ActivityRequestDTO(status, null))
                    .build();
        else return requestSpecBuilder
                    .setBody(new ActivityRequestDTO(status, timestamp))
                    .build();
    }

    public RequestSpecBuilder createBasisSpecBuilderWithID(int id) {
        return new RequestSpecBuilder()
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new RequestLoggingFilter())
                .setBaseUri("http://localhost:8080/users/" + id);
    }

    public RequestSpecBuilder createBasisSpecBuilderWithoutID() {
        return new RequestSpecBuilder()
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new RequestLoggingFilter())
                .setBaseUri("http://localhost:8080/users/");

    }

    public RequestSpecBuilder createBasicImageUploadSpecBuilder() {
        return new RequestSpecBuilder()
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new RequestLoggingFilter())
                .setBaseUri("http://localhost:8080/images/upload");
    }


    public RequestSpecBuilder createBasicGetWithConditionSpecBuilder() {
        return new RequestSpecBuilder()
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new RequestLoggingFilter())
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .setContent(ContentType.JSON)
                .setBaseUri("http://localhost:8080/users/activity");

    }






}
