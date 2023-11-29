package comp655groupproject;

//This class provides gRPC service implementations that provides
//the same CRUD operations over gRPC rather than HTTP/REST
//it extends gRPC service base class generated from protobuf and implements the methods

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import jakarta.transaction.Transactional;
import io.quarkus.grpc.GrpcService;
import jakarta.inject.Singleton;

@Singleton
@GrpcService
public class CustomerServiceImpl extends CustomerServiceGrpc.CustomerServiceImplBase {

    /* gRPC's method that retrieves a random customer from the database */
    @Transactional
    @Override
    public void getRandomCustomer(GetRandomCustomerRequest request, StreamObserver<CustomerResponse> responseObserver) {
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
        CustomerMessage updatedInfo = request.getCustomer();

        Customer customer = Customer.findCustomerById(id);
        if (customer != null) {
            customer.name = updatedInfo.getName();
            customer.email = updatedInfo.getEmail();
            customer.balance = updatedInfo.getBalance();
            Customer.updateCustomer(customer);

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

}

