package eduportal.api;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.*;
import com.google.api.server.spi.config.*;
import com.google.appengine.api.datastore.Text;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.*;
import eduportal.dao.*;
import eduportal.dao.entity.*;
import eduportal.model.AuthContainer;

@Api(name = "admin", title = "Admin API", version = "v1")
public class AdminAPI {
	// Init Objectify
	static {
		ObjectifyService.begin();
		ObjectifyService.register(UserEntity.class);
		ObjectifyService.register(DeletedUser.class);
		ObjectifyService.register(Product.class);
		ObjectifyService.register(Country.class);
		ObjectifyService.register(City.class);
		ObjectifyService.register(Order.class);
	}

	@ApiMethod(name = "createCity", httpMethod = "GET", path = "create/city")
	public Text addCity(@Named("city") String cityname, @Named("country") String country) {
		if (GeoDAO.getCity(cityname) != null) {
			return new Text(GeoDAO.getCity(cityname).toString());
		}
		Country ctr = GeoDAO.getCountry(country);
		City c = GeoDAO.createCity(cityname, ctr);
		if (c != null) {
			return new Text("City added");
		} else {
			return new Text("City not added");
		}
	}

	@ApiMethod(name = "addProduct", httpMethod = "GET", path = "add/product")
	public Text addProduct(@Named("title") String title, @Named("description") String descr,
			@Named("cityid") String cityname) {
		City c = GeoDAO.getCityById(cityname);
		if (c == null) {
			return new Text("No such city exist");
		}
		return new Text("");
	}

	@ApiMethod(name = "promote", httpMethod = "GET", path = "promote")
	public UserEntity promoteUser(@Named("token") String token, @Named("target") String target,
			@Named("access") String access) {
		if (AuthContainer.getAccessGroup(token) != 0xBACC) {
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
		return AuthContainer.testMethod();
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
