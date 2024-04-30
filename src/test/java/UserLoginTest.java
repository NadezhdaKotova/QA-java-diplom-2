import user.User;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;
import user.UserMethods;
@DisplayName("Логин пользователя: POST /api/auth/login")
public class UserLoginTest {
    String accessToken;
    @Test
    @DisplayName("Успешная авторизация")
    @Description("Логин под существующим пользователем")
    public void loginSuccessUserExists() {
        User user = UserMethods.createNewUser();
        Response response = UserMethods.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        response = UserMethods.loginUser(user);
        response.then().log().all()
                .assertThat().statusCode(200)
                .and().body("success", Matchers.is(true))
                .and().body("accessToken", Matchers.notNullValue())
                .and().body("refreshToken", Matchers.notNullValue())
                .and().body("user.email", Matchers.equalTo(user.getEmail()))
                .and().body("user.name", Matchers.equalTo(user.getName()));
    }
    @Test
    @DisplayName("Успешная авторизация без параметра имя")
    @Description("Логин под существующим пользователем. Имя не передано")
    public void loginSuccessUserExistsWithoutName() {
        User user = UserMethods.createNewUser();
        Response response = UserMethods.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        user.setName(null);
        response = UserMethods.loginUser(user);
        response.then().log().all()
                .assertThat().statusCode(200)
                .and().body("success", Matchers.is(true))
                .and().body("accessToken", Matchers.notNullValue())
                .and().body("refreshToken", Matchers.notNullValue())
                .and().body("user.email", Matchers.equalTo(user.getEmail()));
    }
    @Test
    @DisplayName("Неуспешная авторизация без параметра email")
    @Description("Параметр Email не передано")
    public void loginSuccessUserExistsWithoutEmail() {
        User user = UserMethods.createNewUser();
        Response response = UserMethods.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        user.setEmail(null);
        user.setName(null);
        response = UserMethods.loginUser(user);
        response.then().log().all()
                .assertThat().statusCode(401)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("email or password are incorrect"));
    }
    @Test
    @DisplayName("Неуспешная авторизация без параметра password")
    @Description("Параметр Password не передано")
    public void loginSuccessUserExistsWithoutPassword() {
        User user = UserMethods.createNewUser();
        Response response = UserMethods.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        user.setPassword(null);
        user.setName(null);
        response = UserMethods.loginUser(user);
        response.then().log().all()
                .assertThat().statusCode(401)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("email or password are incorrect"));
    }
    @Test
    @DisplayName("Неуспешная авторизация email не существует в БД")
    @Description("Параметр Password не передано")
    public void loginUnsuccessfulUserNotExists() {
        User user = UserMethods.createNewUser();
        Response response = UserMethods.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        user.setEmail("abc1685" + user.getEmail());
        user.setName(null);
        response = UserMethods.loginUser(user);
        response.then().log().all()
                .assertThat().statusCode(401)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("email or password are incorrect"));
    }
    @Test
    @DisplayName("Неуспешная авторизация пароль некорректен")
    @Description("Параметр Password не корректно, e-mail корректно")
    public void loginUnsuccessfulPasswordIncorrect() {
        User user = UserMethods.createNewUser();
        Response response = UserMethods.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        user.setPassword("abc1685" + user.getPassword());
        user.setName(null);
        response = UserMethods.loginUser(user);
        response.then().log().all()
                .assertThat().statusCode(401)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("email or password are incorrect"));
    }

    @After
    public void deleteUser() {
        if (accessToken != null) {
            UserMethods.deleteUser(accessToken);
        }
    }
}
