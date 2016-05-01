package eduportal.api;

import com.google.api.server.spi.config.*;
import com.google.appengine.api.datastore.Text;

import eduportal.dao.*;
import eduportal.dao.entity.*;
import eduportal.model.AuthContainer;

@Api(name = "admin", title = "Admin API")
public class AdminAPI {
	
	@ApiMethod (name = "createCity", httpMethod = "GET", path = "create/city")
	public Text addCity (@Named ("city") String cityname, @Named ("country") String country) {
		Country ctr = GeoDAO.getCountry(country);
		City c = GeoDAO.createCity(cityname, ctr);
		if (c != null) {
			return new Text ("City added");
		} else {
			return new Text ("City not added");
		}
	}
	
	@ApiMethod (name = "addProduct", httpMethod = "GET", path = "add/product")
	public Text addProduct (@Named ("title") String title, @Named ("description") String descr, @Named ("cityid") String cityname) {
		City c = GeoDAO.getCityById(cityname);
		if (c == null) {
			return new Text ("No such city exist");
		}
		return new Text ("");
	}

	@ApiMethod (name = "promote", httpMethod = "GET", path = "promote")
	public UserEntity promoteUser (@Named("token") String token, @Named("target") String target, @Named("access") String access) {
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
	
}
