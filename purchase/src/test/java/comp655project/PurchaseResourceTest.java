package comp655project;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;

@QuarkusTest
@TestHTTPEndpoint(PurchaseResource.class)
public class PurchaseResourceTest {

    @Test
    public void testCreatePurchase() {
        var statusCode = given()
            .contentType(MediaType.APPLICATION_JSON)
            .when()
            .post("/purchase")
            .then()
            .extract()
            .statusCode();
        assertEquals(statusCode, 201);
    }
}
