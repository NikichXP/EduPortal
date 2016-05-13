package eduportal.dao;

import java.util.*;
import static com.googlecode.objectify.ObjectifyService.ofy;
import eduportal.dao.entity.*;

public class OrderDAO {
	
	
	
	public static Order getOrder (String id) {
		id = id.trim();
		return ofy().load().type(Order.class).id(Long.parseLong(id)).now();
	}

	public static List<Order> getAllOrders() {
		return ofy().load().type(Order.class).list();
	}

	public static List<Product> getAllProducts() {
		return ofy().load().type(Product.class).list();
	}
	
	public static void saveOrder (Order o) {
		if (o.getStart() == null) {
			o.setStart(new Date());
		}
		if (o.getEnd() == null) {
			o.setEnd(new Date (System.currentTimeMillis() + ( 1_000 * 3600)));
		}
		ofy().save().entity(o); 
	}
	
	public static void saveProduct (Product p) {
		ofy().save().entity(p).now();
	}
	
	public static boolean saveProduct (String title, String description, City c, double defaultPrice) {
		Product p = new Product(title, description, c).setDefaultPrice(defaultPrice);
		ofy().save().entity(p).now();
		return true;
	}

	public static Product getProduct(long product) {
		return ofy().load().type(Product.class).id(product).now();
	}
}
