package eduportal.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;
import java.util.*;

import com.googlecode.objectify.Ref;

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
		City ct = ofy().load().type(City.class).filter("name == ", name).first().now();
		if (ct != null) {
			return ct;
		}
		City city = new City();
		Country c = getCountry(country);
		city.setName(name);
		city.setCountry(c);
		ofy().save().entity(city);
		return city;
	}
	
	public static void saveCity (City c) {
		ofy().save().entity(c).now();
	}
	
	public static void saveCountry (Country c) {
		ofy().save().entity(c).now();
	}
	
	public static City getCity(String cityname) {
		try {
			City c = ofy().load().type(City.class).filter("name", cityname).first().now();
			if (c == null) {
				c = ofy().load().type(City.class).filter("cyrname", cityname).first().now();
			}
			return c;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static City getCityById(Long id) {
		try {
			return ofy().load().type(City.class).id(id).now();
		} catch (Exception e) {
			return null;
		}
	}
	
	public static City getCityById(long id) {
		try {
			return ofy().load().type(City.class).id(id).now();
		} catch (Exception e) {
			return null;
		}
	}
	
	public static List<Country> getCountryList (String filterExp) {
		return ofy().load().type(Country.class).filter("name >= ", filterExp).filter("name <=", filterExp+"\uFFFD").list();
	}
	
	public static Country getCountry(String country) {
		Country c = ofy().load().type(Country.class).filter("name", country).first().now();
		if (c == null) {
			c = ofy().load().type(Country.class).filter("cyrname", country).first().now();
		}
		if (c == null) {
			Country ctr = new Country(country);
			ofy().save().entity(ctr);
			return ctr;
		}
		return c;
	}

	public static Country getCountryById(Long countryid) {
		return ofy().load().type(Country.class).id(countryid).now();
	}

	public static List<City> getCityList() {
		return ofy().load().type(City.class).list();
	}

	public static int deleteCity(long parseLong) {
		City city = ofy().load().type(City.class).id(parseLong).now();
		if (ofy().load().type(Product.class).filter("city", Ref.create(city).getKey()).list().isEmpty() == false) {
			return -1;
		}
		Country ctr = city.getCountry();
		ofy().delete().entity(city).now();
		if (ofy().load().type(City.class).filter("country",	Ref.create(ctr).getKey()).list().isEmpty() == true) {
			ofy().delete().entity(ctr);
		}
		return 0;
	}

}
