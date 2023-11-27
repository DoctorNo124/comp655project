package comp655project;

import java.util.List;
import java.util.Random;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.mutiny.Uni;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class Product extends PanacheEntityBase {

    @Min(message = "ID may not be negative", value = 0)
    public long id;
    
    @NotBlank(message = "Name may not be blank")
    public String name;
    
    @Min(message = "Quantity may not be negative", value = 0)
    public long quantity;

    @Min(message = "Quantity may not be negative", value = 0)
    public double price;
    
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
    
    public static Uni<Void> deleteProduct(long id) {
        return deleteById(id).replaceWithVoid();
    }
}
