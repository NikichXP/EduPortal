package eduportal.api;

import javax.inject.Inject;
import javax.servlet.http.*;
import com.google.api.server.spi.config.*;
import com.google.appengine.api.datastore.Text;
import eduportal.dao.UserDAO;
import eduportal.dao.entity.UserEntity;
import eduportal.model.*;
import eduportal.util.AuthToken;

@Api(name = "user", version = "v1", title = "User API", auth = @ApiAuth(allowCookieAuth = AnnotationBoolean.TRUE))
public class UserAPI {
	
	@Inject
	private static AuthContainer auth;

	@ApiMethod(name = "auth", httpMethod = "GET", path = "auth")
	public AuthToken auth(@Named("login") String login, @Named("pass") String pass) {
		return auth.authenticate(login, pass);
	}

	@ApiMethod(name = "register", httpMethod = "GET", path = "register")
	public AuthToken register(@Named("name") String name, @Named("pass") String pass, @Named("login") String login,
			@Named("surname") String surname, @Named("phone") String phone, @Named("mail") String mail) {
		UserDAO.create(login, pass, name, surname, phone, mail);
		return auth.authenticate(login, pass);
	}

	@ApiMethod(name = "create", httpMethod = "POST", path = "create")
	public Text create(UserEntity user) {
		System.out.println(user.toString());
		if (user.hasNull()) {
			return new Text("One of fields are not filled");
		}
		if (!user.getMail().matches("[0-9a-zA-Z]{2,}@[0-9a-zA-Z]{2,}\\.[a-zA-Z]{2,5}")) {
			return new Text("Mail is invalid");
		}
		if (!user.getPhone().matches("[+]{0,1}[0-9]{10,12}")) {
			return new Text("Phone is invalid");
		}
		user.setAccessGroup(0);
		UserEntity u = UserDAO.create(user);
		if (u == null) {
			return new Text("User is probably registered");
		}
		return new Text("SID=" + auth.authenticate(user.getLogin(), user.getPass()).getSessionId());

	}

	@ApiMethod(name = "getName", httpMethod = "GET", path = "getname")
	public Dummy getName(@Named("token") String token, HttpServletRequest req) {
		String token_ = null;
		for (Cookie c : req.getCookies()) {
			if (c.getName().equals("sesToken")) { //XXX: name of cookie can be changed!
				token_ = c.getValue();
				break;
			}
		}
		if (token_ == null) {
			return null;
		}
		final UserEntity u = auth.getUser(token_);
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
	public UserEntity updateUserInfo(@Named("name") @Nullable String name, @Named("token") @Nullable String token,
			@Named("surname") @Nullable String surname, @Named("mail") @Nullable String mail, @Named("phone") @Nullable String phone) {
		UserEntity u = auth.getUser(token);
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
	
	@ApiMethod (name = "changePassword", httpMethod = "GET", path = "changepass")
	public Text changePass (HttpServletRequest req, @Named ("exist") String exist, @Named ("new") String newpass) {
		String token = null;
		UserEntity u = null;
		for (Cookie c : req.getCookies()) {
			if (c.getName().equals("sesToken")) { //XXX: name of cookie can be changed!
				token = c.getValue();
				break;
			}
		}
		try {
			u = auth.getUser(token);
			if (u.getPass().equals(UserEntity.encodePass(exist))) {
				u.setPass(newpass);
				UserDAO.update(u);
				return new Text ("Done successfully");
			} else {
				return new Text ("Wrong password");
			}
		} catch (Exception e){
			return new Text ("No suitable token recieved");
		}
	}

	@ApiMethod(name = "deleteUser", httpMethod = "delete", path = "delete")
	public Text userDelete(@Named("target") String target, @Named("token") String token) {
		if (auth.getAccessGroup(token) >= AccessSettings.MIN_MODERATOR_LVL) {
			UserDAO.delete(target);
			return new Text("Success");
		}
		return new Text("fail");
	}

	interface Dummy {
	}
}
