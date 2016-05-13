package eduportal.dao.entity;

import java.util.*;

import com.googlecode.objectify.*;
import com.googlecode.objectify.annotation.*;

@Entity
public class Order extends AbstractEntity {
	
	@Index
	private Key<UserEntity> user;
	@Index
	private Key<Product> product;
	private double price;
	private double paid;
	private Date start;
	private Date end;
	@Index
	private Key<UserEntity> createdBy;
	
	//		<!--- For frontend-only! ---!>
	
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

	public long getUser() {
		return user.getId();
	}
	
	public UserEntity userEntity() {
		return Ref.create(user).get();
	}

	public long getProduct() {
		return product.getId();
	}
	
	public Product productEntity() {
		return Ref.create(product).get();
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

	public long getCreatedBy() {
		return createdBy.getId();
	}
	
	public UserEntity createdByEntity() {
		return Ref.create(createdBy).get();
	}

	public void setUser(UserEntity user) {
		this.user = Ref.create(user).getKey();
		this.clientName = user.getSurname() + " " + user.getName();
	}

	public Order setProduct(Product product) {
		this.product = Ref.create(product).getKey();
		this.productName = product.getTitle();
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

	public void setCreatedBy(UserEntity user) {
		this.createdBy = Ref.create(user).getKey();
		this.creatorName = user.getSurname() + " " + user.getName();
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [id=" + id +", user=" + user + ", product=" + product + ", price=" + price + ", paid=" + paid + ", start="
				+ start + ", end=" + end + ", createdBy=" + createdBy + "]";
	}
	
//	<!--- For frontend-only! ---!>
	
	private String clientName;
	private String productName;
	private String creatorName;

	public String getClientName() {
		return clientName;
	}

	public String getProductName() {
		return productName;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public void setProductName(String orderName) {
		this.productName = orderName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	
	
	
}
