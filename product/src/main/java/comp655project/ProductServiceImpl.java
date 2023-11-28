package comp655project;

import java.util.stream.Collectors;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.transaction.Transactional;

@GrpcService
public class ProductServiceImpl implements ProductService {

    @Override
    @Transactional
    public Uni<ProductMessage> findProduct(ProductIDMessage request) {
        return Product.findProduct(request.getValue()) // Use the getValue() method here
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
    @Transactional
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
    @Transactional
    public Uni<ProductListMessage> findAllProducts(Empty request) {
        return Product.findAllProducts()
                .map(products -> products.stream()
                        .map(product -> ProductMessage.newBuilder()
                                .setId(product.id)
                                .setName(product.name)
                                .setQuantity(product.quantity)
                                .setPrice(product.price)
                                .build())
                        .collect(Collectors.toList()))
                .map(ProductListMessage.newBuilder()::addAllProducts)
                .map(ProductListMessage.Builder::build);
    }


    @Override
    @Transactional
    public Uni<ProductMessage> createProduct(ProductMessage request) {
        Product product = new Product();
        product.name = request.getName();
        product.quantity = request.getQuantity();
        product.price = request.getPrice();
        return product.<Product>persist()
                .onItem().transform(savedProduct -> ProductMessage.newBuilder()
                        .setId(savedProduct.id)
                        .setName(savedProduct.name)
                        .setQuantity(savedProduct.quantity)
                        .setPrice(savedProduct.price)
                        .build());
    }

    @Override
    @Transactional
    public Uni<Empty> deleteProduct(ProductIDMessage request) {
        return Product.deleteProduct(request.getValue()) // Use the getValue() method here
                .replaceWith(Empty.newBuilder().build());
    }
}
