package eduportal.api;
//
//import static com.googlecode.objectify.ObjectifyService.ofy;
import java.util.*;

import com.google.api.server.spi.config.*;
import com.google.appengine.api.datastore.Text;
import com.googlecode.objectify.*;
import eduportal.dao.*;
import eduportal.dao.entity.*;
import eduportal.model.*;

@Api(name = "admin", title = "Admin API", version = "v1")
public class AdminAPI {

	// Init Objectify

	public final static Class<?>[] objectifiedClasses = { UserEntity.class, DeletedUser.class, Product.class,
			Country.class, City.class, Order.class };
	public final static String[] objectifiedClassesNames = { "UserEntity", "DeletedUser", "Product", "Country", "City",
			"Order" };

	static {
		ObjectifyService.begin();
		for (Class<?> c : objectifiedClasses) {
			ObjectifyService.register(c);
		}
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

	// <! ==== Users moderating below ===== !>

	@ApiMethod(name = "setModerator", httpMethod = "GET", path = "setModerator")
	public UserEntity promoteUser(@Named("token") String token, @Named("target") Long target,
			@Named("access") Integer access) {
		if (AuthContainer.getAccessGroup(token) < AccessSettings.ADMIN_LEVEL) {
			return null;
		}
		UserEntity u = UserDAO.get(target);
		if (u == null) {
			return u;
		}
		u.defineAccessGroup(access);
		UserDAO.update(u);
		u.setPass(null);
		return u;
	}
	
	@ApiMethod (name = "allowCountry", httpMethod = "GET", path = "allowCountry")
	public UserEntity allowCountry (@Named("token") String token, @Named("countryid") Long countryid, @Named("userid") Long userid) {
		if (AuthContainer.getAccessGroup(token) < AccessSettings.ADMIN_LEVEL) {
			return null;
		}
		Country c = GeoDAO.getCountryById(countryid);
		UserEntity user = UserDAO.get(userid);
		user.getAccessLevel().addCountry(c);
		UserDAO.update(user);
		user.wipeSecData();
		return user;
	}

	@ApiMethod(name = "listSessions", path = "list/session", httpMethod = "GET")
	public List<String> listSession() {
		return AuthContainer.testMethod();
	}

	
}
