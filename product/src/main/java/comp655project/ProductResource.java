package comp655project;

import io.smallrye.common.annotation.NonBlocking;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.transaction.Transactional;
import java.util.List;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@NonBlocking
public class ProductResource {

    @GET
    @Path("/{id}")
    @Transactional
    public Uni<Product> getProduct(@PathParam("id") long id) {
        return Product.findProduct(id);
    }

    @GET
    @Path("/random")
    @Transactional
    public Uni<Product> getRandomProduct() {
        return Product.findRandomProduct();
    }

    @GET
    @Transactional
    public Uni<List<Product>> getAllProducts() {
        return Product.findAllProducts();
    }


    @POST
    @Transactional
    public Uni<Product> createProduct(Product product) {
        return product.persist();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Uni<Boolean> deleteProduct(@PathParam("id") long id) {
        return Product.deleteProduct(id);
    }
}