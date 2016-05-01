package eduportal.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import eduportal.dao.entity.*;

public class UserDAO {
	
	public static UserEntity create(String login, String pass, String name, String surname, String mail, String phone) {
		if (ofy().load().kind("UserEntity").filter("login", login).list().isEmpty() == false) {
			return null;
		}
		UserEntity u = new UserEntity();
		u.setLogin(login);
		u.setPass(pass);
		u.setName(name);
		u.setSurname(surname);
		ofy().save().entity(u).now();
		return u;
	}
	
	public static UserEntity get (String login, String pass) {
		MessageDigest mDigest = null;
		try {
			mDigest = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		byte[] result = mDigest.digest(pass.getBytes());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < result.length; i++) {
			sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
		}
		pass = sb.toString();
		try {
			return (UserEntity) ofy().load().kind("UserEntity").filter("login", login).filter("pass", pass).first().now();
		} catch (Exception e) {
			return null;
		}
	}

	public static void update(UserEntity u) {
		ofy().save().entity(u).now();
	}

	public static UserEntity get(long parseLong) {
		return (UserEntity) ofy().load().kind("UserEntity").id(parseLong).now();
	} 
	
	public static UserEntity get(String parseLong) {
		return (UserEntity) ofy().load().kind("UserEntity").id(parseLong).now();
	}

	public static void delete(String target) {
		UserEntity u = (UserEntity) ofy().load().kind("UserEntity").id(target).now();
		DeletedUser du = new DeletedUser(u);
		ofy().delete().entity(u).now();
		ofy().save().entity(du);
	}

	public static UserEntity create(UserEntity user) {
		ofy().save().entity(user);
		return user;
	} 
}
