package comp655project;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Random;

@Entity
@Table(name = "products")
public class Product extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotBlank(message = "Name may not be blank")
    @Column(nullable = false)
    public String name;

    @Min(message = "Quantity may not be negative", value = 0)
    public Long quantity;

    @Min(message = "Price may not be negative", value = 0)
    @Max(message = "Price may not be greater than 1000000", value = 1000000)
    public double price;

    public Product() {
        // Default constructor
    }

    public static Uni<Product> findProduct(long id) {
        return findById(id);
    }

    public static Uni<Product> findRandomProduct() {
        Random random = new Random();
        return count()
                .onItem().transform(count -> random.nextInt(count.intValue()))
                .chain(index -> findAll().page(index, 1).firstResult());
    }

    public static Uni<List<Product>> findAllProducts() {
        return listAll();
    }

    public static Uni<Boolean> deleteProduct(long id) {
        return deleteById(id);
    }
}
