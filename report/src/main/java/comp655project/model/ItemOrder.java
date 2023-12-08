package comp655project.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.UUID;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@RegisterForReflection
public class ItemOrder extends PanacheEntityBase {
	@Id
	public UUID id;
	@NotNull
	public Long customerId;
	@NotNull
	public Long productId;
	@NotNull
	public Integer amount;
}