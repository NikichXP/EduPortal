package eduportal.api;

import java.util.*;
import com.google.api.server.spi.config.*;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.Text;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.impl.Keys;

import eduportal.dao.GeoDAO;
import eduportal.dao.UserDAO;
import eduportal.dao.entity.*;
import eduportal.model.*;
import eduportal.util.*;

@Api(name = "user", version = "v1", title = "User API") 
//, auth =	@ApiAuth(allowCookieAuth = AnnotationBoolean.TRUE)
public class UserAPI {
	
	// Init Objectify
	public final static Class<?>[] objectifiedClasses = { UserEntity.class, DeletedUser.class, Product.class,
			Country.class, City.class, Order.class, Corporation.class, AuthSession.class, SavedFile.class};

	static {
		ObjectifyService.begin();
		for (Class<?> c : objectifiedClasses) {
			ObjectifyService.register(c);
		}
	}
	
	@ApiMethod (path = "fields", name = "Available_fields", httpMethod = "GET")
	public String[] getFields () {
		return UserEntity.userParams;
	}
	
	@ApiMethod (path = "getUserFiles", httpMethod = "GET")
	public List<SavedFile> getFiles (@Named("token") String token, @Named("user") @Nullable String user) {
		UserEntity admin = AuthContainer.getUser(token);
		//TODO AccessCheck
		if (user != null) {
			return UserDAO.get(user).getFiles();
		} else {
			return admin.getFiles();
		}
	}
	
	@ApiMethod (path = "getBlobPath", httpMethod = "GET")
	public Text getBlobFile () {
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
	public AuthToken register(
			@Named("name") String name, 
			@Named("surname") String surname,
			@Named("password") @Nullable String pass, 
			@Named("phone") String phone, 
			@Named("mail") String mail,
			@Named("passport") @Nullable String passport,
			@Named("token") String token,
			@Named("birthday") Long borned) {
		UserEntity creator = AuthContainer.getUser(token);
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
			pass = "TEST"; //TODO Gen pass here
		}
		Date born;
		if (borned == null) {
			born = new Date(System.currentTimeMillis() - (3600L * 24 * 365 * 20));
		} else {
			born = new Date (borned);
		}
		UserDAO.create(passport, pass, name, surname, mail, phone, creator, born);
		return AuthContainer.authenticate(mail, pass);
	}
	
	@ApiMethod(name = "createUser", httpMethod = "POST", path = "createuser")
	public Text create(UserDeploy deploy) {
		UserEntity user = new UserEntity();
		UserEntity creator = AuthContainer.getUser(deploy.token);
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
		user.setPermission(new Permission());
		user.setCreator(creator);
		user.setName(deploy.name);
		user.setSurname(deploy.surname);
		user.setPass(UUID.randomUUID().toString().substring(0, 8));
		user.setPhone(deploy.phone);
		user.setMail(deploy.mail);
		user.setBorn(new Date());
		user.setPassportActive(new Date());
		String[] keys = deploy.keys.split("ף");
		String[] values = deploy.values.split("ף");
		if (values.length != keys.length) {
			return new Text ("Err in keys-values");
		}
		for (int i = 0; i < keys.length; i++) {
			user.addData(keys[i], values[i]);
		}
		UserEntity u = UserDAO.create(user);
		if (u == null) {
			return new Text("User is probably registered");
		}
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
			@Named("mail") @Nullable String mail, @Named ("token") String token) {
		return AccessLogic.listUsers(phone, name, mail, login, token);
	}
	
	@ApiMethod(name = "user.filter.admin", path = "user/filterAll", httpMethod = "GET")
	public List<UserEntity> listEveryUserFilter(@Named("login") @Nullable String login,
			@Named("phone") @Nullable String phone, @Named("name") @Nullable String name,
			@Named("mail") @Nullable String mail, @Named ("token") String token) {
		if (AccessLogic.canListAllUsers(token)) {
			return UserDAO.searchUsers(phone, name, mail);
		} else {
			return null;
		}
	}
	
	@ApiMethod (path = "getInfo", httpMethod = "GET")
	public UserEntity getInfo (@Named ("token") String token) {
		final UserEntity u;
		u = AuthContainer.getUser(token);
		if (u == null) {
			return null;
		}
		u.wipeSecData();
		return u;
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

	@ApiMethod(name = "updateUser", httpMethod = "GET", path = "update")
	public UserEntity updateUserInfo(@Named("name") @Nullable String name,
			@Named("surname") @Nullable String surname, @Named("mail") @Nullable String mail,
			@Named("phone") @Nullable String phone, @Named ("token") String token) {
		UserEntity u = AuthContainer.getUser(token);
		if (u == null) {
			return null;
		}
		if (mail != null && !mail.matches("[0-9a-zA-Z]{2,}@[0-9a-zA-Z]{2,}\\.[a-zA-Z]{2,5}")) {
			mail = null;
		}
		if (phone != null && !phone.matches("[+]{0,1}[0-9]{10,12}")) {
			phone = null;
		}
		if (name != null) {
			u.setName(name);
		}
		if (surname != null) {
			u.setSurname(surname);
		}
		if (phone != null) {
			u.setPhone(phone);
		}
		if (mail != null) {
			u.setMail(mail);
		}
		UserDAO.update(u);
		u.setPass(null);
		return u;
	}

	@ApiMethod(name = "changePassword", httpMethod = "GET", path = "changepass")
	public Text changePass(@Named ("token") String token, @Named("exist") String exist, @Named("new") String newpass) {
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
		}
		u.setAccessLevel(access);
		UserDAO.update(u);
		u.setPass(null);
		return u;
	}
	
	@ApiMethod (name = "allowCountry", httpMethod = "GET", path = "allowCountry")
	public UserEntity allowCountry (@Named("token") String token, @Named("countryid") Long countryid, @Named("userid") String userid) {
		if (AuthContainer.getAccessGroup(token) < AccessSettings.ADMIN_LEVEL) {
			return null;
		}
		Country c = GeoDAO.getCountryById(countryid);
		UserEntity user = UserDAO.get(userid);
		user.getPermission().addCountry(c);
		UserDAO.update(user);
		user.wipeSecData();
		return user;
	}

	interface Dummy {
	}
	
	public static class UserDeploy {
		public UserDeploy() {}
		
		private String name;
		private String surname;
		private String phone;
		private String mail;
		private String token;
		private String born;
		private String passportActive;
		private String keys;
		private String values;
		
		public boolean hasNull() {
			if (name == null || surname == null || phone == null || mail == null) {
				return true;
			}
			return false;
		}
		public String getName() {
			return name;
		}
		public String getSurname() {
			return surname;
		}
		public String getPhone() {
			return phone;
		}
		public String getMail() {
			return mail;
		}
		public String getToken() {
			return token;
		}
		public void setName(String name) {
			this.name = name;
		}
		public void setSurname(String surname) {
			this.surname = surname;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public void setMail(String mail) {
			this.mail = mail;
		}
		public void setToken(String token) {
			this.token = token;
		}
		public String getBorn() {
			return born;
		}
		public void setBorn(String born) {
			this.born = born;
		}
		public String getPassportActive() {
			return passportActive;
		}
		public void setPassportActive(String passportActive) {
			this.passportActive = passportActive;
		}
		public String getKeys() {
			return keys;
		}
		public void setKeys(String keys) {
			this.keys = keys;
		}
		public String getValues() {
			return values;
		}
		public void setValues(String values) {
			this.values = values;
		}
		
	}
}
