package eduportal.dao.entity;

import java.util.*;

import com.googlecode.objectify.annotation.*;

import eduportal.util.IdUtils;

@Entity
public class Order {

	@Id
	private long id;
	@Ignore
	private UserEntity user;
	@Index
	private long userid;
	@Index
	private long productid;
	@Ignore
	private Product product;
	private double price;
	private double paid;
	private Date start;
	private Date end;
	private String createdBy;
	@Index
	private String clientName;
	
	protected final int maxIdValue = Integer.MAX_VALUE;

	public Order() {
		do {
			this.id = new Random().nextLong();
		} while (this.id < 0);
	}

	public String getIdKey() {
		return IdUtils.convertId(this.id);
	}
	
	public boolean isDonePaid () {
		if (price == paid) {
			return true;
		} else if (Math.abs((price-paid)) < 0.1) {
			return true;
		}
		return false;
	}

	public long getId() {
		return id;
	}

	public UserEntity getUser() {
		return user;
	}

	public long getUserid() {
		return userid;
	}

	public long getProductid() {
		return productid;
	}

	public Product getProduct() {
		return product;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public String getClientName() {
		return clientName;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setUser(UserEntity user) {
		this.user = user;
		this.userid = user.getId();
		this.clientName = user.getSurname() + " " + user.getName();
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public void setProductid(long productid) {
		this.productid = productid;
	}

	public void setProduct(Product product) {
		this.product = product;
		this.productid = product.getId();
		this.price = product.getDefaultPrice();
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

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clientName == null) ? 0 : clientName.hashCode());
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		long temp;
		temp = Double.doubleToLongBits(paid);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result + (int) (productid ^ (productid >>> 32));
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result + (int) (userid ^ (userid >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Order)) {
			return false;
		}
		Order other = (Order) obj;
		if (clientName == null) {
			if (other.clientName != null) {
				return false;
			}
		} else if (!clientName.equals(other.clientName)) {
			return false;
		}
		if (createdBy == null) {
			if (other.createdBy != null) {
				return false;
			}
		} else if (!createdBy.equals(other.createdBy)) {
			return false;
		}
		if (end == null) {
			if (other.end != null) {
				return false;
			}
		} else if (!end.equals(other.end)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (Double.doubleToLongBits(paid) != Double.doubleToLongBits(other.paid)) {
			return false;
		}
		if (Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price)) {
			return false;
		}
		if (product == null) {
			if (other.product != null) {
				return false;
			}
		} else if (!product.equals(other.product)) {
			return false;
		}
		if (productid != other.productid) {
			return false;
		}
		if (start == null) {
			if (other.start != null) {
				return false;
			}
		} else if (!start.equals(other.start)) {
			return false;
		}
		if (user == null) {
			if (other.user != null) {
				return false;
			}
		} else if (!user.equals(other.user)) {
			return false;
		}
		if (userid != other.userid) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", user=" + user + ", userid=" + userid + ", productid=" + productid + ", product="
				+ product + ", price=" + price + ", paid=" + paid + ", start=" + start + ", end=" + end + ", createdBy="
				+ createdBy + ", clientName=" + clientName + "]";
	}
}
