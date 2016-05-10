package eduportal.dao.entity;

import java.util.*;

import com.googlecode.objectify.*;
import com.googlecode.objectify.annotation.*;

@Entity
public class Order extends AbstractEntity {
	
	@Index
	private Ref<UserEntity> user;
	@Index
	private Ref<Product> product;
	private double price;
	private double paid;
	private Date start;
	private Date end;
	private Ref<UserEntity> createdBy;
	
	protected final int maxIdValue = Integer.MAX_VALUE;

	public Order() {
		super();
	}
	
	public boolean isDonePaid () {
		if (price == paid) {
			return true;
		} else if (Math.abs((price-paid)) < 0.1) {
			return true;
		}
		return false;
	}

	public UserEntity getUser() {
		return user.get();
	}

	public Product getProduct() {
		return product.get();
	}

	public double getPrice() {
		return price;
	}

	public double getPaid() {
		return paid;
	}

	public Date getStart() {
		return start;
	}

	public Date getEnd() {
		return end;
	}

	public UserEntity getCreatedBy() {
		return createdBy.get();
	}

	public void setUser(UserEntity user) {
		this.user = Ref.create(user);
	}

	public Order setProduct(Product product) {
		this.product = Ref.create(product);
		return this;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setPaid(double paid) {
		this.paid = paid;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public void setCreatedBy(UserEntity createdBy) {
		this.createdBy = Ref.create(createdBy);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [id=" + id +", user=" + user + ", product=" + product + ", price=" + price + ", paid=" + paid + ", start="
				+ start + ", end=" + end + ", createdBy=" + createdBy + "]";
	}
	
}
