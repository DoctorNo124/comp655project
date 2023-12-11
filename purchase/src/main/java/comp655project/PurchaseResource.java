package comp655project;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import org.jboss.logging.Logger;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;

import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/purchase")
public class PurchaseResource {
    private static final Logger LOG = Logger.getLogger(PurchaseResource.class);

    @GrpcClient("customer")
    CustomerServiceGrpc.CustomerServiceBlockingStub blockingCustomerService;
    @GrpcClient("product")
    MutinyProductServiceGrpc.MutinyProductServiceStub mutinyProductService;

	@Channel("order-data") Emitter<ItemOrder> orderRequestEmitter;
    @Channel("order-response") Multi<String> orderResponses;
    


    @POST
    @Path("/purchase")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Purchase createPurchase() throws InterruptedException, ExecutionException {
        boolean sent = false;
        while (!sent) {
            CustomerResponse customer = blockingCustomerService.getRandomCustomer(null);
            for (int i = 0; i < 3; i++) {
                Purchase finalPurchase = mutinyProductService.findRandomProduct(null).map(
                        product -> {
                            Message<ItemOrder> message = null;
                            if (customer.getBalance() >= product.getPrice()) {
                                Purchase purchase = new Purchase();
                                ItemOrder order = new ItemOrder();
                                order.customerId = customer.getId();
                                order.productId = product.getId();
                                purchase.orderId = UUID.randomUUID();
                                purchase.customer = new Customer(customer.getId(), customer.getName(), customer.getEmail(), customer.getBalance());
                                purchase.product = new Product(product.getId(), product.getName(), product.getQuantity(), product.getPrice());
                                order.id = purchase.orderId;
                                order.amount = 1;
                                message = Message.of(order);
                                orderRequestEmitter.send(message);
                                return purchase;
                            }
                            return null;
                        }).subscribeAsCompletionStage().get();
                if(finalPurchase != null) { 
                    String orderId = orderResponses.toUni().map(id -> { 
                        if(id.equals(finalPurchase.orderId.toString())) { 
                            return id;
                        }
                        return null;
                    }).subscribeAsCompletionStage().get();
                    if(orderId != null) { 
                        sent = true;
                        double newBalance = customer.getBalance() - finalPurchase.product.price;
                        UpdateCustomerRequest customerRequest = UpdateCustomerRequest.newBuilder()
                                .setId(customer.getId())
                                .setBalance(newBalance)
                                .build();
                        blockingCustomerService.updateCustomer(customerRequest);
                        LOG.info("Updated customer of id " + customer.getId() + " with balance " + newBalance);
                        UpdateProductRequest productRequest = UpdateProductRequest.newBuilder()
                                .setId(finalPurchase.product.id)
                                .setQuantity(finalPurchase.product.quantity - 1)
                                .build();
                        mutinyProductService.updateProduct(productRequest).subscribe().with(result -> LOG.info("Updated product of id " + result.getId() + " with quantity of " + result.getQuantity()));
                        return finalPurchase;
                    }
                } 
            }
        }
        return null;
    }

    @GET
    @Path("products")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<Product>> getProducts() { 
        return mutinyProductService.listProducts(Empty.newBuilder().build()).onItem().transformToUni(productListResponse -> { 
            return Uni.createFrom().item(productListResponse.getProductsList().stream().map(productMessage -> { 
                return new Product(productMessage.getId(), productMessage.getName(), productMessage.getQuantity(), productMessage.getPrice());
            }).collect(Collectors.toList()));
        });
    }

    @GET
    @Path("customers")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Customer> getCustomers() { 
        return blockingCustomerService.getAllCustomers(CustomerEmpty.newBuilder().build()).getCustomersList().stream().map(customerResponse -> { 
            return new Customer(customerResponse.getId(), customerResponse.getName(), customerResponse.getEmail(), customerResponse.getBalance());
        }).collect(Collectors.toList());
    }

    @POST
    @Path("customers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Customer createCustomer(CreateCustomerRequest request) { 
        CustomerResponse response = blockingCustomerService.createCustomer(request);
        return new Customer(response.getId(), response.getName(), response.getEmail(), response.getBalance());
    }

    @POST
    @Path("products")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Product> createProduct(CreateProductRequest request) { 
       return mutinyProductService.createProduct(request).onItem().transformToUni(productMessage -> { 
            return Uni.createFrom().item(new Product(productMessage.getId(), productMessage.getName(), productMessage.getQuantity(), productMessage.getPrice()));
        });
    }
}
