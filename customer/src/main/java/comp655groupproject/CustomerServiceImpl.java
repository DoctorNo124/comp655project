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

@Singleton
@GrpcService
public class CustomerServiceImpl extends CustomerServiceGrpc.CustomerServiceImplBase {

    /* gRPC's method that retrieves a random customer from the database */
    @Transactional
    @Override
    public void getRandomCustomer(Empty request, StreamObserver<CustomerResponse> responseObserver) {
        Customer customer = Customer.findRandomCustomer();
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

    /* Updates customer details */
    @Override
    @Transactional
    public void updateCustomer(UpdateCustomerRequest request, StreamObserver<CustomerResponse> responseObserver) {
        Long id = request.getId();
        double newBalance = request.getBalance();

        Customer customer = Customer.findCustomerById(id);
        if (customer != null) {
            customer.balance = newBalance; // Only update the balance
            Customer.updateCustomer(customer);

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

    @Override
    @Transactional
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

}