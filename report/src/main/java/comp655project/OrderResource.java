package comp655project;

import java.util.List;

import comp655project.model.ItemOrder;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/order")
public class OrderResource {


	
	@POST
	@Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public String create_order(ItemOrder order) {
		order.persist(order);
        return "Order Saved Successfully";
    }
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ItemOrder> get_order() {
		ItemOrder order=new ItemOrder();
        return order.findAll().list();
    }
	@GET
	@Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ItemOrder get_order_id(long id) {
		ItemOrder order=new ItemOrder();
        return (ItemOrder) order.findById(id);
    }
	@DELETE
	@Transactional
	@Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean delete_order_id(long id) {
		ItemOrder order=new ItemOrder();
        return order.deleteById(id);
    }
}

