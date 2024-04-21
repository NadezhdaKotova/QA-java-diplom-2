package Order;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static Constants.UrlApi.*;

public class OrderMethods {
    @Step("Получение данных об ингредиентах.")
    public static Ingredients getIngredient() {
        return spec()
                .when()
                .get(INGREDIENTS)
                .body()
                .as(Ingredients.class);
    }
    @Step("Создание заказа без токена авторизации")
    public static Response createOrderWithoutToken(Order order) {
        return spec()
                .body(order)
                .when()
                .post(ORDER);
    }
    @Step("Создание заказа с токеном авторизации")
    public static Response createOrderWithToken(Order order, String accessToken) {
        return spec()
                .header("Authorization",accessToken)
                .body(order)
                .when()
                .post(ORDER);
    }
}
