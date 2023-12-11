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
    public Uni<ProductMessage> updateProduct(UpdateProductRequest request) {
        return Product.findProduct(request.getId())
                .onItem().ifNull()
                .failWith(() -> new WebApplicationException(Response.Status.NOT_FOUND))
                .onItem().ifNotNull()
                .transform(product -> {
                    product.id = request.getId();
                    product.quantity = request.getQuantity();
                    product.persist();
                    return product;
                })
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
    public Uni<ProductListResponse> listProducts(Empty request) {
        return Product.findAllProducts()
                .onItem().transform(products -> {
                    ProductListResponse.Builder responseBuilder = ProductListResponse.newBuilder();
                    for (Product product : products) {
                        ProductMessage productMessage = ProductMessage.newBuilder()
                                .setId(product.id)
                                .setName(product.name)
                                .setQuantity(product.quantity)
                                .setPrice(product.price)
                                .build();
                        responseBuilder.addProducts(productMessage);
                    }
                    return responseBuilder.build();
                });
    }

    @Override
    @WithSession
    @WithTransaction
    public Uni<ProductMessage> createProduct(CreateProductRequest request) {
        Product product = new Product();
        product.name = request.getName();
        product.quantity = request.getQuantity();
        product.price = request.getPrice();

        return product.persist()
                .onItem().transform(item -> ProductMessage.newBuilder()
                                .setId(product.id)
                                .setName(product.name)
                                .setQuantity(product.quantity)
                                .setPrice(product.price)
                                .build());
    }


}
