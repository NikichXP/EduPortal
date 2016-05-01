package eduportal.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;
import java.util.*;
import eduportal.dao.entity.*;

public class ProductDAO {
	
	public static boolean create (Product p) {
		ofy().save().entity(p).now();
		return (ofy().load().kind("Product").id(p.getId()).now() != null);
	}

	public static Product get(String productId) {
		return (Product) ofy().load().kind("Product").id(productId).now();
	}
	
	public static List<Object> getAll() {
		return ofy().load().kind("Product").list();
	}
	
	public static List<Object> getAllByCounry(String countryid) {
		return ofy().load().kind("Product").filter("countryid", countryid).list();
	}
	
	public static List<Object> getAllByCity(String countryid) {
		return ofy().load().kind("Product").filter("cityid", countryid).list();
	}
	
	public static List<Object> getAllByCity(City city) {
		return ofy().load().kind("Product").filter("cityid", city.getId()).list();
	}
}
