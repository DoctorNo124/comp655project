package comp655groupproject;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;


@QuarkusTest
public class CustomerResourceTest {

    @Inject
    CustomerServiceImpl customerService; // Inject the service implementation

}

