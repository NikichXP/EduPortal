package eduportal.api;

import java.util.*;

import javax.inject.Inject;
import javax.servlet.http.*;
import com.google.api.server.spi.config.*;
import com.google.appengine.api.datastore.Text;
import com.googlecode.objectify.cmd.*;
import static com.googlecode.objectify.ObjectifyService.ofy;
import eduportal.dao.*;
import eduportal.dao.entity.*;
import eduportal.model.*;
import eduportal.util.UserUtils;

@Api(name = "order", version = "v1")
public class OrderAPI {

	/*
	 * We need: 1.: List active products 2.: Assign product to user: moderator,
	 * admin, partner 3.:
	 */

	@Inject
	private static AuthContainer auth = AuthContainer.getInstance();

	/**
	 * @return Orders associated with user
	 */
	@ApiMethod(name = "getAllOrders", path = "allOrders", httpMethod = "GET")
	public List<Order> getAllOrders(HttpServletRequest req, @Named("token") @Nullable String token) {
		UserEntity u = null;
		if (token == null) {
			for (Cookie c : req.getCookies()) {
				if (c.getName().equals("sesToken")) {
					u = auth.getUser(c.getValue());
				}
			}
		} else {
			u = auth.getUser(token);
		}
		return ((u == null) ? null : OrderDAO.getOrdersByUser(u));
	}

	@ApiMethod(name = "getAllProducts", path = "allProducts", httpMethod = "GET")
	public List<Product> getAllProducts(@Named("token") String token) {
		// if (AuthContainer.checkReq(token, AccessSettings.MIN_MODERATOR_LVL)
		// == false) {
		// return null;
		// }
		return OrderDAO.getAllProducts();
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

}
