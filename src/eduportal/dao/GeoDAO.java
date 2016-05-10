package eduportal.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;
import java.util.*;
import eduportal.dao.entity.*;

public class GeoDAO {

	public static City createCity (String name, Country c) {
		City city = new City();
		city.setName(name);
		city.setCountry(c);
		ofy().save().entity(city);
		return city;
	}
	
	public static City createCity (String name, String country) {
		Object ct = ofy().load().kind("City").filter("name == ", name).first().now();
		if (ct != null) {
			return (City) ct;
		}
		City city = new City();
		Country c = getCountry(country);
		city.setName(name);
		city.setCountry(c);
		ofy().save().entity(city);
		return city;
	}
	
	public static City getCity(String cityname) {
		try {
			return (City) ofy().load().kind("City").filter("name", cityname).first().now();
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
	
	public static List<Country> getCountryList (String filterExp) {
		return Arrays.asList((Country[])ofy().load().kind("Country").filter("name >= ", filterExp).
				filter("name <=", filterExp+"\uFFFD").list().toArray());
	}
	
	public static Country getCountry(String country) {
		Country c = (Country) ofy().load().kind("Country").filter("name", country).first().now();
		if (c == null) {
			Country ctr = new Country(country);
			ofy().save().entity(ctr);
			return ctr;
		}
		return c;
	}

}
