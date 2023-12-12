package comp655project;

import comp655project.model.ItemOrder;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import java.util.UUID;

@QuarkusTest
public class OrderResourceTest {

    private Long productId; // Assume this is set to a valid product ID

    @BeforeEach
    public void setup() {
        // Initialize your product ID here, perhaps by querying the database
        productId = 1L; // Replace with actual ID from your database
    }

    @Test
    public void testCreateOrder() {
        ItemOrder order = new ItemOrder();
        order.customerId = 1L;
        order.productId = productId;
        order.amount = 10;

        given()
                .contentType(ContentType.JSON)
                .body(order)
                .when().post("/order")
                .then()
                .statusCode(201)
                .body("id", notNullValue());
    }

    @Test
    public void testGetAllOrders() {
        given()
                .when().get("/order")
                .then()
                .statusCode(200);
    }

    @Test
    public void testGetOrderById() {
        // First, create an order to ensure there is one to retrieve
        ItemOrder newOrder = new ItemOrder();
        newOrder.customerId = 1L;
        newOrder.productId = productId;
        newOrder.amount = 10;

        UUID orderId = given()
                .contentType(ContentType.JSON)
                .body(newOrder)
                .when().post("/order")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath().getUUID("id");

        given()
                .when().get("/order/" + orderId)
                .then()
                .statusCode(200)
                .body("id", is(orderId.toString()));
    }

    @Test
    public void testDeleteOrder() {
        // First, create an order to ensure there is one to delete
        ItemOrder newOrder = new ItemOrder();
        newOrder.customerId = 1L;
        newOrder.productId = productId;
        newOrder.amount = 10;

        UUID orderId = given()
                .contentType(ContentType.JSON)
                .body(newOrder)
                .when().post("/order")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath().getUUID("id");

        given()
                .when().delete("/order/" + orderId)
                .then()
                .statusCode(204);
    }
}
