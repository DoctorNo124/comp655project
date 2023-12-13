package comp655project;

import java.util.List;
import java.util.Random;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;


@Entity
@Table(name = "products")
public class Product extends PanacheEntity {
    
    @NotBlank(message = "Name may not be blank")
    public String name;
    
    @Min(message = "Quantity may not be negative", value = 0)
    public long quantity;

    @Min(message = "Quantity may not be negative", value = 0)
    public double price;
    
    public Product() {
    }
    
    public Product(String name, long quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public static Uni<Product> findProduct(long id) {
        return findById(id);
    }

    public static Uni<Product> findRandomProduct() {
        Random random = new Random();
        return count()
                .map(count -> random.nextInt(count.intValue()))
                .chain(index -> findAll().page(index.intValue(), 1).firstResult());
    }
    
    public static Uni<List<Product>> findAllProducts() {
        return listAll();
    }
    
    public static Uni<Boolean> deleteProduct(long id) {
        return deleteById(id);
    }
}
