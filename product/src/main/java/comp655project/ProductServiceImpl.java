package comp655project;

import io.quarkus.grpc.GrpcService;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@GrpcService
public class ProductServiceImpl implements ProductService {

    @Override
    @WithSession
    @WithTransaction
    public Uni<ProductMessage> findRandomProduct(Empty request) {
        return Product.findRandomProduct()
                .onItem().ifNull()
                .failWith(() -> new WebApplicationException(Response.Status.NOT_FOUND))
                .map(product -> ProductMessage.newBuilder()
                        .setId(product.id)
                        .setName(product.name)
                        .setQuantity(product.quantity)
                        .setPrice(product.price)
                        .build());
    }

    @Override
    @WithSession
    @WithTransaction
    public Uni<ProductMessage> updateProduct(ProductMessage request) {
        return Product.findProduct(request.getId())
                .onItem().ifNull()
                .failWith(() -> new WebApplicationException(Response.Status.NOT_FOUND))
                .onItem().ifNotNull()
                .transform(product -> {
                    product.id = request.getId();
                    product.name = request.getName();
                    product.quantity = request.getQuantity();
                    product.price = request.getPrice();
                    return product;
                })
                .map(product -> ProductMessage.newBuilder()
                        .setId(product.id)
                        .setName(product.name)
                        .setQuantity(product.quantity)
                        .setPrice(product.price)
                        .build());
    }
}
