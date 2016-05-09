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
	public ArrayList<Object> test () {
		ArrayList<Object> ret = new ArrayList<>();
//		UserEntity u = (UserEntity) ofy().load().kind("UserEntity").filter(", login).filter("pass == ", pass).first().now();
		ret.addAll(ofy().load().kind("UserEntity").list());
		ret.add("end");
		return ret;
	}

	@ApiMethod(name = "ping", path = "ping", httpMethod = "GET")
	public Text ping() {
		return new Text("ping");
	}

	@ApiMethod(name = "Rebuild_user_DB", path = "rebuildDB", httpMethod = "GET")
	public UserEntity[] rebuildDB() {
		for (Class<?> clazz : AdminAPI.objectifiedClasses) {
			for (Object u : ofy().load().kind(clazz.getName()).list()) {
				ofy().delete().entity(u).now();
			}
		}
		UserEntity[] users = {
				new UserEntity("admin", "pass", "Admin", "Adminov", "+123456789012", "mail@me.now")
						.setAccessGroup(AccessSettings.ADMIN_LEVEL + 1),
						new UserEntity("user", "user", "User", "User", "+123456789013", "mail@me2.now"),
						new UserEntity("johndoe", "johndoe", "John", "Doe", "+123456789014", "john@doe.bar")};
		ofy().save().entities(users);
		City c = GeoDAO.createCity("Kiev", "Ukraine");
		Product p = new Product("Test product", "Some product to test", c);
		ofy().save().entity(p).now();
		Order o = new Order();
		o.setUser(users[1]);
		o.setProduct(p);
		o.setCreatedBy(users[0].getIdString());
		o.setStart(new Date());
		o.setEnd(new Date(System.currentTimeMillis()+1_000_000));
		ofy().save().entity(o).now();
		return users;
	}
	
	@ApiMethod(path = "getAll", httpMethod = "GET")
	public List<String> getAll() {
		List<String> ret = new ArrayList<>();
		for (String clazz : AdminAPI.objectifiedClassesNames) {
			for (Object o : ofy().load().kind(clazz).list()) {
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
