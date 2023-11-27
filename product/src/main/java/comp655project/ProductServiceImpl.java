package comp655project;

import java.util.stream.Collectors;

import io.quarkus.grpc.GrpcService;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@GrpcService
public class ProductServiceImpl implements ProductService {

    @Override
    public Uni<ProductMessage> findProduct(ProductIDMessage request) {
        return Product.findProduct(request.getValue())
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
    public Uni<ProductListMessage> findAllProducts(Empty request) {
        return Product.findAllProducts()
                .map(products -> products.stream().map(product -> ProductMessage.newBuilder()
                        .setId(product.id)
                        .setName(product.name)
                        .setQuantity(product.quantity)
                        .setPrice(product.price)
                        .build()))
                .chain(products -> Uni.createFrom().item(ProductListMessage.newBuilder()
                        .addAllProducts(products.collect(Collectors.toList()))
                        .build()));
    }

    @WithTransaction
    @Override
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

    @WithTransaction
    @Override
    public Uni<ProductMessage> createProduct(ProductMessage request) {
        return Product.persist(request)
                .map(product -> request);
    }

    @WithTransaction
    @Override
    public Uni<Empty> deleteProduct(ProductIDMessage request) {
        return Product.findProduct(request.getValue())
                .onItem().ifNull()
                .failWith(() -> new WebApplicationException(Response.Status.NOT_FOUND))
                .onItem().ifNotNull().transformToUni(product -> Product.deleteProduct(request.getValue()))
                .chain(product -> Uni.createFrom().item(Empty.newBuilder().build()));
    }
}
