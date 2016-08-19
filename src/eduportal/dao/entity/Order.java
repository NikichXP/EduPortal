package eduportal.dao.entity;

import java.io.Serializable;
import java.util.*;

import com.googlecode.objectify.*;
import com.googlecode.objectify.annotation.*;

import eduportal.dao.OrderDAO;
import eduportal.dao.UserDAO;
import lombok.*;

@Entity
@ToString(callSuper = true)
@Data
@EqualsAndHashCode
public class Order implements Serializable {
	private static final long serialVersionUID = -8893348970030751098L;
	@Id
	private String id;
	@Index
	private Key<ClientEntity> client;
	@Index
	private String product;
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
		this.id = UUID.randomUUID().toString().substring(4, 13);
		comment = "";
		files = new ArrayList<>();
		files.ensureCapacity(5);
	}

	public Order(Product p) {
		this.id = UUID.randomUUID().toString().substring(4, 13);
		this.product = p.getId();
		comment = new String();
		this.price = (double)p.getDefaultPrice();
		this.paid = 0.0;
		this.productName = p.getTitle();
		files = new ArrayList<>();
		files.ensureCapacity(5);
		this.currency = p.getCurrency();
	}
	
	public UserEntity clientEntity() {
		return Ref.create(client).get();
	}

	public Product productEntity() {
		return OrderDAO.getProduct(product);
	}
	
	public Employee createdByEntity() {
		return Ref.create(createdBy).get();
	}

	public void setClient (ClientEntity user) {
		this.client = Ref.create(user).getKey();
		user.setOrderid(this.id);
		UserDAO.update(user);
		this.clientName = user.getSurname() + " " + user.getName();
	}

	public Order setProduct(Product product) {
		this.product = product.getId();
		this.productName = product.getTitle();
		this.setPrice(product.getDefaultPrice());
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
	
	// <!-- Somehow it should be here --!>
	
	public long getClient() {
		return client.getId();
	}
	
	public long getCreatedBy() {
		return createdBy.getId();
	}
}
