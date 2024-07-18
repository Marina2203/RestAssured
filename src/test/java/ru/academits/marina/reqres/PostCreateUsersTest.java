package ru.academits.marina.reqres;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;


public class PostCreateUsersTest {

    private static final String BASE_URL = "https://reqres.in/api";

    private static final String USERS_URL = BASE_URL + "/users";

    private static final String USERNAME_FIELD = "name";
    private static final String USERNAME = "Ivan";

    private static final String JOB_FIELD = "job";
    private static final String JOB = "Engineer";



    @Test
    public void createUserRequestIsSuccessful() {
        Map<String, String> body = new HashMap<>();
        body.put(USERNAME_FIELD, USERNAME);
        body.put(JOB_FIELD, JOB);

        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(USERS_URL)
                .andReturn();
        response.prettyPrint();

        Assertions.assertEquals(201, response.statusCode());
    }

    @Test
    public void createUserResponseHasProperFields() {
        Map<String, String> body = new HashMap<>();
        body.put(USERNAME_FIELD, USERNAME);
        body.put(JOB_FIELD, JOB);

        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(USERS_URL)
                .andReturn();
        response.prettyPrint();

        Assertions.assertEquals(201, response.statusCode());

        String[] expectedFields = {"name", "job", "id", "createdAt"};
        for (String expectedField : expectedFields) {
            response.then().assertThat().body("$", hasKey(expectedField));
        }
    }

    @Test
    public void createUserResponseFieldsHasProperValues() {
        Map<String, String> body = new HashMap<>();
        body.put(USERNAME_FIELD, USERNAME);
        body.put(JOB_FIELD, JOB);

        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(USERS_URL)
                .andReturn();
        response.prettyPrint();

        Assertions.assertEquals(201, response.statusCode());

        Assertions.assertEquals(USERNAME, response.jsonPath().getString("name"));
        Assertions.assertEquals(JOB, response.jsonPath().getString("job"));
        Assertions.assertNotNull(response.jsonPath().getString("id"));
        Assertions.assertNotNull(response.jsonPath().getString("createdAt"));
    }

    @Test
    public void createUserRequestWrongUrlIsFailed() {
        Response response = RestAssured
                .given()
                .when()
                .post("https://reqres.in/api/jfhwfljwa")
                .andReturn();
        response.prettyPrint();

        Assertions.assertEquals(415, response.statusCode());
    }

    @Test
    public void createUserWithoutBodyIsSuccessful() {
        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .post(USERS_URL)
                .andReturn();
        response.prettyPrint();

        Assertions.assertEquals(201, response.statusCode());

        response.then().assertThat().body("$", not(hasKey(USERNAME_FIELD)));
        response.then().assertThat().body("$", not(hasKey(JOB_FIELD)));
        Assertions.assertNotNull(response.jsonPath().getString("id"));
        Assertions.assertNotNull(response.jsonPath().getString("createdAt"));
    }

}
