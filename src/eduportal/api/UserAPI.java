package eduportal.api;

import com.google.api.server.spi.config.*;
import com.google.appengine.api.datastore.Text;
import eduportal.dao.UserDAO;
import eduportal.dao.entity.UserEntity;
import eduportal.model.AuthContainer;

@Api(name = "user", version = "v1", title = "API for user-accounts section")
public class UserAPI {
	
	@ApiMethod(name = "auth", httpMethod = "GET", path = "auth")
	public Text auth (@Named ("login") String login, @Named ("pass") String pass) {
		return new Text (AuthContainer.auth(login, pass));
	}
	
	@ApiMethod (name = "register", httpMethod = "GET", path="register")
	public Text register (@Named ("name") String name, @Named ("pass") String pass, @Named ("login") String login, @Named ("surname") String surname) {
		UserEntity u = UserDAO.create(login, pass, name, surname);
		return new Text ("done creating " + u.getId() + u.getName());
	}
	
	@ApiMethod (name = "updateUser", httpMethod = "GET", path = "update")
	public Text updateUserInfo (@Named ("name") String name, @Named ("token") String token, @Named ("surname") String surname) {
		UserEntity u = AuthContainer.getUser(token);
		u.setName(name);
		u.setSurname(surname);
		UserDAO.update(u);
		return null;
	}
	
}
