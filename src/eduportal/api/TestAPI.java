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
		// Order o = new Order();
		// UserEntity u1 = new UserEntity("order", "order", "New", "Order",
		// "+123456789015", "kelly@neworder.org");
		// UserEntity u2 = new UserEntity("adminus", "adminus", "Adminus",
		// "Maximus", "+123456789016", "virto@asus.com");
		// City c = GeoDAO.createCity("Lvov", "Ukraine");
		// Product p = new Product("NewOrderTest", "Some product to test new
		// order", c);
		// OrderDAO.saveProduct(p);
		// o.setProduct(p);
		// o.setUser(u1);
		// o.setCreatedBy(u2);
		// // ofy().save().entity(o).now();
		// for (Object obj : ofy().load().kind("NewOrder").list()) {
		// System.out.println(((Order) obj).getProduct().toString());
		// ret.add(obj);
		// }
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
	public List<String> rebuildDB(@Named ("size") String size) {
		ofy().cache(false).flush();
		for (Class<?> clazz : AdminAPI.objectifiedClasses) {
			for (Object u : ofy().load().kind(clazz.getSimpleName()).list()) {
				ofy().delete().entity(u).now();
			}
		}
		UserEntity[] users = {
				new UserEntity("admin", "pass", "Admin", "Adminov", "+123456789012", "mail@me.now")
						.setAccessGroup(AccessSettings.ADMIN_LEVEL + 1),
				new UserEntity("order", "order", "New", "Order", "+123456789015", "kelly@neworder.org")
					.setAccessGroup(AccessSettings.MIN_MODERATOR_LVL),
				new UserEntity("adminus", "adminus", "Adminus", "Maximus", "+123456789016", "virto@asus.com")
						.setAccessGroup(AccessSettings.MIN_MODERATOR_LVL),
				new UserEntity("user", "user", "User", "User", "+123456789013", "mail@me2.now"),
				new UserEntity("johndoe", "johndoe", "John", "Doe", "+123456789014", "john@doe.bar") };
		ofy().save().entities(users);
		City[] c = { 
				GeoDAO.createCity("Kiev", "Ukraine"), 
				GeoDAO.createCity("Lvov", "Ukraine"),
				GeoDAO.createCity("Prague", "Czech Republic"), 
				GeoDAO.createCity("Budapest", "Hungary"), 
				GeoDAO.createCity("London", "United Kingdom") };
		Product p[] = { new Product("Test product", "Some product to test", c[0]),
				new Product("NewOrderTest", "Some product to test new order", c[1]),
				new Product("Prague Study School", "Nice Prague school of english, .......", c[2]),
				new Product("Высшая школа Будапешта", "Описание программы", c[3]),
				new Product("Высшая школа Лондона", "Описание программы", c[4]),
				new Product("LSE", "Описание программы", c[4]),
				new Product("Ещё один ВУЗ", "Описание программы", c[2]),
				new Product("КПИ", "Как же без него?", c[0]),
				};
		
		ProductDAO.save(p);
		Order o[];
		if ("large".equals(size)) {
			o = new Order[100];
		} else {
			o = new Order[20];
		}
		for (int ptr = 0; ptr < o.length; ptr++) {
			o[ptr] = new Order();
		}
		int i = 0;
		for (Order ord : o) {
			i++;
			ord.setUser(users[(i%2)+3]);
			ord.setCreatedBy(users[i%3]);
			ord.setProduct(p[i%p.length]);
			OrderDAO.saveOrder(ord);
		}
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
