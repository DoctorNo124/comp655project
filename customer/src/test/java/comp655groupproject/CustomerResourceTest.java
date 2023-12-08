package comp655groupproject;

import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import comp655project.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


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

}


