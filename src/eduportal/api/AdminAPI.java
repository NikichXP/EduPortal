package eduportal.api;

import static com.googlecode.objectify.ObjectifyService.ofy;
import java.util.*;

import javax.inject.Inject;

import com.google.api.server.spi.config.*;
import com.google.appengine.api.datastore.Text;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.cmd.*;
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
	
	@Inject
	private static AuthContainer auth;

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
	public Text setUnActualProduct(@Named("id") String id) {
		if (id == null) {
			return new Text("No id sent");
		}
		Product p = ofy().load().type(Product.class).id(Long.parseLong(id)).now();
		if (p != null) {
			p.setActual(false);
			ofy().save().entity(p).now();
		}
		return new Text("=(");
	}

	@ApiMethod(name = "setActive", httpMethod = "GET", path = "product/active")
	public Text setActualProduct(@Named("id") String id) {
		if (id == null) {
			return new Text("No id sent");
		}
		Product p = ofy().load().type(Product.class).id(Long.parseLong(id)).now();
		if (p != null) {
			p.setActual(true);
			ofy().save().entity(p).now();
		}
		return new Text("=(");
	}

	// <! ==== Users moderating below ===== !>
	
	@ApiMethod (name = "getCreatedUsers", httpMethod = "GET", path = "getCreatedUsers")
	public List<UserEntity> getCreatedUsers (@Named("token") String token) {
		UserEntity admin = auth.getUser(token);
		ArrayList<UserEntity> ret = new ArrayList<>();
		Key<UserEntity> k = Ref.create(ofy().load().type(UserEntity.class).id(admin.getId()).now()).getKey();
		for (UserEntity u : ofy().load().type(UserEntity.class).filter("creator", k).list()) {
			ret.add(u);
		}
		return ret;
	}

	@ApiMethod(name = "promote", httpMethod = "GET", path = "promote")
	public UserEntity promoteUser(@Named("token") String token, @Named("target") String target,
			@Named("access") String access) {
		if (auth.getAccessGroup(token) < AccessSettings.ADMIN_LEVEL) {
			return null;
		}
		UserEntity u = UserDAO.get(target);
		if (u == null) {
			return u;
		}
		try {
			u.setAccessGroup(Integer.parseInt(access));
		} catch (Exception e) {
			return null;
		}
		UserDAO.update(u);
		u.setPass(null);
		return u;
	}

	@ApiMethod(name = "listSessions", path = "list/session", httpMethod = "GET")
	public List<String> listSession() {
		return auth.testMethod();
	}

	@ApiMethod(name = "user.filter2", path = "user/search", httpMethod = "GET")
	public List<UserEntity> listAnotherUserFilter(@Named("data") String data) {
		return UserDAO.searchUsers(data);
	}

	@ApiMethod(name = "user.filter", path = "user/filter", httpMethod = "GET")
	public List<UserEntity> listUserFilter(@Named("login") String login, @Named("phone") String phone,
			@Named("name") String name, @Named("mail") String mail) {
		Query<UserEntity> q = ofy().load().kind("UserEntity");

		List<UserEntity> ret = new ArrayList<>();
		if (login != null) {
			q = q.filter("login >=", login).filter("login <= ", login + "\uFFFD");
		}
		if (phone != null) {
			q = q.filter("phone >=", phone).filter("phone <= ", phone + "\uFFFD");
		}
		if (mail != null) {
			q = q.filter("mail >=", mail).filter("mail <= ", mail + "\uFFFD");
		}
		if (name != null) {
			if (name.split(" ").length > 1) {
				for (String key : name.split(" ")) {
					for (UserEntity elem : q.iterable()) {
						if (elem.getName().contains(key) || elem.getSurname().contains(key)) {
							ret.add(elem);
						}
					}
				}
			} else {
				Query<UserEntity> q1 = q.filter("name >=", name).filter("name <= ", name + "\uFFFD"),
						q2 = q.filter("surname >=", name).filter("surname <= ", name + "\uFFFD");
				ret = q1.list();
				ret.addAll(q2.list());
			}
		}
		if (ret.isEmpty()) {
			ret = q.list();
		}
		return ret;
	}
}
