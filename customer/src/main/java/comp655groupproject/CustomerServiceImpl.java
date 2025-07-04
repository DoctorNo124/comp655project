package comp655groupproject;

import comp655project.*;

//This class provides gRPC service implementations that provides
//the same CRUD operations over gRPC rather than HTTP/REST
//it extends gRPC service base class generated from protobuf and implements the methods

import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.protobuf.ProtoUtils;
import io.grpc.stub.StreamObserver;
import jakarta.transaction.Transactional;
import io.quarkus.grpc.GrpcService;
import jakarta.inject.Singleton;
import java.util.List;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

// Declaring the class as a Singleton and a gRPC Service
@Singleton
@GrpcService
@Tag(name = "Customer Service", description = "Customers's updates")
public class CustomerServiceImpl extends CustomerServiceGrpc.CustomerServiceImplBase {

    /* gRPC's method that retrieves a random customer from the database */
    @Transactional
    @Override
    @Tag(name = "Get Random Customer", description = "Find a random customer from the database")
    public void getRandomCustomer(Empty request, StreamObserver<CustomerResponse> responseObserver) {
        // Finding a random customer from the database
        Customer customer = Customer.findRandomCustomer();
        // If a customer is found, provides the details
        if (customer != null) {
            CustomerResponse response = CustomerResponse.newBuilder()
                    .setId(customer.id)
                    .setName(customer.name)
                    .setEmail(customer.email)
                    .setBalance(customer.balance)
                    .build();
            responseObserver.onNext(response);
        } else {
            responseObserver.onError(Status.NOT_FOUND.asRuntimeException());
        }
        responseObserver.onCompleted();
    }

    /* Method to updates customer's details */
    @Override
    @Transactional
    @Tag(name = "Update Customer", description = "Update a customer's details")
    public void updateCustomer(UpdateCustomerRequest request, StreamObserver<CustomerResponse> responseObserver) {
        Long id = request.getId();
        double newBalance = request.getBalance();
        // Finding the customer by ID
        Customer customer = Customer.findCustomerById(id);
        if (customer != null) {
            customer.balance = newBalance; // Only update the balance
            Customer.updateCustomer(customer);
            // Creating and sending the updated customer response
            CustomerResponse response = CustomerResponse.newBuilder()
                    .setId(customer.id)
                    .setName(customer.name)
                    .setEmail(customer.email)
                    .setBalance(customer.balance)
                    .build();
            responseObserver.onNext(response);
        } else {
            // Custom error handling
            String errorMessage = "Customer with ID of " + id + " not found";
            ErrorResponse errorResponse = ErrorResponse.newBuilder()
                    .setExpectedValue("Valid customer ID")
                    .setId(id)
                    .build();
            Metadata metadata = new Metadata();
            Metadata.Key<ErrorResponse> errorResponseKey = ProtoUtils.keyForProto(ErrorResponse.getDefaultInstance());
            metadata.put(errorResponseKey, errorResponse);
            responseObserver.onError(Status.NOT_FOUND.withDescription(errorMessage).asRuntimeException(metadata));
        }
        responseObserver.onCompleted();
    }

    // Method to find all customers in database
    @Override
    @Transactional
    @Tag(name = "Get All Customers", description = "Find all customers in the database")
    public void getAllCustomers(Empty request, StreamObserver<AllCustomers> responseObserver) {
        List<Customer> customers = Customer.findAllCustomers();
        AllCustomers.Builder allCustomersBuilder = AllCustomers.newBuilder();
        for(Customer customer : customers) {
            CustomerResponse response = CustomerResponse.newBuilder()
                    .setId(customer.id)
                    .setName(customer.name)
                    .setEmail(customer.email)
                    .setBalance(customer.balance)
                    .build();
            allCustomersBuilder.addCustomers(response);
        }
        responseObserver.onNext(allCustomersBuilder.build());
        responseObserver.onCompleted();
    }

    //Method to create new customer
    @Override
    @Transactional
    @Tag(name = "Create Customer", description = "Create a new customer")
    public void createCustomer(CreateCustomerRequest request, StreamObserver<CustomerResponse> responseObserver) {
        try {
            Customer customer = new Customer(request.getName(), request.getEmail(), request.getBalance());
            Customer.persistCustomer(customer);

            CustomerResponse response = CustomerResponse.newBuilder()
                    .setId(customer.id)
                    .setName(customer.name)
                    .setEmail(customer.email)
                    .setBalance(customer.balance)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

}