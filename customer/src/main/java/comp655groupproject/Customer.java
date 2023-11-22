package comp655groupproject;


import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

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

