package comp655project.processor;


import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import comp655project.model.ItemOrder;
import io.vertx.core.json.JsonObject;
public class OrderMqProcessor {
	
	
	@Incoming("order-data")
    @Outgoing("order-response")
    public Long processOrderQueue(JsonObject order) {
        try 
        { 
        	ItemOrder newOrder = order.mapTo(ItemOrder.class);
        	newOrder.persist();
        	return newOrder.getId();
        
            
        } catch (Exception e) {
            
            throw new RuntimeException("Error processing Order message", e);
        }
    }
}

