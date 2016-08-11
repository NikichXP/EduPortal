package eduportal.api;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import com.google.api.server.spi.config.*;
import com.google.appengine.api.blobstore.*;
import com.google.appengine.api.datastore.Text;
import com.googlecode.objectify.ObjectifyService;
import eduportal.dao.*;
import eduportal.dao.entity.*;
import eduportal.model.*;
import eduportal.util.*;
import lombok.Data;

@Api(name = "user", version = "v1", title = "User API")
public class UserAPI {

	// Init Objectify
	public final static Class<?>[] objectifiedClasses = { UserEntity.class, DeletedUser.class, Product.class,
			Country.class, City.class, Order.class, AuthSession.class, SavedFile.class, Employee.class,
			ClientEntity.class };

	static final String LINESEPARATOR = "Ʉ"; // U001e

	static {
		ObjectifyService.begin();
		ObjectifyService.ofy().cache(false).deadline(5.0);
		for (Class<?> c : objectifiedClasses) {
			ObjectifyService.register(c);
		}
	}

	@ApiMethod(path = "fields", name = "Available_fields", httpMethod = "GET")
	public String[] getFields() {
		return ClientEntity.userParams;
	}

	@ApiMethod(path = "optvalues", httpMethod = "GET")
	public String[][] getOptValues(@Named("token") String token, @Named("user") @Nullable String userid) {
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

	@ApiMethod(path = "getUserFiles", httpMethod = "GET")
	public List<SavedFile> getFiles(@Named("token") String token, @Named("user") @Nullable String user) {
		UserEntity admin = AuthContainer.getUser(token);
		// TODO AccessCheck
		if (user != null) {
			return UserDAO.get(user).getFiles();
		} else {
			return admin.getFiles();
		}
	}

	@ApiMethod(path = "getBlobPath", httpMethod = "GET")
	public Text getBlobFile() {
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		String URL = blobstoreService.createUploadUrl("/FileProcessorServlet");
		return new Text(URL);
	}

	@ApiMethod(name = "auth", httpMethod = "GET", path = "auth")
	public AuthToken auth(@Named("login") String login, @Named("pass") String pass) {
		return AuthContainer.authenticate(login, pass);
	}

	@ApiMethod(name = "checkToken", httpMethod = "GET", path = "checkToken")
	public Text checkToken(@Named("token") String token) {
		return ((AuthContainer.checkToken(token) == true) ? new Text("true") : new Text("false"));
	}

	@ApiMethod(name = "register", httpMethod = "GET", path = "register")
	public AuthToken register(@Named("name") String name, @Named("surname") String surname,
			@Named("password") @Nullable String pass, @Named("phone") String phone, @Named("mail") String mail,
			@Named("passport") @Nullable String passport, @Named("token") String token,
			@Named("birthday") Long borned) {
		Employee creator = AuthContainer.getEmp(token);
		if (creator == null || AccessLogic.canCreateUser(creator) == false) {
			return null;
		}
		if (mail.matches("[0-9a-zA-Z]{2,}@[0-9a-zA-Z]{2,}\\.[a-zA-Z]{2,5}")) {
			return null;
		}
		if (phone.matches("[+]{0,1}[0-9]{10,12}")) {
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

	@ApiMethod(name = "updateUser", httpMethod = "POST", path = "updateuser")
	public Text updateUser(UserDeploy deploy) {
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

	@ApiMethod(name = "createUser", httpMethod = "POST", path = "createuser")
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

	@ApiMethod(name = "getMyClients", path = "getMyClients", httpMethod = "GET")
	public List<UserEntity> getMyUsers(@Named("token") String token) {
		UserEntity u = AuthContainer.getUser(token);
		return ((u == null) ? null : UserDAO.getClients(u));
	}

	@ApiMethod(name = "user.filter.anyData", path = "user/search", httpMethod = "GET")
	public List<UserEntity> listAnotherUserFilter(@Named("data") String data) {
		return UserDAO.searchUsers(data);
	}

	@ApiMethod(name = "user.filter", path = "user/filter", httpMethod = "GET")
	public List<UserEntity> listUserFilter(@Named("login") @Nullable String login,
			@Named("phone") @Nullable String phone, @Named("name") @Nullable String name,
			@Named("mail") @Nullable String mail, @Named("token") String token) {
		return AccessLogic.listUsers(phone, name, mail, login, token);
	}

	@ApiMethod(name = "user.filter.admin", path = "user/filterAll", httpMethod = "GET")
	public List<UserEntity> listEveryUserFilter(@Named("login") @Nullable String login,
			@Named("phone") @Nullable String phone, @Named("name") @Nullable String name,
			@Named("mail") @Nullable String mail, @Named("token") String token) {
		if (AccessLogic.canListAllUsers(token)) {
			return UserDAO.searchUsers(phone, name, mail);
		} else {
			return null;
		}
	}

	@ApiMethod(path = "getInfo", httpMethod = "GET")
	public UserEntity getInfo(@Named("token") String token, @Named("clientid") @Nullable String client) {
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

	@ApiMethod(name = "getName", httpMethod = "GET", path = "getname")
	public Dummy getName(@Named("token") String token) {
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

	@ApiMethod(name = "changePassword", httpMethod = "GET", path = "changepass")
	public Text changePass(@Named("token") String token, @Named("exist") String exist, @Named("new") String newpass) {
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

	@ApiMethod(name = "deleteUser", httpMethod = "delete", path = "delete")
	public Text userDelete(@Named("target") String target, @Named("token") String token) {
		if (AuthContainer.getAccessGroup(token) >= AccessSettings.MODERATOR_LEVEL) {
			UserDAO.delete(target);
			return new Text("Success");
		}
		return new Text("fail");
	}

	@ApiMethod(name = "setModerator", httpMethod = "GET", path = "setModerator")
	public UserEntity promoteUser(@Named("token") String token, @Named("target") String target,
			@Named("access") Integer access) {
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
	// @ApiMethod(name = "allowCountry", httpMethod = "GET", path =
	// "allowCountry")
	// public UserEntity allowCountry(@Named("token") String token,
	// @Named("countryid") Long countryid,
	// @Named("userid") String userid) {
	// if (AuthContainer.getAccessGroup(token) < AccessSettings.ADMIN_LEVEL) {
	// return null;
	// }
	// Country c = GeoDAO.getCountryById(countryid);
	// UserEntity user = UserDAO.get(userid);
	// user.getEmpData().getPermission().addCountry(c);
	// UserDAO.update(user);
	// user.wipeSecData();
	// return user;
	// }

	interface Dummy {
	}

	public @Data static class UserDeploy {

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
