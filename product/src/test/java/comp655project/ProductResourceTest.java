package comp655project;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.ws.rs.core.MediaType;
import static io.restassured.RestAssured.given;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.wildfly.common.Assert.assertTrue;

@QuarkusTest
@TestHTTPEndpoint(ProductResource.class)
public class ProductResourceTest {


    @BeforeEach
    public void setup() {
        addTestProduct(new Product("Nintendo Switch", 50L, 299.99));
        addTestProduct(new Product("Xbox Series S", 100L, 399.99));
    }

    private void addTestProduct(Product product) {
        int statusCode = given()
            .body(product)
            .contentType(MediaType.APPLICATION_JSON)
            .when()
            .post("/product")
            .then()
            .extract()
            .statusCode();
        assertTrue(statusCode == 201);
    }
    
    @Test
    public void testGetProduct() {
        var response = given()
            .pathParam("id", 2)
            .when()
            .get("/product/{id}")
            .then()
            .extract()
            .response();
        assertEquals(response.jsonPath().getString("name"), "Xbox Series S");
        assertEquals(response.jsonPath().getInt("quantity"), 100L);
        assertEquals(response.jsonPath().getDouble("price"), 399.99);
    }

    @Test
    public void testGetAllProducts() {
        var response = given()
            .when()
            .get("/products")
            .then()
            .statusCode(200)
            .extract()
            .response();
        assertTrue(response.jsonPath().getList("name").contains("Nintendo Switch"));
        assertTrue(response.jsonPath().getList("name").contains("Xbox Series S"));
    }
    
    @Test
    public void testUpdateProduct() {
        var statusCode = given()
            .body(new Product("Xbox Series X", 30L, 499.99))
            .contentType(MediaType.APPLICATION_JSON)
            .pathParam("id", 1)
            .when()
            .put("/product/{id}")
            .then()
            .extract()
            .statusCode();
        assertEquals(statusCode, 204);
    }

    @Test
    public void testCreateProduct() {
        var statusCode = given()
            .body(new Product("PlayStation 5", 30L, 499.99))
            .contentType(MediaType.APPLICATION_JSON)
            .when()
            .post("/product")
            .then()
            .extract()
            .statusCode();
        assertEquals(statusCode, 201);
    }

    @Test
    public void testDeleteProduct() {
        given()
            .pathParam("id", 1)
            .delete("/product/{id}")
            .then()
            .statusCode(204);
        var response = given()
            .when()
            .get("/products")
            .then()
            .statusCode(200)
            .extract()
            .response();
        assertFalse(response.jsonPath().getList("name").contains("Nintendo Switch"));
    }
    
    @Test
    public void testGetRandomProduct() {
        var response = given()
            .when()
            .get("/product/random")
            .then()
            .statusCode(200)
            .extract()
            .response();
        assertNotNull(response.jsonPath().getString("name"), "Name should not be null");
        assertNotNull(response.jsonPath().get("quantity"), "Quantity should not be null");
        assertNotNull(response.jsonPath().get("price"), "Price should not be null");
    }
}
