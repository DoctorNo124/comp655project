package comp655project;

import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Metadata;
import io.smallrye.mutiny.Multi;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.UUID;
@Path("purchase")
public class PurchaseResource {
	@Channel("order-data") Emitter<ItemOrder> orderRequestEmitter;
    @Channel("order-response") Multi<UUID> orderResponses;
    
    @POST
    @Path("/order")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
 public Message<ItemOrder> sendOrder(ItemOrder order) {
    Message<ItemOrder> message = Message.of(order);
    orderRequestEmitter.send(message);
    return message;
}
    @GET
    @Path("/response")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @Consumes(MediaType.TEXT_PLAIN) 
    @Tag(name="Receive Response", description="Gives a stream of the Orders in String format from the student-honor queue")
    public Multi<UUID> recvResponse() { 
        return orderResponses;
    }

}
