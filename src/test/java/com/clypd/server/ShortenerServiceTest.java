package com.clypd.server;

import org.junit.Test;
import static org.junit.Assert.*;
import static io.restassured.RestAssured.*;

/**
 *
 * @author mahen
 */
public class ShortenerServiceTest {

    @Test
    public void testHealthCheck() {
        String api = "/health_check";
        get(api)
                .then()
                .statusCode(200);
    }

    /**
     * Test of shorten method, of class ShortenerService.
     */
    @Test
    public void testShorten() throws Exception {
        String api = "/short";
        System.out.println("shorten");

        String body = given()
                .multiPart("content", "https://localhost:8080/testPath/service?param1=value1&param2=value2&param3=value3")
                .when()
                .post(api)
                .getBody()
                .prettyPrint();

        assertNotNull(body);
    }

    /**
     * Test of unShorten method, of class ShortenerService.
     */
    @Test
    public void testUnShorten() throws Exception {
        String api = "/";
        System.out.println("UnShorten");
        String id = "cd1f6bdd";
        String originalUrl = "https://localhost:8080/testPath/service?param1=value1&param2=value2&param3=value3";

        String body = given()
                .when()
                .get(api + id)
                .getBody()
                .prettyPrint();

        assertTrue(body.contains(originalUrl));
    }
    
    /**
     * Negative test to verify non obvious results
     */
    @Test
    public void testRandomUnShorten() throws Exception {
        String api = "/";
        System.out.println("UnShorten");
        String id = "12345";

        given()
                .when()
                .get(api + id)
                .then()
                .statusCode(400);
    }
}
