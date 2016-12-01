package eduportal.api;

import java.util.*;
import com.google.api.server.spi.config.*;
import com.google.appengine.api.datastore.Text;

import eduportal.dao.GeoDAO;
import eduportal.dao.UserDAO;
import eduportal.dao.entity.*;
import eduportal.model.*;

@Api(name = "admin", version = "v1")
public class AdminAPI {
	
	@ApiMethod(path = "myEmployees", httpMethod = "GET", name = "getMyEmployees")
	public List<UserEntity> myEmployees(@Named("token") String token) {
		Employee emp = AuthContainer.getEmp(token);
		if (emp == null) {
			return null;
		}
		return UserDAO.getCorpEmployees(emp.getCorporation());
	}
	
	@ApiMethod(path = "myClients", httpMethod = "GET", name = "getMyClients")
	public List<UserEntity> myClients (@Named("token") String token) {
		Employee emp = AuthContainer.getEmp(token);
		if (emp == null) {
			return null;
		}
		return UserDAO.getClients(emp);
	}
	
	@ApiMethod(path = "unactiveClients", httpMethod = "GET", name = "unactiveClients")
	public List<UserEntity> unactiveClients (@Named("token") String token) {
		Employee emp = AuthContainer.getEmp(token);
		if (emp == null) {
			return null;
		}
		return UserDAO.getUnactiveClients(false);
	}
	
	@ApiMethod (path = "cityList", httpMethod = "GET")
	public List<City> getCityList () {
		return GeoDAO.getCityList();
	}
	
	@ApiMethod (path = "createcity", httpMethod = "GET")
	public Text createcity (@Named("city") String cityname, @Named("country") String countryname, @Named ("token") String token) {
		Employee user = AuthContainer.getEmp(token);
		if (user.getAccessLevel() < AccessSettings.MODERATOR_LEVEL || user.getCorporation().equals(AccessSettings.OWNERCORP_NAME)) {
			return null;
		}
		Country ctr = GeoDAO.getCountry(countryname);
		City c = GeoDAO.createCity(cityname, ctr);
		return new Text ("Успешно создан город. Вернитесь назад");
	}
	
	@ApiMethod (path = "deletecity", httpMethod = "GET")
	public Text deleteCity (@Named("city") String cityid, @Named("token") String token) {
		int resp = GeoDAO.deleteCity(Long.parseLong(cityid));
		return new Text((resp == -1) ? "City in use" : "Deleted");
	}

}
