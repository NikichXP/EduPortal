package eduportal.api;

import static com.googlecode.objectify.ObjectifyService.ofy;
import java.util.*;
import com.google.api.server.spi.config.*;
import com.google.appengine.api.datastore.Text;
import eduportal.dao.*;
import eduportal.dao.entity.*;
import eduportal.model.*;

@Api(name = "test", version = "v1")
public class TestAPI {

	@ApiMethod(path = "test", httpMethod = "GET")
	public ArrayList<Object> test() {
		ArrayList<Object> ret = new ArrayList<>();
//		Order o = new Order();
//		UserEntity u1 = new UserEntity("order", "order", "New", "Order", "+123456789015", "kelly@neworder.org");
//		UserEntity u2 = new UserEntity("adminus", "adminus", "Adminus", "Maximus", "+123456789016", "virto@asus.com");
//		City c = GeoDAO.createCity("Lvov", "Ukraine");
//		Product p = new Product("NewOrderTest", "Some product to test new order", c);
//		OrderDAO.saveProduct(p);
//		o.setProduct(p);
//		o.setUser(u1);
//		o.setCreatedBy(u2);
//		// ofy().save().entity(o).now();
//		for (Object obj : ofy().load().kind("NewOrder").list()) {
//			System.out.println(((Order) obj).getProduct().toString());
//			ret.add(obj);
//		}
		UserEntity u = ofy().load().type(UserEntity.class).first().now();
		u.addOrder(ofy().load().type(Order.class).first().now());
		ofy().save().entity(u).now();
		ret.add("end");
		return ret;
	}

	@ApiMethod(name = "ping", path = "ping", httpMethod = "GET")
	public Text ping() {
		return new Text("ping");
	}

	@ApiMethod(name = "Rebuild__DB", path = "rebuildDB", httpMethod = "GET")
	public List<String> rebuildDB() {
		ofy().cache(false).flush();
		for (Class<?> clazz : AdminAPI.objectifiedClasses) {
			for (Object u : ofy().load().kind(clazz.getSimpleName()).list()) {
				ofy().delete().entity(u).now();
			}
		}
		UserEntity[] users = {
				new UserEntity("admin", "pass", "Admin", "Adminov", "+123456789012", "mail@me.now")
						.setAccessGroup(AccessSettings.ADMIN_LEVEL + 1),
				new UserEntity("user", "user", "User", "User", "+123456789013", "mail@me2.now"),
				new UserEntity("johndoe", "johndoe", "John", "Doe", "+123456789014", "john@doe.bar"),
				new UserEntity("order", "order", "New", "Order", "+123456789015", "kelly@neworder.org"),
				new UserEntity("adminus", "adminus", "Adminus", "Maximus", "+123456789016", "virto@asus.com")
						.setAccessGroup(AccessSettings.MIN_MODERATOR_LVL) };
		ofy().save().entities(users);
		City[] c = { GeoDAO.createCity("Kiev", "Ukraine"), GeoDAO.createCity("Lvov", "Ukraine"),
				GeoDAO.createCity("Prague", "Czech Republic") };
		Product p[] = { new Product("Test product", "Some product to test", c[0]),
				new Product("NewOrderTest", "Some product to test new order", c[1]) };
		
		ProductDAO.save(p[0], p[1]);
		Order o = new Order();
		Order o1 = new Order();
		o1.setUser(users[1]);
		o1.setProduct(p[0]);
		o1.setCreatedBy(users[0]);
		o1.setStart(new Date());
		o1.setEnd(new Date(System.currentTimeMillis() + 1_000_000));
		o.setProduct(p[1]);
		o.setUser(users[3]);
		o.setCreatedBy(users[4]);
		OrderDAO.saveOrder(o1);
		OrderDAO.saveOrder(o);
		return getAll();
	}

	@ApiMethod(path = "getAll", httpMethod = "GET")
	public List<String> getAll() {
		List<String> ret = new ArrayList<>();
		for (Class<?> clazz : AdminAPI.objectifiedClasses) {
			for (Object o : ofy().load().kind(clazz.getSimpleName()).list()) {
				ret.add(o.toString());
			}
		}
		ret.addAll(AuthContainer.testMethod());
		return ret;
	}

	@ApiMethod(path = "getCountry", httpMethod = "GET")
	public List<Object> getCountry() {
		return ofy().load().kind("Country").list();

	}

}
