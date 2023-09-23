package org.example;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.Condition;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class SimpleBooks {

    public static String BaseURI = "https://simple-books-api.glitch.me";
    public static String AccessKey = "";




    @Test(priority = 1)
    public void authenticate(){

        String requestBody = "{\n" +
                "    \"clientName\": \"Shubhrajit1\",\n" +
                "    \"clientEmail\": \"Shubhrajit1@example.com\"\n" +
                "}";
        RestAssured.baseURI= BaseURI;
        Response resp = given()
                .header("Content-Type","application/json")
                .body(requestBody)
                .when()
                .post("/api-clients/")
                .then()
                .assertThat().statusCode(201)
                .extract().response();

        String JsonResp = resp.asPrettyString();
        JsonPath js = new JsonPath(JsonResp);
        AccessKey = js.get("accessToken");

        System.out.println("AccessKey is :"+AccessKey);


    }





}