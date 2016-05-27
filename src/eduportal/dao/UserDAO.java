package eduportal.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;
import java.util.*;
import com.googlecode.objectify.*;
import com.googlecode.objectify.cmd.Query;
import eduportal.dao.entity.*;
import eduportal.util.UserUtils;

public class UserDAO {

	private static String[] credentialVariables = { "mail", "phone" };
		
	private static List<Corporation> corpList = ofy().load().type(Corporation.class).list();

	public static List<UserEntity> listAll() {
		return ofy().load().type(UserEntity.class).list();
	}

	public static List<UserEntity> getCorpEmployees(Corporation corp) {
		return ofy().load().type(UserEntity.class).filter("corporation", corp.getId()).list();
	}

	public static UserEntity create(String passport, String pass, String name, String surname, String cyrillicName,
			String cyrillicSurname, String mail, String phone, UserEntity creator, Date born) {
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
		UserEntity u = new UserEntity(passport, name, surname, cyrillicName, cyrillicSurname, mail, pass, phone, born);
		u.setCreator(creator);

		ofy().save().entity(u);
		return u;
	}
	
	public static List<Corporation> getCorpList() {
		return corpList;
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

	public static void update(UserEntity u) {
		ofy().save().entity(u).now();
	}

	public static UserEntity get(String id) {
		return ofy().load().type(UserEntity.class).id(id).now();
	}

	public static void delete(String target) {
		UserEntity u = ofy().load().type(UserEntity.class).id(target).now();
		DeletedUser du = new DeletedUser(u);
		ofy().delete().entity(u).now();
		ofy().save().entity(du);
	}

	public static UserEntity create(UserEntity user) {
		ofy().save().entity(user).now();
		return user;
	}

	public static List<UserEntity> getClients(UserEntity u) {
		return ofy().load().type(UserEntity.class).filter("creator", Ref.create(u).getKey()).list();
	}

	public static List<UserEntity> searchUsers(String phone, String name, String mail, Corporation corp) {
		return userFilter(ofy().load().type(UserEntity.class).filter("corpId", corp.getId()), phone, name, mail);
	}

	public static List<UserEntity> searchUsers(String phone, String name, String mail, long corp) {
		System.out.println(corp);
		return userFilter(ofy().load().type(UserEntity.class).filter("corpId", corp), phone, name, mail);
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
	
	public static UserEntity getUserByMail (String mail) {
		return ofy().load().type(UserEntity.class).filter("mail", mail).first().now();
	}

	public static void createCorp(Corporation... corp) {
		ofy().save().entities(corp);
		for (Corporation c : corp) {
			corpList.add(c);
		}
	}

	public static Corporation getCorp(long id) {
		for (Corporation c : corpList) {
			if (c.getId() == id) {
				return c;
			}
		}
		return null;
	}

	public static Corporation getCorp(String name) {
		for (Corporation c : corpList) {
			if (c.getName().equals(name)) {
				return c;
			}
		}
		return null;
	}

	public static Corporation getOwnerCorp() {
		for (Corporation c : corpList) {
			if (c.isOwnerCorp() == true) {
				return c;
			}
		}
		return null;
	}
}
