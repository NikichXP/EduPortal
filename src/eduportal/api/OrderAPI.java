package eduportal.api;

import java.util.*;
import com.google.api.server.spi.config.*;
import com.google.appengine.api.datastore.Text;
import com.googlecode.objectify.cmd.*;
import static com.googlecode.objectify.ObjectifyService.ofy;
import eduportal.dao.*;
import eduportal.dao.entity.*;
import eduportal.model.*;

@Api(name = "order", version = "v1")
public class OrderAPI {
	
	/*
	 We need:
	 1.: List active products
	 2.: Assign product to user: moderator, admin, partner
	 3.: 
	 */
	

	@ApiMethod(name = "getAll", path = "all", httpMethod = "GET") 
	public List<Order> getAllOrders (@Named ("token") String token) {
		if (AuthContainer.checkReq(token, AccessSettings.MIN_MODERATOR_LVL) == false) {
			return null;
		}
		return OrderDAO.getAll();
	}
	
	@ApiMethod (path = "create", httpMethod = "GET")
	public Text createOrder (
			@Named ("product_id") String productId,
			@Named ("client_id") String clientId) {
		Order o = new Order ();
		try {
		o.setProductid(Long.parseLong(productId));
		} catch (Exception e) {
			try {
				o.setProductid(Long.parseLong(productId, 16));
			} catch (Exception ex) {
				return new Text ("Error in ID");
			}
		}
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
