package com.eduportal.api;


import com.eduportal.entity.Employee;
import com.eduportal.entity.UserEntity;
import com.eduportal.Text;
import com.eduportal.dao.UserDAO;
import com.eduportal.entity.ClientEntity;
import com.eduportal.entity.SavedFile;
import com.eduportal.model.AccessLogic;
import com.eduportal.model.AccessSettings;
import com.eduportal.model.AuthContainer;
import com.eduportal.model.MailSender;
import com.eduportal.util.AuthToken;
import com.eduportal.util.UserUtils;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserAPI {

	static final String LINESEPARATOR = "Ʉ"; // U001e

	@RequestMapping(path = "fields", name = "Available_fields", method = RequestMethod.GET)
	public String[] getFields() {
		return ClientEntity.userParams;
	}

	@RequestMapping(path = "optvalues", method = RequestMethod.GET)
	public String[][] getOptValues(@RequestParam("token") String token, @RequestParam(value = "user", required = false) String userid) {
		Employee admin = AuthContainer.getEmp(token);
		UserEntity user;
		if (userid != null) {
			user = UserDAO.get(userid);
		} else {
			user = admin;
		}
		if (AccessLogic.canEditUser(admin, user) == false) {
			return null;
		}
		Set<String> params = user.toMap().keySet();
		String[][] ret = new String[params.size()][2];
		int i = 0;
		for (String param : params) {
			ret[i][0] = param;
			ret[i][1] = user.getData(param);
			i++;
		}
		return ret;
	}

	@RequestMapping (path = "resetpass", method = RequestMethod.GET)
	public Text resetPass (@RequestParam ("user") String userid, @RequestParam("token") String token, @RequestParam ("newpass") String newpass) {
		Employee emp = AuthContainer.getEmp(token);
		if (emp == null) {
			return new Text("Сессия окончилась или не была начата. Залогиньтесь на главной странице.");
		}
		UserEntity user = UserDAO.get(userid);
		if (user == null) {
			return new Text ("Пользователь с таким идентификатором не найден. Перезапустите систему и попробуйте вновь.");
		}
		if (AccessLogic.canEditUser(emp, user)) {
			int length = user.getMail().indexOf('@');
			user.setPass(user.getMail().substring(0, (length != -1) ? length : 8));
			return new Text (user.getMail().substring(0, (length != -1) ? length : 8));
		}
		return new Text ("Не удалось сменить пароль. Недостаточно прав доступа.");
	}

	@RequestMapping(path = "getUserFiles", method = RequestMethod.GET)
	public List<SavedFile> getFiles(@RequestParam("token") String token, @RequestParam(value = "user", required = false) String user) {
		UserEntity admin = AuthContainer.getUser(token);
		// TODO AccessCheck
		if (user != null) {
			return UserDAO.getClient(user).getFiles();
		} else {
			return admin.getFiles();
		}
	}

	@RequestMapping(path = "getBlobPath", method = RequestMethod.GET)
	public Text getBlobFile() {
//		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
//		String URL = blobstoreService.createUploadUrl("/FileProcessorServlet");
		return new Text(null);
	}

	@RequestMapping(name = "auth", method = RequestMethod.GET, path = "auth")
	public AuthToken auth(@RequestParam("login") String login, @RequestParam("pass") String pass) {
		return AuthContainer.authenticate(login, pass);
	}

	@RequestMapping(name = "checkToken", method = RequestMethod.GET, path = "checkToken")
	public Text checkToken(@RequestParam("token") String token) {
		return ((AuthContainer.checkToken(token) == true) ? new Text("true") : new Text("false"));
	}

	@RequestMapping(name = "register", method = RequestMethod.GET, path = "register")
	public AuthToken register(@RequestParam("name") String name, @RequestParam("surname") String surname,
	                          @RequestParam(value = "password", required = false) String pass, @RequestParam("phone") String phone, @RequestParam("mail") String mail,
	                          @RequestParam(value = "passport", required = false) String passport, @RequestParam("token") String token,
	                          @RequestParam("birthday") Long borned) {
		Employee creator = AuthContainer.getEmp(token);
		if (creator == null || AccessLogic.canCreateUser(creator) == false) {
			return null;
		}
		if (!mail.matches("[0-9a-zA-Z]{2,}@[0-9a-zA-Z]{2,}\\.[a-zA-Z]{2,5}")) {
			return null;
		}
		if (!phone.matches("[+]{0,1}[0-9]{10,12}")) {
			return null;
		}
		if (pass == null) {
			pass = "TEST"; // TODO Gen pass here
		}
		Date born;
		if (borned == null) {
			born = new Date(System.currentTimeMillis() - (3600L * 24 * 365 * 20));
		} else {
			born = new Date(borned);
		}
		UserDAO.create(passport, pass, name, surname, mail, phone, creator, born);
		return AuthContainer.authenticate(mail, pass);
	}

	@PostMapping("update/user")
	public Text updateUser(UserDeploy deploy) throws Exception {
		UserEntity user = AuthContainer.getUser(deploy.token);
		if (deploy.id == null || deploy.id.equals(deploy.token)) {
			deploy.id = user.getId();
		}
		UserEntity target = UserDAO.get(deploy.id);
		if (target instanceof ClientEntity) {
			if (((ClientEntity)target).isFinal()) {
				return new Text("Profile is final");
			}

		}
		if (!AccessLogic.canEditUser(user, target)) {
			return new Text("Cannot edit user");
		}

		// Reflection: brain warning [below]

		HashMap<String, Method> other = new HashMap<>(); // deploy
		HashMap<String, Method> had = new HashMap<>(); // entity
		HashMap<String, Method> setters = new HashMap<>(); // setters of avail.

		for (Method meth : target.getClass().getMethods()) {
			if (meth.getName().startsWith("get")) {
				had.put(meth.getName().substring(3), meth);
			}
		}
		for (Method meth : deploy.getClass().getMethods()) {
			if (meth.getName().startsWith("get")) {
				other.put(meth.getName().substring(3), meth);
			}
		}
		for (Method meth : target.getClass().getMethods()) {
			if (meth.getName().startsWith("set")) {
				setters.put(meth.getName().substring(3), meth);
			}
		}
		String ret = "Q";
		for (String name : other.keySet()) {
			ret = ret + name + " & " + name.toLowerCase() + " ; ";
			switch (name.toLowerCase()) {
				case "userdata":
					break;
				default:
					if (had.get(name) != null && setters.get(name) != null) {
						try {
							if (other.get(name).invoke(deploy) != null
									&& other.get(name).invoke(deploy).toString().length() > 1) {
								setters.get(name).invoke(target, other.get(name).invoke(deploy));
							}
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							e.printStackTrace();
						}
					}
					break;
			}
		}

		if (deploy.keys != null && deploy.values != null) {
			String[] keys = deploy.keys.split(LINESEPARATOR);
			String[] values = deploy.values.split(LINESEPARATOR);
			if (values.length != keys.length) {
				return new Text("Err in keys-values");
			}
			for (int i = 0; i < keys.length; i++) {
				target.addData(keys[i], values[i]);
			}
		}

		target.setActive(checkActive(target));
		UserDAO.update(target);

		return new Text(deploy.toString() + "\n" + target.toString());
	}

	private boolean checkActive(UserEntity target) {
		if (target instanceof Employee) {
			return true;
		}
		for (String str : ClientEntity.userParams) {
			if (target.getData(str) == null) {
				return false;
			}
		}
		return true;
	}

	@PostMapping("/create/user")
	public Text create(UserDeploy deploy) {
		ClientEntity user = new ClientEntity();
		Employee creator = AuthContainer.getEmp(deploy.token);
		if (creator == null || AccessLogic.canCreateUser(creator) == false) {
			return new Text("Invalid login");
		}
		if (deploy.hasNull()) {
			return new Text("One of fields are not filled");
		}
		if (!deploy.mail.matches("[0-9a-zA-Z]{2,}@[0-9a-zA-Z]{2,}\\.[a-zA-Z]{2,5}")) {
			return new Text("Mail is invalid");
		}
		if (!deploy.phone.matches("[+]{0,1}[0-9]{10,12}")) {
			return new Text("Phone is invalid");
		}
		if (UserDAO.getUserByMail(deploy.mail) != null) {
			return new Text("This mail is already in use");
		}
		user.setCreator(creator);
		user.setName(deploy.name);
		user.setSurname(deploy.surname);
		user.setFathersname(deploy.fathersname);
		String pass = UUID.randomUUID().toString().substring(0, 8);
		user.setBorn(deploy.born);
		user.setPass(pass);
		user.setPhone(deploy.phone);
		user.setMail(deploy.mail);
		String[] keys = deploy.keys.split(LINESEPARATOR);
		String[] values = deploy.values.split(LINESEPARATOR);
		if (values.length != keys.length) {
			return new Text("Err in keys-values");
		}
		for (int i = 0; i < keys.length; i++) {
			user.addData(keys[i], values[i]);
		}
		user.setActive(checkActive(user));
		UserEntity u = UserDAO.create(user);
		if (u == null) {
			return new Text("User is probably registered");
		}
		MailSender.sendAccountCreation(user.getMail(), pass, user.getName());
		return new Text("SID=" + AuthContainer.authenticate(user.getMail(), user.getPass()).getSessionId());

	}

	@RequestMapping(name = "getMyClients", path = "getMyClients", method = RequestMethod.GET)
	public List<ClientEntity> getMyUsers(@RequestParam("token") String token) {
		UserEntity u = AuthContainer.getUser(token);
		return ((u == null) ? null : UserDAO.getClients(u));
	}

	@RequestMapping(name = "user.filter.anyData", path = "user/search", method = RequestMethod.GET)
	public List<UserEntity> listAnotherUserFilter(@RequestParam("data") String data) {
		return UserDAO.searchUsers(data);
	}

	@RequestMapping(name = "user.filter", path = "user/filter", method = RequestMethod.GET)
	public List<UserEntity> listUserFilter(@RequestParam(value = "login", required = false) String login,
	                                       @RequestParam(value = "phone", required = false) String phone, @RequestParam(value = "name", required = false) String name,
	                                       @RequestParam(value = "mail", required = false) String mail, @RequestParam("token") String token) {
		return AccessLogic.listUsers(phone, name, mail, login, token);
	}

	@RequestMapping(name = "user.filter.admin", path = "user/filterAll", method = RequestMethod.GET)
	public List<UserEntity> listEveryUserFilter(@RequestParam(value = "login", required = false) String login,
	                                            @RequestParam(value = "phone", required = false) String phone, @RequestParam(value = "name", required = false) String name,
	                                            @RequestParam(value = "mail", required = false) String mail, @RequestParam(value = "token", required = false) String token) {
		if (AccessLogic.canListAllUsers(token)) {
			return UserDAO.searchUsers(phone, name, mail);
		} else {
			return null;
		}
	}

	@RequestMapping(path = "getInfo", method = RequestMethod.GET)
	public UserEntity getInfo(@RequestParam("token") String token, @RequestParam(value = "clientid", required = false) String client) {
		UserEntity admin;
		admin = AuthContainer.getUser(token);
		if (admin == null) {
			return null;
		}
		if (client != null) {
			UserEntity ret = UserDAO.get(client);
			if (ret instanceof ClientEntity) {
				return (ClientEntity) ret.wipeSecData();
			}
			if (ret instanceof Employee) {
				return (Employee) ret.wipeSecData();
			}
			return UserDAO.get(ret.getId());
		}
		if (admin instanceof ClientEntity) {
			return (ClientEntity) UserDAO.get(admin.getId()).wipeSecData();
		}
		if (admin instanceof Employee) {
			return (Employee) UserDAO.get(admin.getId()).wipeSecData();
		}
		return UserDAO.get(admin.getId());
	}

	@RequestMapping(name = "getName", method = RequestMethod.GET, path = "getname")
	public Dummy getName(@RequestParam("token") String token) {
		final UserEntity u;
		u = AuthContainer.getUser(token);
		if (u == null) {
			return null;
		}
		@SuppressWarnings("unused")
		Dummy o = new Dummy() {
			private String name = u.getName(), surname = u.getSurname();

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public String getSurname() {
				return surname;
			}

			public void setSurname(String name) {
				this.surname = name;
			}
		};
		return o;
	}

	@RequestMapping(name = "changePassword", method = RequestMethod.GET, path = "changepass")
	public Text changePass(@RequestParam("token") String token, @RequestParam("exist") String exist, @RequestParam("new") String newpass) {
		UserEntity u = AuthContainer.getUser(token);
		if (u == null) {
			return new Text("No suitable token recieved");
		}
		if (u.getPass().equals(UserUtils.encodePass(exist))) {
			u.setPass(newpass);
			UserDAO.update(u);
			return new Text("Done successfully");
		} else {
			return new Text("Wrong password");
		}
	}

	@PostMapping("/delete/user")
	public Text userDelete(@RequestParam("target") String target, @RequestParam("token") String token) {
		if (AuthContainer.getAccessGroup(token) >= AccessSettings.MODERATOR_LEVEL) {
			UserDAO.delete(target);
			return new Text("Success");
		}
		return new Text("fail");
	}

	@RequestMapping(name = "setModerator", method = RequestMethod.GET, path = "setModerator")
	public UserEntity promoteUser(@RequestParam("token") String token, @RequestParam("target") String target,
	                              @RequestParam("access") Integer access) {
		if (AuthContainer.getAccessGroup(token) < AccessSettings.ADMIN_LEVEL) {
			return null;
		}
		UserEntity u = UserDAO.get(target);
		if (u == null) {
			return u;
		} else if (u instanceof Employee == false) {
			return null;
		}
		Employee emp = (Employee) u;
		emp.setAccessLevel(access);
		UserDAO.update(emp);
		emp.setPass(null);
		return emp;
	}

	// TODO FUUUUUUCK REPAIR DAMN
	// @RequestMapping(name = "allowCountry", method = RequestMethod.GET, path =
	// "allowCountry")
	// public UserEntity allowCountry(@RequestParam("token") String token,
	// @RequestParam("countryid") Long countryid,
	// @RequestParam("user") String user) {
	// if (AuthContainer.getAccessGroup(token) < AccessSettings.ADMIN_LEVEL) {
	// return null;
	// }
	// Country c = GeoDAO.getCountryById(countryid);
	// UserEntity user = UserDAO.get(user);
	// user.getEmpData().getPermission().addCountry(c);
	// UserDAO.update(user);
	// user.wipeSecData();
	// return user;
	// }

	interface Dummy {
	}

	@Data
	public static class UserDeploy {

		public UserDeploy() {
		}

		private String id;
		private String name;
		private String surname;
		private String fathersname;
		private String phone;
		private String mail;
		private String token;
		private String born;
		private String keys;
		private String values;

		public boolean hasNull() {
			if (name == null || surname == null || phone == null || mail == null) {
				return true;
			}
			return false;
		}

	}

}
