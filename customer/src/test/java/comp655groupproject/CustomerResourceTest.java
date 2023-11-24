package comp655groupproject;

import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


@QuarkusTest
public class CustomerResourceTest {

    @Inject
    @GrpcService
    CustomerServiceImpl customerService; // The actual service implementation

    private Customer customer;
    private CustomerResponse expectedResponse;

    @BeforeEach
    @Transactional
    public void setup() {
        // Assuming you have an in-memory database initialized and transaction management configured
        // Insert a customer into the database
        customer = new Customer("Irina Nogina", "nogina@gmail.com", 100.00);
        customer.persist();

        expectedResponse = CustomerResponse.newBuilder()
                .setId(customer.id)
                .setName(customer.name)
                .setEmail(customer.email)
                .setBalance(customer.balance)
                .build();
    }

    @AfterEach
    @Transactional
    public void tearDown() {
        // Clean up the in-memory database
        Customer.deleteAll();
    }

    @Test
    @Transactional
    public void testCreateCustomer() {

        // Arrange: create a new customer instance
        Customer newCustomer = new Customer("Irina Nogina", "nogina@gmail.com", 200.0);

        // Act: persist the new customer
        newCustomer.persist();

        // Assert: verify the customer was persisted, e.g., by retrieving it again or checking its generated ID

        // Cleanup: delete the customer (needs to be within a transaction)
        Customer.delete("email", newCustomer.email);

        // Prepare the request object
        CustomerMessage request = CustomerMessage.newBuilder()
                .setName("Irina Nogina")
                .setEmail("nogina@gmail.com")
                .setBalance(200.00)
                .build();

        // Create an instance to capture the response from the gRPC service
        final List<CustomerResponse> responses = new ArrayList<>();

        // Implement the StreamObserver to capture the responses
        StreamObserver<CustomerResponse> responseObserver = new StreamObserver<CustomerResponse>() {
            @Override
            public void onNext(CustomerResponse customerResponse) {
                responses.add(customerResponse);
            }

            @Override
            public void onError(Throwable throwable) {
                // handle error
            }

            @Override
            public void onCompleted() {
                // handle completion
            }
        };

        // Call the gRPC service method
        customerService.createCustomer(request, responseObserver);

        // Assert the response
        assertFalse(responses.isEmpty());
        CustomerResponse response = responses.get(0);
        assertEquals("Irina Nogina", response.getName());
        assertEquals("nogina@gmail.com", response.getEmail());
        assertEquals(200.00, response.getBalance(), 0.01);

        // Clean up test data
        Customer.delete("email", "nogina@gmail.com");
    }
}


