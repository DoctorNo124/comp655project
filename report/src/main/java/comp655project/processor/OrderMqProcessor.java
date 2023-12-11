package comp655project.processor;


import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import comp655project.model.ItemOrder;
import io.vertx.core.json.JsonObject;
import jakarta.transaction.Transactional;
public class OrderMqProcessor {
	
	
	@Incoming("order-data")
    @Outgoing("order-response")
    @Transactional(Transactional.TxType.REQUIRED)
    public String processOrderQueue(JsonObject order) {
        try 
        { 
        	ItemOrder newOrder = order.mapTo(ItemOrder.class);
            ItemOrder orderToPersist = new ItemOrder();
            orderToPersist.id = newOrder.id;
            orderToPersist.customerId = newOrder.customerId;
            orderToPersist.productId = newOrder.productId;
            orderToPersist.amount = newOrder.amount;
        	orderToPersist.persist();
        	return orderToPersist.id.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error processing Order message", e);
        }
    }
}

