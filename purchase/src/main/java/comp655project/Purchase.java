package comp655project;

import java.util.UUID;

public class Purchase {

    public UUID orderId;
    public Customer customer;
    public Product product;
    
    public Purchase() {
        
    }
    
    public Purchase(UUID orderId, Customer customer, Product product) {
        this.orderId = orderId;
        this.customer = customer;
        this.product = product;
    }
}