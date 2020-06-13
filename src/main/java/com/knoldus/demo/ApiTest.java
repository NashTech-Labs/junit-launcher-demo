package com.knoldus.demo;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ApiTest {

    private final static Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @SmokeTest
    public void makeGetCall() {

        logger.info("Hitting Get Request !!");
        Response response = given().when().get("https://reqres.in/api/users/2").then().using().extract().response();
        Map<String, String> data = response.jsonPath().getMap("data");
        Assert.assertEquals(response.statusCode() ,200);
        Assert.assertEquals(data.get("id"), 2);

    }
}
