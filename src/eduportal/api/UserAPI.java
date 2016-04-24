package eduportal.api;

import com.google.api.server.spi.config.*;
import com.google.appengine.api.datastore.Text;
import eduportal.dao.UserDAO;
import eduportal.dao.entity.UserEntity;
import eduportal.model.AuthContainer;
import eduportal.util.AuthToken;

@Api(name = "user", version = "v1", title = "API for user-accounts section")
public class UserAPI {
	
	@ApiMethod(name = "auth", httpMethod = "GET", path = "auth")
	public AuthToken auth (@Named ("login") String login, @Named ("pass") String pass) {
		return AuthContainer.authToken(login, pass);
	}
	
	@ApiMethod (name = "register", httpMethod = "GET", path="register")
	public Text register (@Named ("name") String name, @Named ("pass") String pass, @Named ("login") String login, @Named ("surname") String surname) {
		UserEntity u = UserDAO.create(login, pass, name, surname);
		return new Text ("done creating " + u.getId() + u.getName());
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
	
}
