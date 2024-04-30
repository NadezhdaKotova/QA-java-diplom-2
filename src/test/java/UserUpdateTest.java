import user.User;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;
import user.UserMethods;

@DisplayName("Создание пользователя: PATCH /api/auth/user")
public class UserUpdateTest {
    String accessToken;
    @Test
    @DisplayName("Успешное обновление имя и e-mail пользователя")
    @Description("токен авторизации передан корректно")
    public void updateSuccessUser() {
        User user = UserMethods.createNewUser();
        Response response = UserMethods.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        user.setName("name" + user.getName());
        user.setEmail("email" + user.getEmail());
        response = UserMethods.updateUser(user, accessToken);
        response.then().log().all()
                .assertThat().statusCode(200)
                .and().body("success", Matchers.is(true))
                .and().body("user.email", Matchers.equalTo(user.getEmail()))
                .and().body("user.name", Matchers.equalTo(user.getName()));
    }
    @Test
    @DisplayName("Неуспешное обновление имя и e-mail при некорректном токене")
    @Description("токен авторизации передан некорректно - 403")
    public void updateUserWithIncorrectToken() {
        User user = UserMethods.createNewUser();
        Response response = UserMethods.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        user.setName("name" + user.getName());
        user.setEmail("email" + user.getEmail());
        String incorrectToken = accessToken+"abc";
        response = UserMethods.updateUser(user, incorrectToken);
        response.then().log().all()
                .assertThat().statusCode(403)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("invalid signature"));
    }
    @Test
    @DisplayName("Обновление имя и e-mail пользователя без Токена")
    @Description("токен авторизации не передан - 401")
    public void updateUserWithoutToken() {
        User user = UserMethods.createNewUser();
        Response response = UserMethods.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        user.setName("name" + user.getName());
        user.setEmail("email" + user.getEmail());
        response = UserMethods.updateUserWithoutToken(user);
        response.then().log().all()
                .assertThat().statusCode(401)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("You should be authorised"));
    }
    @Test
    @DisplayName("Успешное обновление пароля пользователя")
    @Description("токен авторизации передан корректно")
    public void updatePasswordUser() {
        User user = UserMethods.createNewUser();
        Response response = UserMethods.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        user.setPassword("password" + user.getPassword());
        response = UserMethods.updateUser(user, accessToken);
        response.then().log().all()
                .assertThat().statusCode(200);
        //поскольку в теле ответа не выводится информация об успешности измененного пароля,
        //проверяем актуальности изменения пароля залогиниванием с новым паролем. Если успешно = пароль изменен корректно
        response = UserMethods.loginUser(user);
        response.then().log().all().assertThat().statusCode(200);
    }
    @Test
    @DisplayName("Успешное обновление пароля пользователя без токена")
    @Description("токен авторизации не передан")
    public void updatePasswordUserWithoutToken() {
        User user = UserMethods.createNewUser();
        Response response = UserMethods.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        user.setPassword("password" + user.getPassword());
        response = UserMethods.updateUserWithoutToken(user);
        response.then().log().all()
                .assertThat().statusCode(401)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("You should be authorised"));
        //поскольку в теле ответа не выводится информация об успешности измененного пароля,
        //проверяем актуальность изменения пароля залогиниванием с новым паролем. Если успешно = пароль изменен корректно
        response = UserMethods.loginUser(user);
        response.then().log().all().assertThat().statusCode(401);
    }
    @After
    public void deleteUser() {
        if (accessToken != null) {
            UserMethods.deleteUser(accessToken);
        }
    }}
