package eduportal.api;

import java.util.*;
import com.google.api.server.spi.config.*;
import com.google.appengine.api.datastore.Text;
import com.googlecode.objectify.cmd.*;
import static com.googlecode.objectify.ObjectifyService.ofy;
import eduportal.dao.*;
import eduportal.dao.entity.*;
import eduportal.model.*;
import eduportal.util.IdUtils;

@Api(name = "order", version = "v1")
public class OrderAPI {
	
	/*
	 We need:
	 1.: List active products
	 2.: Assign product to user: moderator, admin, partner
	 3.: 
	 */
	

	@ApiMethod(name = "getAllOrders", path = "all", httpMethod = "GET") 
	public List<Order> getAllOrders (@Named ("token") String token) {
//		if (AuthContainer.checkReq(token, AccessSettings.MIN_MODERATOR_LVL) == false) {
//			return null;
//		}
		return OrderDAO.getAll();
	}
	
	@ApiMethod(name = "getAllProducts", path = "allProducts", httpMethod = "GET") 
	public List<Product> getAllProducts (@Named ("token") String token) {
//		if (AuthContainer.checkReq(token, AccessSettings.MIN_MODERATOR_LVL) == false) {
//			return null;
//		}
		return OrderDAO.getAllProducts();
	}
	
	@ApiMethod (path = "createorder", httpMethod = "GET")
	public Text createOrder (
			@Named ("product_id") String productId,
			@Named ("client_id") String clientId,
			@Named ("token") String token) {		//Token to identify creator
		Order o = new Order ();
		o.setProductid(IdUtils.convertString(productId));
		o.setProduct(ProductDAO.get(productId));
		return new Text ("");
	}
	
	@ApiMethod (name = "filter", path = "filter", httpMethod = "GET")
	public List<Order> filterObjects (										//TODO Add security!
			@Named ("client_name") String clientName, 
			@Named ("client_id") String clientId, 
			@Named ("is_paid") String isPaid, 
			@Named ("created_by") String createdBy) {
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
	
}
