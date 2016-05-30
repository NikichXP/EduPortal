package eduportal.api;

import java.util.List;

import com.google.api.server.spi.config.*;
import com.google.appengine.api.datastore.Text;

import eduportal.dao.GeoDAO;
import eduportal.dao.entity.City;
import eduportal.dao.entity.Country;
import eduportal.dao.entity.UserEntity;
import eduportal.model.AccessSettings;
import eduportal.model.AuthContainer;

@Api(name = "util", version = "v1", title = "Misc features") 
public class UtilAPI {

	@ApiMethod (path = "cityList", httpMethod = "GET")
	public List<City> getCityList () {
		return GeoDAO.getCityList();
	}
	
	@ApiMethod (path = "createcity", httpMethod = "GET")
	public Text createcity (@Named("city") String cityname, @Named("country") String countryname, @Named ("token") String token) {
		UserEntity user = AuthContainer.getUser(token);
		if (user.getAccessLevel() < AccessSettings.MODERATOR_LEVEL || user.getCorporation() != AccessSettings.OWNERCORP().getId()) {
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
