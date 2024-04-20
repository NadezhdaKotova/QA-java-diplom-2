package Constants;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UrlApi {
    public static final String MAIN_URL = "https://stellarburgers.nomoreparties.site";
    public static final String USER_REGISTER = "api/auth/register";
    public static final String USER = "api/auth/user";
    public static final String USER_AUTH = "/api/auth/login";


    //указываем, что нам надо иметь в спецификации URL и Content-Type Json.
    public static RequestSpecification spec() {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(MAIN_URL)
                .log()
                .all();

    }
}
