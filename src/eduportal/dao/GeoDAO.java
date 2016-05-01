package eduportal.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;
import java.util.*;
import eduportal.dao.entity.*;

public class GeoDAO {

	public static City createCity (String name, Country c) {
		City city = new City();
		city.setName(name);
		city.setCountry(c);
		city.setCountryId(c.getId());
		ofy().save().entity(city);
		return city;
	}
	
	public static City getCity(String cityname) {
		try {
			return (City) ofy().load().kind("City").filter("city = ", cityname).list().get(0);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static City getCityById(String id) {
		try {
			return (City) ofy().load().kind("City").id(id).now();
		} catch (Exception e) {
			return null;
		}
	}

	public static Country getCountry(String country) {
		List<Object> list = ofy().load().kind("City").filter("country = ", country).list();
		if (list.size() == 0) {
			Country ctr = new Country(country);
			ofy().save().entity(ctr);
			return ctr;
		} else if (list.size() > 1) {
			System.out.println("Err: more than one country");
			return null;
		}
		return (Country) list.get(0);
	}

}
