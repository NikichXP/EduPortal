package com.eduportal.entity;

import com.eduportal.dao.OrderDAO;
import com.eduportal.dao.UserDAO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@ToString(callSuper = true)
@Data
@EqualsAndHashCode
public class Order implements Serializable {

	private static final long serialVersionUID = -8893348970030751098L;
	@Id
	private String id;
	private String productId;
	private double price, paid;
	private boolean donePaid;
	private Date start, end;
	private String clientId, agentId, curatorId, createdById;
	private String comment;
	private ArrayList<SavedFile> files;
	private String currency;
	
	public Order() {
		this.id = UUID.randomUUID().toString().substring(4, 13);
		comment = "";
		files = new ArrayList<>();
		files.ensureCapacity(5);
		this.agentId = null;
		this.curatorId = null;
	}

	public Order(Product p) {
		this.id = UUID.randomUUID().toString().substring(4, 13);
		this.productId = p.getId();
		comment = "";
		this.price = (double)p.getDefaultPrice();
		this.paid = 0.0;
		this.productName = p.getTitle();
		files = new ArrayList<>();
		files.ensureCapacity(5);
		this.currency = p.getCurrency();
		this.agentId = null;
		this.curatorId = null;
	}
	
	public void defineAgent(Employee agent) {
		this.agentId = agent.getId();
	}
	
	public void defineCurator(Employee agent) {
		this.curatorId = agent.getId();
	}

	public Employee curatorEntity() {
		return (curatorId == null) ? null : UserDAO.getEmp(curatorId);
	}

	public Employee agentEntity() {
		return (agentId == null) ? null : UserDAO.getEmp(agentId);
	}

	public Product productEntity() {
		return OrderDAO.getProduct(productId);
	}

	public Employee createdByEntity() {
		return UserDAO.getEmp(createdById);
	}

	public void defineClient(ClientEntity user) {
		this.clientId = user.getId();
		user.setOrderid(this.id);
		UserDAO.update(user);
		this.clientName = user.getSurname() + " " + user.getName();
	}

	public Order defineProduct(Product product) {
		this.productId = product.getId();
		this.productName = product.getTitle();
		this.setPrice(product.getDefaultPrice());
		this.setCurrency(product.getCurrency());
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

	public void defineCreator(Employee user) {
		this.createdById = user.getId();
		this.creatorName = user.getSurname() + " " + user.getName();
	}

	public boolean getDonePaid() {
		return price >= paid;
	}
	
	// <!--- For frontend-only! ---!>

	private String clientName;
	private String productName;
	private String creatorName;

	public void addFile(SavedFile file) {
		this.files.add(file);
	}

}
