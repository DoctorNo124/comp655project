package comp655groupproject;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import io.quarkus.grpc.GrpcService;
import jakarta.inject.Singleton;


import java.util.List;
import java.util.stream.Collectors;

@Singleton
@GrpcService
public class CustomerServiceImpl extends CustomerServiceGrpc.CustomerServiceImplBase {

    @Transactional
    @Override
    public void createCustomer(CustomerMessage request, StreamObserver<CustomerResponse> responseObserver) {
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
    }

    @Transactional
    @Override
    public void getCustomerById(GetCustomerByIdRequest request, StreamObserver<CustomerResponse> responseObserver) {
        Customer customer = Customer.findCustomerById(request.getId());
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

    @Transactional
    @Override
    public void getCustomerByName(GetCustomerByNameRequest request, StreamObserver<CustomerResponse> responseObserver) {
        Customer customer = Customer.findCustomerByName(request.getName());
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

    @Transactional
    @Override
    public void getAllCustomers(GetAllCustomersRequest request, StreamObserver<CustomerListResponse> responseObserver) {
        List<Customer> customers = Customer.findAllCustomers();
        List<CustomerResponse> customerResponses = customers.stream()
                .map(customer -> CustomerResponse.newBuilder()
                        .setId(customer.id)
                        .setName(customer.name)
                        .setEmail(customer.email)
                        .setBalance(customer.balance)
                        .build())
                .collect(Collectors.toList());
        CustomerListResponse response = CustomerListResponse.newBuilder()
                .addAllCustomers(customerResponses)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

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

    @Transactional
    @Override
    public void updateCustomer(UpdateCustomerRequest request, StreamObserver<CustomerResponse> responseObserver) {
        Customer customer = Customer.findCustomerById(request.getId());
        if (customer != null) {
            customer.name = request.getCustomer().getName();
            customer.email = request.getCustomer().getEmail();
            customer.balance = request.getCustomer().getBalance();
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

    @Transactional
    @Override
    public void deleteCustomer(DeleteCustomerRequest request, StreamObserver<Empty> responseObserver) {
        boolean deleted = Customer.deleteCustomer(request.getId());
        if (deleted) {
            responseObserver.onNext(Empty.newBuilder().build());
        } else {
            responseObserver.onError(Status.NOT_FOUND.asRuntimeException());
        }
        responseObserver.onCompleted();
    }

    @Transactional
    @Override
    public void updateCustomerBalance(UpdateCustomerBalanceRequest request, StreamObserver<CustomerResponse> responseObserver) {
        Customer customer = Customer.updateCustomerBalance(request.getId(), request.getNewBalance());
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
}
