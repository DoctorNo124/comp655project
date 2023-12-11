package comp655project;

public class Customer {

    public long id;
    public String name;
    public String email;
    public double balance;

    public Customer() {
        
    }


    public Customer(long id, String name, String email, double balance) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.balance = balance;
    }
}


