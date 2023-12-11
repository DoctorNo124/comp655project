package comp655project;

public class Product {

    public long id;
    public String name;
    public long quantity;
    public double price;
    
    public Product() {
        
    }
    
    public Product(long id, String name, long quantity, double price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }
}
