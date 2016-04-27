package eduportal.api;

import java.util.HashMap;
import com.google.api.server.spi.config.*;
import com.google.appengine.api.datastore.Text;
import eduportal.dao.UserDAO;
import eduportal.dao.entity.UserEntity;
import eduportal.model.AuthContainer;
import eduportal.util.AuthToken;
import eduportal.util.JSONUtils;

@Api(name = "user", version = "v1", title = "API for user-accounts section")
public class UserAPI {
	
	@ApiMethod(name = "auth", httpMethod = "GET", path = "auth")
	public AuthToken auth (@Named ("login") String login, @Named ("pass") String pass) {
		return AuthContainer.authenticate(login, pass);
	}
	
	@ApiMethod (name = "register", httpMethod = "GET", path="register")
	public AuthToken register (@Named ("name") String name, @Named ("pass") String pass, @Named ("login") String login, @Named ("surname") String surname, @Named ("phone") String phone, @Named ("mail") String mail) {
		UserDAO.create(login, pass, name, surname, phone, mail);
		return AuthContainer.authenticate(login, pass);
	}
	
	@ApiMethod (name = "create", httpMethod = "POST", path = "create")
	public Text create (Object user) {
//		HashMap<String, String> map = JSONUtils.parse(user.toString());
//		UserDAO.create(map.get("login"), map.get("pass"), map.get("name"), map.get("surname"), map.get("mail"), map.get("phone"));
//		return AuthContainer.authenticate(map.get("login"), map.get("pass"));
		return new Text (user.toString());
	}
	
	@ApiMethod (name = "updateUser", httpMethod = "GET", path = "update")
	public UserEntity updateUserInfo (@Named ("name") String name, @Named ("token") String token, @Named ("surname") String surname, @Named ("pass") String pass) {
		UserEntity u = AuthContainer.getUser(token);
		u.setName(name);
		u.setSurname(surname);
		u.setPass(pass);
		UserDAO.update(u);
		u.setPass(null);
		return u;
	}
	
	@ApiMethod(name = "deleteUser", httpMethod = "delete", path = "delete")
	public Text userDelete (@Named ("target") String target, @Named ("token") String token) {
		if (AuthContainer.getAccessGroup(token) > 100) {
			UserDAO.delete(target);
			return new Text ("Success");
		}
		return new Text("fail");
	}
}
