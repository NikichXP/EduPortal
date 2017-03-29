package com.eduportal.api;

import com.eduportal.Text;
import com.eduportal.dao.GeoDAO;
import com.eduportal.dao.ProductDAO;
import com.eduportal.entity.*;
import com.eduportal.dao.OrderDAO;
import com.eduportal.model.AccessLogic;
import com.eduportal.model.AccessSettings;
import com.eduportal.model.AuthContainer;
import com.eduportal.model.OrderLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/order")
public class OrderAPI {

	@RequestMapping(name = "getActualProducts", path = "products", method = RequestMethod.GET)
	public List<Product> getActualProducts(@RequestParam("token") String token) {
		if (AccessLogic.canSeeProducts(token)) {
			return ProductDAO.getActual(true);
		}
		return null;
	}

	@RequestMapping(name = "getProductById", path = "getproductbyid", method = RequestMethod.GET)
	public Object getById(@RequestParam("id") String id, @RequestParam("token") String token) {
		if (AccessLogic.canSeeAllProducts(token)) {
			return ProductDAO.get(id);
		}
		return new Text("No product get");
	}

	@RequestMapping(name = "getAllProducts", path = "allProducts", method = RequestMethod.GET)
	public List<Product> getAllProducts(@RequestParam("token") String token) {
		if (AccessLogic.canSeeAllProducts(token)) {
			return ProductDAO.getAll();
		}
		return null;
	}

	@RequestMapping(name = "setActivity", method = RequestMethod.GET, path = "productActivation")
	public Text setUnActualProduct(@RequestParam("id") String id, @RequestParam("token") String token,
	                               @RequestParam("actual") Boolean actual) {
		if (AccessLogic.canActivateProduct(token)) {
			try {
				Product p = ProductDAO.get(id);
				p.setActual(actual);
				ProductDAO.save(p);
				return new Text("Done");
			} catch (Exception e) {
				return new Text("No id sent");
			}
		} else {
			return new Text("No permission");
		}
	}

	@RequestMapping(path = "createorder", method = RequestMethod.GET)
	public Order createOrder(@RequestParam("productid") String productid, @RequestParam("clientid") String clientid,
	                         @RequestParam("token") String token, @RequestParam(value = "paid", required = false) Double paid, @RequestParam("year") Integer year,
	                         @RequestParam(value = "comment", required = false) String comment) { // Token to identify
		// creator
		Employee admin = AuthContainer.getEmp(token);
		if (admin == null || AccessLogic.canCreateOrder(admin) == false) {
			return null;
		}

		Order o = OrderLogic.createOrder(clientid, admin.getId(), productid, paid, null, null, comment);
		OrderDAO.saveOrder(o);
		return o;

	}

	@RequestMapping(path = "unsignedOrders", method = RequestMethod.GET)
	public List<Order> getUnsignedOrdersList() {
		return OrderDAO.getNoCuratorOrders();
	}

	@RequestMapping(name = "editOrder", path = "editorder", method = RequestMethod.GET)
	public Text editOrder(@RequestParam("orderid") String orderid, @RequestParam("token") String token,
	                      @RequestParam(value = "paid", required = false) Double paid, @RequestParam(value = "comment", required = false) String comment) {
		UserEntity admin = AuthContainer.getUser(token);
		Order order = OrderDAO.getOrder(orderid);
		if (AccessLogic.canEditOrder(admin, order) == false) {
			return new Text("You cannot edit this order!");
		}
		boolean flag = false;
		if (paid != null) {
			order.setPaid(order.getPaid() + paid);
			flag = true;
		}
		if (comment != null) {
			order.setComment(comment);
			flag = true;
		}
		if (flag) {
			OrderDAO.saveOrder(order);
		}
		return new Text(order.toString());
	}

	@RequestMapping(path = "cancelOrder", method = RequestMethod.GET)
	public Text cancelOrder(@RequestParam("token") String token, @RequestParam("orderid") String orderid) {
		Order order = OrderDAO.getOrder(orderid);
		if (!AccessLogic.canCancelOrder(token, order)) {
			return new Text("403 Forbidden");
		}
		OrderDAO.deleteOrder(order);
		return new Text("Order cancelled");
	}

	/**
	 * @return Orders associated with user
	 */
	@RequestMapping(name = "getAllOrders", path = "allOrders", method = RequestMethod.GET)
	public List<Order> getAllOrders(@RequestParam("token") String token) {
		UserEntity u = AuthContainer.getUser(token);
		return ((u == null) ? null
				: (u instanceof Employee && ((Employee) u).getAccessLevel() >= AccessSettings.MODERATOR_LEVEL)
				? OrderDAO.getOrdersCreatedByUser(u) : OrderDAO.getSelfOrdersByUser(u));
	}

	@RequestMapping(name = "getEveryOrders", path = "everyOrders", method = RequestMethod.GET)
	public List<Order> getEveryOrders(@RequestParam("token") String token) {
		UserEntity u = AuthContainer.getUser(token);
		return ((u == null) ? null : OrderDAO.getOrdersByUser(u));
	}

	@RequestMapping(name = "getCreatedOrders", path = "createdOrders", method = RequestMethod.GET)
	public List<Order> getCreatedOrders(@RequestParam("token") String token) {
		UserEntity u = AuthContainer.getUser(token);
		return ((u == null) ? null : OrderDAO.getOrdersCreatedByUser(u));
	}

	@RequestMapping(name = "getMyOrders", path = "myOrders", method = RequestMethod.GET)
	public List<Order> getMyOrders(@RequestParam("token") String token) {
		UserEntity u = AuthContainer.getUser(token);
		return ((u == null) ? null : OrderDAO.getSelfOrdersByUser(u));
	}

	@RequestMapping(name = "filter_through_all_orders", path = "filter", method = RequestMethod.GET)
	public List<Order> filterOrders( @RequestParam(value = "client_name", required = false) String clientName,
	                                 @RequestParam(value = "client_id", required = false) String clientId,
	                                 @RequestParam(value = "is_paid", required = false) Boolean isPaid,
	                                 @RequestParam(value = "created_by", required = false) String createdBy,
	                                 @RequestParam("token") String token) throws InvocationTargetException, IllegalAccessException {
		if (!AccessLogic.canSeeAllOrders(token)) {
			return null;
		}
		return OrderDAO.filterOrders(clientName, clientId, isPaid, createdBy);
	}

	@RequestMapping(name = "createCity", method = RequestMethod.GET, path = "create/city")
	public Text addCity(@RequestParam("city") String cityname, @RequestParam("country") String country,
	                    @RequestParam("token") String token) {
		if (!AccessLogic.canCreateCity(token)) {
			return new Text("403 Forbidden");
		}
		if (GeoDAO.getCity(cityname) != null) {
			return new Text(GeoDAO.getCity(cityname).toString() + " already exists");
		}
		City c = GeoDAO.createCity(cityname, country);
		if (c != null) {
			return new Text("City added");
		} else {
			return new Text("City not added");
		}
	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	@ToString(callSuper = true)
	class ProductDeploy extends Product {
		private static final long serialVersionUID = 1170309933073341696L;
		private String token;
	}

}
