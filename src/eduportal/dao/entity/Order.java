package eduportal.dao.entity;

import java.util.*;

import com.googlecode.objectify.*;
import com.googlecode.objectify.annotation.*;

@Entity
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
	private Key<UserEntity> createdBy;
	private String comment;
	private ArrayList<UserSavedFile> files;
	
	protected final long maxIdValue = 0;
	
	public Order() {
		super();
		comment = "";
		files.ensureCapacity(5);
	}

	public Order(Product p) {
		super();
		this.product = Ref.create(p).getKey();
		comment = new String();
		this.price = p.getDefaultPrice();
		this.productName = p.getTitle();
		files.ensureCapacity(5);
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

	public boolean getDonePaid() {
		if (price >= paid) {
			return true;
		}
		return false;
	}

	public void setDonePaid(boolean donePaid) {
		this.donePaid = donePaid;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public void addFile (UserSavedFile file) {
		this.files.add(file);
	}

	public ArrayList<UserSavedFile> getFiles() {
		return files;
	}

	public void setFiles(ArrayList<UserSavedFile> files) {
		this.files = files;
	}

	@Override
	public String toString() {
		return "Order [user=" + user + ", product=" + product + ", price=" + price + ", paid=" + paid + ", donePaid="
				+ donePaid + ", start=" + start + ", end=" + end + ", createdBy=" + createdBy + ", comment=" + comment
				+ "]";
	}

	// <!--- For frontend-only! ---!>

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
