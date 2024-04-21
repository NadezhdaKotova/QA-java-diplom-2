import Order.Ingredients;
import Order.Order;
import User.User;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.hamcrest.Matchers;
import Order.OrderMethods;
import java.util.ArrayList;
import User.UserMethods;
@DisplayName("Создание заказа: POST /api/orders")
public class OrderCreateTest {
    private String accessToken;


    @Test
    @DisplayName("Успешное создание заказа без авторизации с ингредиентами")
    @Description("Ингредиенты добавлены, токен не передан. 200")
    public void createOrderNoAuthWithIngredients() {
        Ingredients ingredients = OrderMethods.getIngredient();
        ArrayList<String> ingredient1 = new ArrayList<>();
        ingredient1.add(ingredients.getData().get(1).get_id());
        ingredient1.add(ingredients.getData().get(2).get_id());
        ingredient1.add(ingredients.getData().get(3).get_id());
        Order order = new Order(ingredient1);
        Response response = OrderMethods.createOrderWithoutToken(order);
        response.then().log().all()
                .assertThat().statusCode(200)
                .and().body("success", Matchers.is(true))
                .and().body("name", Matchers.notNullValue())
                .and().body("order.number", Matchers.any(Integer.class));
    }
    @Test
    @DisplayName("Успешное создание заказа с авторизацией и с ингредиентами")
    @Description("Ингредиенты добавлены, токен передан. 200")
    public void createOrderWithAuthWithIngredients() {
        //создание пользователя
        User user = UserMethods.createNewUser();
        Response response = UserMethods.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        //создание заказа
        Ingredients ingredients = OrderMethods.getIngredient();
        ArrayList<String> ingredient1 = new ArrayList<>();
        ingredient1.add(ingredients.getData().get(1).get_id());
        ingredient1.add(ingredients.getData().get(2).get_id());
        ingredient1.add(ingredients.getData().get(3).get_id());
        Order order = new Order(ingredient1);
        response = OrderMethods.createOrderWithToken(order, accessToken);
        response.then().log().all()
                .assertThat().statusCode(200)
                .and().body("success", Matchers.is(true))
                .and().body("name", Matchers.notNullValue())
                .and().body("order.number", Matchers.any(Integer.class));
    }
    @Test
    @DisplayName("Создание заказа с авторизацией и без ингредиентов")
    @Description("Ингредиенты не добавлены, токен передан. 400")
    public void createOrderWithAuthNoIngredients() {
        //создание пользователя
        User user = UserMethods.createNewUser();
        Response response = UserMethods.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        //создание заказа
        Order order = new Order(null);
        response = OrderMethods.createOrderWithToken(order, accessToken);
        response.then().log().all()
                .assertThat().statusCode(400)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("Ingredient ids must be provided"));
    }
    @Test
    @DisplayName("Создание заказа без авторизации и без ингредиентов")
    @Description("Ингредиенты не добавлены, токен не передан. 500")
    public void createOrderNoAuthNoIngredients() {
        //создание пользователя
        User user = UserMethods.createNewUser();
        Response response = UserMethods.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        //создание заказа
        Order order = new Order(null);
        response = OrderMethods.createOrderWithoutToken(order);
        response.then().log().all()
                .assertThat().statusCode(400)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("Ingredient ids must be provided"));
    }
    @Test
    @DisplayName("Создание заказа с авторизацией и c невалидным ингредиентом")
    @Description("Добавлен один невалидный ингредиент, токен передан. 400")
    public void createOrderWithAuthIngredientsNotValid() {
        //создание пользователя
        User user = UserMethods.createNewUser();
        Response response = UserMethods.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        //создание заказа
        Ingredients ingredients = OrderMethods.getIngredient();
        ArrayList<String> ingredient1 = new ArrayList<>();
        ingredient1.add(ingredients.getData().get(1).get_id() + "yandex");
        Order order = new Order(ingredient1);
        response = OrderMethods.createOrderWithToken(order, accessToken);
        response.then().log().all()
                .assertThat().statusCode(500);
    }
    @Test
    @DisplayName("Создание заказа с авторизацией и c одним невалидным ингредиентом")
    @Description("Добавлены валидные и один невалидный ингредиент, токен передан. 500")
    public void createOrderWithAuthOneOfIngredientsNotValid() {
        //создание пользователя
        User user = UserMethods.createNewUser();
        Response response = UserMethods.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        //создание заказа
        Ingredients ingredients = OrderMethods.getIngredient();
        ArrayList<String> ingredient1 = new ArrayList<>();
        ingredient1.add(ingredients.getData().get(1).get_id() + "yandex");
        ingredient1.add(ingredients.getData().get(2).get_id());
        ingredient1.add(ingredients.getData().get(3).get_id());
        Order order = new Order(ingredient1);
        response = OrderMethods.createOrderWithToken(order, accessToken);
        response.then().log().all()
                .assertThat().statusCode(500);
    }

    @After
    public void deleteUser() {
        if (accessToken != null) {
            UserMethods.deleteUser(accessToken);
        }
    }}
