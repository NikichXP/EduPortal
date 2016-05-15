package eduportal.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;
import java.util.*;
import com.googlecode.objectify.*;
import com.googlecode.objectify.cmd.Query;

import eduportal.dao.entity.*;
import eduportal.util.UserUtils;

public class UserDAO {

	private static String[] credentialVariables = { "mail", "login", "phone" };
	
	public static UserEntity create(String login, String pass, String name, String surname, String mail, String phone) {
		if (ofy().load().kind("UserEntity").filter("login == ", login).list().isEmpty() == false) {
			return null;
		}
		if (ofy().load().kind("UserEntity").filter("mail == ", mail).list().isEmpty() == false) {
			return null;
		}
		if (ofy().load().kind("UserEntity").filter("phone == ", phone).list().isEmpty() == false) {
			return null;
		}
		UserEntity u = new UserEntity();
		u.setLogin(login);
		u.setPass(pass);
		u.setName(name);
		u.setMail(mail);
		u.setPhone(phone);
		u.setSurname(surname);
		ofy().save().entity(u).now();
		return u;
	}

	/**
	 * Performs search through users DB
	 * 
	 * @param creds
	 * @return
	 */
	public static List<UserEntity> searchUsers(String creds) {
		Query<UserEntity> q = ofy().load().kind("UserEntity");
		ArrayList<Query<UserEntity>> qn = new ArrayList<>();
		qn.ensureCapacity(credentialVariables.length);
		if (creds == null) {
			return null;
		}
		List<UserEntity> ret = new ArrayList<>();
		for (String filter : credentialVariables) {
			qn.add(q.filter(filter + " > ", creds).filter(filter + " < ", creds + "\uFFFD"));
		}
		if (creds.contains(" ")) {
			for (String key : creds.split(" ")) {
				for (UserEntity elem : q.iterable()) {
					if (elem.getName().contains(key) || elem.getSurname().contains(key)) {
						ret.add(elem);
					}
				}
			}
		} else {
			Query<UserEntity> q1 = q.filter("name >=", creds).filter("name <= ", creds + "\uFFFD"),
					q2 = q.filter("surname >=", creds).filter("surname <= ", creds + "\uFFFD");
			ret = q1.list();
			ret.addAll(q2.list());
		}
		for (Query<UserEntity> query : qn) {
			try {
				ret.addAll(query.list());
			} catch (Exception e) {
				System.out.println("Here is null thrown " + e.toString());
			}
		}
		return ret;
	}

	public static UserEntity get(String login, String pass) {
		if (pass.length() != UserUtils.CRYPTOLENGTH) {
			pass = UserUtils.encodePass(pass);
		}
		UserEntity u = null;
		for (String par : credentialVariables) {
			u = (UserEntity) ofy().load().kind("UserEntity").filter(par, login).filter("pass == ", pass).first().now();
			if (u != null) {
				return u;
			}
		}
		return null;
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
		ofy().save().entity(user).now();
		return user;
	}

	public static List<UserEntity> getClients(UserEntity u) {
		Key<UserEntity> key = Ref.create(u).getKey();
		return ofy().load().type(UserEntity.class).filter("creator", key).list();
	}
}
