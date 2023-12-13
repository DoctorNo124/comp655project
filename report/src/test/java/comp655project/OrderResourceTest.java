package comp655project;

import comp655project.model.ItemOrder;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import java.util.UUID;

@QuarkusTest
public class OrderResourceTest {

    private static final Long PRODUCT_ID = 999L;
    private static final Long CUSTOMER_ID = 888L;
    private static final UUID ORDER_ID = UUID.fromString("999e4567-e89b-12d3-a456-426614174000");
    private static final UUID uniqueOrderId = UUID.fromString("888e4567-e89b-12d3-a456-000000000000");

    @BeforeEach
    @Transactional
    public void setup() {
        ItemOrder.delete("id", ORDER_ID);
        ItemOrder newOrder = new ItemOrder();
        newOrder.id = ORDER_ID;
        newOrder.customerId = CUSTOMER_ID;
        newOrder.productId = PRODUCT_ID;
        newOrder.amount = 5;
        newOrder.persist();
    }

    @Test
    public void testCreateOrder() {
        ItemOrder order = new ItemOrder();
        order.id = UUID.randomUUID();
        order.customerId = CUSTOMER_ID;
        order.productId = PRODUCT_ID;
        order.amount = 10;
        given()
                .contentType(ContentType.JSON)
                .body(order)
                .when().post("/order")
                .then()
                .statusCode(201)
                .header("Location", notNullValue());
    }

    @Test
    public void testGetOrderById() {
        given()
                .when().get("/order/" + ORDER_ID)
                .then()
                .statusCode(200);
    }


    @Test
    public void testGetOrderByIdNotFound() {
        UUID nonExistentOrderId = UUID.randomUUID();

        given()
                .when().get("/order/" + nonExistentOrderId)
                .then()
                .statusCode(404);
    }

    @Test
    public void testGetAllOrders() {
        given()
                .when().get("/order")
                .then()
                .statusCode(200);
    }
}

