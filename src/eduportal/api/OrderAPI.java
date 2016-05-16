package eduportal.api;

import java.util.*;
import com.google.api.server.spi.config.*;
import com.google.appengine.api.datastore.Text;
import com.googlecode.objectify.cmd.*;
import static com.googlecode.objectify.ObjectifyService.ofy;
import eduportal.dao.*;
import eduportal.dao.entity.*;
import eduportal.model.*;

@Api(name = "order", version = "v1", title = "Order/Product API")
public class OrderAPI {

	/*
	 * We need: 1.: List active products 2.: Assign product to user: moderator,
	 * admin, partner 3.:
	 */

	/**
	 * @return Orders associated with user
	 */
	@ApiMethod(name = "getAllOrders", path = "allOrders", httpMethod = "GET")
	public List<Order> getAllOrders(@Named("token") String token) {
		UserEntity u = null;
		if (token != null) {
			u = AuthContainer.getUser(token);
		}
		return ((u == null) ? null : OrderDAO.getOrdersByUser(u));
	}

	@ApiMethod(name = "editOrder", path = "editorder", httpMethod = "GET")
	public Text editOrder(@Named("id") @Nullable Long id, @Named("token") String token,
			@Named("clientid") @Nullable Long clientid, @Named("paid") @Nullable Double paid) {
		UserEntity admin = AuthContainer.getUser(token);
		Order order = OrderDAO.getOrder(id);
		if (AccessLogic.canEditOrder(admin, order)) {
			
		}
		return new Text(id + " " + clientid + " " + paid + " " + token);
	}

	@ApiMethod(name = "getAllProducts", path = "allProducts", httpMethod = "GET")
	public List<Product> getAllProducts(@Named("token") String token) {
		// if (AuthContainer.checkReq(token, AccessSettings.MIN_MODERATOR_LVL)
		// == false) {
		// return null;
		// }
		return ProductDAO.getAllProducts();
	}

	@ApiMethod(path = "createorder", httpMethod = "GET")
	public Text createOrder(@Named("product_id") String productId, @Named("client_id") String clientId,
			@Named("token") String token) { // Token to identify creator
		Order o = new Order();
		o.setProduct(ProductDAO.get(productId));
		return new Text("");
	}

	@ApiMethod(name = "filter", path = "filter", httpMethod = "GET")
	public List<Order> filterOrders( // TODO Add security!
			@Named("client_name") String clientName, @Named("client_id") String clientId,
			@Named("is_paid") String isPaid, @Named("created_by") String createdBy) {
		Query<Order> q = ofy().load().kind("Order");
		if (clientName != null) {
			q = q.filter("clientName = ", clientName);
		}
		if (clientId != null) {
			q = q.filter("userid = ", clientId);
		}
		List<Order> list = q.list();
		return list;
	}
	
	@ApiMethod(name = "createCity", httpMethod = "GET", path = "create/city")
	public Text addCity(@Named("city") String cityname, @Named("country") String country) {
		if (GeoDAO.getCity(cityname) != null) {
			return new Text(GeoDAO.getCity(cityname).toString() + " already exists");
		}
		Country ctr = GeoDAO.getCountry(country);
		City c = GeoDAO.createCity(cityname, ctr);
		if (c != null) {
			return new Text("City added");
		} else {
			return new Text("City not added");
		}
	}

	@ApiMethod(name = "addProduct", httpMethod = "GET", path = "product/add")
	public Text addProduct(@Named("title") String title, @Named("description") String descr,
			@Named("cityid") String cityname) {
		City city = GeoDAO.getCityById(cityname);
		if (city == null) {
			city = GeoDAO.getCity(cityname);
			if (city == null) {
				return new Text("No such city exist");
			}
		}
		Product p = new Product(title, descr, city);
		ProductDAO.save(p);
		return new Text(p.toString());
	}

	@ApiMethod(name = "setInactive", httpMethod = "GET", path = "product/inactive")
	public Text setUnActualProduct(@Named("id") Long id) {
		if (id == null) {
			return new Text("No id sent");
		}
		Product p = ProductDAO.get(id);
		if (p != null) {
			p.setActual(false);
			ProductDAO.save(p);
		}
		return new Text("=(");
	}

	@ApiMethod(name = "setActive", httpMethod = "GET", path = "product/active")
	public Text setActualProduct(@Named("id") String id) {
		if (id == null) {
			return new Text("No id sent");
		}
		Product p = ProductDAO.get(id);
		if (p != null) {
			p.setActual(true);
			ProductDAO.save(p);
		}
		return new Text("=(");
	}

}
