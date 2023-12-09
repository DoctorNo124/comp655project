package comp655project;

import java.util.UUID;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;

import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/purchase")
public class PurchaseResource {

    @GrpcClient("customer")
    CustomerServiceGrpc.CustomerServiceBlockingStub blockingCustomerService;
    @GrpcClient("product")
    MutinyProductServiceGrpc.MutinyProductServiceStub mutinyProductService;

	@Channel("order-data") Emitter<ItemOrder> orderRequestEmitter;
    @Channel("order-response") Multi<UUID> orderResponses;
    
    static CustomerResponse customer;
    static ProductMessage product;
    static double price;
    static boolean sent;
    static UUID orderId;
    
    @POST
    @Path("/purchase")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Purchase createPurchase() {
        Purchase purchase = new Purchase();
        customer = blockingCustomerService.getRandomCustomer(null);
        sent = false;
        while (!sent) {
            for (int i = 0; i < 3 && !sent; i++) {
                mutinyProductService.findRandomProduct(null).map(
                        product -> {
                            Message<ItemOrder> message = null;
                            if (customer.getBalance() >= product.getPrice()) {
                                price = product.getPrice();
                                ItemOrder order = new ItemOrder();
                                order.customerId = customer.getId();
                                order.productId = product.getId();
                                orderId = UUID.randomUUID();
                                order.id = orderId;
                                order.amount = 1;
                                message = Message.of(order);
                                orderRequestEmitter.send(message);
                                sent = true;
                            }
                            return product;
                        }).subscribe().with(result -> product = result);
            }
            customer = blockingCustomerService.getRandomCustomer(null);
        }
        orderResponses.subscribe().with(
                item -> {
                    if (item == orderId) {
                        customer.toBuilder().setBalance(customer.getBalance() - price).build();
                        UpdateCustomerRequest customerRequest = UpdateCustomerRequest.newBuilder()
                                .setId(customer.getId())
                                .setBalance(customer.getBalance())
                                .build();
                        blockingCustomerService.updateCustomer(customerRequest);
                            product.toBuilder().setQuantity(product.getQuantity() - 1).build();
                            UpdateProductRequest productRequest = UpdateProductRequest.newBuilder()
                                    .setId(product.getId())
                                    .setQuantity(product.getQuantity())
                                    .build();
                            mutinyProductService.updateProduct(productRequest);
                    }
                });
        purchase.customer = new Customer(customer.getName(), customer.getEmail(), customer.getBalance());
        purchase.product = new Product(product.getName(), product.getQuantity(), product.getPrice());
        purchase.orderId = orderId;
        return purchase;
    }

}
