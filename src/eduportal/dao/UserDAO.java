package eduportal.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;
import java.util.*;
import com.googlecode.objectify.*;
import com.googlecode.objectify.cmd.Query;
import eduportal.dao.entity.*;
import eduportal.util.UserUtils;

public class UserDAO {

	private static String[] credentialVariables = { "mail", "phone" };

	public static List<UserEntity> listAll() {
		return ofy().load().type(UserEntity.class).list();
	}

	public static List<UserEntity> getCorpEmployees(String corp) {
		return ofy().load().type(UserEntity.class).filter("corporation", corp).list();
	}

	public static UserEntity create(String passport, String pass, String name, String surname, String mail,
			String phone, Employee creator, Date born) {
		if (ofy().load().type(UserEntity.class).filter("mail == ", mail).list().isEmpty() == false) {
			return null;
		}
		if (ofy().load().type(UserEntity.class).filter("phone == ", phone).list().isEmpty() == false) {
			return null;
		}
		if (passport == null) {
			Random r = new Random();
			passport = ((char) (r.nextInt(('Z' - 'A' + 1)) + 'A')) + "" + ((char) (r.nextInt(('Z' - 'A' + 1)) + 'A'))
					+ (r.nextInt(900_000) + 100_000);
		}
		ClientEntity u = new ClientEntity(passport, name, surname, mail, pass, phone, born);
		u.setCreator(creator);
		ofy().save().entity(u);
		return u;
	}

	public static List<UserEntity> getUnactiveClients(boolean active) {
		return ofy().load().type(UserEntity.class).filter("isActive", active).list();
	}

	/**
	 * Performs search through users DB
	 * 
	 * @param creds
	 * @return
	 */
	@Deprecated
	public static List<UserEntity> searchUsers(String creds) {
		Query<UserEntity> q = ofy().load().type(UserEntity.class);
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
			u = (UserEntity) ofy().load().type(UserEntity.class).filter(par, login).filter("pass == ", pass).first()
					.now();
			if (u != null) {
				return u;
			}
		}
		return null;
	}

	public static UserEntity update(final UserEntity u) {
		ofy().save().entity(u);
		return null;
	}

	public static UserEntity get(String id) {
		return ofy().cache(false).load().type(UserEntity.class).id(id).now();
	}
	
	public static ClientEntity getClient(String userid) {
		return ofy().load().type(ClientEntity.class).id(userid).now();
	}
	
	public static Employee getEmp (String id) {
		return ofy().load().type(Employee.class).id(id).now();
	}
	
	public static List<Employee> getEmployeeList () {
		return ofy().load().type(Employee.class).filter("isAgent", "true").list();
	}

	public static void delete(String target) {
		UserEntity u = ofy().load().type(UserEntity.class).id(target).now();
		DeletedUser du = new DeletedUser(u);
		ofy().delete().entity(u).now();
		ofy().save().entity(du);
	}

	public static UserEntity create(UserEntity user) {
		ofy().save().entity(user).now();
		return ofy().load().type(UserEntity.class).id(user.getId()).now();
	}

	public static List<UserEntity> getClients(UserEntity u) {
		return ofy().load().type(UserEntity.class).filter("creator", u.getId()).list();
	}

	public static List<UserEntity> searchUsers(String phone, String name, String mail, String corp) {
		return userFilter(ofy().load().type(UserEntity.class), phone, name, mail);
	}

	public static List<UserEntity> searchUsers(String phone, String name, String mail) {
		return userFilter(ofy().load().type(UserEntity.class), phone, name, mail);
	}

	private static List<UserEntity> userFilter(Query<UserEntity> q, String phone, String name, String mail) {
		List<UserEntity> ret = new ArrayList<>();
		if (phone != null) {
			q = q.filter("phone >=", phone).filter("phone <= ", phone + "\uFFFD");
		}
		if (mail != null) {
			q = q.filter("mail >=", mail).filter("mail <= ", mail + "\uFFFD");
		}
		if (name != null) {
			if (name.split(" ").length > 1) {
				for (String key : name.split(" ")) {
					for (UserEntity elem : q.iterable()) {
						if (elem.getName().contains(key) || elem.getSurname().contains(key)) {
							ret.add(elem);
						}
					}
				}
			} else {
				Query<UserEntity> q1 = q.filter("name >=", name).filter("name <= ", name + "\uFFFD"),
						q2 = q.filter("surname >=", name).filter("surname <= ", name + "\uFFFD");
				ret = q1.list();
				ret.addAll(q2.list());
			}
		}
		if (ret.isEmpty()) {
			ret = q.list();
		}
		return ret;
	}

	public static UserEntity getUserByMail(String mail) {
		return ofy().load().type(UserEntity.class).filter("mail", mail).first().now();
	}

	

}
