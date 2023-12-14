package comp655project;

import java.net.URI;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    @GET
    @Path("/product/{id}")
    @Tag(name = "Get Product", description = "Gets a product by its ID")
    public Uni<Product> getProduct(@PathParam("id") long id) {
        return Product.findProduct(id).onItem().ifNull()
                .failWith(() -> new WebApplicationException(Response.Status.NOT_FOUND));
    }
    
    @GET
    @Path("/product/random")
    @Tag(name = "Get Random Product", description = "Gets a random product")
    public Uni<Product> getRandomProduct() {
        return Product.findRandomProduct().onItem().ifNull()
                .failWith(() -> new WebApplicationException(Response.Status.NOT_FOUND));
    }

    @GET
    @Path("/products")
    @Tag(name = "Get All Products", description = "Gets all products")
    public Uni<List<Product>> getAllProducts() {
        return Product.findAllProducts();
    }
    
    @PUT
    @Path("/product/{id}")
    @Tag(name = "Update Product", description = "Updates a product")
    @WithTransaction
    public Uni<RestResponse<Void>> updateProduct(@PathParam("id") long id, Product product) {
        return Product.findProduct(id)
                .onItem().ifNull()
                .failWith(() -> new WebApplicationException(Response.Status.NOT_FOUND))
                .onItem().ifNotNull()
                .transformToUni(item -> Uni.createFrom().item(ResponseBuilder.<Void>noContent().build()));
    }

    @POST
    @Path("/product")
    @Tag(name = "Create Product", description = "Creates a product")
    @WithTransaction
    public Uni<RestResponse<Void>> createProduct(Product product) {
        return product.persist().onItem().transformToUni(item -> Uni.createFrom().item(ResponseBuilder.<Void>created(URI.create(("/products/product/" + ((Product)item).id))).build()));
    }

    @DELETE
    @Path("/product/{id}")
    @Tag(name = "Delete Product", description = "Deletes a product")
    @WithTransaction
    public Uni<RestResponse<Void>> deleteProduct(@PathParam("id") long id) {
        return Product.deleteProduct(id).onItem().transformToUni(item -> { 
            return Uni.createFrom().item(ResponseBuilder.<Void>noContent().build());
        });
    }
}
