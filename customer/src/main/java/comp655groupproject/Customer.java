package comp655groupproject;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotEmpty(message ="Name cannot be empty")
    public String name;

    @NotEmpty(message ="Email cannot be empty")
    public String email;

    @Min(0)
    @Max(1000000)
    public double balance;

    public Customer() {
        // Default constructor
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Customer(String name, String email, double balance) {
        this.name = name;
        this.email = email;
        this.balance = balance;
    }

    public static Customer persistCustomer(Customer customer) {
        customer.persist();
        return customer;
    }

    public static Customer findCustomerById(Long id) {
        return findById(id);
    }

    public static Customer findCustomerByName(String name) {
        return find("name", name).firstResult();
    }

    public static List<Customer> findAllCustomers() {
        return listAll();
    }

    public static Customer findRandomCustomer() {
        return find("ORDER BY RAND()").firstResult();
    }

    public static boolean deleteCustomer(Long id) {
        return deleteById(id);
    }

    public static Customer updateCustomer(Customer customer) {
        customer.persistOrUpdate();
        return customer;
    }

    public static Customer updateCustomerBalance(Long id, double newBalance) {
        Customer customer = findCustomerById(id);
        if (customer != null) {
            customer.balance = newBalance;
            customer.persistOrUpdate();
            return customer;
        }
        return null;
    }

    private void persistOrUpdate() {
        if (this.id == null) {
            persist();
        } else {
            Customer customer = Customer.findCustomerById(this.id);
            if (customer != null) {
                customer.name = this.name;
                customer.email = this.email;
                customer.balance = this.balance;
                customer.persist();
            }
        }
    }
}


