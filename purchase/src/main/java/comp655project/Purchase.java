package comp655project;

import io.smallrye.mutiny.Uni;

public class Purchase {

    public long orderId;
    public CustomerResponse customer;
    public Uni<ProductMessage> product;
    
    public Purchase() {
        
    }
    
    public Purchase(long orderId, CustomerResponse customer, Uni<ProductMessage> product) {
        this.orderId = orderId;
        this.customer = customer;
        this.product = product;
    }
}