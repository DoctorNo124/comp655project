package comp655groupproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@QuarkusTest
public class CustomerResourceTest {

    @Inject
    CustomerResource customerResource;

    // Test data
    private final List<Customer> testCustomers = List.of(
            new Customer("Irina Nogina", "nogina@gmail.com", 100.00),
            new Customer("Michael Smith", "smith@gmail.com", 200.00)

    );

    @BeforeEach
    @Transactional
    public void setup() {
        // Initialize test data
        testCustomers.forEach(Customer::persistCustomer);
    }

    @AfterEach
    @Transactional
    public void tearDown() {
        // Cleanup test data
        Customer.deleteAll();
    }

    @Test
    public void testCreateCustomer() {
        Customer newCustomer = new Customer("Kathy Perri", "kperry@gmail.com", 300.0);
        Response response = customerResource.createCustomer(newCustomer);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    public void testGetCustomerById() {
        Customer expectedCustomer = testCustomers.get(0);
        Response response = customerResource.getCustomerById(expectedCustomer.id);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Customer actualCustomer = (Customer) response.getEntity();

        assertEquals(expectedCustomer.id, actualCustomer.id);
        assertEquals(expectedCustomer.name, actualCustomer.name);
        assertEquals(expectedCustomer.email, actualCustomer.email);
        assertEquals(expectedCustomer.balance, actualCustomer.balance);
    }


    @Test
    public void testGetCustomerByName() {
        Customer expectedCustomer = testCustomers.get(0);
        Response response = customerResource.getCustomerByName(expectedCustomer.name);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        Customer actualCustomer = (Customer) response.getEntity();
        assertNotNull(actualCustomer);
        assertEquals(expectedCustomer.id, actualCustomer.id);
        assertEquals(expectedCustomer.name, actualCustomer.name);
        assertEquals(expectedCustomer.email, actualCustomer.email);
        assertEquals(expectedCustomer.balance, actualCustomer.balance, 0.001); // Use delta for comparing doubles
    }


    @Test
    public void testGetAllCustomers() {
        Response response = customerResource.getAllCustomers();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    public void testUpdateCustomer() {
        Customer customer = testCustomers.get(0);
        customer.balance = 500.0;
        Response response = customerResource.updateCustomer(customer.id, customer);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Customer updatedCustomer = (Customer) response.getEntity();
        assertEquals(500.0, updatedCustomer.balance);
    }

    @Test
    public void testDeleteCustomer() {
        Customer customer = testCustomers.get(0);
        Response response = customerResource.deleteCustomer(customer.id);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void testUpdateCustomerBalance() {
        Customer customer = testCustomers.get(0);
        double newBalance = 400.0;
        Response response = customerResource.updateCustomerBalance(customer.id, newBalance);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Customer updatedCustomer = (Customer) response.getEntity();
        assertEquals(newBalance, updatedCustomer.balance);
    }

}


