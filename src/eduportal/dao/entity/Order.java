package eduportal.dao.entity;

import java.util.*;

import com.googlecode.objectify.*;
import com.googlecode.objectify.annotation.*;
import lombok.*;

@Entity
@ToString(callSuper = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class Order extends AbstractEntity {
	private static final long serialVersionUID = -8893348970030751098L;
	@Index
	private Key<UserEntity> user;
	@Index
	private Key<Product> product;
	private double price;
	private double paid;
	@Index
	private boolean donePaid;
	private Date start;
	private Date end;
	@Index
	private Key<Employee> createdBy;
	private String comment;
	private ArrayList<SavedFile> files;
	private String currency;
	
	protected final long maxIdValue = 0;
	
	public Order() {
		super();
		comment = "";
		files = new ArrayList<>();
		files.ensureCapacity(5);
	}

	public Order(Product p) {
		super();
		this.product = Ref.create(p).getKey();
		comment = new String();
		this.price = p.getDefaultPrice();
		this.productName = p.getTitle();
		files = new ArrayList<>();
		files.ensureCapacity(5);
		this.currency = p.getCurrency();
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

	public long getCreatedBy() {
		return createdBy.getId();
	}

	public Employee createdByEntity() {
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
		if (price >= paid) {
			donePaid = true;
		}
		donePaid = false;
	}

	public void setPaid(double paid) {
		this.paid = paid;
		if (price >= paid) {
			donePaid = true;
		}
		donePaid = false;
	}

	public void setCreatedBy(Employee user) {
		this.createdBy = Ref.create(user).getKey();
		this.creatorName = user.getSurname() + " " + user.getName();
	}

	public boolean getDonePaid() {
		if (price >= paid) {
			return true;
		}
		return false;
	}

	// <!--- For frontend-only! ---!>

	private String clientName;
	private String productName;
	private String creatorName;

	public void addFile(SavedFile file) {
		this.files.add(file);
	}

}
