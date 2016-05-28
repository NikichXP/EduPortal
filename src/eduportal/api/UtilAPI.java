package eduportal.api;

import java.util.List;

import com.google.api.server.spi.config.*;

import eduportal.dao.GeoDAO;
import eduportal.dao.entity.City;

@Api(name = "util", version = "v1", title = "Misc features") 
public class UtilAPI {

	@ApiMethod (path = "cityList", httpMethod = "GET")
	public List<City> getCityList () {
		return GeoDAO.getCityList();
	}
}
