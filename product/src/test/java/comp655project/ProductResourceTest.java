package comp655project;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.ws.rs.core.MediaType;
import static io.restassured.RestAssured.given;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
        // Test to add a product to your test database
        int statusCode = given()
                .body(product)
                .contentType(MediaType.APPLICATION_JSON)
                .when().post("/product")
                .then()
                .extract().statusCode();
        assertTrue(statusCode == 200);
    }

    @Test
    public void testProductCreation() {
        // Test to create a new product
        Product newProduct = new Product("PlayStation 5", 30L, 499.99);
        int statusCode = given()
                .body(newProduct)
                .contentType(MediaType.APPLICATION_JSON)
                .when().post("/product")
                .then()
                .extract().statusCode();
        assertTrue(statusCode == 200);
    }
    @Test
    public void testGetProduct() {
        int statusCode = given()
                .pathParam("id", 1)
                .when().get("/product/{id}")
                .then()
                .extract().statusCode();
        assertTrue(statusCode == 200);
    }

    @Test
    public void testProductDeletion() {
        // Example test to delete a product
        long productIdToDelete = 1; // Assuming this ID exists
        int statusCode = given()
                .pathParam("id", productIdToDelete)
                .when().delete("/product/{id}")
                .then()
                .extract().statusCode();
        assertTrue(statusCode == 200);
    }

    @Test
    public void testGetAllProducts() {
        var response = given()
                .when().get("/products")
                .then()
                .statusCode(200)
                .extract().response();

        assertTrue(response.jsonPath().getList("name").contains("Nintendo Switch"));
        assertTrue(response.jsonPath().getList("quantity").contains(50));

    }

    @Test
    public void testCreateProduct() {
        Product newProduct = new Product("PlayStation 5", 30L, 499.99);

        // Extract the response after creating the product
        var response = given()
                .body(newProduct)
                .contentType(MediaType.APPLICATION_JSON)
                .when().post("/product")
                .then()
                .statusCode(200)  // Asserting the status code first
                .extract().response();  // Extracting the response

        // Asserting the name and quantity in the response
        assertEquals("PlayStation 5", response.jsonPath().getString("name"));
        assertEquals(30, response.jsonPath().getInt("quantity"));
        assertEquals(499.99, response.jsonPath().getDouble("price"), 0.01);
    }


    @Test
    public void testDeleteProduct() {
        int statusCode = given()
                .pathParam("id", 1)
                .when().delete("/product/{id}")
                .then()
                .extract().statusCode();
        assertTrue(statusCode == 200);
    }
    @Test
    public void testGetRandomProduct() {
        var response = given()
                .when().get("/product/random")
                .then()
                .statusCode(200)
                .extract().response();

        assertNotNull(response.jsonPath().getString("name"), "Name should not be null");
        assertNotNull(response.jsonPath().get("quantity"), "Quantity should not be null");
        assertNotNull(response.jsonPath().get("price"), "Price should not be null");
    }


}
