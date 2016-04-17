package eduportal.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import eduportal.dao.entity.UserEntity;

public class UserDAO {
	
	public static UserEntity create(String login, String pass, String name, String surname) {
		UserEntity u = new UserEntity();
		u.setLogin(login);
		u.setPass(pass);
		u.setName(name);
		u.setSurname(surname);
		ofy().save().entity(u).now();
		return u;
	}
	
	public static UserEntity get (String login, String pass) {
		try {
			return (UserEntity) ofy().load().kind("UserEntity").filter("login", login).filter("pass", pass).first().now();
		} catch (Exception e) {
			return null;
		}
	}

	public static void update(UserEntity u) {
		ofy().save().entity(u).now();
	} 
}
