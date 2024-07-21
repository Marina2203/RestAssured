package ru.academits.marina.mantis;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountTest {

    private String PHPSESSID;
    private String MANTIS_secure_session;
    private String MANTIS_STRING_COOKIE;
    private Map<String, String> cookies = new HashMap<>();

    @BeforeEach
    public void getCookies() {
        Response responseLogin = RestAssured
                .given()
                .contentType("application/x-www-form-urlencoded")
                .body("return=index.php&username=admin&password=admin20&secure_session=on")
                .post("https://academ-it.ru/mantisbt/login.php")
                .andReturn();

        PHPSESSID = responseLogin.cookie("PHPSESSID");
        System.out.println("PHPSESSID = " + PHPSESSID);

        MANTIS_secure_session = responseLogin.cookie("MANTIS_secure_session");
        System.out.println("MANTIS_secure_session = " + MANTIS_secure_session);

        MANTIS_STRING_COOKIE = responseLogin.cookie("MANTIS_STRING_COOKIE");
        System.out.println("MANTIS_STRING_COOKIE = " + MANTIS_STRING_COOKIE);

        cookies.put("PHPSESSID", PHPSESSID);
        cookies.put("MANTIS_secure_session", MANTIS_secure_session);
        cookies.put("MANTIS_STRING_COOKIE", MANTIS_STRING_COOKIE);
    }

    @Test
    public void getAccountPageTest() {

        Response response = RestAssured
                .given()
                .cookies(cookies)
                .get("https://academ-it.ru/mantisbt/account_page.php")
                .andReturn();

        System.out.println("\nResponse:");
        response.prettyPrint();

        assertEquals(200, response.statusCode(), "Response status code is not expected");
        assertTrue(response.body().asString().contains("Real Name"));
    }

    @Test
    public void updateAccountTest() {
        String realName = Instant.now().toString();

        Response responseRealName = RestAssured
                .given()
                .contentType("application/x-www-form-urlencoded")
                .cookies(cookies)
                .body("password_current=&password=&password_confirm=&email=rovan3014%40mail.ru&realname=" + realName)
                .post("https://academ-it.ru/mantisbt/account_update.php")
                .andReturn();

        System.out.println("\nUpdate Real Name Response:");
        responseRealName.prettyPrint();

        assertEquals(200, responseRealName.statusCode(), "Response status code is not expected");
        assertTrue(responseRealName.body().asString().contains("Real name successfully updated"));

        Response response = RestAssured
                .given()
                .cookies(cookies)
                .get("https://academ-it.ru/mantisbt/account_page.php")
                .andReturn();

        System.out.println("\nGet Account Response:");
        response.prettyPrint();

        assertEquals(200, response.statusCode(), "Response status code is not expected");
        assertTrue(response.body().asString().contains(realName));
    }
}
