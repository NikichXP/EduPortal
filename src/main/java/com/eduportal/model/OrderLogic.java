package com.eduportal.model;

import com.eduportal.dao.ProductDAO;
import com.eduportal.dao.UserDAO;
import com.eduportal.entity.ClientEntity;
import com.eduportal.entity.Employee;
import com.eduportal.entity.Order;
import com.eduportal.entity.Product;

import java.util.Date;

public class OrderLogic {

	public static Order createOrder(ClientEntity user, Employee admin, Product p, double paid, String comment,
	                                Date start, Date end) {

		return (admin.isAgent())
				? createOrderByAgent(user, admin, p, paid, start, end, comment)
				: createOrderByAdmin(user, admin, p, paid, start, end, comment);

	}

	public static Order createOrder(String userid, String creatorid, String productid, double paid, Date start, Date end, String comment) {

		UserDAO.get(userid);
		Employee emp = UserDAO.getEmp(creatorid);

		if (emp.isAgent()) {
			return createOrderByAgent(UserDAO.getClient(userid), emp, ProductDAO.get(productid), paid, start, end, comment);
		} else {
			return createOrderByAdmin(UserDAO.getClient(userid), emp, ProductDAO.get(productid), paid, start, end, comment);
		}

	}

	private static Order createOrderByAdmin(ClientEntity user, Employee admin, Product p, double paid, Date start,
	                                        Date end, String comment) {
		Order o = new Order(p);

		o.defineCreator(admin);
		o.defineClient(user);
		o.defineCurator(admin);
		o.setPaid(paid);
		o.setStart((start == null) ? start : new Date());
		o.setEnd((end == null) ? end : new Date());

		return o;
	}

	private static Order createOrderByAgent(ClientEntity user, Employee admin, Product p, double paid, Date start,
	                                        Date end, String comment) {
		Order o = new Order(p);

		o.defineCreator(admin);
		o.defineClient(user);
		o.defineAgent(admin);
		o.setPaid(0.0);
		o.setStart((start == null) ? start : new Date());
		o.setEnd((end == null) ? end : new Date());

		return o;
	}

}
