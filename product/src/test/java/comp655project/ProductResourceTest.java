package comp655project;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@QuarkusTest
public class ProductResourceTest {

    @Inject
    ProductResource productResource;

    private final List<Product> testProducts = List.of(
            new Product("Nintendo Switch", 50L, 299.99),
            new Product("Xbox Series S", 100L, 399.99)
    );

    @BeforeEach
    @Transactional
    public void setup() {
        testProducts.forEach(product -> Product.persist(product));
    }

    @AfterEach
    @Transactional
    public void tearDown() {
        Product.deleteAll();
    }

    @Test
    public void testGetProductById() {
        Response response = given()
            .when()
            .get("/product/0")
            .then()
            .statusCode(200)
            .contentType("application/json")
            .extract().response();
        assertTrue((response.jsonPath().getList("name")).contains("Nintendo Switch"));
        assertTrue((response.jsonPath().getList("quantity")).contains(50));
        assertTrue((response.jsonPath().getList("price")).contains(299.99));
    }

    @Test
    public void testGetRandomProduct() {
        Response response = given()
            .when()
            .get("/product/random")
            .then()
            .statusCode(200)
            .contentType("application/json")
            .extract().response();
        assertTrue((response.jsonPath().getList("name")).contains("Nintendo Switch")
                || (response.jsonPath().getList("name")).contains("Xbox Series S"));
    }

    @Test
    public void testGetAllProducts() {
        Response response = given()
            .when()
            .get("/products")
            .then()
            .statusCode(200)
            .contentType("application/json")
            .extract().response();
        assertTrue((response.jsonPath().getList("name")).contains("Nintendo Switch"));
        assertTrue((response.jsonPath().getList("name")).contains("Xbox Series S"));
    }

    @Test
    public void testUpdateProduct() {
        given()
            .when()
            .body("{\"name\" : \"Nintendo Switch\"}")
            .contentType("application/json")
            .put("/product/0")
            .then()
            .statusCode(200)
            .contentType("application/json")
            .body(
                containsString("\"id\":"),
                containsString("\"name\":\"Bintendo Swatch\""),
                containsString("\"quantity\":5"),
                containsString("\"price\":49.99"))
            .extract().response();
        Response response = given()
            .when()
            .get("/product/0")
            .then()
            .statusCode(200)
            .contentType("application/json")
            .extract().response();
        assertTrue((response.jsonPath().getList("name")).contains("Bintendo Swatch"));
        assertTrue((response.jsonPath().getList("quantity")).contains(5));
        assertTrue((response.jsonPath().getList("price")).contains(49.99));
    }

    @Test
    public void testCreateProduct() {
        given()
            .when()
            .body("{\"name\" : \"Sony Playstation 5\"}")
            .contentType("application/json")
            .post("/product")
            .then()
            .statusCode(201)
            .body(
                containsString("\"id\":"),
                containsString("\"name\":\"Sony Playstation 5\""),
                containsString("\"quantity\":25"),
                containsString("\"price\":499.99"));
        Response response = given()
            .when()
            .get("/product/2")
            .then()
            .statusCode(200)
            .contentType("application/json")
            .extract().response();
        assertTrue((response.jsonPath().getList("name")).contains("Sony Playstation 5"));
        assertTrue((response.jsonPath().getList("quantity")).contains(25));
        assertTrue((response.jsonPath().getList("price")).contains(499.99));
    }

    @Test
    public void testDeleteCustomer() {
        given()
            .when()
            .delete("product/0")
            .then()
            .statusCode(204);
        Response response = given()
            .when()
            .get("/products")
            .then()
            .statusCode(200)
            .contentType("application/json")
            .extract().response();
        assertFalse((response.jsonPath().getList("name")).contains("Nintendo Switch"));
    }

}
