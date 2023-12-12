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
    public void testGetAllOrders() {
        given()
                .when().get("/order")
                .then()
                .statusCode(200);
    }


}
