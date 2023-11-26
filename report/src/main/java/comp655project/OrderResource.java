package comp655project;

import java.net.URI;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import comp655project.model.ItemOrder;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/order")
public class OrderResource {
	
	@POST
	@Transactional(Transactional.TxType.REQUIRED)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name="Create Order", description="Creates a valid order")
    public Response createOrder(@Valid ItemOrder order) {
        try { 
		    ItemOrder.persist(order);
            return Response.created(URI.create("/order/" + order.id)).build();
        }
        catch(Exception ex) { 
            return Response.serverError().build();
        }
    }

	@GET
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name="Gets all orders", description="Gets all orders")
    public Response getAllOrders() {
        return Response.ok(ItemOrder.findAll().list()).build();
    }

	@GET
	@Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    @Tag(name="Get order by ID", description="Gets an order by id and returns a not found if not found")
    public Response getOrderById(long id) {
		ItemOrder order = ItemOrder.findById(id);
        if(order == null) { 
            throw new NotFoundException();
        }
        return Response.ok(order).build();
    }

	@DELETE
	@Transactional
	@Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Tag(name="Delete order", description="Deletes an order by id and returns a not found if not found")
    public Response deleteOrder(long id) {
		ItemOrder order = ItemOrder.findById(id);
        if(order == null) { 
            throw new NotFoundException();
        }
        return Response.noContent().build();
    }
}

