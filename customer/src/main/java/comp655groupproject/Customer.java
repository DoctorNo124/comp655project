package comp655groupproject;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.util.List;

@Entity // Annotation to declare this class as an Entity for JPA (Java Persistence API)
@Table(name = "customers")
public class Customer extends PanacheEntity {

    @NotEmpty(message ="Name cannot be empty")
    public String name;

    @NotEmpty(message ="Email cannot be empty")
    public String email;

    @Min(0) // Validates that the balance is not negative
    @Max(1000000) // Validates the balance does not exceed the limit
    public double balance;

    public Customer() {
        // Default constructor
    }

    // Constructor with parameters
    public Customer(String name, String email, double balance) {
        this.name = name;
        this.email = email;
        this.balance = balance;
    }

    public static Customer persistCustomer(Customer customer) {
        customer.persist();
        return customer;
    }
    //Method to find customer by id
    public static Customer findCustomerById(Long id) {
        return findById(id);
    }
    //Method to find customer by name
    public static Customer findCustomerByName(String name) {
        return find("name", name).firstResult();
    }
    // Method to list all customers
    public static List<Customer> findAllCustomers() {
        return listAll();
    }
    //Method to find random customer
    public static Customer findRandomCustomer() {
        return find("ORDER BY RANDOM()").firstResult();
    }
    //Method to delete a customer by id
    public static boolean deleteCustomer(Long id) {
        return deleteById(id);
    }
    //Method to update customer's info
    public static Customer updateCustomer(Customer customer) {
        customer.persistOrUpdate();
        return customer;
    }
    //Method to update customer's balance
    public static Customer updateCustomerBalance(Long id, double newBalance) {
        Customer customer = findCustomerById(id);
        if (customer != null) {
            customer.balance = newBalance;
            customer.persistOrUpdate();
            return customer;
        }
        return null;
    }
    // Method to persist or update the customer
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


