package User;
import static Constants.UrlApi.*;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class UserMethods {
    public static User createNewUser() {
        return new User("aaa222@aaa2222.ru", "pswd", "aaa");
    }
    @Step("Создание нового пользователя")
    public static Response createUser(User user) {
        return spec()
                .body(user)
                .when()
                .post(USER_REGISTER);
    }
    @Step("Авторизация пользователя")
    public static Response loginUser(User user) {
        return spec()
                .body(user)
                .when()
                .post(USER_AUTH);
    }

    @Step("Обновление пользователя")
    public static Response updateUser(User user, String accessToken) {
        return spec()
                .header("Authorization",accessToken)
                .body(user)
                .when()
                .patch(USER);
    }
    @Step("Обновление пользователя без токена в headers")
    public static Response updateUserWithoutToken(User user) {
        return spec()
                .body(user)
                .when()
                .patch(USER);
    }

    @Step("Удаление пользователя")
    public static void deleteUser(String accessToken) {
                 spec()
                .header("Authorization",accessToken)
                .when()
                .delete(USER);
    }
}
