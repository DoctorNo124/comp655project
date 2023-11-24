package comp655project.model;

import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;

@Entity
@RegisterForReflection
public class ItemOrder extends PanacheEntity {

	//// Order (id, customer_id, product_id, time, amount)

	
	  @NotNull
	  
	  @NotEmpty
	 
	 public long id; 

	public String customerid;
	public String productid;
	public int amount;

	
	  public long getId() { return id; }
	 

	
	  public void setId(long id) { this.id = id; }
	 

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getProductid() {
		return productid;
	}

	public void setProductid(String productid) {
		this.productid = productid;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public ItemOrder persist(ItemOrder order) {
		// TODO Auto-generated method stub
		order.persist();
		return order;
	}

	public static ItemOrder findOrderById(Long id) {
		return findById(id);
	}

	public static List<ItemOrder> findAllOrder() {
		return listAll();
	}

	public static void deleteOrder(Long id) {
		delete("id", id);
	}
}