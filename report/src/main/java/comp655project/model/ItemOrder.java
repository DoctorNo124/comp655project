package comp655project.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.Entity;

@Entity
@RegisterForReflection
public class ItemOrder extends PanacheEntity {
	@NotNull
	public Long customerId;
	@NotNull
	public Long productId;
	@NotNull
	public Integer amount;
}